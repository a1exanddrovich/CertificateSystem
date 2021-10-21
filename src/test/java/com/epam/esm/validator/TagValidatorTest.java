package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TagValidatorTest {

    private static final String TEST_NAME_VALID = "testNameValid";
    private static final String TEST_NAME_INVALID = "te";

    private final TagValidator validator = new TagValidator();

    @Test
    void testShouldReturnTrueWhenValidTagGiven() {
        //given
        Tag tag = new Tag(TEST_NAME_VALID);

        //when
        boolean actual = validator.validate(tag);

        //then
        assertTrue(actual);
    }

    @Test
    void testShouldReturnFalseWhenInvalidTagGiven() {
        //given
        Tag tag = new Tag(TEST_NAME_INVALID);

        //when
        boolean actual = validator.validate(tag);

        //then
        assertFalse(actual);
    }

}
