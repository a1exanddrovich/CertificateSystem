package com.epam.esm.hateoas;

import com.epam.esm.controller.TagsController;
import com.epam.esm.dto.TagDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagsHateoasIssuer extends AbstractHateoasIssuer<TagDto> {

    private static final Class<TagsController> TAGS_CONTROLLER = TagsController.class;

    public void addTagLink(TagDto tag) {
        addLinks(TAGS_CONTROLLER, tag, tag.getId(), SELF_LINK);
    }

    public CollectionModel<TagDto> addTagLinks(List<TagDto> tagDtos, Integer page, Integer pageSize, boolean hasNextPage) {
        for (TagDto tagDto : tagDtos) {
            addTagLink(tagDto);
        }
        return addPagesLinks(tagDtos, page, pageSize, hasNextPage);
    }

    private CollectionModel<TagDto> addPagesLinks(List<TagDto> tagDtos, Integer page, Integer pageSize, boolean hasNextPage) {
        Link previousPage = linkTo(methodOn(TAGS_CONTROLLER)
                .getTags(evaluatePreviousPage(page), pageSize)).withSelfRel();
        Link nextPage = linkTo(methodOn(TAGS_CONTROLLER)
                .getTags(evaluateNextPage(page, hasNextPage), pageSize)).withSelfRel();
        return CollectionModel.of(tagDtos, previousPage, nextPage);
    }

}
