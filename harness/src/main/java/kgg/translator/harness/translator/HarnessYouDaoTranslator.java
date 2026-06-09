package kgg.translator.harness.translator;

import kgg.translator.translator.YouDaoTranslator;

import java.util.Scanner;

/**
 * 有道翻译 —— 无 Minecraft 依赖的 CLI 版本。
 */
public class HarnessYouDaoTranslator extends YouDaoTranslator {

    public void configure(Scanner scanner) {
        System.out.print("  有道翻译 AppId: ");
        String appId = scanner.nextLine().trim();
        System.out.print("  有道翻译 AppSecret: ");
        String appKey = scanner.nextLine().trim();

        setConfig(appId, appKey);
    }
}
