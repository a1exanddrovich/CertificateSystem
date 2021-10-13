package com.epam.esm.dao;

import com.epam.esm.config.TestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.utils.Constructor;
import com.epam.esm.utils.QueryConstructor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles(profiles = "dev")
public class GiftCertificareDaoTest {

    private static GiftCertificateDao dao;

    private static GiftCertificateTagDao giftCertificateTagDao;

    @BeforeClass
    public static void init() {
        EntityManager manager = null;
        JdbcTemplate template = new JdbcTemplate(new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("test-data.sql").build());
        giftCertificateTagDao = new GiftCertificateTagDao(manager);
        dao = new GiftCertificateDao(manager, new QueryConstructor(), new Constructor<>());
    }

    @Test
    public void testShouldFindById() {
        //given
        GiftCertificate expected = new GiftCertificate();
        long id = 2;
        expected.setId(2);
        expected.setName("Starbucks");
        expected.setDescription("Taste more than a thousand a kinds of coffee in Starbucks.");
        expected.setPrice(new BigDecimal("157.00"));
        expected.setDuration(Duration.ofDays(90));
        expected.setCreationDate(ZonedDateTime.parse("2021-08-20T06:11:43.547Z"));
        expected.setLastUpdateDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
        expected.setTags(Set.of(new Tag("Coffee"), new Tag("Shopping"), new Tag("New")));

        //when
        Optional<GiftCertificate> actual = dao.findById(id);

        //then
        Assert.assertEquals(expected, actual.get());
    }

    @Test
    @Rollback
    public void testShouldUpdateCertificates() {
        //given
        long id = 1;
        GiftCertificate updatedGiftCertificate = new GiftCertificate();
        updatedGiftCertificate.setName("test");
        updatedGiftCertificate.setPrice(new BigDecimal(130));
        updatedGiftCertificate.setDuration(Duration.ofDays(20));
        updatedGiftCertificate.setCreationDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
        updatedGiftCertificate.setLastUpdateDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));

        GiftCertificate expected = new GiftCertificate();
        expected.setId(1);
        expected.setName("test");
        expected.setDescription("Take a peek inside one of the state's most award-winning songs, located in iTunes.");
        expected.setPrice(new BigDecimal("130.00"));
        expected.setDuration(Duration.ofDays(20));
        expected.setCreationDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
        expected.setLastUpdateDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
        expected.setTags(Set.of(new Tag("Relax"), new Tag("Shopping"), new Tag("New")));


        //when
        dao.updateGiftCertificate(id, updatedGiftCertificate);
        Optional<GiftCertificate> actual = dao.findById(id);

        Assert.assertEquals(expected, actual.get());

    }

    @Test
    @Rollback
    public void testShouldCreateCertificate() {
        //given
        GiftCertificate expected = new GiftCertificate();
        expected.setId(8);
        expected.setName("new");
        expected.setDescription("description");
        expected.setPrice(new BigDecimal("330.00"));
        expected.setDuration(Duration.ofDays(200));
        expected.setCreationDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
        expected.setLastUpdateDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
        expected.setTags(Set.of(new Tag("Relax"), new Tag("Shopping"), new Tag("New")));

        //when
        long id = dao.create(expected);
        giftCertificateTagDao.createConnections(id, expected.getTags());
        Optional<GiftCertificate> actual = dao.findById(id);

        //then
        Assert.assertEquals(expected, actual.get());
    }

    @Test
    public void testShouldGetListOfGiftCertificates() {

        List<GiftCertificate> giftCertificates = dao.getGiftCertificates(null, null, null, null, null, null, null);

        Assert.assertNotNull(giftCertificates);
    }

    @Test
    @Rollback
    public void testShouldDeleteGiftCertificate() {
        //given
        long id = 5;

        //when
        giftCertificateTagDao.deleteGiftCertificateById(id);
        dao.deleteById(id);
        Optional<GiftCertificate> optionalGiftCertificate = dao.findById(id);

        //then
        Assert.assertTrue(optionalGiftCertificate.isEmpty());
    }

}