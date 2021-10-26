package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.dtomapper.GiftCertificateDtoMapper;
import com.epam.esm.utils.GiftCertificateQueryParameters;
import com.epam.esm.validator.PaginationValidator;
import com.epam.esm.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GiftCertificateService {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final GiftCertificateValidator validator;
    private final GiftCertificateDtoMapper dtoMapper;
    private final PaginationValidator paginationValidator;

    @Autowired
    public GiftCertificateService(GiftCertificateDao giftCertificateDao, TagDao tagDao, GiftCertificateValidator validator, GiftCertificateDtoMapper dtoMapper, PaginationValidator paginationValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.validator = validator;
        this.dtoMapper = dtoMapper;
        this.paginationValidator = paginationValidator;
    }

    public List<GiftCertificateDto> getGiftCertificates(GiftCertificateQueryParameters parameters, Integer page, Integer pageSize) {

        return giftCertificateDao
                .getGiftCertificates(parameters, page, paginationValidator.paginate(page, pageSize, giftCertificateDao.countGiftCertificates()))
                .stream()
                .map(dtoMapper::map)
                .collect(Collectors.toList());
    }

    public GiftCertificateDto getGiftCertificate(long id) throws EntityNotExistsException {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(id);
        if (optionalGiftCertificate.isEmpty()) {
            throw new EntityNotExistsException();
        }

        return dtoMapper.map(optionalGiftCertificate.get());
    }

    public void deleteGiftCertificate(long id) throws EntityNotExistsException {
        if (giftCertificateDao.findById(id).isEmpty()) {
            throw new EntityNotExistsException();
        }

        giftCertificateDao.deleteById(id);
    }

    public GiftCertificateDto updateGiftCertificate(long id, GiftCertificateDto giftCertificateDto) throws EntityNotExistsException, BadEntityException {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(id);
        if (optionalGiftCertificate.isEmpty()) {
            throw new EntityNotExistsException();
        }

        GiftCertificate modifiedGiftCertificate = dtoMapper.unmap(giftCertificateDto);

        if (!validator.validateUpdate(modifiedGiftCertificate)) {
            throw new BadEntityException();
        }

        GiftCertificate readGiftCertificate = optionalGiftCertificate.get();

        modifiedGiftCertificate.setLastUpdateDate(ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))));
        modifiedGiftCertificate.setTags(initializeTags(modifiedGiftCertificate.getTags()));

        update(readGiftCertificate, modifiedGiftCertificate);

        giftCertificateDao.updateGiftCertificate(readGiftCertificate);

        return getGiftCertificate(id);
    }

    public GiftCertificateDto createGiftCertificate(GiftCertificateDto giftCertificateDto) throws BadEntityException, EntityNotExistsException {
        GiftCertificate giftCertificate = dtoMapper.unmap(giftCertificateDto);

        if (!validator.validateCreate(giftCertificate)) {
            throw new BadEntityException();
        }

        giftCertificate.setCreationDate(ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))));
        giftCertificate.setLastUpdateDate(ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.ofHours(3)).format(DateTimeFormatter.ofPattern(DATE_FORMAT))));
        giftCertificate.setTags(initializeTags(giftCertificate.getTags()));

        return getGiftCertificate(giftCertificateDao.create(giftCertificate));
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

    private void update(GiftCertificate readGiftCertificate, GiftCertificate modifiedGiftCertificate) {
        if (Objects.nonNull(modifiedGiftCertificate.getName())) {
            readGiftCertificate.setName(modifiedGiftCertificate.getName());
        }

        if (Objects.nonNull(modifiedGiftCertificate.getDescription())) {
            readGiftCertificate.setDescription(modifiedGiftCertificate.getDescription());
        }

        if (Objects.nonNull(modifiedGiftCertificate.getPrice())) {
            readGiftCertificate.setPrice(modifiedGiftCertificate.getPrice());
        }

        if (Objects.nonNull(modifiedGiftCertificate.getDuration())) {
            readGiftCertificate.setDuration(modifiedGiftCertificate.getDuration());
        }

        if (Objects.nonNull(modifiedGiftCertificate.getTags())) {
            readGiftCertificate.setTags(initializeTags(modifiedGiftCertificate.getTags()));
        }

        readGiftCertificate.setLastUpdateDate(modifiedGiftCertificate.getLastUpdateDate());

    }

}
