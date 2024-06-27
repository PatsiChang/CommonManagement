package com.common.validation.service;

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

    private Object invokeGetter(Object obj, Field field) {
        try {
            String fieldName = field.getName();
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method getter = obj.getClass().getMethod(getterName);
            return getter.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke getter for field: " + field.getName(), e);
        }
    }


    //Mask and Set the value back into the field
    public Object masking(String fieldInput, Field field, Object object, MaskingFields maskingFields) {
        try {
            field.set(object, maskingFields.mask(fieldInput));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    private boolean isStaticOrSerialVersionUID(Field field) {
        return Modifier.isStatic(field.getModifiers()) || "serialVersionUID".equals(field.getName())
            || Modifier.isTransient(field.getModifiers());
    }

    //Handle list<String> masking
    public List<String> maskingListsOfString(List<String> listToBeProcessed, MaskingFields maskingFields) {
        return listToBeProcessed.stream()
            .map(stringToBeProcessed -> maskingFields.mask(stringToBeProcessed))
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
                    field.set(object, maskingListsOfString((List<String>) field.get(object), maskingFields));
                    return object;
                } else {
                    List<?> nestedList = (List<?>) field.get(object);
                    for (Object nestedObject : nestedList) {
                        for (Field nestedField : nestedObject.getClass().getDeclaredFields()) {
                            if (!isStaticOrSerialVersionUID(nestedField)) {
                                getFields(nestedField, nestedObject, maskingFields);
                            }
                        }
                    }
                }
            } else if (field.get(object) instanceof String) {
                return masking((String) invokeGetter(object, field), field, object, maskingFields);
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
            for (Field field : fields) {
                Class<? extends Annotation> annotationClass = maskingFields.accept();
                if (field.isAnnotationPresent(annotationClass)) {
                    if (!field.canAccess(object)) {
                        try {
                            field.setAccessible(true);
                            object = getFields(field, object, maskingFields);
                        } catch (InaccessibleObjectException e) {
                        }
                    }
                }
            }
        }
        return object;
    }
}
