package com.common.validation.utilTests;

import com.common.validation.utils.ListHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
public class ListHelperTest {

    @Data
    @AllArgsConstructor
    private class TestClassListOfString {
        private List<String> listOfStringField;
        private String stringField;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TestClassListOfString testClassListOfString
            = new TestClassListOfString(List.of("listOfStringField"), "stringField");
    }

    @Test
    void testNewList() {
        assertEquals(new ArrayList<>(), ListHelper.newList());
    }

    @Test
    void testConvertObjectToList() {
        String[] array = {"a", "b", "c"};
        assertEquals(Arrays.asList("a", "b", "c"), ListHelper.convertObjectToList(array));
    }

    @Test
    void testIsListOfStrings() throws NoSuchFieldException, IllegalAccessException {
        Field testClassListOfString = TestClassListOfString.class.getDeclaredField("listOfStringField");
        assertEquals(true, ListHelper.isListOfStrings(testClassListOfString));
    }

    @Test
    void testIsNotListOfStrings() throws NoSuchFieldException, IllegalAccessException {
        Field testClassString = TestClassListOfString.class.getDeclaredField("stringField");
        assertEquals(false, ListHelper.isListOfStrings(testClassString));
    }

    @Test
    void testIsList() throws NoSuchFieldException {
        Field testClassString = TestClassListOfString.class.getDeclaredField("listOfStringField");
        assertEquals(true, ListHelper.isList(testClassString));
    }

    @Test
    void testIsNotList() throws NoSuchFieldException {
        Field testClassString = TestClassListOfString.class.getDeclaredField("stringField");
        assertEquals(false, ListHelper.isList(testClassString));
    }
}
