package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
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

    public static final String FIND_ALL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, gct.gift_certificate_id, t.name AS tagName " +
                                           "FROM gift_certificate gc " +
                                           "JOIN gift_certificate_tag gct ON gct.gift_certificate_id = gc.id " +
                                           "JOIN tag t ON gct.tag_id = t.id";
    private static final String FIND_BY_ID = FIND_ALL + " WHERE gc.id = ?";
    private static final String DELETE_FROM_GIFT_CERTIFICATE = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String CREATE_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";

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
        template.update(DELETE_FROM_GIFT_CERTIFICATE, id);
    }

    @Override
    public long create(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(CREATE_GIFT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, giftCertificate.getName());
            statement.setString(2, giftCertificate.getDescription());
            statement.setString(3, giftCertificate.getPrice().toString());
            statement.setString(4, String.valueOf(giftCertificate.getDuration().toDays()));
            statement.setString(5, giftCertificate.getCreationDate());
            statement.setString(6, giftCertificate.getLastUpdateDate());
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
        return template.query(FIND_BY_ID, mapper, new Object[]{id}).stream().findAny();
    }

    public void updateGiftCertificate(long id, GiftCertificate giftCertificate) {
        String updateQuery = constructor.constructUpdateQuery(id, giftCertificate);
        template.update(updateQuery);
    }

}
