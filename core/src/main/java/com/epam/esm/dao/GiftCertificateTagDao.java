package com.epam.esm.dao;

import com.epam.esm.sql.SqlQueries;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Component
public class GiftCertificateTagDao {

    private final JdbcTemplate template;

    @Autowired
    public GiftCertificateTagDao(JdbcTemplate template) {
        this.template = template;
    }

    public void addTagId(long id, Long tagId) {
        template.update(SqlQueries.CONNECT_TAG_TO_CERTIFICATE, id, tagId);
    }

    public void deleteTagId(long id, Long tagId) {
        template.update(SqlQueries.DISCONNECT_TAG_TO_CERTIFICATE, id, tagId);
    }

    public void createConnections(long giftCertificateId, Set<Tag> tags) {
        for (Tag tag : tags) {
            template.update(SqlQueries.MAKE_TIDED_TAGS_AND_CERTIFICATIONS, giftCertificateId, tag.getName());
        }
    }

    public List<Long> getIdsBeforeUpdate(long id) {
        return template.queryForList(SqlQueries.SELECT_TAG_ID_BY_CERTIFICATE_ID, Long.class, id);
    }

    public void deleteGiftCertificateById(long id) {
        template.update(SqlQueries.MAKE_UNTIED_CERTIFICATE, id);
    }

    public void deleteByTagId(long id) {
        template.update(SqlQueries.MAKE_UNTIED_TAG, id);
    }


}
