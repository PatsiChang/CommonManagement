package com.common.validation.maskTests;

import com.common.validation.annotations.IsDisplayFields;
import com.common.validation.bean.ProfanityWords;
import com.common.validation.mask.DisplayFieldsMask;
import com.common.validation.repository.ProfanityWordsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class DisplayFieldsMaskTest {
    @InjectMocks
    private DisplayFieldsMask displayFieldsMask;
    @Mock
    private ProfanityWordsRepository profanityWordsRepository;

    static final List<ProfanityWords> profanityWordList =
        List.of(new ProfanityWords("bad"),
            new ProfanityWords("badd"),
            new ProfanityWords("baddd"),
            new ProfanityWords("badddd"));
    static final String inputWithProfanity = "This sentence contains bad and badbad and badd and badddd word";

    @BeforeEach
    void setUp() {
        lenient().when(profanityWordsRepository.findAll())
            .thenReturn(profanityWordList);
    }

    @Test
    void testAcceptedClass() {
        assertEquals(IsDisplayFields.class, displayFieldsMask.accept());
    }

    @Test
    void testValidEmailValidation() {
        String result = displayFieldsMask.mask(inputWithProfanity);
        assertEquals("This sentence contains ****** and badbad and ****** and ****** word", result);
    }
}
