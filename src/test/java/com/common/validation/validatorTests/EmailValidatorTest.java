package com.common.validation.validatorTests;

import com.common.validation.annotations.IsEmail;
import com.common.validation.utils.ValidationHelper;
import com.common.validation.validator.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("test")
public class EmailValidatorTest {
    @InjectMocks
    private EmailValidator emailValidator;

    static final String validEmailForValidation = "validEmail@gmail.com";
    static final String invalidEmailForValidation = "invalidEmail";
    static final Map<String, Object> emptyMapForEmailValidation = Map.of();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    void testValidEmailValidation() {
        Object result = emailValidator.validate(validEmailForValidation, emptyMapForEmailValidation);
        assertEquals(List.of(), result);
    }

    @Test
    void testInvalidEmailValidation() {
        Object result = emailValidator.validate(invalidEmailForValidation, emptyMapForEmailValidation);
        assertEquals(List.of("Invalid Email!"), result);

    }
}
