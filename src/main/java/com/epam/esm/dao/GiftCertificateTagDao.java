package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class GiftCertificateTagDao {

    private static final String ADD_TAG = "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
    private static final String DELETE_TAG = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ? AND tag_id = ?";
    private static final String MAKE_TIDED = "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, (SELECT id from tag WHERE name = ?))";
    private static final String GET_TAG_IDS_BEFORE_UPDATE = "SELECT tag_id FROM gift_certificate_tag WHERE gift_certificate_id = ?";
    private static final String DELETE_FROM_GIFT_CERTIFICATE_TAG = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ?";
    private static final String DELETE_GIFT_TAG = "DELETE FROM gift_certificate_tag WHERE tag_id = ?";

    private final JdbcTemplate template;

    @Autowired
    public GiftCertificateTagDao(JdbcTemplate template) {
        this.template = template;
    }

    public void addTagId(long id, Long tagId) {
        template.update(ADD_TAG, id, tagId);
    }

    public void deleteTagId(long id, Long tagId) {
        template.update(DELETE_TAG, id, tagId);
    }

    public void createConnections(long giftCertificateId, List<Tag> tags) {
        for (Tag tag : tags) {
            template.update(MAKE_TIDED, giftCertificateId, tag.getName());
        }
    }

    public List<Long> getIdsBeforeUpdate(long id) {
        return template.queryForList(GET_TAG_IDS_BEFORE_UPDATE, Long.class, id);
    }

    public void deleteGiftCertificateById(long id) {
        template.update(DELETE_FROM_GIFT_CERTIFICATE_TAG, id);
    }

    public void deleteByTagId(long id) {
        template.update(DELETE_GIFT_TAG, id);
    }


}
