package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.hateoas.TagsHateoasIssuer;
import com.epam.esm.service.TagService;
import com.epam.esm.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The class that represents an API for basic operations with the application concerned to tags.
 *
 * @author Aliaksei Bliznichenka
 */
@RestController
@RequestMapping("/tags")
public class TagsController {

    private static final String ID = "id";

    private final TagService service;
    private final TagsHateoasIssuer hateoasIssuer;

    @Autowired
    public TagsController(TagService service, TagsHateoasIssuer hateoasIssuer) {
        this.service = service;
        this.hateoasIssuer = hateoasIssuer;
    }

    /**
     * Returns an {@link ResponseEntity} object contained {@link HttpStatus} status and a {@link List} list of {@link Tag}
     * tags retrieved from database.
     * All parameters are optional.
     *
     * @param page     specifies page of the result list.
     * @param pageSize specifies number of tags to be displayed in a single page. In case page were passed without page size
     *                 default page size applies.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and {@link List} of {@link Tag} tags.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<TagDto>> getTags(@RequestParam(required = false, defaultValue = Constants.DEFAULT_FIRST_PAGE) Integer page,
                                                           @RequestParam(required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) Integer pageSize) {
        List<TagDto> tags = service.getTags(page, pageSize);
        return ResponseEntity.ok(hateoasIssuer.addTagLinks(tags, page, pageSize, service.hasNextPage(page, pageSize)));
    }

    /**
     * Returns an {@link ResponseEntity} object contained {@link HttpStatus} status and a {@link Tag} object from database.
     *
     * @param id id of {@link Tag} that has to be retrieved from database.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and an {@link Tag} object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTag(@PathVariable(ID) long id) {
        TagDto tag = service.getTag(id);
        hateoasIssuer.addTagLink(tag);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    /**
     * Deletes an {@link Tag} object by id retrieved from request and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status.
     *
     * @param id id of {@link Tag} that has to be deleted from database.
     * @return {@link ResponseEntity} contained {@link HttpStatus} status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<TagDto> deleteTag(@PathVariable(ID) long id) {
        service.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Creates new {@link Tag} object and returns an {@link ResponseEntity} object contained
     * {@link HttpStatus} status and created {@link Tag} object.
     *
     * @param tag new data for creating an {@link Tag} object.
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and created {@link Tag} object.
     */
    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tag) {
        TagDto createdTag = service.createTag(tag);
        hateoasIssuer.addTagLink(createdTag);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    /**
     * Returns an {@link ResponseEntity} object contained {@link HttpStatus} status and a {@link Tag} object from database.
     *
     * @return {@link ResponseEntity} contained both {@link HttpStatus} status and {@link Tag} object.
     */
    @GetMapping("/mostPopular")
    public ResponseEntity<TagDto> getMostPopularTag() {
        TagDto tag = service.getMostPopular();
        hateoasIssuer.addTagLink(tag);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

}
