package com.common.validation.validatorTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public abstract class CommonValidatorTest {

    static Map<String, Object> mapForValidation = new HashMap<>();

    @Test
    abstract void testAcceptedClass();
    @Test
    abstract void testValidateWithValidValue();
    @Test
    abstract void testValidateWithInvalidValue();



}
