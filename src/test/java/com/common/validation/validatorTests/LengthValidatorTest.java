package com.common.validation.validatorTests;

import com.common.validation.annotations.CheckLength;
import com.common.validation.utils.ValidationHelper;
import com.common.validation.validator.LengthValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LengthValidatorTest extends CommonValidatorTest{
    @InjectMocks
    private LengthValidator lengthValidator;

    static final String validInputLengthValidation = "validLength!";
    static final String invalidInputLengthValidation = "invalidLengthhhhhhhhhhhhhhhh";

    @BeforeAll
    static void setUpLengthValidatorTest() {
        mapForValidation.put("min", Integer.valueOf(5));
        mapForValidation.put("max", Integer.valueOf(15));
        mapForValidation.put("fieldName", "Input");
        try (MockedStatic<ValidationHelper> fileHelper = Mockito.mockStatic(ValidationHelper.class)) {
            fileHelper.when(() -> ValidationHelper.validateLength(validInputLengthValidation, 5, 15))
                .thenReturn(true);
            fileHelper.when(() -> ValidationHelper.validateLength(invalidInputLengthValidation, 5, 15))
                .thenReturn(false);
        }
    }

    @Test
    void testAcceptedClass() {
        assertEquals(CheckLength.class, lengthValidator.accept());
    }


    @Test
    void testValidateWithValidValue() {
        Object result = lengthValidator.validate(validInputLengthValidation, mapForValidation);
        assertEquals(List.of(), result);
    }

    @Test
    void testValidateWithInvalidValue() {
        Object result = lengthValidator.validate(invalidInputLengthValidation, mapForValidation);
        assertEquals(List.of("Input Must be between 5 and 15 characters!"), result);
    }
}
