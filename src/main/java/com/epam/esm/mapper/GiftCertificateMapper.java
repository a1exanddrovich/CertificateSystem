package com.epam.esm.mapper;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GiftCertificateMapper implements ResultSetExtractor<List<GiftCertificate>> {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";
    private static final String TAG_NAME = "tagName";

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
                Duration duration = Duration.ofDays(resultSet.getInt(DURATION));
                String createDate = resultSet.getString(CREATE_DATE);
                String lastUpdateDate = resultSet.getString(LAST_UPDATE_DATE);
                giftCertificate = new GiftCertificate(id, name, description, price, duration, createDate, lastUpdateDate);
                giftCertificatesMap.put(giftCertificateId,giftCertificate);
            }
            giftCertificate.addTag(new Tag(resultSet.getString(TAG_NAME)));
        }
        return giftCertificatesMap.values().stream().collect(Collectors.toList());
    }

}
