package com.common.validation.service;

import com.common.commonUtils.ListHelper;
import com.common.validation.mask.MaskingFields;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MaskingService {

    @Autowired
    private List<MaskingFields> maskingFields;

    //Mask and Set the value back into the field
    public Object masking(String fieldInput, Field field, Object object, MaskingFields maskingFields) {
        try {
            field.set(object, maskingFields.mask(fieldInput));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    //Handle list<String>
    public List<String> maskingListsOfString(List<String> listToBeProcessed, MaskingFields maskingFields) {
        return listToBeProcessed.stream()
            .map(maskingFields::mask)
            .collect(Collectors.toList());
    }

    //Handle Nested
    private void processNestedList(Field field, Object object, MaskingFields maskingFields) throws IllegalAccessException {
        List<?> nestedList = (List<?>) field.get(object);
        for (Object nestedObject : nestedList) {
            for (Field nestedField : nestedObject.getClass().getDeclaredFields()) {
                getFields(nestedField, nestedObject, maskingFields);
            }
        }
    }

    //Handle Each field
    private Object getFields(Field field, Object object, MaskingFields maskingFields) {
        try {
            field.setAccessible(true);
            if (ListHelper.isList(field)) {
                if (ListHelper.isListOfStrings(field)) {
                    field.set(object, maskingListsOfString((List<String>) field.get(object), maskingFields));
                    return object;
                } else {
                    processNestedList(field, object, maskingFields);
                }
            } else if (field.get(object) instanceof String) {
                return masking((String) field.get(object), field, object, maskingFields);
            }
        } catch (IllegalAccessException | InaccessibleObjectException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    //First check if field is annotated
    public Object maskSensitiveFields(Object object) {
        log.info("In MaskingService");
        Field[] fields = object.getClass().getDeclaredFields();
        for (MaskingFields maskingFields : maskingFields) {
            Class<? extends Annotation> annotationClass = maskingFields.accept();
            for (Field field : fields) {
                if (field.isAnnotationPresent(annotationClass)) {
                    try {
                        object = getFields(field, object, maskingFields);
                    } catch (InaccessibleObjectException e) {
                        log.info("Unable to access variable in masking service!");
                    }
                }
            }
        }
        return object;
    }
}
