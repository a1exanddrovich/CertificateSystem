package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.validator.TagValidator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class TagServiceTest {

    private static TagDao tagDao;
    private static GiftCertificateTagDao giftCertificateTagDao;
    private static TagValidator tagValidator;
    private static TagService service;
    private static Tag testTag;
    private static Optional<Tag> optionalTag;

    @BeforeClass
    public static void init() throws EntityNotExistsException {
        tagDao = Mockito.mock(TagDao.class);
        testTag = Mockito.mock(Tag.class);
        giftCertificateTagDao = Mockito.mock(GiftCertificateTagDao.class);
        tagValidator = Mockito.mock(TagValidator.class);
        service = new TagService(tagDao, giftCertificateTagDao, tagValidator);
        optionalTag = Optional.of(testTag);
        when(tagValidator.validate(any())).thenReturn(true);
        when(tagDao.findById(anyLong())).thenReturn(optionalTag);
        when(tagDao.findTagByName(anyString())).thenReturn(optionalTag);
        when(service.getTag(anyLong())).thenReturn(testTag);
        Mockito.doNothing().when(tagDao).deleteById(anyLong());
    }

    @Test
    public void testShouldReturnAllTags() {
        //given
        when(tagDao.findAll()).thenReturn(new ArrayList<>());

        //when
        List<Tag> tags = service.getTags();

        //then
        Assert.assertNotNull(tags);
    }

    @Test
    public void testShouldFindById() throws EntityNotExistsException {
        //when
        Tag tag = service.getTag(1L);

        //then
        Assert.assertNotNull(tag);
    }

    @Test
    public void testShouldCreateTag() throws EntityNotExistsException, EntityAlreadyExistsException, BadEntityException {
        //given
        Mockito.doNothing().when(tagDao.create(any()));

        //when
        Tag tag = service.createTag(new Tag());

        //then
        Assert.assertNotNull(tag);
    }

    @Test
    public void testShouldDeleteTag() throws EntityNotExistsException {
        //given
        service.deleteEntity(1L);

        //when
        Mockito.verify(tagDao, times(1)).deleteById(anyLong());
        Mockito.verify(giftCertificateTagDao, times(1)).deleteByTagId(anyLong());
    }

}
