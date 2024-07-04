package com.common.validation.serviceTests;

import com.common.validation.annotations.IsDisplayFields;
import com.common.validation.mask.DisplayFieldsMask;
import com.common.validation.mask.MaskingFields;
import com.common.validation.service.MaskingService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
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
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(displayFieldsMask.mask("basicStringField bad word"))
            .thenReturn("basicStringField *** word");
        when(displayFieldsMask.mask("First String with Bad Word"))
            .thenReturn("First String with *** Word");
        when(displayFieldsMask.mask("Second string without negative word"))
            .thenReturn("Second string without negative word");
        //Nested
        when(displayFieldsMask.mask("firstNestedField"))
            .thenReturn("firstNestedField");
        when(displayFieldsMask.mask("secondNestedField"))
            .thenReturn("secondNestedField");
        when(displayFieldsMask.mask("firstNestedField bad word"))
            .thenReturn("firstNestedField *** word");
        when(displayFieldsMask.mask("Second string without negative word"))
            .thenReturn("Second string without negative word");
        when(displayFieldsMask.mask("secondNestedField bad bad word"))
            .thenReturn("secondNestedField *** *** word");
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
    void testMaskSensitiveFields() throws NoSuchFieldException {
//        maskingService = spy(new MaskingService());
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
