package kgg.translator.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {
    @Test
    void isBlankStripsFormattingCodes() {
        assertTrue(StringUtil.isBlank("§a   "));
        assertFalse(StringUtil.isBlank("§ahello"));
    }

    @Test
    void stripRemovesFormattingCodes() {
        assertEquals("hello", StringUtil.strip("§ahello"));
    }

    @Test
    void equalsIgnoresFormattingAndWhitespace() {
        assertTrue(StringUtil.equals("§ahello world", "hello\nworld"));
    }

    @Test
    void getOutStringTruncatesLongText() {
        assertEquals("12345678901234567890...", StringUtil.getOutString("123456789012345678901234567890"));
    }
}