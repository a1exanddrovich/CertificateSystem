package com.epam.esm.dao;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.utils.QueryConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = SpringConfig.class)
@ActiveProfiles("dev")
public class GiftCertificateDaoTest {

    private QueryConstructor constructor;
    private GiftCertificateMapper mapper;
    private GiftCertificateDao dao;
    private GiftCertificate giftCertificate;
    @Autowired
    private JdbcTemplate template;

    @Before
    public void init() {
//        EmbeddedDatabase database = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("test-data.sql").build();
//        template = new JdbcTemplate(database);
        constructor = new QueryConstructor();
        mapper = new GiftCertificateMapper();
        giftCertificate = new GiftCertificate();
        dao = new GiftCertificateDao(template, mapper, constructor);
    }

    @Test
    public void testShouldFindById() {
        //given
        long id = 2;

        //when
        Optional<GiftCertificate> certificate = dao.findById(id);

        //then
        Assert.assertNotNull(certificate.get());
    }

    @Test
    @Rollback
    public void testShouldUpdateCertificates() {
        //given
        long id = 2;
        giftCertificate.setName("test");
        giftCertificate.setPrice(new BigDecimal(120));
        giftCertificate.setDuration(Duration.ofDays(20));

        //when
        dao.updateGiftCertificate(2, giftCertificate);

        //then
        verify(dao, times(1)).updateGiftCertificate(id, giftCertificate);
    }

    @Test
    @Rollback
    public void testShouldCreateGiftCertificate() {
        //given
        giftCertificate.setName("test");
        giftCertificate.setPrice(new BigDecimal(120));
        giftCertificate.setDuration(Duration.ofDays(20));

        //when
        long id = dao.create(giftCertificate);

        //then
        Assert.assertNotNull(dao.findById(id));
    }

    @Test
    public void testShouldGetGiftCertificates() {
        //when
        List<GiftCertificate> giftCertificates = dao.getGiftCertificates(null, null, null, null, null);

        //then
        Assert.assertNotNull(giftCertificates);
    }

    @Test
    @Rollback
    public void testShouldDeleteFromDatabase() {
        //given
        long id = 1;

        //when
        dao.deleteById(id);
        Optional<GiftCertificate> optionalGiftCertificate = dao.findById(id);

        //then
        Assert.assertNull(optionalGiftCertificate);
    }

}
