package com.common.validation.validatorTests;

import com.common.validation.annotations.IsEmail;
import com.common.validation.utils.ValidationHelper;
import com.common.validation.validator.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmailValidatorTest extends CommonValidatorTest{
    @InjectMocks
    private EmailValidator emailValidator;

    static final String validEmailForValidation = "validEmail@gmail.com";
    static final String invalidEmailForValidation = "invalidEmail";

    @BeforeEach
    void setUp() {
        try (MockedStatic<ValidationHelper> fileHelper = Mockito.mockStatic(ValidationHelper.class)) {
            fileHelper.when(() -> ValidationHelper.validateValidEmail(validEmailForValidation))
                .thenReturn(true);
            fileHelper.when(() -> ValidationHelper.validateValidEmail(invalidEmailForValidation))
                .thenReturn(false);
        }
    }

    @Test
    void testAcceptedClass() {
        assertEquals(IsEmail.class, emailValidator.accept());
    }

    @Test
    void testValidateWithValidValue() {
        Object result = emailValidator.validate(validEmailForValidation, mapForValidation);
        assertEquals(List.of(), result);
    }

    @Test
    void testValidateWithInvalidValue() {
        Object result = emailValidator.validate(invalidEmailForValidation, mapForValidation);
        assertEquals(List.of("Invalid Email!"), result);

    }
}
