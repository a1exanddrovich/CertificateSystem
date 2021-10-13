package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.dtomapper.GiftCertificateDtoMapper;
import com.epam.esm.utils.Paginator;
import com.epam.esm.validator.GiftCertificateValidator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class GiftCertificateServiceTest {

    private static GiftCertificateDao giftCertificateDao;
    private static GiftCertificateTagDao giftCertificateTagDao;
    private static TagDao tagDao;
    private static OrderDao orderDao;
    private static GiftCertificateValidator validator;
    private static GiftCertificateDtoMapper dtoMapper;
    private static GiftCertificateDto expected;
    private static GiftCertificate expectedGiftCertificate;
    private static final long id = 2;
    private static GiftCertificateService service;
    private static Paginator paginator;

    @BeforeClass
    public static void init() {
        giftCertificateDao = Mockito.mock(GiftCertificateDao.class);
        giftCertificateTagDao = Mockito.mock(GiftCertificateTagDao.class);
        tagDao = Mockito.mock(TagDao.class);
        orderDao = Mockito.mock(OrderDao.class);
        validator = Mockito.mock(GiftCertificateValidator.class);
        dtoMapper = Mockito.mock(GiftCertificateDtoMapper.class);
        paginator = Mockito.mock(Paginator.class);
        service = new GiftCertificateService(giftCertificateDao, tagDao, validator, dtoMapper, paginator);

        Set<String> tags = new HashSet<>();
        Set<Tag> giftCertificateTags = new HashSet<>();
        tags.add("name1");
        tags.add("name2");
        giftCertificateTags.add(new Tag("name1"));
        giftCertificateTags.add(new Tag("name2"));


        expected = new GiftCertificateDto();
        expected.setId(id);
        expected.setName("testName");
        expected.setPrice(new BigDecimal(120));
        expected.setDescription("testDescription");
        expected.setDuration(20);
        expected.setCreationDate("2021-08-21T06:11:43.547Z");
        expected.setLastUpdateDate("2021-08-21T06:11:43.547Z");
        expected.setTags(tags);

        expectedGiftCertificate = new GiftCertificate();
        expectedGiftCertificate.setId(id);
        expectedGiftCertificate.setName("testName");
        expectedGiftCertificate.setDescription("testDescription");
        expectedGiftCertificate.setPrice(new BigDecimal(120));
        expectedGiftCertificate.setDuration(Duration.ofDays(20));
        expectedGiftCertificate.setCreationDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
        expectedGiftCertificate.setLastUpdateDate(ZonedDateTime.parse("2021-08-21T06:11:43.547Z"));
        expectedGiftCertificate.setTags(giftCertificateTags);

    }

    @Test
    public void testShouldReturnListOfGiftCertificates() throws EntityNotExistsException {
        //given
        List<GiftCertificate> expectedGiftCertificates = Arrays.asList(expectedGiftCertificate, expectedGiftCertificate);
        List<GiftCertificateDto> expectedGiftCertificateDtos = Arrays.asList(expected, expected);
        when(giftCertificateDao.getGiftCertificates(null, null, null, null, null, null, null)).thenReturn(expectedGiftCertificates);
        when(dtoMapper.map(expectedGiftCertificate)).thenReturn(expected);

        //when
        List<GiftCertificateDto> actual = service.getGiftCertificates(null, null, null, null, null, null, null);

        //then
        Assert.assertEquals(expectedGiftCertificateDtos, actual);
    }

    @Test
    public void testShouldThrowEntityNotExistsExceptionWhenInvalidIdPassed() {
        Assert.assertThrows(EntityNotExistsException.class, () -> {
            Mockito.when(giftCertificateDao.findById(id)).thenReturn(Optional.empty());
            service.getGiftCertificate(id);
        });
    }

    @Test
    public void testShouldGetGiftCertificateById() throws EntityNotExistsException {
        //given
        Mockito.when(dtoMapper.map(expectedGiftCertificate)).thenReturn(expected);
        Mockito.when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));

        //when
        GiftCertificateDto actual = service.getGiftCertificate(id);

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testShouldThrowEntityNotExistsExceptionWhenDeletingGiftCertificateWithInvalidId() {
        Assert.assertThrows(EntityNotExistsException.class, () -> {
            Mockito.when(giftCertificateDao.findById(id)).thenReturn(Optional.empty());

            service.deleteGiftCertificate(id);
        });
    }

    @Test
    public void testShouldDeleteGiftCertificateCorrectly() throws EntityNotExistsException {
        //when
        when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));
        doNothing().when(giftCertificateTagDao).deleteGiftCertificateById(id);
        doNothing().when(giftCertificateDao).deleteById(id);
        service.deleteGiftCertificate(id);

        //then
        Mockito.verify(giftCertificateDao, times(1)).deleteById(anyLong());
        Mockito.verify(giftCertificateTagDao, times(1)).deleteGiftCertificateById(anyLong());
    }

    @Test
    public void testShouldThrowBadEntityExceptionWhenInvalidGiftCertificatePassedWhileCreating() {
        Assert.assertThrows(BadEntityException.class, () -> {
            when(dtoMapper.unmap(expected)).thenReturn(expectedGiftCertificate);
            when(validator.validateCreate(expectedGiftCertificate)).thenReturn(false);

            service.createGiftCertificate(expected);
        });

    }

    @Test
    public void testShouldCreateGiftCertificateCorrectly() throws EntityNotExistsException, BadEntityException {
        //given
        when(dtoMapper.unmap(expected)).thenReturn(expectedGiftCertificate);
        when(validator.validateCreate(expectedGiftCertificate)).thenReturn(true);
        when(giftCertificateDao.create(expectedGiftCertificate)).thenReturn(id);
        when(tagDao.findTagByName(anyString())).thenReturn(Optional.of(new Tag()));
        doNothing().when(giftCertificateTagDao).createConnections(id, expectedGiftCertificate.getTags());
        when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));
        when(dtoMapper.map(expectedGiftCertificate)).thenReturn(expected);

        //when
        GiftCertificateDto actual = service.createGiftCertificate(expected);

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testShouldThrowEntityNotExistsExceptionWhenInvalidIdPassedAfterCreation() {
        Assert.assertThrows(EntityNotExistsException.class, () -> {
            when(dtoMapper.unmap(expected)).thenReturn(expectedGiftCertificate);
            when(validator.validateCreate(expectedGiftCertificate)).thenReturn(true);
            when(giftCertificateDao.create(expectedGiftCertificate)).thenReturn(id);
            when(tagDao.findTagByName(anyString())).thenReturn(Optional.of(new Tag()));
            doNothing().when(giftCertificateTagDao).createConnections(id, expectedGiftCertificate.getTags());
            when(giftCertificateDao.findById(id)).thenReturn(Optional.empty());

            service.createGiftCertificate(expected);
        });
    }

    @Test
    public void testShouldThrowEntityNotExistsExceptionWhenGiftCertificateWithInvalidIdPassed() {
        Assert.assertThrows(EntityNotExistsException.class, () -> {
            when(giftCertificateDao.findById(id)).thenReturn(Optional.empty());

            service.updateGiftCertificate(id, expected);
        });
    }

    @Test
    public void testShouldThrowBadEntityExceptionWhenInvalidGiftCertificatePassedWhileUpdating() {
        Assert.assertThrows(BadEntityException.class, () -> {
            when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));
            when(validator.validateUpdate(expectedGiftCertificate)).thenReturn(false);

            service.updateGiftCertificate(id, expected);
        });
    }

    @Test
    public void testShouldUpdateGiftCertificateCorrectly() throws EntityNotExistsException, BadEntityException {
        //given
        when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));
        when(dtoMapper.unmap(expected)).thenReturn(expectedGiftCertificate);
        when(validator.validateUpdate(expectedGiftCertificate)).thenReturn(true);
        when(tagDao.findTagByName(anyString())).thenReturn(Optional.of(new Tag()));
        doNothing().when(giftCertificateDao).updateGiftCertificate(id,expectedGiftCertificate);
        doNothing().when(giftCertificateTagDao).addTagId(anyLong(), anyLong());
        doNothing().when(giftCertificateTagDao).deleteTagId(anyLong(), anyLong());
        when(giftCertificateTagDao.getIdsBeforeUpdate(id)).thenReturn(new ArrayList<>());
        when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));
        when(dtoMapper.map(expectedGiftCertificate)).thenReturn(expected);

        //when
        GiftCertificateDto actual = service.updateGiftCertificate(id, expected);

        //then
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void testShouldThrowEntityNotExistsExceptionWhileUpdatingGiftCertificate() {
        Assert.assertThrows(EntityNotExistsException.class, () -> {
            when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));
            when(dtoMapper.unmap(expected)).thenReturn(expectedGiftCertificate);
            when(validator.validateUpdate(expectedGiftCertificate)).thenReturn(true);
            when(tagDao.findTagByName(anyString())).thenReturn(Optional.of(new Tag()));
            doNothing().when(giftCertificateDao).updateGiftCertificate(id,expectedGiftCertificate);
            doNothing().when(giftCertificateTagDao).addTagId(anyLong(), anyLong());
            doNothing().when(giftCertificateTagDao).deleteTagId(anyLong(), anyLong());
            when(giftCertificateTagDao.getIdsBeforeUpdate(id)).thenReturn(new ArrayList<>());
            when(giftCertificateDao.findById(id)).thenReturn(Optional.empty());

            service.updateGiftCertificate(id, expected);
        });
    }

}
