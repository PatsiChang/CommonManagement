package com.common.validation.service;

import com.common.validation.mask.MaskingFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaskingService {

    @Autowired
    private List<MaskingFields> maskingFields;

    //Mask and Set the value back into the field
    public Object masking(String fieldInput, Field field, Object object, MaskingFields maskingFields) {
        try {
            System.out.println("check"+maskingFields.mask(fieldInput));
            field.set(object, maskingFields.mask(fieldInput));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    //Handle list<String> masking
    public List<String> maskingLists(List<String> listToBeProcessed, MaskingFields maskingFields) {
        return listToBeProcessed.stream()
            .map(maskingFields::mask)
            .collect(Collectors.toList());
    }

    //Handle Each field
    public Object getFields(Field field, Object object, MaskingFields maskingFields) {
        try {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            if (fieldType.equals(List.class)) {
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> nestedType = (Class<?>) listType.getActualTypeArguments()[0];
                if (nestedType.equals(String.class)) {
                    return maskingLists((List<String>) field.get(object), maskingFields);
                } else {
                    List<?> nestedList = (List<?>) field.get(object);
                    Field[] fields = nestedList.get(0).getClass().getDeclaredFields();
                    for (Object nestedObject : nestedList) {
                        for (Field nestedField : fields) {
                            getFields(nestedField, nestedObject, maskingFields);
                        }
                    }
                }
            } else if (field.get(object) instanceof String) {
                return masking((String) field.get(object), field, object, maskingFields);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    //First check if field is annotated
    public Object maskSensitiveFields(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (MaskingFields maskingFields : maskingFields) {
            for (Field field : fields) {
                Class<? extends Annotation> annotationClass = maskingFields.accept();
                if (field.isAnnotationPresent(annotationClass)) {
                    object = getFields(field, object, maskingFields);
                }
            }
        }
        return object;
    }
}
