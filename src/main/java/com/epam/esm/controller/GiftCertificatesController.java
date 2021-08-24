package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/giftCertificates")
public class GiftCertificatesController {

    private final static String JSON = "application/json";

    private final GiftCertificateService service;

    @Autowired
    public GiftCertificatesController(GiftCertificateService service) {
        this.service = service;
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<List<GiftCertificate>> giftCertificates(@RequestParam(required = false) String tagName,
                                                                  @RequestParam(required = false) String giftCertificateName,
                                                                  @RequestParam(required = false) String description,
                                                                  @RequestParam(required = false) String sortByName,
                                                                  @RequestParam(required = false) String sortByDate) {
        List<GiftCertificate> giftCertificates = service.getGiftCertificates(tagName, giftCertificateName, description, sortByName, sortByDate);

        return giftCertificates.size() == 0 ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                                            : new ResponseEntity<>(giftCertificates, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = JSON)
    public ResponseEntity<GiftCertificate> getGiftCertificate(@PathVariable("id") long id) {
        Optional<GiftCertificate> giftCertificateOptional = service.getGiftCertificate(id);

        return giftCertificateOptional.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                                                 : new ResponseEntity<>(giftCertificateOptional.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GiftCertificate> deleteGiftCertificate(@PathVariable("id") long id) throws EntityNotExistException {
        service.deleteGiftCertificate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(produces = JSON)
    public ResponseEntity<GiftCertificate> createGiftCertificate(@RequestBody GiftCertificate giftCertificate) throws BadEntityException {
        service.createGiftCertificate(giftCertificate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = "/{id}", produces = JSON)
    public ResponseEntity<GiftCertificate> updateGiftCertificate(@PathVariable("id") long id, @RequestBody GiftCertificate giftCertificate) throws EntityNotExistException, BadEntityException {
        service.updateGiftCertificate(id, giftCertificate);
        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
    }

}
