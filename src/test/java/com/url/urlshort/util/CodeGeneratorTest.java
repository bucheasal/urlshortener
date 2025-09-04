package com.url.urlshort.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CodeGeneratorTest {
    @Test
    void randomCode() {
        //given
        int len = 30;

        //when
        String code = CodeGenerator.randomCode(len);
        //then
        assertThat(code.length()).isEqualTo(len);
    }
    @Test
    void randomCodeX() {
        assertThrows(IllegalArgumentException.class, () -> CodeGenerator.randomCode(2));
        assertThrows(IllegalArgumentException.class, () -> CodeGenerator.randomCode(65));

    }

    @Test
    void validAlias() {
        assertTrue(CodeGenerator.validAlias("abcde"));
        assertFalse(CodeGenerator.validAlias(null));
        assertFalse(CodeGenerator.validAlias("한글"));
        assertFalse(CodeGenerator.validAlias("!!!!!"));
    }

    @Test
    void normalizeUrl() {
        assertEquals(CodeGenerator.normalizeUrl("ehdakrrhf.com"), "http://ehdakrrhf.com");
        assertEquals("HTTPS://ehdakrrhF.com", CodeGenerator.normalizeUrl("  HTTPS://ehdakrrhF.com  "));
        
    }
}