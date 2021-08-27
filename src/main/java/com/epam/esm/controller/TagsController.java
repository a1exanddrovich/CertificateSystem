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

    @GetMapping(produces = JSON)
    public ResponseEntity<List<Tag>> getTags() {
        return new ResponseEntity<>(service.getTags(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = JSON)
    public ResponseEntity<Tag> getTag(@PathVariable(ID) long id) throws EntityNotExistsException {
        return new ResponseEntity<>(service.getTag(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable(ID) long id) throws EntityNotExistsException {
        service.deleteEntity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(produces = JSON)
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) throws BadEntityException, EntityAlreadyExistsException, EntityNotExistsException {
        return new ResponseEntity<>(service.createTag(tag), HttpStatus.CREATED);
    }

}
