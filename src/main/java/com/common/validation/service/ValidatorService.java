package com.common.validation.service;

import com.common.validation.mask.MaskingFields;
import com.common.validation.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidatorService {

    @Autowired
    private List<Validator> validators;

    public List<String> callValidator(Field field, Object object, Validator validator
        , Map<String, Object> annotationParamMap) {
        List<String> errorListAll = new ArrayList<>();
        try {
            if (field.getType().equals(List.class)) {
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> nestedType = (Class<?>) listType.getActualTypeArguments()[0];
                if (nestedType.equals(String.class)) {
                    List<String> fieldValue = (List<String>) field.get(object);
                    for (String str : fieldValue) {
                        List<String> error = validator.validate(str, annotationParamMap);
                        if (error.size() != 0) {
                            errorListAll.addAll(error);
                            return errorListAll;
                        }
                    }
                }
            } else if (field.get(object) instanceof String) {
                return validator.validate(field.get(object), annotationParamMap);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return errorListAll;
    }

    public List<String> checkAnnotation(Object object) {
        List<String> errorListAll = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Validator validator : validators) {
            for (Field field : fields) {
                Class<? extends Annotation> annotationClass = validator.accept();
                if (field.isAnnotationPresent(annotationClass)) {
                    Annotation annotation = field.getAnnotation(annotationClass);
                    try {
                        field.setAccessible(true);
                        Map<String, Object> annotationParamMap = new HashMap<>();
                        for (Method method : annotationClass.getDeclaredMethods()) {
                            Object value = method.invoke(annotation);
                            annotationParamMap.put(method.getName(), value);
                        }
                        errorListAll = callValidator(field, object, validator, annotationParamMap);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return errorListAll;
    }
}