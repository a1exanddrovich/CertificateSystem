package com.epam.esm.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GiftCertificateMapper implements ResultSetExtractor<List<GiftCertificate>> {

    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";
    private final static String PRICE = "price";
    private final static String DURATION = "duration";
    private final static String CREATE_DATE = "create_date";
    private final static String LAST_UPDATE_DATE = "last_update_date";
    private final static String TAG_NAME = "tagName";

    @Override
    public List<GiftCertificate> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Long, GiftCertificate> giftCertificatesMap = new LinkedHashMap<>();
        while (resultSet.next()) {
            Long giftCertificateId = resultSet.getLong(ID);
            GiftCertificate giftCertificate = giftCertificatesMap.get(giftCertificateId);
            if (giftCertificate == null) {
                long id = resultSet.getLong(ID);
                String name = resultSet.getString(NAME);
                String description = resultSet.getString(DESCRIPTION);
                BigDecimal price = resultSet.getBigDecimal(PRICE);
                int duration = resultSet.getInt(DURATION);
                String createDate = resultSet.getString(CREATE_DATE);
                String lastUpdateDate = resultSet.getString(LAST_UPDATE_DATE);
                giftCertificate = new GiftCertificate(id, name, description, price, duration, createDate, lastUpdateDate);
                giftCertificatesMap.put(giftCertificateId,giftCertificate);
            }
            giftCertificate.addTag(resultSet.getString(TAG_NAME));
        }
        return giftCertificatesMap.values().stream().collect(Collectors.toList());
    }
}
