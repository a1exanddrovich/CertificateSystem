package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping(produces = JSON)
    public ResponseEntity<List<GiftCertificateDto>> giftCertificates(@RequestParam(required = false) String tagName,
                                                                     @RequestParam(required = false) String giftCertificateName,
                                                                     @RequestParam(required = false) String description,
                                                                     @RequestParam(required = false) String sortByName,
                                                                     @RequestParam(required = false) String sortByDate) {
        return new ResponseEntity<>(service.getGiftCertificates(tagName, giftCertificateName, description, sortByName, sortByDate), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = JSON)
    public ResponseEntity<GiftCertificateDto> getGiftCertificate(@PathVariable(ID) long id) throws EntityNotExistsException {
        return new ResponseEntity<>(service.getGiftCertificate(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> deleteGiftCertificate(@PathVariable(ID) long id) throws EntityNotExistsException {
        service.deleteEntity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping()
    public ResponseEntity<GiftCertificateDto> createGiftCertificate(@RequestBody GiftCertificateDto giftCertificate) throws BadEntityException, EntityNotExistsException {
        return new ResponseEntity<>(service.createGiftCertificate(giftCertificate), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", produces = JSON)
    public ResponseEntity<GiftCertificateDto> updateGiftCertificate(@PathVariable(ID) long id, @RequestBody GiftCertificateDto giftCertificate) throws EntityNotExistsException, BadEntityException {
        return new ResponseEntity<>(service.updateGiftCertificate(id, giftCertificate), HttpStatus.OK);
    }

}
