package com.common.validation.utilTests;

import com.common.validation.utils.MaskingHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class MaskingHelperTest {
    static final String input = "This contains bad words like BADDD and BADDDD and BADDD!";
    static final String pattern1 = "BADDD";
    static final String pattern2 = "bad";
    static final List<String> listOfBadWords = List.of(pattern1, pattern2);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMaskProfanity() {
        String result = MaskingHelper.maskProfanity(input, pattern1);
        assertEquals("This contains bad words like ****** and BADDDD and ******!", result);
    }

    @Test
    void testMaskAllProfanity() {
        String result = MaskingHelper.maskAllProfanity(input, listOfBadWords);
        assertEquals("This contains *** words like *** and BADDDD and ***!", result);
    }

    @Test
    void testMaskAll() {
        String result = MaskingHelper.maskAll(input);
        assertEquals("***********", result);
    }





}
