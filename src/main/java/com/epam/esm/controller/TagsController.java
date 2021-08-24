package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagsController {

    private final static String JSON = "application/json";

    private final TagService service;

    @Autowired
    public TagsController(TagService service) {
        this.service = service;
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<List<Tag>> getTags() {
        List<Tag> tags = service.getTags();

        return tags.size() == 0 ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                                : new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping(value =  "/{id}", produces = JSON)
    public ResponseEntity<Tag> getTag(@PathVariable("id") long id) {
        Optional<Tag> tag = service.getTag(id);

        return tag.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                             : new ResponseEntity<>(tag.get(), HttpStatus.OK);
    }

    @DeleteMapping( "/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable("id") long id) throws EntityNotExistException {
        service.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(produces = JSON)
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) throws BadEntityException {
        service.createTag(tag);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
