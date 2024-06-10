package com.common.validation.validator;


import java.util.List;
import java.util.Map;

public interface Validator {

    Class accept();

    List<String> validate(Object input, Map<String, Object> field);
}
