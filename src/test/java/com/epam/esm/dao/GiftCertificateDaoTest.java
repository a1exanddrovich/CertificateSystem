package com.epam.esm.dao;

import com.epam.esm.config.TestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.utils.GiftCertificateQueryParameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@ActiveProfiles("dev")
public class GiftCertificateDaoTest {

    @Autowired
    private GiftCertificateDao dao;

    @Test
    public void testShouldFindById() {
        //given
        GiftCertificate expected = new GiftCertificate();
        long id = 2;
        expected.setId(2);
        expected.setName("Starbucks");
        expected.setDescription("Taste more than a thousand a kinds of coffee in Starbucks.");
        expected.setPrice(new BigDecimal("57.00"));
        expected.setDuration(Duration.ofSeconds(4320000));
        expected.setCreationDate(ZonedDateTime.parse("2021-08-21T07:11:43.547Z"));
        expected.setLastUpdateDate(ZonedDateTime.parse("2021-08-27T10:15:28.472Z"));
        expected.setTags(Set.of(new Tag(1, "Coffee"), new Tag(8, "Recreation"), new Tag(4,"Relax")));

        //when
        Optional<GiftCertificate> actual = dao.findById(id);

        //then
        Assert.assertEquals(expected, actual.get());
    }

//    @Test
////    @Rollback
//    public void testShouldUpdateCertificates() {
//        //given
//        long id = 1;
//        GiftCertificate updatedGiftCertificate = new GiftCertificate();
//        updatedGiftCertificate.setName("test");
//        updatedGiftCertificate.setPrice(new BigDecimal(130));
//        updatedGiftCertificate.setDuration(Duration.ofDays(20));
//        updatedGiftCertificate.setCreationDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
//        updatedGiftCertificate.setLastUpdateDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
//
//        GiftCertificate expected = new GiftCertificate();
//        expected.setId(1);
//        expected.setName("test");
//        expected.setDescription("Take a peek inside one of the state's most award-winning songs, located in iTunes.");
//        expected.setPrice(new BigDecimal("120.00"));
//        expected.setDuration(Duration.ofDays(20));
//        expected.setCreationDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
//        expected.setLastUpdateDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
//        expected.setTags(Set.of(new Tag("Relax"), new Tag("Shopping"), new Tag("New")));
//
//
//        //when
//        dao.updateGiftCertificate(updatedGiftCertificate);
//        Optional<GiftCertificate> actual = dao.findById(id);
//
//        Assert.assertEquals(expected, actual.get());
//
//    }

//    @Test
//    @Rollback
//    public void testShouldCreateCertificate() {
//        //given
//        Optional<GiftCertificate> giftCertificate = dao.findById(1);
//        GiftCertificate newGiftCertificate = new GiftCertificate();
//        if (giftCertificate.isPresent()) {
//            Set tags = new HashSet();
//            tags.add(new Tag("test"));
//            tags.add(new Tag("test1"));
//            tags.add(new Tag("yui"));
//            newGiftCertificate.setName(giftCertificate.get().getName());
//            newGiftCertificate.setPrice(giftCertificate.get().getPrice());
//            newGiftCertificate.setTags(tags);
//            newGiftCertificate.setDuration(giftCertificate.get().getDuration());
//            newGiftCertificate.setDescription("tetstedfghjkl");
//        }
//
//        //when
//        long id = dao.create(newGiftCertificate);
//        Optional<GiftCertificate> actual = dao.findById(id);
//
//        //then
//        Assert.assertEquals(newGiftCertificate, actual.get());
//    }

    @Test
    public void testShouldGetListOfGiftCertificates() {

        List<GiftCertificate> giftCertificates = dao.getGiftCertificates(new GiftCertificateQueryParameters("TestName", "description", null, "name", "desc"), 1, 1);

        Assert.assertNotNull(giftCertificates);
    }

//    @Test
//    @Rollback
//    public void testShouldDeleteGiftCertificate() {
//        //given
//        long id = 1;
//
//        //when
//        dao.deleteById(id);
//        Optional<GiftCertificate> optionalGiftCertificate = dao.findById(id);
//
//        //then
//        Assert.assertTrue(optionalGiftCertificate.isEmpty());
//    }

}