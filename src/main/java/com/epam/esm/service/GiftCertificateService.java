package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.GiftCertificateTagDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.utils.GiftCertificateDtoMapper;
import com.epam.esm.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateService {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private final GiftCertificateDao giftCertificateDao;
    private final GiftCertificateTagDao giftCertificateTagDao;
    private final TagDao tagDao;
    private final GiftCertificateValidator validator;
    private final GiftCertificateDtoMapper dtoMapper;

    @Autowired
    public GiftCertificateService(GiftCertificateDao giftCertificateDao, GiftCertificateTagDao giftCertificateTagDao, TagDao tagDao, GiftCertificateValidator validator, GiftCertificateDtoMapper dtoMapper) {
        this.giftCertificateDao = giftCertificateDao;
        this.giftCertificateTagDao = giftCertificateTagDao;
        this.tagDao = tagDao;
        this.validator = validator;
        this.dtoMapper = dtoMapper;
    }

    public List<GiftCertificateDto> getGiftCertificates(String tagName, String giftCertificateName, String description, String sortByName, String sortByDate) {
        return giftCertificateDao.getGiftCertificates(tagName, giftCertificateName, description, sortByName, sortByDate).stream().map(dtoMapper::map).collect(Collectors.toList());
    }

    public GiftCertificateDto getGiftCertificate(long id) throws EntityNotExistsException {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(id);
        if (optionalGiftCertificate.isEmpty()) {
            throw new EntityNotExistsException();
        }

        return dtoMapper.map(optionalGiftCertificate.get());
    }

    @Transactional
    public void deleteGiftCertificate(long id) throws EntityNotExistsException {
        if (giftCertificateDao.findById(id).isEmpty()) {
            throw new EntityNotExistsException();
        }

        giftCertificateTagDao.deleteGiftCertificateById(id);
        giftCertificateDao.deleteById(id);
    }

    @Transactional
    public GiftCertificateDto updateGiftCertificate(long id, GiftCertificateDto giftCertificateDto) throws EntityNotExistsException, BadEntityException {
        if (giftCertificateDao.findById(id).isEmpty()) {
            throw new EntityNotExistsException();
        }

        GiftCertificate giftCertificate = dtoMapper.unmap(giftCertificateDto);

        if (!validator.validateUpdate(giftCertificate)) {
            throw new BadEntityException();
        }

        giftCertificate.setLastUpdateDate(ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))));

        giftCertificate.setTags(initializeTags(giftCertificate.getTags()));
        giftCertificateDao.updateGiftCertificate(id, giftCertificate);

        if (!giftCertificate.getTags().isEmpty()) {
            connectCertificatesAndTags(id, giftCertificate);
        }

        return getGiftCertificate(id);
    }

    @Transactional
    public GiftCertificateDto createGiftCertificate(GiftCertificateDto giftCertificateDto) throws BadEntityException, EntityNotExistsException {
        GiftCertificate giftCertificate = dtoMapper.unmap(giftCertificateDto);

        if (!validator.validateCreate(giftCertificate)) {
            throw new BadEntityException();
        }

        giftCertificate.setCreationDate(ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))));
        giftCertificate.setLastUpdateDate(ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))));

        giftCertificate.setTags(initializeTags(giftCertificate.getTags()));
        long lastInsertedId = giftCertificateDao.create(giftCertificate);
        giftCertificateTagDao.createConnections(lastInsertedId, giftCertificate.getTags());

        return getGiftCertificate(lastInsertedId);
    }

    private Set<Tag> initializeTags(Set<Tag> incomingTags) {
        return incomingTags.stream().map(tag -> {
            Optional<Tag> optionalTag = tagDao.findTagByName(tag.getName());
            if (optionalTag.isPresent()) {
                return optionalTag.get();
            } else {
                Tag newTag = new Tag(tag.getName());
                long newTagId = tagDao.create(newTag);
                newTag.setId(newTagId);
                return newTag;
            }
        }).collect(Collectors.toSet());
    }

    private void connectCertificatesAndTags(long id, GiftCertificate giftCertificate) {
        List<Long> tagIdsBeforeUpdate = giftCertificateTagDao.getIdsBeforeUpdate(id);
        List<Long> tagIdsAfterUpdate = giftCertificate.getTags().stream().map(Tag::getId).collect(Collectors.toList());

        for (Long tagId : tagIdsAfterUpdate) {
            if (!tagIdsBeforeUpdate.contains(tagId)) {
                giftCertificateTagDao.addTagId(id, tagId);
            }
        }

        for (Long tagId : tagIdsBeforeUpdate) {
            if (!tagIdsAfterUpdate.contains(tagId)) {
                giftCertificateTagDao.deleteTagId(id, tagId);
            }
        }

    }

}
