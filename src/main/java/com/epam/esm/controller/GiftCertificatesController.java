package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * The class that represents an API for basic operations with the application concerned to gift certificates.
 *
 * @author Aliaksei Bliznichenka
 */
@RestController
@RequestMapping("/giftCertificates")
public class GiftCertificatesController {

    private static final String JSON = "application/json";
    private static final String ID = "id";

    private final GiftCertificateService service;

    @Autowired
    public GiftCertificatesController(GiftCertificateService service) {
        this.service = service;
    }

    /**
     * Returns an {@link ResponseEntity} object contained {@link HttpStatus} status and a {@link List} list of {@link GiftCertificateDto}
     * mapped from a list of {@link GiftCertificate} gift certificates retrieved from database.The retrieved data has to fulfill parameters received from request.
     * All parameters are optional.
     *
     * @param tagName - name of a {@link Tag} tag should be contained in a gift certificate.
     * @param giftCertificateName - part of a name of a searched gift certificate.
     * @param description - part of a description of a searched gift certificate.
     * @param sortByName - sort of the retrieved gift certificates by name.
     * @param sortByDate - sort of the retrieved gift certificates by creation date.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and {@link List} of {@link GiftCertificateDto}
     */
    @GetMapping(produces = JSON)
    public ResponseEntity<List<GiftCertificateDto>> giftCertificates(@RequestParam(required = false) String tagName,
                                                                     @RequestParam(required = false) String giftCertificateName,
                                                                     @RequestParam(required = false) String description,
                                                                     @RequestParam(required = false) String sortByName,
                                                                     @RequestParam(required = false) String sortByDate) {
        return new ResponseEntity<>(service.getGiftCertificates(tagName, giftCertificateName, description, sortByName, sortByDate), HttpStatus.OK);
    }

    /**
     * Returns an {@link ResponseEntity} object contained {@link HttpStatus} status and a {@link GiftCertificateDto} object
     * mapped from a  {@link GiftCertificate} gift certificates retrieved from database.
     *
     * @param id - id of {@link GiftCertificate} that has to be retrieved from database.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and {@link GiftCertificateDto} object
     * @throws EntityNotExistsException in case if nothing found with searched id.
     */
    @GetMapping(value = "/{id}", produces = JSON)
    public ResponseEntity<GiftCertificateDto> getGiftCertificate(@PathVariable(ID) long id) throws EntityNotExistsException {
        return new ResponseEntity<>(service.getGiftCertificate(id), HttpStatus.OK);
    }

    /**
     * Deletes an {@link GiftCertificate} object by id retrieved from request and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status.
     *
     * @param id - id of {@link GiftCertificate} that has to be deleted from database.
     * @return {@link ResponseEntity} contained {@link HttpStatus} status.
     * @throws EntityNotExistsException in case if nothing found with searched id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> deleteGiftCertificate(@PathVariable(ID) long id) throws EntityNotExistsException {
        service.deleteGiftCertificate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Creates new {@link GiftCertificate} object and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status and {@link GiftCertificateDto} mapped from passed {@link GiftCertificate} object.
     *
     * @param giftCertificate - data for creating new {@link GiftCertificate} object.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and created {@link GiftCertificate} object.
     * @throws BadEntityException in case if invalid data passed in request to construct a {@link GiftCertificate} object.
     * @throws EntityNotExistsException in case if nothing found with searched id.
     */
    @PostMapping()
    public ResponseEntity<GiftCertificateDto> createGiftCertificate(@RequestBody GiftCertificateDto giftCertificate) throws BadEntityException, EntityNotExistsException {
        return new ResponseEntity<>(service.createGiftCertificate(giftCertificate), HttpStatus.CREATED);
    }

    /**
     * Updates {@link GiftCertificate} object by id and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status and {@link GiftCertificateDto} mapped from {@link GiftCertificate} object with passed id.
     *
     * @param id - id of {@link GiftCertificate} object to be updated.
     * @param giftCertificate - new data for updating an {@link GiftCertificate} object to be updated.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and updated {@link GiftCertificate} object.
     * @throws BadEntityException in case if invalid data passed in request to upadte a {@link GiftCertificate} object.
     * @throws EntityNotExistsException in case if nothing found with searched id.
     */
    @PatchMapping(value = "/{id}", produces = JSON)
    public ResponseEntity<GiftCertificateDto> updateGiftCertificate(@PathVariable(ID) long id, @RequestBody GiftCertificateDto giftCertificate) throws EntityNotExistsException, BadEntityException {
        return new ResponseEntity<>(service.updateGiftCertificate(id, giftCertificate), HttpStatus.OK);
    }

}
