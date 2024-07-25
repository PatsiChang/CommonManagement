package com.common.validation.serviceTests;

import com.common.validation.annotations.IsEmail;
import com.common.validation.service.ValidatorService;
import com.common.validation.validator.EmailValidator;
import com.common.validation.validator.Validator;
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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
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
    void setUpValidatorServiceTest() {
        validators = List.of(emailValidator);
        ReflectionTestUtils.setField(validatorService, "validators", validators);
        when(emailValidator.accept()).thenReturn(IsEmail.class);

        // Stub the validate method for any String argument and an empty map
        when(emailValidator.validate(any(String.class), eq(Map.of()))).thenAnswer(invocation -> {
            String email = invocation.getArgument(0, String.class);
            if ("validEmail@gmail.com".equals(email)) {
                return List.of();
            } else {
                return List.of("Invalid Email!");
            }
        });
    }

    @Test
    void testCheckAnnotationWithValidEmail() {
        List<String> result = validatorService.checkAnnotation(validObj);
        assertEquals(List.of(), result);
    }

    @Test
    void testCheckAnnotationWithInvalidEmail() {
        List<String> result = validatorService.checkAnnotation(invalidObj);
        assertEquals(List.of("Invalid Email!"), result);
    }

}
