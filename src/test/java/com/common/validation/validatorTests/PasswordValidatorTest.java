package com.common.validation.validatorTests;

import com.common.validation.annotations.IsPassword;
import com.common.validation.utils.ValidationHelper;
import com.common.validation.validator.PasswordValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PasswordValidatorTest extends CommonValidatorTest{
    @InjectMocks
    private PasswordValidator PasswordValidator;

    static final String validPasswordForValidation = "validPwd!";
    static final String invalidPasswordForValidation = "inValidPasswordWithoutSpecialCharacter";

    @BeforeAll
    static void setUpPasswordValidatorTest() {
        mapForValidation.put("min", Integer.valueOf(5));
        mapForValidation.put("max", Integer.valueOf(10));
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
    void testValidateWithValidValue() {
        List<String> result = PasswordValidator.validate(validPasswordForValidation, mapForValidation);
        assertEquals(List.of(), result);
    }

    @Test
    void testValidateWithInvalidValue() {
        List<String> result = PasswordValidator.validate(invalidPasswordForValidation, mapForValidation);
        assertEquals(List.of("Password Must be between 5 and 10 characters!",
            "Password Must contains at least one special characters (ie. !*?$ )!"), result);
    }

}
