package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mapper.GiftCertificateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class GiftCertificateDao {

    private final static String FIND_ALL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, gct.gift_certificate_id, t.name AS tagName " +
                                           "FROM gift_certificate gc " +
                                           "JOIN gift_certificate_tag gct ON gct.gift_certificate_id = gc.id " +
                                           "JOIN tag t ON gct.tag_id = t.id";
    private final static String FIND_BY_ID = FIND_ALL + " WHERE gc.id = ?";
    private final static String DELETE_FROM_GIFT_CERTIFICATE = "DELETE FROM gift_certificate WHERE id = ?";
    private final static String CREATE_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";

    private final JdbcTemplate template;
    private final GiftCertificateMapper mapper;

    @Autowired
    public GiftCertificateDao(JdbcTemplate template, GiftCertificateMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    public void deleteById(long id) {
        template.update(DELETE_FROM_GIFT_CERTIFICATE, id);
    }

    public void create(GiftCertificate giftCertificate) {
        template.update(CREATE_GIFT_CERTIFICATE, giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getCreationDate(), giftCertificate.getLastUpdateDate());
    }

    public List<GiftCertificate> getGiftCertificates(String query) {
        return template.query(query, mapper);
    }

    public Optional<GiftCertificate> findById(long id) {
        return template.query(FIND_BY_ID, mapper, new Object[]{id}).stream().findAny();
    }

    public void updateGiftCertificate(String updateQuery) {
        template.update(updateQuery);
    }

}
