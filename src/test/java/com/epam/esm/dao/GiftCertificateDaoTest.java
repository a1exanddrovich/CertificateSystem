package com.epam.esm.dao;

import com.epam.esm.config.TestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.utils.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("dev")
@SpringBootTest(classes = TestConfig.class)
@SpringBootApplication
@Transactional
class GiftCertificateDaoTest {

    @Autowired
    private GiftCertificateDao dao;

    @Test
    void testShouldFindById() {
        //given
        GiftCertificate expected = new GiftCertificate();
        long id = 2;
        expected.setId(2);
        expected.setName("Starbucks");
        expected.setDescription("Taste more than a thousand a kinds of coffee in Starbucks.");
        expected.setPrice(new BigDecimal("57.00"));
        expected.setDuration(Duration.ofSeconds(4320000));
        expected.setCreationDate(LocalDateTime.parse("2021-08-21T07:11:43.547Z", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
        expected.setLastUpdateDate(LocalDateTime.parse("2021-08-27T10:15:28.472Z", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
        expected.setTags(Set.of(new Tag(1, "Coffee"), new Tag(8, "Recreation"), new Tag(4, "Relax")));

        //when
        Optional<GiftCertificate> actual = dao.findById(id);

        //then
        assertEquals(expected, actual.get());

    }

    @Test
    @Rollback
    void testShouldUpdateCertificates() {
        //given
        long id = 1;
        GiftCertificate updatedGiftCertificate = new GiftCertificate();
        updatedGiftCertificate.setId(id);
        updatedGiftCertificate.setName("test");
        updatedGiftCertificate.setDescription("Take a peek inside one of the state's most award-winning songs, located in iTunes.");
        updatedGiftCertificate.setPrice(new BigDecimal(130));
        updatedGiftCertificate.setDuration(Duration.ofDays(20));
        updatedGiftCertificate.setCreationDate(LocalDateTime.parse("2021-08-21T06:11:43.547Z", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
        updatedGiftCertificate.setLastUpdateDate(LocalDateTime.parse("2021-08-21T06:11:43.547Z", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
        updatedGiftCertificate.setTags(Set.of(new Tag(3, "Relax"), new Tag(4, "Shopping"), new Tag(8, "New")));

        GiftCertificate expected = new GiftCertificate();
        expected.setId(1);
        expected.setName("test");
        expected.setDescription("Take a peek inside one of the state's most award-winning songs, located in iTunes.");
        expected.setPrice(new BigDecimal(130));
        expected.setDuration(Duration.ofDays(20));
        expected.setCreationDate(LocalDateTime.parse("2021-08-21T06:11:43.547Z", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
        expected.setLastUpdateDate(LocalDateTime.parse("2021-08-21T06:11:43.547Z", DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
        expected.setTags(Set.of(new Tag(3, "Relax"), new Tag(4, "Shopping"), new Tag(8, "New")));


        //when
        dao.updateGiftCertificate(updatedGiftCertificate);
        Optional<GiftCertificate> actual = dao.findById(id);

        assertEquals(expected, actual.get());

    }

    @Test
     void testShouldGetListOfGiftCertificates() {

        List<GiftCertificate> giftCertificates = dao.getGiftCertificates(null, 1, 4);

        assertNotNull(giftCertificates);
    }

    @Test
    @Rollback
    void testShouldDeleteGiftCertificate() {
        //given
        long id = 1;

        //when
        dao.deleteById(id);
        Optional<GiftCertificate> optionalGiftCertificate = dao.findById(id);

        //then
        assertTrue(optionalGiftCertificate.isEmpty());
    }

}
