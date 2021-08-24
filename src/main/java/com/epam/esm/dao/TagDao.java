package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TagDao {

    private final static String CREATE = "INSERT INTO tag (name) VALUES (?)";
    private final static String DELETE_TAG = "DELETE FROM tag WHERE id = ?";
    private final static String FIND_ALL = "SELECT * FROM tag";
    private final static String FIND_BY_ID = "SELECT * FROM tag WHERE id = ?";
    private final static String GET_TAG_IDS_AFTER_UPDATE = "SELECT id FROM tag WHERE name = ?";

    private final JdbcTemplate template;
    private final TagMapper mapper;

    @Autowired
    public TagDao(JdbcTemplate template, TagMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    public void create(Tag tag) {
        template.update(CREATE, tag.getName());
    }

    public void deleteById(long id) {
        template.update(DELETE_TAG, id);
    }

    public Optional<Tag> findById(long id) {
        return template.query(FIND_BY_ID, mapper, new Object[]{id}).stream().findAny();
    }

    public List<Tag> findAll() {
        return template.query(FIND_ALL, mapper);
    }

    public List<Long> getIdsAfterUpdate(List<String> tags) {
        List<Long> ids = new ArrayList<>();
        tags.stream().forEach(tagName -> ids.add(template.queryForObject(GET_TAG_IDS_AFTER_UPDATE, Long.class, tagName)));
        return ids;
    }

}
