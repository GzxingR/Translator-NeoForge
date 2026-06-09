package kgg.translator.harness.translator;

import kgg.translator.LLMManager;
import kgg.translator.translator.LLMTranslator;

/**
 * LLM 翻译器 —— 无 Minecraft 依赖的 CLI 版本。
 * 可直接用 LLMManager.addModel() 注册，无需单独实例化。
 */
public class HarnessLLMTranslator extends LLMTranslator {
    public HarnessLLMTranslator(LLMManager.Model model) {
        super(model);
    }
}
