package com.epam.esm.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ConjugationDao {

    private final static String ADD_TAG = "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
    private final static String DELETE_TAG = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ? AND tag_id = ?";
    private final static String MAKE_TIDED = "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES ((SELECT id FROM gift_certificate ORDER BY id DESC LIMIT 1), (SELECT id from tag WHERE name = ?))";
    private final static String GET_TAG_IDS_BEFORE_UPDATE = "SELECT tag_id FROM gift_certificate_tag WHERE gift_certificate_id = ?";
    private final static String DELETE_FROM_GIFT_CERTIFICATE_TAG = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ?";
    private final static String DELETE_GIFT_TAG = "DELETE FROM gift_certificate_tag WHERE tag_id = ?";

    private final JdbcTemplate template;

    @Autowired
    public ConjugationDao(JdbcTemplate template) {
        this.template = template;
    }

    public void addTagId(long id, Long tagId) {
        template.update(ADD_TAG, id, tagId);
    }

    public void deleteTagId(long id, Long tagId) {
        template.update(DELETE_TAG, id, tagId);
    }

    public void createConnections(List<String> tags) {
        for (String tag : tags) {
            template.update(MAKE_TIDED,tag);
        }
    }

    public List<Long> getIdsBeforeUpdate(long id) {
        return template.queryForList(GET_TAG_IDS_BEFORE_UPDATE, Long.class, id);
    }

    public void deleteGiftCertificateById(long id) {
        template.update(DELETE_FROM_GIFT_CERTIFICATE_TAG, id);
    }

    public void deleteTagByIdFromGiftTag(long id) {
        template.update(DELETE_GIFT_TAG, id);
    }


}
