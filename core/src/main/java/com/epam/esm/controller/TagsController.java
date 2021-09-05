package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotExistsException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * The class that represents an API for basic operations with the application concerned to tags.
 *
 * @author Aliaksei Bliznichenka
 */
@RestController
@RequestMapping("/tags")
public class TagsController {

    private static final String JSON = "application/json";
    private static final String ID = "id";

    private final TagService service;

    @Autowired
    public TagsController(TagService service) {
        this.service = service;
    }

    /**
     * Returns an {@link ResponseEntity} object contained {@link HttpStatus} status and a {@link List} list of {@link Tag}
     * tags retrieved from database.
     *
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and {@link List} of {@link Tag} tags.
     */
    @GetMapping(produces = JSON)
    public ResponseEntity<List<Tag>> getTags() {
        return new ResponseEntity<>(service.getTags(), HttpStatus.OK);
    }

    /**
     * Returns an {@link ResponseEntity} object contained {@link HttpStatus} status and a {@link Tag} object from database.
     *
     * @param id - id of {@link Tag} that has to be retrieved from database.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and an {@link Tag} object.
     * @throws EntityNotExistsException in case if nothing found with searched id.
     */
    @GetMapping(value = "/{id}", produces = JSON)
    public ResponseEntity<Tag> getTag(@PathVariable(ID) long id) throws EntityNotExistsException {
        return new ResponseEntity<>(service.getTag(id), HttpStatus.OK);
    }

    /**
     * Deletes an {@link Tag} object by id retrieved from request and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status.
     *
     * @param id - id of {@link Tag} that has to be deleted from database.
     * @return {@link ResponseEntity} contained {@link HttpStatus} status.
     * @throws EntityNotExistsException in case if nothing found with searched id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable(ID) long id) throws EntityNotExistsException {
        service.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Creates new {@link Tag} object and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status and created {@link Tag} object.
     *
     * @param tag - new data for creating an {@link Tag} object.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and created {@link Tag} object.
     * @throws BadEntityException in case if invalid data passed in request to create an {@link Tag} object.
     * @throws EntityAlreadyExistsException in case if passed tag already exists in database.
     * @throws EntityNotExistsException in case if nothing found with searched id.
     */
    @PostMapping(produces = JSON)
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) throws BadEntityException, EntityAlreadyExistsException, EntityNotExistsException {
        return new ResponseEntity<>(service.createTag(tag), HttpStatus.CREATED);
    }

}
