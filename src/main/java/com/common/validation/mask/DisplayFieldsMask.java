package com.common.validation.mask;

import com.common.validation.annotations.IsDisplayFields;
import com.common.validation.repository.ProfanityWordsRepository;
import com.common.validation.utils.MaskingHelper;
import com.common.validation.utils.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DisplayFieldsMask implements MaskingFields {
    @Autowired
    private ProfanityWordsRepository profanityWordsRepository;

    public Class accept() {
        return IsDisplayFields.class;
    }

    public String mask(String input) {
        List<String> profanityWordList = profanityWordsRepository.findAll().stream()
            .map(profanityWords -> profanityWords.getWord())
            .toList();
        return MaskingHelper.maskAllProfanity(String.valueOf(input), profanityWordList);
    }
}
