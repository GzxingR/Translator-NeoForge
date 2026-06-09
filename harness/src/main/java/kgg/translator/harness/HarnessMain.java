package kgg.translator.harness;

import kgg.translator.*;
import kgg.translator.exception.NoTranslatorException;
import kgg.translator.exception.NotConfiguredException;
import kgg.translator.exception.TranslateException;
import kgg.translator.harness.translator.*;
import kgg.translator.platform.PlatformHooks;
import kgg.translator.translator.Translator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Translator Mod 测试框架入口。
 * <p>
 * 独立于 Minecraft 运行时，可以在纯 JVM 环境中测试各翻译接口的连通性
 * 和核心翻译流程（Language、ChatFormat、TranslateService 等）。
 * <p>
 * 使用方式：
 * <pre>gradlew :harness:run</pre>
 * 或编译后直接运行 {@code java -jar harness.jar}。
 */
public class HarnessMain {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("  Translator Mod - Harness (测试框架)");
        System.out.println("  独立于 Minecraft 运行环境");
        System.out.println("============================================");
        System.out.println();

        // 1. 初始化平台无关环境
        initEnvironment();

        // 2. 注册翻译器
        registerTranslators();

        // 3. 加载配置
        loadConfig();

        // 4. 交互菜单
        mainLoop();
    }

    // ======================== 初始化 ========================

    private static void initEnvironment() {
        // 设置 PlatformHooks（不依赖 Minecraft FML）
        PlatformHooks.init(
            () -> "1.21.1",
            () -> Path.of(System.getProperty("harness.configDir", "config"))
        );

        // 设置为用 Harness 的 LLM Translator 工厂
        LLMManager.setTranslatorFactory(HarnessLLMTranslator::new);

        // 设置选项存储（替代 OptionRegistry，不依赖 Minecraft OptionInstance）
        HarnessOptionStorage optionStorage = new HarnessOptionStorage();
        TranslatorConfig.setOptionStorage(optionStorage);

        System.out.println("[OK] 环境已初始化 (config dir: " + PlatformHooks.getConfigDir() + ")");
    }

    private static void registerTranslators() {
        // 注册各翻译器，与 TranslatorMod 逻辑一致但不依赖 ClothConfig / ModMenu
        TranslatorManager.addTranslator(new HarnessBaiduTranslator());
        TranslatorManager.addTranslator(new HarnessYouDaoTranslator());
        TranslatorManager.addTranslator(new HarnessTencentTranslator());
        // 内置 LLM Model 会由 LLMManager.readConfig 加载
        // Bing 翻译保留但默认不注册，可通过菜单手动添加

        System.out.println("[OK] 翻译器已注册（百度 / 有道 / 腾讯 / LLM）");
    }

    private static void loadConfig() {
        try {
            boolean ok = TranslatorConfig.readFile();
            if (ok) {
                System.out.println("[OK] 配置已加载");
            } else {
                System.out.println("[!]  配置文件读取失败（首次运行是正常的）");
            }
        } catch (Exception e) {
            System.out.println("[!]  配置加载异常: " + e.getMessage());
        }
    }

    // ======================== 主循环 ========================

    private static void mainLoop() {
        while (true) {
            System.out.println("\n--- 主菜单 ---");
            System.out.println("1)  列出现有翻译器");
            System.out.println("2)  切换当前翻译器");
            System.out.println("3)  配置翻译器");
            System.out.println("4)  管理 LLM 模型");
            System.out.println("5)  翻译文字");
            System.out.println("6)  查看 / 修改翻译方向");
            System.out.println("7)  保存配置");
            System.out.println("8)  清除缓存");
            System.out.println("9)  运行测试集");
            System.out.println("0)  退出");
            System.out.print("选择: ");

            String choice = SCANNER.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> listTranslators();
                    case "2" -> switchTranslator();
                    case "3" -> configureTranslator();
                    case "4" -> manageLLMModels();
                    case "5" -> translateText();
                    case "6" -> configureDirection();
                    case "7" -> saveConfig();
                    case "8" -> clearCache();
                    case "9" -> runTests();
                    case "0" -> {
                        System.out.println("退出 harness。");
                        return;
                    }
                    default -> System.out.println("无效选项，请重新输入。");
                }
            } catch (Exception e) {
                System.out.println("[!] 错误: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
    }

    // ======================== 菜单功能 ========================

    private static void listTranslators() {
        List<Translator> ts = TranslatorManager.getTranslators();
        if (ts.isEmpty()) {
            System.out.println("（无已注册翻译器）");
            return;
        }
        System.out.println("注册的翻译器 (" + ts.size() + "):");
        for (int i = 0; i < ts.size(); i++) {
            Translator t = ts.get(i);
            String current = t == TranslatorManager.getCurrent() ? " ← 当前" : "";
            String configured = t.isConfigured() ? "✓" : "✗";
            System.out.printf("  %d) [%s] %s%s%n", i, configured, t.getName(), current);
        }
    }

    private static void switchTranslator() {
        List<Translator> ts = TranslatorManager.getTranslators();
        if (ts.isEmpty()) {
            System.out.println("没有可用的翻译器。");
            return;
        }
        System.out.println("选择翻译器:");
        for (int i = 0; i < ts.size(); i++) {
            System.out.printf("  %d) %s%n", i, ts.get(i).getName());
        }
        System.out.print("输入序号: ");
        try {
            int idx = Integer.parseInt(SCANNER.nextLine().trim());
            if (idx >= 0 && idx < ts.size()) {
                boolean same = TranslatorManager.setTranslator(ts.get(idx));
                if (same) {
                    System.out.println("已切换至 " + ts.get(idx).getName());
                } else {
                    System.out.println("已切换至 " + ts.get(idx).getName() + "（语言映射不存在，使用默认方向）");
                }
            } else {
                System.out.println("序号超出范围。");
            }
        } catch (NumberFormatException e) {
            System.out.println("请输入有效数字。");
        }
    }

    @SuppressWarnings("unchecked")
    private static void configureTranslator() {
        List<Translator> ts = TranslatorManager.getTranslators();
        // 收集可配置的翻译器
        System.out.println("选择要配置的翻译器:");
        int idx = 0;
        for (int i = 0; i < ts.size(); i++) {
            Translator t = ts.get(i);
            if (t instanceof HarnessBaiduTranslator
                || t instanceof HarnessYouDaoTranslator
                || t instanceof HarnessTencentTranslator
                || t instanceof HarnessBingTranslator) {
                System.out.printf("  %d) %s%n", idx, t.getName());
                idx++;
            }
        }
        if (idx == 0) {
            System.out.println("没有可配置的翻译器。");
            return;
        }
        System.out.print("输入序号: ");
        try {
            int sel = Integer.parseInt(SCANNER.nextLine().trim());
            int n = -1;
            for (Translator t : ts) {
                if (t instanceof HarnessBaiduTranslator) { n++; if (n == sel) { ((HarnessBaiduTranslator) t).configure(SCANNER); return; } }
                if (t instanceof HarnessYouDaoTranslator) { n++; if (n == sel) { ((HarnessYouDaoTranslator) t).configure(SCANNER); return; } }
                if (t instanceof HarnessTencentTranslator) { n++; if (n == sel) { ((HarnessTencentTranslator) t).configure(SCANNER); return; } }
                if (t instanceof HarnessBingTranslator) { n++; if (n == sel) { ((HarnessBingTranslator) t).configure(); return; } }
            }
            System.out.println("序号超出范围。");
        } catch (NumberFormatException e) {
            System.out.println("请输入有效数字。");
        } catch (TranslateException e) {
            System.out.println("[!] 配置失败: " + e.getMessage());
        }
    }

    private static void manageLLMModels() {
        while (true) {
            System.out.println("\n--- LLM 模型管理 ---");
            System.out.println("1) 列出 LLM 模型");
            System.out.println("2) 添加 LLM 模型");
            System.out.println("3) 删除 LLM 模型");
            System.out.println("4) 编辑 Prompt");
            System.out.println("5) 返回主菜单");
            System.out.print("选择: ");

            switch (SCANNER.nextLine().trim()) {
                case "1" -> {
                    var models = LLMManager.getModels();
                    if (models.isEmpty()) {
                        System.out.println("（没有 LLM 模型）");
                    } else {
                        System.out.println("LLM 模型:");
                        models.forEach((name, model) -> {
                            String configured = !model.apiKey.isEmpty() && !model.model.isEmpty() ? "✓" : "✗";
                            System.out.printf("  %s [%s] url=%s, model=%s%n", name, configured, model.url, model.model);
                        });
                    }
                }
                case "2" -> {
                    System.out.print("  名称: ");
                    String name = SCANNER.nextLine().trim();
                    System.out.print("  API URL: ");
                    String url = SCANNER.nextLine().trim();
                    System.out.print("  模型名: ");
                    String model = SCANNER.nextLine().trim();
                    System.out.print("  API Key: ");
                    String apiKey = SCANNER.nextLine().trim();
                    System.out.print("  QPS（-1 不限）: ");
                    String qpsStr = SCANNER.nextLine().trim();
                    int qps = qpsStr.isEmpty() ? -1 : Integer.parseInt(qpsStr);

                    LLMManager.addModel(new LLMManager.Model(name, url, model, apiKey, qps));
                    System.out.println("[OK] 模型 '" + name + "' 已添加");
                }
                case "3" -> {
                    System.out.print("  要删除的模型名称: ");
                    String name = SCANNER.nextLine().trim();
                    if (LLMManager.removeModel(name)) {
                        System.out.println("[OK] 模型 '" + name + "' 已删除");
                    } else {
                        System.out.println("[!] 未找到模型 '" + name + "'");
                    }
                }
                case "4" -> {
                    System.out.println("当前 Prompt (Ctrl+D 或空行结束):");
                    System.out.println(LLMManager.getPrompt());
                    System.out.println("\n--- 输入新 Prompt ---");
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while (SCANNER.hasNextLine() && !(line = SCANNER.nextLine()).isEmpty()) {
                        sb.append(line).append("\n");
                    }
                    String newPrompt = sb.toString().trim();
                    if (!newPrompt.isEmpty()) {
                        try {
                            TranslatorConfig.write("prompt.txt", newPrompt);
                            System.out.println("[OK] Prompt 已更新");
                        } catch (IOException e) {
                            System.out.println("[!] 写入失败: " + e.getMessage());
                        }
                    }
                }
                case "5" -> { return; }
                default -> System.out.println("无效选项。");
            }
        }
    }

    private static void translateText() {
        Translator current = TranslatorManager.getCurrent();
        if (current == null) {
            System.out.println("[!] 当前未选择翻译器。");
            return;
        }
        if (!current.isConfigured()) {
            System.out.println("[!] 当前翻译器未配置。请先进入菜单 3 配置。");
            return;
        }

        System.out.println("当前翻译方向: " + TranslatorManager.getFrom() + " -> " + TranslatorManager.getTo());
        System.out.println("当前翻译器: " + current.getName());
        System.out.println("输入要翻译的文字（空行返回主菜单）:");

        while (true) {
            System.out.print("> ");
            String text = SCANNER.nextLine();
            if (text.isBlank()) break;

            try {
                String result = TranslateService.translate(text, "harness");
                System.out.println("  => " + result);
            } catch (NoTranslatorException e) {
                System.out.println("[!] 没有可用的翻译器。");
                break;
            } catch (NotConfiguredException e) {
                System.out.println("[!] 翻译器未配置: " + e.getMessage());
                break;
            } catch (TranslateException e) {
                System.out.println("[!] 翻译失败: " + e.getMessage());
            }
        }
    }

    private static void configureDirection() {
        System.out.println("当前翻译方向: " + TranslatorManager.getFrom() + " -> " + TranslatorManager.getTo());
        System.out.print("源语言（留空不变）: ");
        String from = SCANNER.nextLine().trim();
        if (!from.isEmpty()) {
            TranslatorManager.setFrom(from);
        }
        System.out.print("目标语言（留空不变）: ");
        String to = SCANNER.nextLine().trim();
        if (!to.isEmpty()) {
            TranslatorManager.setTo(to);
        }
        System.out.println("已设置: " + TranslatorManager.getFrom() + " -> " + TranslatorManager.getTo());
    }

    private static void saveConfig() {
        boolean ok = TranslatorConfig.writeFile();
        System.out.println(ok ? "[OK] 配置已保存" : "[!] 配置保存失败");
    }

    private static void clearCache() {
        TranslateService.clearCache();
        System.out.println("[OK] 缓存已清除");
    }

    // ======================== 快速测试 ========================

    private static void runTests() {
        System.out.println("\n--- 快速测试 ---");
        System.out.println("1) Language 语言映射测试");
        System.out.println("2) ChatFormat 格式匹配测试");
        System.out.println("3) SkipTranslation 跳过逻辑测试");
        System.out.println("4) 返回");
        System.out.print("选择: ");

        switch (SCANNER.nextLine().trim()) {
            case "1" -> testLanguage();
            case "2" -> testChatFormat();
            case "3" -> testSkipTranslation();
            default -> {}
        }
    }

    private static void testLanguage() {
        System.out.println("--- Language 测试 ---");
        System.out.println("zh_cn -> api(tencent): " + Language.resolveApiLang("腾讯翻译", "zh_cn"));
        System.out.println("en_us -> api(tencent): " + Language.resolveApiLang("腾讯翻译", "en_us"));
        System.out.println("zh_cn -> api(baidu):  " + Language.resolveApiLang("百度翻译", "zh_cn"));
        System.out.println("en_us -> api(default): " + Language.resolveApiLang(null, "en_us"));

        System.out.println("\nLeftLang 反向查找:");
        System.out.println("  tencent 'zh' -> " + Language.getLeftLang("腾讯翻译", "zh"));
        System.out.println("  baidu  'en' -> " + Language.getLeftLang("百度翻译", "en"));

        System.out.println("\nPredicate 中文检测:");
        System.out.println("  '你好世界' -> " + Language.getPredicate("zh_cn").test("你好世界"));
        System.out.println("  'hello'   -> " + Language.getPredicate("zh_cn").test("hello"));
    }

    private static void testChatFormat() {
        System.out.println("--- ChatFormat 测试 ---");
        System.out.println("当前格式: " + ChatFormat.getCurrentFormat());
        System.out.println("可用格式: " + String.join(", ", ChatFormat.listFormats()));

        // 测试 hypixel 格式
        ChatFormat.setCurrentFormat("hypixel");
        String test1 = "PlayerName: Hello world";
        System.out.println("  hypixel: '" + test1 + "' -> '" + ChatFormat.match(test1) + "'");

        // 测试 normal 格式
        ChatFormat.setCurrentFormat("normal");
        String test2 = "<Steve> Hi there!";
        System.out.println("  normal:  '" + test2 + "' -> '" + ChatFormat.match(test2) + "'");

        // 恢复
        ChatFormat.setCurrentFormat("none");
    }

    private static void testSkipTranslation() {
        System.out.println("--- SkipTranslation 测试 ---");
        System.out.println("  '' (空):        " + TranslateService.shouldSkipTranslation(""));
        System.out.println("  '   ' (空白):   " + TranslateService.shouldSkipTranslation("   "));
        System.out.println("  '12345' (数字): " + TranslateService.shouldSkipTranslation("12345"));
    }
}
