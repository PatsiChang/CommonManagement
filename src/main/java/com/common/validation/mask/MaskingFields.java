package com.common.validation.mask;


public interface MaskingFields {
    Class accept();

    String mask(String input);
}
