package com.common.validation.service;

import com.common.validation.mask.MaskingFields;
import com.common.validation.utils.ListHelper;
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

    private List<String> callValidator(Field field, Object object, Validator validator
        , Map<String, Object> annotationParamMap) {
        List<String> errorListAll = new ArrayList<>();
        try {
            Object fieldValue = field.get(object);
            if (ListHelper.isList(field) && ListHelper.isListOfStrings(field)) {
                List<String> stringList = (List<String>) fieldValue;
                for (String str : stringList) {
                    List<String> error = validator.validate(str, annotationParamMap);
                    if (error.size() != 0) {
                        errorListAll.addAll(error);
                        return errorListAll;
                    }
                }
            } else if (fieldValue instanceof String) {
                return validator.validate(fieldValue, annotationParamMap);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return errorListAll;
    }

    private Map<String, Object> extractAnnotationParams(Annotation annotation,
                                                        Class<? extends Annotation> annotationClass)
        throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> annotationParamMap = new HashMap<>();
        for (Method method : annotationClass.getDeclaredMethods()) {
            Object value = method.invoke(annotation);
            annotationParamMap.put(method.getName(), value);
        }
        return annotationParamMap;
    }

    public List<String> checkAnnotation(Object object) {
        List<String> errorListAll = ListHelper.newList();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Validator validator : validators) {
            for (Field field : fields) {
                Class<? extends Annotation> annotationClass = validator.accept();
                if (field.isAnnotationPresent(annotationClass)) {
                    Annotation annotation = field.getAnnotation(annotationClass);
                    try {
                        field.setAccessible(true);
                        errorListAll = callValidator(field, object, validator,
                            extractAnnotationParams(annotation, annotationClass));
                        System.out.println(errorListAll);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return errorListAll;
    }
}