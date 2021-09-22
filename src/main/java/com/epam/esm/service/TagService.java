package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.utils.Paginator;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagDao tagDao;
    private final GiftCertificateTagDao giftCertificateTagDao;
    private final TagValidator validator;
    private final Paginator paginator;

    @Autowired
    public TagService(TagDao tagDao, GiftCertificateTagDao giftCertificateTagDao, TagValidator validator, Paginator paginator) {
        this.tagDao = tagDao;
        this.giftCertificateTagDao = giftCertificateTagDao;
        this.validator = validator;
        this.paginator = paginator;
    }

    public List<Tag> getTags(Integer page, Integer pageSize) {
        return tagDao.findAll(page, paginator.paginate(page, pageSize, tagDao.countTags()));
    }

    public Tag getTag(long id) throws EntityNotExistsException {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (optionalTag.isEmpty()) {
            throw new EntityNotExistsException();
        }

        return optionalTag.get();
    }


    public Tag createTag(Tag tag) {
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

        giftCertificateTagDao.deleteByTagId(id);
        tagDao.deleteById(id);
    }

    public Tag getMostPopular() {
        return tagDao.getMostPopular();
    }
}
