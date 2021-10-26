package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dtomapper.TagDtoMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagService {

    private final TagDao tagDao;
    private final TagValidator validator;
    private final PaginationValidator paginationValidator;
    private final TagDtoMapper mapper;

    @Autowired
    public TagService(TagDao tagDao, TagValidator validator, PaginationValidator paginationValidator, TagDtoMapper mapper) {
        this.tagDao = tagDao;
        this.validator = validator;
        this.paginationValidator = paginationValidator;
        this.mapper = mapper;
    }

    public List<TagDto> getTags(Integer page, Integer pageSize) {
        return tagDao
                .findAll(page, paginationValidator.paginate(page, pageSize, tagDao.countTags()))
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    public TagDto getTag(long id) throws EntityNotExistsException {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (optionalTag.isEmpty()) {
            throw new EntityNotExistsException();
        }

        return mapper.map(optionalTag.get());
    }


    public TagDto createTag(TagDto tagDto) {
        Tag tag = mapper.unmap(tagDto);
        if (!validator.validate(tag)) {
            throw new BadEntityException();
        }

        Optional<Tag> optionalTag = tagDao.findTagByName(tag.getName());
        if (optionalTag.isPresent()) {
            throw new EntityAlreadyExistsException();
        }

        return getTag(tagDao.create(tag));
    }

    public void deleteTag(long id) {
        if (tagDao.findById(id).isEmpty()) {
            throw new EntityNotExistsException();
        }

        tagDao.deleteById(id);
    }

    public TagDto getMostPopular() {
        return mapper.map(tagDao.getMostPopular());
    }
}
