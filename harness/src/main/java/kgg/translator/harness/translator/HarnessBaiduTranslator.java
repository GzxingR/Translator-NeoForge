package kgg.translator.harness.translator;

import kgg.translator.translator.BaiduTranslator;

import java.util.Scanner;

/**
 * 百度翻译 —— 无 Minecraft 依赖的 CLI 版本。
 */
public class HarnessBaiduTranslator extends BaiduTranslator {

    /**
     * 交互式配置：从控制台读取 AppId、AppKey、QPS。
     */
    public void configure(Scanner scanner) {
        System.out.print("  百度翻译 AppId: ");
        String appId = scanner.nextLine().trim();
        System.out.print("  百度翻译 AppKey: ");
        String appKey = scanner.nextLine().trim();
        System.out.print("  QPS（每秒请求数，默认 1）: ");
        String qpsStr = scanner.nextLine().trim();
        int qps = qpsStr.isEmpty() ? 1 : Integer.parseInt(qpsStr);

        setConfig(appId, appKey);
        setDelayTime(1000 / Math.max(qps, 1));
    }
}
