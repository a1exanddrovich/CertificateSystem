package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.sql.SqlQueries;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.utils.QueryConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class GiftCertificateDao implements EntityDao<GiftCertificate> {

    private final JdbcTemplate template;
    private final GiftCertificateMapper mapper;
    private final QueryConstructor constructor;

    @Autowired
    public GiftCertificateDao(JdbcTemplate template, GiftCertificateMapper mapper, QueryConstructor constructor) {
        this.template = template;
        this.mapper = mapper;
        this.constructor = constructor;
    }

    @Override
    public void deleteById(long id) {
        template.update(SqlQueries.DELETE_CERTIFICATE, id);
    }

    @Override
    public long create(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SqlQueries.CREATE_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, giftCertificate.getName());
            statement.setString(2, giftCertificate.getDescription());
            statement.setString(3, giftCertificate.getPrice().toString());
            statement.setString(4, String.valueOf(giftCertificate.getDuration().toDays()));
            statement.setString(5, giftCertificate.getCreationDate().toString());
            statement.setString(6, giftCertificate.getLastUpdateDate().toString());
            return statement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public List<GiftCertificate> getGiftCertificates(String tagName, String giftCertificateName, String description, String sortByName, String sortByDate) {
        String query = constructor.constructQuery(tagName, giftCertificateName, description, sortByName, sortByDate);
        return template.query(query, mapper);
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        return template.query(SqlQueries.FIND_CERTIFICATE_BY_ID, mapper, new Object[]{id}).stream().findAny();
    }

    public void updateGiftCertificate(long id, GiftCertificate giftCertificate) {
        String updateQuery = constructor.constructUpdateQuery(id, giftCertificate);
        template.update(updateQuery);
    }

}
