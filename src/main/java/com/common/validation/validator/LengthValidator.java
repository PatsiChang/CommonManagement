package com.common.validation.validator;

import com.common.commonUtils.ListHelper;
import com.common.validation.annotations.CheckLength;
import com.common.validation.utils.ValidationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class LengthValidator implements Validator {
    public Class accept() {
        return CheckLength.class;
    }

    public List<String> validate(Object input, Map<String, Object> field) {
        List<String> errorList = ListHelper.newList();
        try {
            int min = (int) field.get("min");
            int max = (int) field.get("max");
            String fieldName = (String) field.get("fieldName");

            if (!ValidationHelper.validateLength(input.toString(), min, max)) {
                errorList.add(String.format("%s Must be between %d and %d characters!", fieldName, min, max));
            }
            return errorList;
        } catch (Exception e) {
            log.info("Unable to get annotation field values!");
            return errorList;
        }

    }
}
