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
import java.util.stream.Collectors;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class GiftCertificateServiceTest {

    private static GiftCertificateDao giftCertificateDao;
    private static TagDao tagDao;
    private static GiftCertificateValidator validator;
    private static GiftCertificateDtoMapper dtoMapper;
    private static GiftCertificateDto expectedGiftCertificateDto;
    private static GiftCertificate expectedGiftCertificate;
    private static final long id = 2;
    private static GiftCertificateService service;
    private static Paginator paginator;

    @BeforeClass
    public static void init() {
        giftCertificateDao = Mockito.mock(GiftCertificateDao.class);
        tagDao = Mockito.mock(TagDao.class);
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


        expectedGiftCertificateDto = new GiftCertificateDto();
        expectedGiftCertificateDto.setId(id);
        expectedGiftCertificateDto.setName("testName");
        expectedGiftCertificateDto.setPrice(new BigDecimal(120));
        expectedGiftCertificateDto.setDescription("testDescription");
        expectedGiftCertificateDto.setDuration(20);
        expectedGiftCertificateDto.setCreationDate("2021-08-21T06:11:43.547Z");
        expectedGiftCertificateDto.setLastUpdateDate("2021-08-21T06:11:43.547Z");
        expectedGiftCertificateDto.setTags(tags);

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


        //when
        when(dtoMapper.map(expectedGiftCertificate)).thenReturn(expectedGiftCertificateDto);
        when(dtoMapper.unmap(expectedGiftCertificateDto)).thenReturn(expectedGiftCertificate);
        when(giftCertificateDao.countGiftCertificates()).thenReturn(2);
        when(paginator.paginate(1,1, 2)).thenReturn(1);
        when(giftCertificateDao.getGiftCertificates(null, 1, 1)).thenReturn(expectedGiftCertificates);
        List<GiftCertificate> actual = service.getGiftCertificates( null, 1, 1).stream().map(dtoMapper::unmap).collect(Collectors.toList());

        //then
        Assert.assertEquals(expectedGiftCertificates, actual);

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
        Mockito.when(dtoMapper.map(expectedGiftCertificate)).thenReturn(expectedGiftCertificateDto);
        Mockito.when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));

        //when
        GiftCertificateDto actual = service.getGiftCertificate(id);

        //then
        Assert.assertEquals(expectedGiftCertificateDto, actual);
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
        doNothing().when(giftCertificateDao).deleteById(id);
        service.deleteGiftCertificate(id);

        //then
        Mockito.verify(giftCertificateDao, times(1)).deleteById(anyLong());
        //Mockito.verify(giftCertificateTagDao, times(1)).deleteGiftCertificateById(anyLong());
    }

    @Test
    public void testShouldThrowBadEntityExceptionWhenInvalidGiftCertificatePassedWhileCreating() {
        Assert.assertThrows(BadEntityException.class, () -> {
            when(dtoMapper.unmap(expectedGiftCertificateDto)).thenReturn(expectedGiftCertificate);
            when(validator.validateCreate(expectedGiftCertificate)).thenReturn(false);

            service.createGiftCertificate(expectedGiftCertificateDto);
        });

    }

    @Test
    public void testShouldCreateGiftCertificateCorrectly() throws EntityNotExistsException, BadEntityException {
        //given
        when(dtoMapper.unmap(expectedGiftCertificateDto)).thenReturn(expectedGiftCertificate);
        when(validator.validateCreate(expectedGiftCertificate)).thenReturn(true);
        when(giftCertificateDao.create(expectedGiftCertificate)).thenReturn(id);
        when(tagDao.findTagByName(anyString())).thenReturn(Optional.of(new Tag()));
        when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));
        when(dtoMapper.map(expectedGiftCertificate)).thenReturn(expectedGiftCertificateDto);

        //when
        GiftCertificateDto actual = service.createGiftCertificate(expectedGiftCertificateDto);

        //then
        Assert.assertEquals(expectedGiftCertificateDto, actual);
    }

    @Test
    public void testShouldThrowEntityNotExistsExceptionWhenInvalidIdPassedAfterCreation() {
        Assert.assertThrows(EntityNotExistsException.class, () -> {
            when(dtoMapper.unmap(expectedGiftCertificateDto)).thenReturn(expectedGiftCertificate);
            when(validator.validateCreate(expectedGiftCertificate)).thenReturn(true);
            when(giftCertificateDao.create(expectedGiftCertificate)).thenReturn(id);
            when(tagDao.findTagByName(anyString())).thenReturn(Optional.of(new Tag()));
            //doNothing().when(giftCertificateTagDao).createConnections(id, expectedGiftCertificate.getTags());
            when(giftCertificateDao.findById(id)).thenReturn(Optional.empty());

            service.createGiftCertificate(expectedGiftCertificateDto);
        });
    }

    @Test
    public void testShouldThrowEntityNotExistsExceptionWhenGiftCertificateWithInvalidIdPassed() {
        Assert.assertThrows(EntityNotExistsException.class, () -> {
            when(giftCertificateDao.findById(id)).thenReturn(Optional.empty());

            service.updateGiftCertificate(id, expectedGiftCertificateDto);
        });
    }

    @Test
    public void testShouldThrowBadEntityExceptionWhenInvalidGiftCertificatePassedWhileUpdating() {
        Assert.assertThrows(BadEntityException.class, () -> {
            when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));
            when(validator.validateUpdate(expectedGiftCertificate)).thenReturn(false);

            service.updateGiftCertificate(id, expectedGiftCertificateDto);
        });
    }

    @Test
    public void testShouldUpdateGiftCertificateCorrectly() throws EntityNotExistsException, BadEntityException {
        //given
        when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));
        when(dtoMapper.unmap(expectedGiftCertificateDto)).thenReturn(expectedGiftCertificate);
        when(validator.validateUpdate(expectedGiftCertificate)).thenReturn(true);
        when(tagDao.findTagByName(anyString())).thenReturn(Optional.of(new Tag()));
        doNothing().when(giftCertificateDao).updateGiftCertificate(expectedGiftCertificate);
        when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));
        when(dtoMapper.map(expectedGiftCertificate)).thenReturn(expectedGiftCertificateDto);

        //when
        GiftCertificateDto actual = service.updateGiftCertificate(id, expectedGiftCertificateDto);

        //then
        Assert.assertEquals(expectedGiftCertificateDto, actual);

    }

    @Test
    public void testShouldThrowEntityNotExistsExceptionWhileUpdatingGiftCertificate() {
        Assert.assertThrows(EntityNotExistsException.class, () -> {
            when(giftCertificateDao.findById(id)).thenReturn(Optional.of(expectedGiftCertificate));
            when(dtoMapper.unmap(expectedGiftCertificateDto)).thenReturn(expectedGiftCertificate);
            when(validator.validateUpdate(expectedGiftCertificate)).thenReturn(true);
            when(tagDao.findTagByName(anyString())).thenReturn(Optional.of(new Tag()));
            doNothing().when(giftCertificateDao).updateGiftCertificate(expectedGiftCertificate);
            when(giftCertificateDao.findById(id)).thenReturn(Optional.empty());

            service.updateGiftCertificate(id, expectedGiftCertificateDto);
        });
    }

}
