package com.common.validation.serviceTests;

import com.common.validation.annotations.IsDisplayFields;
import com.common.validation.mask.DisplayFieldsMask;
import com.common.validation.mask.MaskingFields;
import com.common.validation.service.MaskingService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MaskingServiceTest {

    @InjectMocks
    private MaskingService maskingService;
    @Mock
    private DisplayFieldsMask displayFieldsMask;
    @Mock
    private List<MaskingFields> maskingFields;

    @Data
    @AllArgsConstructor
    private class TestClass {
        @IsDisplayFields
        private String basicStringField;
        @IsDisplayFields
        private List<String> stringListField;
        @IsDisplayFields
        private List<TestClassForNested> listFieldNestedObjects;
    }

    @Data
    @AllArgsConstructor
    private class TestClassForNested {
        private String firstNestedField;
        private String secondNestedField;
    }

    //first nested obj
    TestClassForNested nestedObj = new TestClassForNested("firstNestedField", "secondNestedField");
    //second nested obj
    TestClassForNested nestedObjWithBadWords = new TestClassForNested(
        "firstNestedField bad word",
        "secondNestedField bad bad word");

    TestClassForNested nestedObjWithMaskedWords = new TestClassForNested(
        "firstNestedField *** word",
        "secondNestedField *** *** word");
    List<TestClassForNested> listOfNestedClass = List.of(nestedObj, nestedObjWithBadWords);
    List<String> stringList = List.of("First String with Bad Word", "Second string without negative word");
    TestClass testClass = new TestClass("basicStringField bad word", stringList, listOfNestedClass);

    @BeforeEach
    void setUpMaskingServiceTest() {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("basicStringField bad word", "basicStringField *** word");
        replacements.put("First String with Bad Word", "First String with *** Word");
        replacements.put("Second string without negative word", "Second string without negative word");
        replacements.put("firstNestedField", "firstNestedField");
        replacements.put("secondNestedField", "secondNestedField");
        replacements.put("firstNestedField bad word", "firstNestedField *** word");
        replacements.put("Second string without negative word", "Second string without negative word");

        when(displayFieldsMask.mask(any(String.class))).thenAnswer(invocation -> {
            String input = invocation.getArgument(0, String.class);
            return replacements.getOrDefault(input, "secondNestedField *** *** word");
        });
    }

    @Test
    void testMaskingWithString() throws NoSuchFieldException {
        Field basicStringField = TestClass.class.getDeclaredField("basicStringField");
        TestClass testClassResult =
            new TestClass("basicStringField *** word", stringList, listOfNestedClass);
        basicStringField.setAccessible(true);
        TestClass result = (TestClass) maskingService
            .masking("basicStringField", basicStringField, testClass, displayFieldsMask);
        assertEquals(testClassResult.getStringListField(), result.getStringListField());
    }

    @Test
    void testMaskingListsOfString() throws NoSuchFieldException {
        Field basicStringField = TestClass.class.getDeclaredField("stringListField");
        List<String> listStringResult =
            List.of("First String with *** Word", "Second string without negative word");
        basicStringField.setAccessible(true);
        List<String> result = maskingService.maskingListsOfString(stringList, displayFieldsMask);
        assertEquals(listStringResult.get(1), result.get(1));
        assertEquals(listStringResult.get(0), result.get(0));
    }

    @Test
    void testMaskSensitiveFields() {
        maskingFields = List.of(displayFieldsMask);
        ReflectionTestUtils.setField(maskingService, "maskingFields", maskingFields);
        when(displayFieldsMask.accept()).thenReturn(IsDisplayFields.class);
        TestClass result = (TestClass) maskingService
            .maskSensitiveFields(testClass);

        assertEquals("firstNestedField", result.getListFieldNestedObjects().get(0).firstNestedField);
        assertEquals("secondNestedField", result.getListFieldNestedObjects().get(0).secondNestedField);
        assertEquals(nestedObjWithMaskedWords.getFirstNestedField(), result.getListFieldNestedObjects().get(1).firstNestedField);
        assertEquals(nestedObjWithMaskedWords.getSecondNestedField(), result.getListFieldNestedObjects().get(1).secondNestedField);
    }
}
