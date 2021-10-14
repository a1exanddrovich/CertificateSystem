package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dtomapper.TagDtoMapper;
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
    private static TagDtoMapper mapper;
    //private static GiftCertificateTagDao giftCertificateTagDao;
    private static TagValidator tagValidator;
    private static TagService service;
    private static Paginator paginator;

    @BeforeClass
    public static void init() {
        tagDao = Mockito.mock(TagDao.class);
        //giftCertificateTagDao = Mockito.mock(GiftCertificateTagDao.class);
        mapper = Mockito.mock(TagDtoMapper.class);
        tagValidator = Mockito.mock(TagValidator.class);
        paginator = Mockito.mock(Paginator.class);
        service = new TagService(tagDao, tagValidator, paginator, mapper);
    }

    @Test
    public void testShouldReturnAllTags() {
        //given
        List<Tag> expected = Arrays.asList(new Tag(), new Tag());
        when(tagDao.findAll(1, 1)).thenReturn(expected);

        //when
        List<TagDto> actual = service.getTags(1, 2);

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
        TagDto actual = service.getTag(id);

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
        TagDto expected = new TagDto(id, TEST_NAME);
        when(tagValidator.validate(mapper.unmap(expected))).thenReturn(true);
        when(tagDao.findTagByName(anyString())).thenReturn(Optional.empty());
        when(tagDao.create(mapper.unmap(expected))).thenReturn(id);
        when(tagDao.findById(id)).thenReturn(Optional.of(mapper.unmap(expected)));

        //when
        TagDto actual = service.createTag(expected);
        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testShouldDeleteTagCorrectly() throws EntityNotExistsException {
        //given
        long id = 0;

        //when
        when(tagDao.findById(id)).thenReturn(Optional.of(new Tag()));
//        doNothing().when(tagDao).deleteById(id);
        service.deleteTag(id);

        //then
//        Mockito.verify(tagDao, times(1)).deleteById(anyLong());
        //Mockito.verify(giftCertificateTagDao, times(1)).deleteByTagId(anyLong());
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
            TagDto tag = new TagDto();
            Mockito.when(tagValidator.validate(mapper.unmap(tag))).thenReturn(false);
            service.createTag(tag);
        });
    }

    @Test
    public void testShouldThrowEntityAlreadyExistExceptionWhenTagWithExistNamePassed() {
        Assert.assertThrows(EntityAlreadyExistsException.class, () -> {
            TagDto tag = new TagDto(1, TEST_NAME);
            Mockito.when(tagValidator.validate(mapper.unmap(tag))).thenReturn(true);
            Mockito.when(tagDao.findTagByName(tag.getName())).thenReturn(Optional.of(mapper.unmap(tag)));

            service.createTag(tag);
        });
    }

}
