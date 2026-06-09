package kgg.translator.harness.translator;

import kgg.translator.translator.TencentTranslator;

import java.util.Scanner;

/**
 * 腾讯翻译 —— 无 Minecraft 依赖的 CLI 版本。
 */
public class HarnessTencentTranslator extends TencentTranslator {

    public void configure(Scanner scanner) {
        System.out.print("  腾讯翻译 SecretId: ");
        String secretId = scanner.nextLine().trim();
        System.out.print("  腾讯翻译 SecretKey: ");
        String secretKey = scanner.nextLine().trim();
        System.out.print("  地域（默认 ap-guangzhou）: ");
        String region = scanner.nextLine().trim();
        System.out.print("  ProjectId（默认 0）: ");
        String projectIdStr = scanner.nextLine().trim();

        if (region.isEmpty()) region = "ap-guangzhou";
        int projectId = projectIdStr.isEmpty() ? 0 : Integer.parseInt(projectIdStr);

        setConfig(secretId, secretKey, region, projectId);
    }
}
