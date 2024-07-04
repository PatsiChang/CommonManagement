package com.common.validation.utilTests;

import com.common.validation.utils.ValidationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class ValidationHelperTest {
    static final String containsSpecialCharacter = "specialChar!";
    static final String noSpecialCharacter = "noSpecialChar";
    static final String validEmail = "validEmail@gmail.com";
    static final String invalidEmail = "invalidEmail";
    static final String lengthToTest = "lengthToTest";
    static final String containsSqlInjection = "delete sql";
    static final String noSqlInjection = "no sql";
    static final String containsProfanity = "bad word 1 haha";
    static final String noProfanity = "lengthToTest";
    static final List<String> profanityWordList = List.of("bad word 1", "bad word 2");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateContainsSpecialCharacter() {
        assertTrue(ValidationHelper.validateContainsSpecialCharacter(containsSpecialCharacter));
    }

    @Test
    void testValidateNotContainsSpecialCharacter() {
        assertFalse(ValidationHelper.validateContainsSpecialCharacter(noSpecialCharacter));
    }

    @Test
    void testValidateValidEmail() {
        assertTrue(ValidationHelper.validateValidEmail(validEmail));
    }

    @Test
    void testValidateInvalidEmail() {
        assertFalse(ValidationHelper.validateValidEmail(invalidEmail));
    }

    @Test
    void testValidateValidateLength() {
        assertTrue(ValidationHelper.validateLength(lengthToTest, 5, 15));
    }

    @Test
    void testValidateInvalidateLength() {
        assertFalse(ValidationHelper.validateLength(lengthToTest, 5, 10));
    }

    @Test
    void testValidateContainsNoSQLInjection() {
        assertEquals("delete", ValidationHelper.validateNoSQLInjection(containsSqlInjection));
    }

    @Test
    void testValidateNotContainsNoSQLInjection() {
        assertEquals("", ValidationHelper.validateNoSQLInjection(noSqlInjection));
    }

    @Test
    void testValidateContainsProfanity() {
        assertFalse(ValidationHelper.validateProfanity(containsProfanity, profanityWordList));
    }

    @Test
    void testValidateNotContainsProfanity() {
        assertTrue(ValidationHelper.validateProfanity(noProfanity, profanityWordList));
    }

    @Test
    public void testIsJpeg_NullInput() {
        assertFalse(ValidationHelper.isJpeg(null));
    }

    @Test
    public void testIsJpeg_InsufficientLength() {
        assertFalse(ValidationHelper.isJpeg(new byte[]{0x00}));
        assertFalse(ValidationHelper.isJpeg(new byte[]{0x00, 0x00}));
    }

    @Test
    public void testIsJpeg_ValidJpegHeader() {
        assertTrue(ValidationHelper.isJpeg(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}));
        assertTrue(ValidationHelper.isJpeg(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0x00}));
    }

    @Test
    public void testIsJpeg_InvalidJpegHeader() {
        assertFalse(ValidationHelper.isJpeg(new byte[]{0x00, 0x00, 0x00}));
        assertFalse(ValidationHelper.isJpeg(new byte[]{(byte) 0xFF, 0x00, 0x00}));
        assertFalse(ValidationHelper.isJpeg(new byte[]{(byte) 0xFF, (byte) 0xD8, 0x00}));
        assertFalse(ValidationHelper.isJpeg(new byte[]{(byte) 0xFF, 0x00, (byte) 0xFF}));
    }

}
