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
import java.util.stream.Collectors;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    private static final String TEST_NAME = "testName";

    private static TagDao tagDao;
    private static TagDtoMapper mapper;
    private static TagValidator tagValidator;
    private static TagService service;
    private static Paginator paginator;

    @BeforeClass
    public static void init() {
        tagDao = Mockito.mock(TagDao.class);
        mapper = Mockito.mock(TagDtoMapper.class);
        tagValidator = Mockito.mock(TagValidator.class);
        paginator = Mockito.mock(Paginator.class);
        service = new TagService(tagDao, tagValidator, paginator, mapper);
    }

    @Test
    public void testShouldReturnAllTags() {
        //given
        Tag testFirst = new Tag(1, TEST_NAME);
        TagDto testDtoFirst = new TagDto(1, TEST_NAME);
        List<Tag> expected = Arrays.asList(testFirst, testFirst);

        //when
        when(mapper.map(testFirst)).thenReturn(testDtoFirst);
        when(mapper.unmap(testDtoFirst)).thenReturn(testFirst);
        when(tagDao.countTags()).thenReturn(2);
        when(paginator.paginate(1,1, 2)).thenReturn(1);
        when(tagDao.findAll(1, 1)).thenReturn(expected);
        List<Tag> actual = service.getTags(1, 1).stream().map(mapper::unmap).collect(Collectors.toList());

        //then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testShouldFindById() throws EntityNotExistsException {
        //given
        long id = 1L;
        Tag testClause = new Tag(id, TEST_NAME);
        TagDto testClauseDto = new TagDto(id, TEST_NAME);

        //when
        when(tagDao.findById(anyLong())).thenReturn(Optional.of(testClause));
        when(mapper.map(testClause)).thenReturn(testClauseDto);
        when(mapper.unmap(testClauseDto)).thenReturn(testClause);
        Tag actual = mapper.unmap(service.getTag(id));


        //then
        Assert.assertEquals(testClause, actual);
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
        TagDto expectedDto = new TagDto();
        Tag expectedTag = new Tag();
        when(mapper.unmap(expectedDto)).thenReturn(expectedTag);
        when(mapper.map((expectedTag))).thenReturn(expectedDto);
        when(tagValidator.validate(mapper.unmap(expectedDto))).thenReturn(true);
        when(tagDao.findTagByName(anyString())).thenReturn(Optional.empty());
        when(tagDao.create(mapper.unmap(expectedDto))).thenReturn(id);

        when(tagDao.findById(id)).thenReturn(Optional.of(expectedTag));

        //when
        Tag actual = mapper.unmap(service.createTag(expectedDto));
        //then
        Assert.assertEquals(expectedTag, actual);
    }

    @Test
    public void testShouldDeleteTagCorrectly() throws EntityNotExistsException {
        //given
        long id = 1;

        //when
        when(tagDao.findById(id)).thenReturn(Optional.of(new Tag()));
        doNothing().when(tagDao).deleteById(id);
        service.deleteTag(id);

        //then
        Mockito.verify(tagDao, times(1)).deleteById(anyLong());
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
            TagDto tagDto = new TagDto(1, TEST_NAME);
            Tag tag = new Tag(1, TEST_NAME);
            when(mapper.unmap(tagDto)).thenReturn(tag);
            when(tagValidator.validate(tag)).thenReturn(true);

            when(tagDao.findTagByName(tagDto.getName())).thenReturn(Optional.of(tag));

            service.createTag(tagDto);
        });
    }

}
