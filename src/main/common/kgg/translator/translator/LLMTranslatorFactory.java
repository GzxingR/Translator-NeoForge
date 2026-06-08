package kgg.translator.translator;

import kgg.translator.LLMManager;

@FunctionalInterface
public interface LLMTranslatorFactory {
    LLMTranslator create(LLMManager.Model model);
}
