package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.utils.GiftCertificateDtoMapper;
import com.epam.esm.validator.GiftCertificateValidator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class GiftCertificateServiceTest {

    private static GiftCertificateDao giftCertificateDao;
    private static GiftCertificateTagDao giftCertificateTagDao;
    private static TagDao tagDao;
    private static GiftCertificateValidator validator;
    private static GiftCertificateDtoMapper dtoMapper;
    private static GiftCertificate giftCertificate;
    private static Environment environment;
    private static GiftCertificateDto giftCertificateDto;
    private static GiftCertificateService service;
    private static Optional<GiftCertificate> optionalGiftCertificate;
    private static List<GiftCertificate> giftCertificates;
    private static List<GiftCertificateDto> giftCertificateDtos;

    @BeforeClass
    public static void init() {
        giftCertificateDao = Mockito.mock(GiftCertificateDao.class);
        giftCertificateTagDao = Mockito.mock(GiftCertificateTagDao.class);
        tagDao = Mockito.mock(TagDao.class);
        validator = Mockito.mock(GiftCertificateValidator.class);
        dtoMapper = Mockito.mock(GiftCertificateDtoMapper.class);
        giftCertificate = Mockito.mock(GiftCertificate.class);
        giftCertificateDto = Mockito.mock(GiftCertificateDto.class);
        environment = Mockito.mock(Environment.class);
        service = new GiftCertificateService(giftCertificateDao,giftCertificateTagDao, tagDao, validator, dtoMapper, environment);
        optionalGiftCertificate = Optional.of(giftCertificate);
        giftCertificates = new ArrayList<>();
        giftCertificateDtos = new ArrayList<>();
        when(giftCertificateDao.findById(anyLong())).thenReturn(optionalGiftCertificate);
        when(dtoMapper.map(any())).thenReturn(giftCertificateDto);
        when(dtoMapper.unmap(any())).thenReturn(giftCertificate);
        when(validator.validateCreating(any())).thenReturn(true);
        when((validator.validateUpdating(any()))).thenReturn(true);
        when(giftCertificateDao.getGiftCertificates(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(giftCertificates);
    }

    @Test
    public void testShouldReturnGiftCertificate() throws EntityNotExistsException {
        //when
        GiftCertificateDto giftCertificateDto = service.getGiftCertificate(1L);

        //then
        Assert.assertNotNull(giftCertificateDto);
    }


    @Test
    public void testShouldDeleteEntity() throws EntityNotExistsException {
        //when
        service.deleteEntity(1L);

        //then
        verify(giftCertificateTagDao, times(1)).deleteGiftCertificateById(anyLong());
        verify(giftCertificateDao, times(1)).deleteById(anyLong());
    }

    @Test
    public void testShouldCreateGiftCertificate() throws EntityNotExistsException, BadEntityException {
        //when
        service.createGiftCertificate(giftCertificateDto);

        //then
        verify(giftCertificateDao, times(1)).create(any());
    }

    @Test
    public void testShouldUpdateGiftCertificate() throws EntityNotExistsException, BadEntityException {
        //when
        service.updateGiftCertificate(1L, giftCertificateDto);

        //then
        verify(giftCertificateDao, times(1)).updateGiftCertificate(anyLong(), any());
    }

    @Test
    public void testShouldReturnGiftCertificates() {
        //when
        List<GiftCertificateDto> dtos = service.getGiftCertificates(anyString(), anyString(), anyString(), anyString(), anyString());

        //then
        Assert.assertNotNull(dtos);
    }

}
