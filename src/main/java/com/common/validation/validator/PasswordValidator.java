package com.common.validation.validator;


import com.common.validation.annotations.IsPassword;
import com.common.validation.repository.ProfanityWordsRepository;
import com.common.validation.utils.ListHelper;
import com.common.validation.utils.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PasswordValidator implements Validator {
    @Autowired
    private ProfanityWordsRepository profanityWordsRepository;

    public Class accept() {
        return IsPassword.class;
    }

    public List<String> validate(Object input, Map<String, Object> field) {
        return validateWithParam(input.toString(), (int) field.get("min"), (int) field.get("max"));
    }

    public List<String> validateWithParam(String input, int min, int max) {
        List<String> errorList = ListHelper.newList();
        if (!ValidationHelper.validateLength(input, min, max)) {
            errorList.add(String.format("Password Must be between %d and %d characters!", min, max));
        }
        if (!ValidationHelper.validateContainsSpecialCharacter(input)) {
            errorList.add("Password Must contains at least one special characters (ie. !*?$ )!");
        }
        return errorList;
    }
}
