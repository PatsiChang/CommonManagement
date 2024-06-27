package com.common.validation.validator;

import com.common.validation.annotations.IsEmail;
import com.common.validation.utils.ListHelper;
import com.common.validation.utils.ValidationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EmailValidator implements Validator {
    public Class accept() {
        return IsEmail.class;
    }

    public List<String> validate(Object input, Map<String, Object> field){
        log.info("In EmailValidator");
        List<String> errorList = ListHelper.newList();
        if (!ValidationHelper.validateValidEmail(input.toString())) {
            errorList.add("Invalid Email!");
            return errorList;
        }
        return List.of();
    };

}