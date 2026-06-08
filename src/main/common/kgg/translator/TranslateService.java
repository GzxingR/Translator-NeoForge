package kgg.translator;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import kgg.translator.event.TranslateEvent;
import kgg.translator.exception.NoTranslatorException;
import kgg.translator.exception.NotConfiguredException;
import kgg.translator.exception.TranslateException;
import kgg.translator.ocrtrans.ResRegion;
import kgg.translator.translator.Translator;
import kgg.translator.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static kgg.translator.TranslatorManager.*;

public class TranslateService {
    private static final Logger LOGGER = LogManager.getLogger(TranslateService.class);
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[-+]?\\d*\\.?\\d+");
    private static final CacheManager CACHE_MANAGER = new CacheManager();

    // ======================== 公共接口 ========================
    public static String translate(String Component, String source) throws TranslateException {
        return translate(Component, getCurrent(), getFrom(), getTo(), source);
    }

    public static String translate(String Component, Translator translator, String from, String to, String source) throws TranslateException {
        if (translator != null) {
            from = Language.resolveApiLang(translator.getLanguageType(), from);
            to = Language.resolveApiLang(translator.getLanguageType(), to);
        }
        if (shouldSkipTranslation(translator, Component, to)) {
            return Component;
        }
        checkTranslator(translator);
        return performTranslation(Component, translator, from, to, source);
    }

    public static String cachedTranslate(String Component, String source) throws TranslateException {
        if (NUMBER_PATTERN.matcher(Component).find()) {
            return CACHE_MANAGER.handleNumericTranslation(Component, source);
        }
        return CACHE_MANAGER.getOrLoadTranslation(Component, source);
    }

    public static ResRegion[] ocrtrans(byte[] img) throws TranslateException {
        return ocrtrans(getCurrent(), img, getFrom(), getTo());
    }

    public static ResRegion[] ocrtrans(Translator translator, byte[] img, String from, String to) throws TranslateException {
        checkTranslator(translator);
        LOGGER.info("{} ocrtrans, from {} to {}", translator, from, to);
        return performOcrTranslation(translator, img, from, to);
    }

    public static void clearCache() {
        LOGGER.info("Clearing translation cache");
        CACHE_MANAGER.clearCache();
    }

    @Nullable
    public static String getCache(String Component, String source) {
        return CACHE_MANAGER.getCache(Component, source);
    }

    public static boolean shouldSkipTranslation(String Component) {
        return shouldSkipTranslation(getCurrent(), Component, getTo());
    }

    public static boolean shouldSkipTranslation(Translator translator, String Component, String to) {
        Predicate<String> predicate = Language.getPredicate(Language.getLeftLang(translator.getLanguageType(), to));
        return StringUtil.isBlank(Component) ||
            StringUtils.isNumeric(Component) ||
            predicate.test(Component);
    }
    // ======================== 私有方法 ========================

    private static void checkTranslator(Translator translator) throws TranslateException {
        if (translator == null) {
            throw new NoTranslatorException();
        }
        if (!translator.isConfigured()) {
            throw new NotConfiguredException(translator);
        }
    }

    private static String performTranslation(String Component, Translator translator,
                                             String from, String to, String source) throws TranslateException {
        try {
            if (!TranslateEvent.fireBegin(Component, from, to, source)) {
                throw new TranslateException("Translation aborted by event handler");
            }

            String translated = translator.translate(Component, from, to, source);
            translated = TranslateEvent.fireAfter(Component, translated, from, to, source);

            logTranslationSuccess(translator, from, to, source, Component, translated);
            return translated;
        } catch (Exception e) {
            logTranslationError(translator, from, to, Component, e);
            if (e instanceof TranslateException) {
                throw (TranslateException) e;
            }
            throw new TranslateException(e);
        }
    }

    private static ResRegion[] performOcrTranslation(Translator translator, byte[] img,
                                                     String from, String to) throws TranslateException {
        try {
            return translator.ocrtrans(img, from, to);
        } catch (Exception e) {
            LOGGER.error("{} ocrtrans from {} to {} failed:", translator, from, to, e);
            if (e instanceof TranslateException) {
                throw (TranslateException) e;
            }
            throw new TranslateException(e);
        }
    }

    private static void logTranslationSuccess(Translator translator, String from, String to,
                                              String source, String original, String translated) {
        LOGGER.info("{} translated from {} to {} (source: {}): \"{}\" -> \"{}\"",
            translator.getName(), from, to, source,
            StringUtil.getOutString(original),
            StringUtil.getOutString(translated));
    }

    private static void logTranslationError(Translator translator, String from,
                                            String to, String Component, Exception e) {
        LOGGER.error("{} translation from {} to {} failed for Component: \"{}\"",
            translator.getName(), from, to,
            StringUtil.getOutString(Component), e);
    }

    // ======================== 缓存管理内部类 ========================
    private static class CacheManager {
        private record TextKey(String Component, String source) {}

        private final LoadingCache<TextKey, String> translationCache =
            CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull String load(@NotNull TextKey key) throws TranslateException {
                        return TranslateService.translate(
                            key.Component(),
                            getCurrent(),
                            getFrom(),
                            getTo(),
                            key.source()
                        );
                    }
                });

        String getOrLoadTranslation(String Component, String source) throws TranslateException {
            try {
                return translationCache.get(new TextKey(Component, source));
            } catch (ExecutionException e) {
                handleCacheException(e);
                return ""; // 不会执行到此处
            }
        }

        String handleNumericTranslation(String Component, String source) throws TranslateException {
            TextKey key = new TextKey(Component, source);
            String cached = getStructuredCachedResult(Component, source);
            if (cached != null) return cached;

            return processNewNumericTranslation(Component, source, key);
        }

        @Nullable
        String getCache(String Component, String source) {
            return translationCache.getIfPresent(new TextKey(Component, source));
        }

        private String getStructuredCachedResult(String Component, String source) {
            char placeholder = findPlaceholder(Component);
            String maskedText = NUMBER_PATTERN.matcher(Component).replaceAll(String.valueOf(placeholder));

            String cachedTemplate = translationCache.getIfPresent(new TextKey(maskedText, source));
            if (cachedTemplate == null) return null;

            return restoreNumbersFromTemplate(Component, cachedTemplate, placeholder);
        }

        private String processNewNumericTranslation(String Component, String source, TextKey key)
            throws TranslateException {
            try {
                char placeholder = findPlaceholder(Component);
                String maskedText = NUMBER_PATTERN.matcher(Component).replaceAll(String.valueOf(placeholder));

                String translated = translationCache.get(key);
                cacheValidStructure(Component, source, maskedText, translated, placeholder);

                return translated;
            } catch (ExecutionException e) {
                handleCacheException(e);
                return ""; // 不会执行到此处
            }
        }

        private void cacheValidStructure(String original, String source, String masked,
                                         String translated, char placeholder) {
            List<String> originalNumbers = extractNumbers(original);
            List<String> translatedNumbers = extractNumbers(translated);

            if (originalNumbers.equals(translatedNumbers)) {
                String maskedTranslation = NUMBER_PATTERN.matcher(translated)
                    .replaceAll(String.valueOf(placeholder));
                translationCache.put(new TextKey(masked, source), maskedTranslation);
            }
        }

        private char findPlaceholder(String Component) {
            char placeholder = 0;
            while (Component.indexOf(placeholder) >= 0) placeholder++;
            return placeholder;
        }

        private List<String> extractNumbers(String Component) {
            return NUMBER_PATTERN.matcher(Component)
                .results()
                .map(MatchResult::group)
                .toList();
        }

        private String restoreNumbersFromTemplate(String original, String template, char placeholder) {
            List<String> numbers = extractNumbers(original);
            String result = template;
            for (String number : numbers) {
                result = result.replaceFirst(String.valueOf(placeholder), number);
            }
            return result;
        }

        private void handleCacheException(ExecutionException e) throws TranslateException {
            Throwable cause = e.getCause();
            if (cause instanceof TranslateException) {
                throw (TranslateException) cause;
            }
            throw new TranslateException(cause);
        }

        void clearCache() {
            translationCache.invalidateAll();
        }
    }
}