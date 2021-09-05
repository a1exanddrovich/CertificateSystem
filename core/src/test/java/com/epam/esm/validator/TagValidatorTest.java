package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import org.junit.Assert;
import org.junit.Test;

public class TagValidatorTest {

    private static final String TEST_NAME_VALID = "testNameValid";
    private static final String TEST_NAME_INVALID = "te";

    private final TagValidator validator = new TagValidator();

    @Test
    public void testShouldReturnTrueWhenValidTagGiven() {
        //given
        Tag tag = new Tag(TEST_NAME_VALID);

        //when
        boolean actual = validator.validate(tag);

        //then
        Assert.assertTrue(actual);
    }

    @Test
    public void testShouldReturnFalseWhenInvalidTagGiven() {
        //given
        Tag tag = new Tag(TEST_NAME_INVALID);

        //when
        boolean actual = validator.validate(tag);

        //then
        Assert.assertFalse(actual);
    }

}
