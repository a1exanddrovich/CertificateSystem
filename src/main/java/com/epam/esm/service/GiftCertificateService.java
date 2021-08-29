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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GiftCertificateService {

    private static final String TIME_ZONE = "timezone";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private final GiftCertificateDao giftCertificateDao;
    private final GiftCertificateTagDao giftCertificateTagDao;
    private final TagDao tagDao;
    private final GiftCertificateValidator validator;
    private final GiftCertificateDtoMapper dtoMapper;
    private final Environment environment;

    @Autowired
    public GiftCertificateService(GiftCertificateDao giftCertificateDao, GiftCertificateTagDao giftCertificateTagDao, TagDao tagDao, GiftCertificateValidator validator, GiftCertificateDtoMapper dtoMapper, Environment environment) {
        this.giftCertificateDao = giftCertificateDao;
        this.giftCertificateTagDao = giftCertificateTagDao;
        this.tagDao = tagDao;
        this.validator = validator;
        this.dtoMapper = dtoMapper;
        this.environment = environment;
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(environment.getProperty(TIME_ZONE)));
    }

    public List<GiftCertificateDto> getGiftCertificates(String tagName, String giftCertificateName, String description, String sortByName, String sortByDate) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.getGiftCertificates(tagName, giftCertificateName, description, sortByName, sortByDate);
        List<GiftCertificateDto> giftCertificateDtos = new ArrayList<>();

        giftCertificates.stream().forEach(giftCertificate -> giftCertificateDtos.add(dtoMapper.map(giftCertificate)));
        return giftCertificateDtos;
    }

    public GiftCertificateDto getGiftCertificate(long id) throws EntityNotExistsException {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(id);

        if (optionalGiftCertificate.isEmpty()) {
            throw new EntityNotExistsException();
        }

        return dtoMapper.map(optionalGiftCertificate.get());
    }

    @Transactional
    public void deleteEntity(long id) throws EntityNotExistsException {
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

        if (!validator.validateUpdating(giftCertificate)) {
            throw new BadEntityException();
        }

        giftCertificate.setLastUpdateDate(DATE_FORMAT.format(new Date()));

        checkForTags(giftCertificate.getTags());
        giftCertificateDao.updateGiftCertificate(id, giftCertificate);

        if (!giftCertificate.getTags().isEmpty()) {
            connectCertificatesAndTags(id, giftCertificate);
        }
        return getGiftCertificate(id);
    }

    @Transactional
    public GiftCertificateDto createGiftCertificate(GiftCertificateDto giftCertificateDto) throws BadEntityException, EntityNotExistsException {
        GiftCertificate giftCertificate = dtoMapper.unmap(giftCertificateDto);

        if (!validator.validateCreating(giftCertificate)) {
            throw new BadEntityException();
        }

        giftCertificate.setCreationDate(DATE_FORMAT.format(new Date()));
        giftCertificate.setLastUpdateDate(DATE_FORMAT.format(new Date()));

        checkForTags(giftCertificate.getTags());
        long lastInsertedId = giftCertificateDao.create(giftCertificate);
        giftCertificateTagDao.createConnections(lastInsertedId, giftCertificate.getTags());

        return getGiftCertificate(lastInsertedId);
    }

    private void checkForTags(Set<Tag> newTags) {
        List<String> newTagNames = new ArrayList<>();
        newTags.stream().forEach(tag -> newTagNames.add(tag.getName()));

        newTagNames.stream().filter(tagName -> tagDao.findTagByName(tagName).isEmpty()).forEach(tagName -> tagDao.create(new Tag(tagName)));

//        for (String tagName : newTagNames) {
//            if (tagDao.findTagByName(tagName).isEmpty()) {
//                tagDao.create(new Tag(tagName));
//            }
//        }
    }

    private void connectCertificatesAndTags(long id, GiftCertificate giftCertificate) {
        List<Long> tagIdsBeforeUpdate = giftCertificateTagDao.getIdsBeforeUpdate(id);
        List<Long> tagIdsAfterUpdate = tagDao.getIdsAfterUpdate(giftCertificate.getTags());

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
