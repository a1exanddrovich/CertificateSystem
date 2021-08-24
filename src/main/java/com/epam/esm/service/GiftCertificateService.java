package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.ConjugationDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.utils.QueryConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class GiftCertificateService {

    private final static String MINSK_TIME = "GMT+3:00";
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private final GiftCertificateDao giftCertificateDao;
    private final ConjugationDao conjugationDao;
    private final QueryConstructor constructor;
    private final TagDao tagDao;

    @Autowired
    public GiftCertificateService(GiftCertificateDao giftCertificateDao, ConjugationDao conjugationDao, TagDao tagDao, QueryConstructor constructor) {
        this.giftCertificateDao = giftCertificateDao;
        this.conjugationDao = conjugationDao;
        this.tagDao = tagDao;
        this.constructor = constructor;
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(MINSK_TIME));
    }

    public List<GiftCertificate> getGiftCertificates(String tagName, String giftCertificateName, String description, String sortByName, String sortByDate) {
        String query = constructor.constructQuery(tagName, giftCertificateName, description, sortByName, sortByDate);
        return giftCertificateDao.getGiftCertificates(query);
    }

    public Optional<GiftCertificate> getGiftCertificate(long id) {
        return giftCertificateDao.findById(id);
    }

    @Transactional
    public void deleteGiftCertificate(long id) throws EntityNotExistException {
        if (giftCertificateDao.findById(id).isEmpty()) {
            throw new EntityNotExistException();
        }
        conjugationDao.deleteGiftCertificateById(id);
        giftCertificateDao.deleteById(id);
    }

    @Transactional
    public void updateGiftCertificate(long id, GiftCertificate giftCertificate) throws EntityNotExistException, BadEntityException {
        if(giftCertificateDao.findById(id).isEmpty()) {
            throw new EntityNotExistException();
        }
        if (giftCertificate == null) {
            throw new BadEntityException();
        }

        giftCertificate.setLastUpdateDate(DATE_FORMAT.format(new Date()));
        String updateQuery = constructor.constructUpdateQuery(id, giftCertificate);

        checkForTags(giftCertificate.getTags());
        connectCertificatesAndTags(id, giftCertificate);
        giftCertificateDao.updateGiftCertificate(updateQuery);
    }

    @Transactional
    public void createGiftCertificate(GiftCertificate giftCertificate) throws BadEntityException {
        if(giftCertificate == null) {
            throw new BadEntityException();
        }

        giftCertificate.setCreationDate(DATE_FORMAT.format(new Date()));
        giftCertificate.setLastUpdateDate(DATE_FORMAT.format(new Date()));

        checkForTags(giftCertificate.getTags());
        giftCertificateDao.create(giftCertificate);
        conjugationDao.createConnections(giftCertificate.getTags());
    }

    private void checkForTags(List<String> giftCertificateTags) {
        List<Tag> tags = tagDao.findAll();
        List<String> tagNames = new ArrayList<>();

        tags.stream().forEach(tag -> tagNames.add(tag.getName()));

        for (String tag : giftCertificateTags) {
            if (!tagNames.contains(tag)) {
                tagDao.create(new Tag(tag));
            }
        }
    }

    private void connectCertificatesAndTags(long id, GiftCertificate giftCertificate) {
        List<Long> tagIdsBeforeUpdate = conjugationDao.getIdsBeforeUpdate(id);
        List<Long> tagIdsAfterUpdate = tagDao.getIdsAfterUpdate(giftCertificate.getTags());

        for (Long tagId : tagIdsAfterUpdate) {
            if (!tagIdsBeforeUpdate.contains(tagId)) {
                conjugationDao.addTagId(id, tagId);
            }
        }

        for (Long tagId : tagIdsBeforeUpdate) {
            if (!tagIdsAfterUpdate.contains(tagId)) {
                conjugationDao.deleteTagId(id, tagId);
            }
        }
    }

}
