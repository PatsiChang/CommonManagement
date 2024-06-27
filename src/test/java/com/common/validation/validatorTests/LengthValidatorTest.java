package com.common.validation.validatorTests;

import com.common.validation.annotations.CheckLength;
import com.common.validation.utils.ValidationHelper;
import com.common.validation.validator.LengthValidator;
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
public class LengthValidatorTest {
    @InjectMocks
    private LengthValidator lengthValidator;

    static final String validInputLengthValidation = "validLength!";
    static final String invalidInputLengthValidation = "invalidLengthhhhhhhhhhhhhhhh";
    static final Map<String, Object> mapForLengthValidation = new HashMap<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapForLengthValidation.put("min", Integer.valueOf(5));
        mapForLengthValidation.put("max", Integer.valueOf(15));
        mapForLengthValidation.put("fieldName", "Input");
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
    void testValidLengthValidation() {
        Object result = lengthValidator.validate(validInputLengthValidation, mapForLengthValidation);
        assertEquals(List.of(), result);
    }

    @Test
    void testInvalidLengthValidation() {
        Object result = lengthValidator.validate(invalidInputLengthValidation, mapForLengthValidation);
        assertEquals(List.of("Input Must be between 5 and 15 characters!"), result);
    }
}
