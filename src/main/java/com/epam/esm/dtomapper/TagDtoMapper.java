package com.epam.esm.dtomapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagDtoMapper {

    public TagDto map(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }

    public Tag unmap(TagDto tagDto) {
        return new Tag(tagDto.getId(), tagDto.getName());
    }

}
