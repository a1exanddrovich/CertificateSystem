package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import java.math.BigDecimal;
import java.time.Duration;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class GiftCertificateValidatorTest {

    private static final String TEST_NAME = "testName";
    private static final String TEST_DESCRIPTION = "testDescription";

    private static final TagValidator tagValidator = Mockito.mock(TagValidator.class);
    private final GiftCertificateValidator validator = new GiftCertificateValidator(tagValidator);

    @BeforeClass
    public static void init() {
        when(tagValidator.validate(any())).thenReturn(true);
    }

    @Test
    public void testShouldReturnTrueWhenValidGiftCertificateGiven() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(TEST_NAME);
        giftCertificate.setDescription(TEST_DESCRIPTION);
        giftCertificate.setPrice(new BigDecimal(120));
        giftCertificate.setDuration(Duration.ofDays(50));

        //when
        boolean actual = validator.validateCreating(giftCertificate);

        //then
        Assert.assertTrue(actual);
    }

    @Test
    public void testShouldReturnFalseWhenInvalidGiftCertificateGiven() {
        //given
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(TEST_NAME);
        giftCertificate.setDescription(TEST_DESCRIPTION);
        giftCertificate.setPrice(new BigDecimal(-120));
        giftCertificate.setDuration(Duration.ofDays(50));

        //when
        boolean actual = validator.validateCreating(giftCertificate);

        //then
        Assert.assertFalse(actual);
    }

}
