package com.common.validation.serviceTests;

import com.common.validation.annotations.IsDisplayFields;
import com.common.validation.annotations.IsEmail;
import com.common.validation.service.ValidatorService;
import com.common.validation.validator.EmailValidator;
import com.common.validation.validator.Validator;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class ValidatorServiceTest {

    @InjectMocks
    private ValidatorService validatorService;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private List<Validator> validators;

    @Data
    @AllArgsConstructor
    private class TestClass {
        @IsEmail
        private String emailTestingField;
        private String passwordTestingField;
    }

    static final String validEmail = "validEmail@gmail.com";
    static final String invalidEmail = "invalidEmail";
    static final String password = "password";

    TestClass validObj = new TestClass(validEmail, password);
    TestClass invalidObj = new TestClass(invalidEmail, password);


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validators = List.of(emailValidator);
        ReflectionTestUtils.setField(validatorService, "validators", validators);
        when(emailValidator.accept()).thenReturn(IsEmail.class);
        when(emailValidator.validate("invalidEmail", Map.of()))
            .thenReturn(List.of("Invalid Email!"));
    }

    @Test
    void testCheckAnnotationWithValidEmail() {
        List<String> result = validatorService.checkAnnotation(validObj);
        assertEquals(List.of(), result);
    }

    @Test
    void testCheckAnnotationWithInvalidEmail() {
        List<String> result = validatorService
            .checkAnnotation(invalidObj);
        assertEquals(List.of("Invalid Email!"), result);
    }

}
