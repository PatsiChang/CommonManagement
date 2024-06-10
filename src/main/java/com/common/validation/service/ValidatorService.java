package com.common.validation.service;

import com.common.validation.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidatorService {

    @Autowired
    private List<Validator> validators;

    public List<String> callValidator(Object object) {
        List<String> errorListAll = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            for (Validator validator : validators) {
                try {
                    Class<? extends Annotation> annotationClass = validator.accept();
                    if (field.isAnnotationPresent(annotationClass)) {
                        Annotation annotation = field.getAnnotation(annotationClass);
                        String fieldInput = (String) field.get(object);
                        Map<String, Object> annotationParamMap = new HashMap<>();
                        for (Method method : annotationClass.getDeclaredMethods()) {
                            Object value = method.invoke(annotation);
                            annotationParamMap.put(method.getName(), value);
                        }
                        List<String> error = validator.validate(fieldInput, annotationParamMap);
                        if (error != null) {
                            errorListAll.addAll(error);
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return errorListAll;
    }
}