package kgg.translator.harness.translator;

import kgg.translator.exception.TranslateException;
import kgg.translator.translator.BingTranslator;

import java.io.IOException;

/**
 * Bing 翻译 —— 控制台版本。
 * BingTranslator 本身已经是具体类，这里只封装一个便捷的工厂方法。
 */
public class HarnessBingTranslator extends BingTranslator {

    /**
     * 尝试在线获取 Bing 翻译令牌。
     */
    public void configure() throws TranslateException {
        try {
            update();
            System.out.println("  Bing 翻译已通过在线抓取完成配置");
        } catch (IOException e) {
            throw new TranslateException("Bing 翻译配置失败: " + e.getMessage(), e);
        }
    }
}
