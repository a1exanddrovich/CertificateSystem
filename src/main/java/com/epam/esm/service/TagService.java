package com.epam.esm.service;

import com.epam.esm.dao.ConjugationDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class TagService {

    private final TagDao tagDao;
    private final ConjugationDao conjugationDao;

    @Autowired
    public TagService(TagDao tagDao, ConjugationDao conjugationDao) {
        this.tagDao = tagDao;
        this.conjugationDao = conjugationDao;
    }

    public List<Tag> getTags() {
        return tagDao.findAll();
    }

    public Optional<Tag> getTag(long id) {
        return tagDao.findById(id);
    }

    public void createTag(Tag tag) throws BadEntityException {
        if (tag == null) {
            throw new BadEntityException();
        }
        tagDao.create(tag);
    }

    @Transactional
    public void deleteTag(long id) throws EntityNotExistException {
        if (tagDao.findById(id).isEmpty()) {
            throw new EntityNotExistException();
        }
        conjugationDao.deleteTagByIdFromGiftTag(id);
        tagDao.deleteById(id);
    }

}
