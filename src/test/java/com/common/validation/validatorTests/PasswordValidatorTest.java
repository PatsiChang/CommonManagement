package com.common.validation.validatorTests;

import com.common.validation.annotations.IsPassword;
import com.common.validation.utils.ValidationHelper;
import com.common.validation.validator.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("test")
public class PasswordValidatorTest {
    @InjectMocks
    private PasswordValidator PasswordValidator;

    static final String validPasswordForValidation = "validPwd!";
    static final String invalidPasswordForValidation = "inValidPasswordWithoutSpecialCharacter";
    static final Map<String, Object> mapForPasswordValidation = new HashMap<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapForPasswordValidation.put("min", Integer.valueOf(5));
        mapForPasswordValidation.put("max", Integer.valueOf(10));
        try (MockedStatic<ValidationHelper> fileHelper = Mockito.mockStatic(ValidationHelper.class)) {
            fileHelper.when(() -> ValidationHelper.validateLength("validPwd!", 5, 10))
                .thenReturn(true);
            fileHelper.when(() -> ValidationHelper.validateLength("inValidPasswordWithoutSpecialCharacter", 5, 10))
                .thenReturn(false);
            fileHelper.when((() -> ValidationHelper
                    .validateContainsSpecialCharacter("validPwd!")))
                .thenReturn(true);
            fileHelper.when((() -> ValidationHelper
                    .validateContainsSpecialCharacter("inValidPasswordWithoutSpecialCharacter")))
                .thenReturn(false);
        }
    }

    @Test
    void testAcceptedClass() {
        assertEquals(IsPassword.class, PasswordValidator.accept());
    }

    @Test
    void testWithValidPassword() {
        List<String> result = PasswordValidator.validate(validPasswordForValidation, mapForPasswordValidation);
        assertEquals(List.of(), result);
    }

    @Test
    void testWithInvalidPassword() {
        List<String> result = PasswordValidator.validate(invalidPasswordForValidation, mapForPasswordValidation);
        assertEquals(List.of("Password Must be between 5 and 10 characters!",
            "Password Must contains at least one special characters (ie. !*?$ )!"), result);
    }

}
