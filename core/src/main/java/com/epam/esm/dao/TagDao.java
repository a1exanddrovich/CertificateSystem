package com.epam.esm.dao;

import com.epam.esm.sql.SqlQueries;
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
import java.util.Set;

@Component
public class TagDao implements EntityDao<Tag> {

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
            PreparedStatement statement = connection.prepareStatement(SqlQueries.CREATE_TAG, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, tag.getName());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void deleteById(long id) {
        template.update(SqlQueries.DELETE_TAG, id);
    }

    @Override
    public Optional<Tag> findById(long id) {
        return template.query(SqlQueries.FIND_TAG_BY_ID, mapper, new Object[]{id}).stream().findAny();
    }

    public List<Tag> findAll() {
        return template.query(SqlQueries.FIND_ALL_TAGS, mapper);
    }

    public List<Long> getIdsAfterUpdate(Set<Tag> tags) {
        List<Long> ids = new ArrayList<>();
        tags.stream().forEach(tag -> ids.add(template.queryForObject(SqlQueries.FIND_TAG_ID_BY_NAME, Long.class, tag.getName())));
        return ids;
    }

    public Optional<Tag> findTagByName(String tagName) {
        return template.query(SqlQueries.FIND_TAG_BY_NAME, mapper, new Object[] {tagName}).stream().findAny();
    }

}
