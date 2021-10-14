package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.utils.GiftCertificateQueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The class that represents an API for basic operations with the application concerned to gift certificates.
 *
 * @author Aliaksei Bliznichenka
 */
@RestController
@RequestMapping("/giftCertificates")
public class GiftCertificatesController {

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
     * @param parameters object {@link GiftCertificateQueryParameters} contains all the parameters could be sent
     *                   in order to filter the result
     * @param page specifies page of the result list.
     * @param pageSize specifies number of gift certificates to be displayed in a single page. In case page were passed without page size
     *                 default page size applies.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and {@link List} of {@link GiftCertificateDto}
     */
    @GetMapping
    public ResponseEntity<CollectionModel<GiftCertificateDto>> getGiftCertificates(@RequestParam(required = false) Integer page,
                                                                                   @RequestParam(required = false) Integer pageSize,
                                                                                   GiftCertificateQueryParameters parameters) {
        List<GiftCertificateDto> giftCertificates = service.getGiftCertificates(parameters, page, pageSize);
        giftCertificates.forEach(giftCertificate -> giftCertificate.add(linkTo(methodOn(GiftCertificatesController.class).getGiftCertificate(giftCertificate.getId())).withSelfRel()));
        return ResponseEntity.ok(CollectionModel.of(giftCertificates));
    }

    /**
     * Returns an {@link ResponseEntity} object contained {@link HttpStatus} status and a {@link GiftCertificateDto} object
     * mapped from a  {@link GiftCertificate} gift certificates retrieved from database.
     *
     * @param id - id of {@link GiftCertificate} that has to be retrieved from database.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and {@link GiftCertificateDto} object
     */
    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getGiftCertificate(@PathVariable(ID) long id) {
        GiftCertificateDto giftCertificate = service.getGiftCertificate(id);
        return new ResponseEntity<>(this.addHateoas(giftCertificate), HttpStatus.OK);
    }

    /**
     * Deletes an {@link GiftCertificate} object by id retrieved from request and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status.
     *
     * @param id - id of {@link GiftCertificate} that has to be deleted from database.
     * @return {@link ResponseEntity} contained {@link HttpStatus} status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> deleteGiftCertificate(@PathVariable(ID) long id) {
        service.deleteGiftCertificate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Creates new {@link GiftCertificate} object and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status and {@link GiftCertificateDto} mapped from passed {@link GiftCertificate} object.
     *
     * @param giftCertificate - data for creating new {@link GiftCertificate} object.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and created {@link GiftCertificate} object.
     */
    @PostMapping()
    public ResponseEntity<GiftCertificateDto> createGiftCertificate(@RequestBody GiftCertificateDto giftCertificate) {
        GiftCertificateDto createdGiftCertificate = service.createGiftCertificate(giftCertificate);
        return new ResponseEntity<>(this.addHateoas(createdGiftCertificate), HttpStatus.CREATED);
    }

    /**
     * Updates {@link GiftCertificate} object by id and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status and {@link GiftCertificateDto} mapped from {@link GiftCertificate} object with passed id.
     *
     * @param id - id of {@link GiftCertificate} object to be updated.
     * @param giftCertificate - new data for updating an {@link GiftCertificate} object to be updated.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and updated {@link GiftCertificate} object.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> updateGiftCertificate(@PathVariable(ID) long id, @RequestBody GiftCertificateDto giftCertificate) {
        GiftCertificateDto updatedGiftCertificate = service.updateGiftCertificate(id, giftCertificate);
        return new ResponseEntity<>(this.addHateoas(updatedGiftCertificate), HttpStatus.OK);
    }

    private GiftCertificateDto addHateoas(GiftCertificateDto giftCertificate) {
        return giftCertificate.add(linkTo(methodOn(GiftCertificatesController.class).getGiftCertificate(giftCertificate.getId())).withSelfRel());
    }

}
