package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagValidator {

    public boolean validate(Tag tag) {
        String name = tag.getName();

        return name != null && name.length() >= 3 && name.length() <= 50;
    }

}
