package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TagDao implements EntityDao<Tag> {

    private static final String CREATE = "INSERT INTO tag (name) VALUES (?)";
    private static final String DELETE_TAG = "DELETE FROM tag WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM tag";
    private static final String FIND_BY_ID = "SELECT * FROM tag WHERE id = ?";
    private static final String GET_TAG_IDS_AFTER_UPDATE = "SELECT id FROM tag WHERE name = ?";
    private static final String GET_TAG_BY_NAME = "SELECT * FROM tag WHERE name = ?";

    private final JdbcTemplate template;
    private final TagMapper mapper;

    @Autowired
    public TagDao(JdbcTemplate template, TagMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    @Override
    public long create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, tag.getName());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void deleteById(long id) {
        template.update(DELETE_TAG, id);
    }

    @Override
    public Optional<Tag> findById(long id) {
        return template.query(FIND_BY_ID, mapper, new Object[]{id}).stream().findAny();
    }

    public List<Tag> findAll() {
        return template.query(FIND_ALL, mapper);
    }

    public List<Long> getIdsAfterUpdate(List<Tag> tags) {
        List<Long> ids = new ArrayList<>();
        tags.stream().forEach(tag -> ids.add(template.queryForObject(GET_TAG_IDS_AFTER_UPDATE, Long.class, tag.getName())));
        return ids;
    }

    public Optional<Tag> findTagByName(String tagName) {
        return template.query(GET_TAG_BY_NAME, mapper, new Object[] {tagName}).stream().findAny();
    }

}
