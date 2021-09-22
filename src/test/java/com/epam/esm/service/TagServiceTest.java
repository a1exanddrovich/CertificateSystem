package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.utils.Paginator;
import com.epam.esm.validator.TagValidator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    private static final String TEST_NAME = "testName";

    private static TagDao tagDao;
    private static GiftCertificateTagDao giftCertificateTagDao;
    private static TagValidator tagValidator;
    private static TagService service;
    private static Paginator paginator;

    @BeforeClass
    public static void init() {
        tagDao = Mockito.mock(TagDao.class);
        giftCertificateTagDao = Mockito.mock(GiftCertificateTagDao.class);
        tagValidator = Mockito.mock(TagValidator.class);
        paginator = Mockito.mock(Paginator.class);
        service = new TagService(tagDao, giftCertificateTagDao, tagValidator, paginator);
    }

    @Test
    public void testShouldReturnAllTags() {
        //given
        List<Tag> expected = Arrays.asList(new Tag(), new Tag());
        when(tagDao.findAll(1, 1)).thenReturn(expected);

        //when
        List<Tag> actual = service.getTags(1, 2);

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testShouldFindById() throws EntityNotExistsException {
        //given
        long id = 0L;
        Tag expected = new Tag();
        Mockito.when(tagDao.findById(anyLong())).thenReturn(Optional.of(expected));

        //when
        Tag actual = service.getTag(id);

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testShouldThrowEntityNotExistExceptionWhenDeletingTag() {
        Assert.assertThrows(EntityNotExistsException.class, () -> {
            long id = 2;
            Mockito.when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
            service.deleteTag(id);
        });
    }

    @Test
    public void testShouldCreateTag() throws EntityNotExistsException, EntityAlreadyExistsException, BadEntityException {
        //given
        long id = 1L;
        Tag expected = new Tag(id, TEST_NAME);
        when(tagValidator.validate(expected)).thenReturn(true);
        when(tagDao.findTagByName(anyString())).thenReturn(Optional.empty());
        when(tagDao.create(expected)).thenReturn(id);
        when(tagDao.findById(id)).thenReturn(Optional.of(expected));

        //when
        Tag actual = service.createTag(expected);
        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testShouldDeleteTagCorrectly() throws EntityNotExistsException {
        //given
        long id = 0;

        //when
        when(tagDao.findById(id)).thenReturn(Optional.of(new Tag()));
        doNothing().when(tagDao).deleteById(id);
        service.deleteTag(id);

        //then
        Mockito.verify(tagDao, times(1)).deleteById(anyLong());
        Mockito.verify(giftCertificateTagDao, times(1)).deleteByTagId(anyLong());
    }

    @Test
    public void testShouldThrowEntityNotExistExceptionWhenTagNotFound() {
        Assert.assertThrows(EntityNotExistsException.class, () -> {
            Optional<Tag> optionalTag = Optional.empty();
            Mockito.when(tagDao.findById(anyLong())).thenReturn(optionalTag);
            service.getTag(1L);
        });
    }

    @Test
    public void testShouldThrowBadEntityExceptionWhenInvalidTagPassed() {
        Assert.assertThrows(BadEntityException.class, () -> {
            Tag tag = new Tag();
            Mockito.when(tagValidator.validate(tag)).thenReturn(false);
            service.createTag(tag);
        });
    }

    @Test
    public void testShouldThrowEntityAlreadyExistExceptionWhenTagWithExistNamePassed() {
        Assert.assertThrows(EntityAlreadyExistsException.class, () -> {
            Tag tag = new Tag(TEST_NAME);
            Mockito.when(tagValidator.validate(anyObject())).thenReturn(true);
            Mockito.when(tagDao.findTagByName(tag.getName())).thenReturn(Optional.of(tag));

            service.createTag(tag);
        });
    }

}
