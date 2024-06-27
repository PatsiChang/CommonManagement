package com.common.validation.serviceTests;

import com.common.validation.service.MaskingService;
import com.common.validation.validator.EmailValidator;
import com.common.validation.validator.LengthValidator;
import com.common.validation.validator.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;

//@SpringBootTest(classes = MainApplication.class)
@ActiveProfiles("test")
public class ValidatorServiceTest {

    @InjectMocks
    private MaskingService maskingService;

    @Mock
    private EmailValidator emailValidator;
    @Mock
    private LengthValidator lengthValidator;
    @Mock
    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
    }


    //Check Email Validation
    //Check Length Validation
    //Check Password Validation


}
