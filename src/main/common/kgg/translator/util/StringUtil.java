package kgg.translator.util;

public class StringUtil {
    public static boolean isBlank(String Component) {
        return Component.replaceAll("§.", "").isBlank();
    }

    public static String strip(String Component) {
        return Component.replaceAll("§.", "").strip();
    }

    public static boolean equals(String text1, String text2) {
        return strip(text1).replace(" ", "").replace("\n", "").equals(strip(text2).replace(" ", "").replace("\n", ""));
    }

    public static String getOutString(String Component) {
        if (Component.length() > 20) {
            return Component.substring(0, 20) + "...";
        }
        return Component;
    }
}
