package com.epam.esm.sql;

public class SqlQueries {

    private SqlQueries() { }

    //order table
    public static final String DELETE_ORDER = "DELETE FROM orders WHERE gift_certificate_id = ?";

    //user table
    public static final String FIND_ALL_USERS = "SELECT * FROM user";
    public static final String FIND_USER_BY_ID = "SELECT * FROM user WHERE id = ?";
    public static final String COUNT_ALL_USERS = "SELECT COUNT(*) FROM user";

    //tag table
    public static final String CREATE_TAG = "INSERT INTO tag (name) VALUES (?)";
    public static final String DELETE_TAG = "DELETE FROM tag WHERE id = ?";
    public static final String FIND_ALL_TAGS = "SELECT * FROM giftcertificatesdatabase.tag";
    public static final String FIND_TAG_BY_ID = "SELECT * FROM giftcertificatesdatabase.tag WHERE id = ?";
    public static final String FIND_TAG_BY_NAME = "SELECT * FROM tag WHERE name = ?";
    public static final String COUNT_ALL_TAGS = "SELECT COUNT(*) FROM tag";

    //gift_certificate table
    public static final String COUNT_ALL_CERTIFICATES = "SELECT COUNT(*) FROM gift_certificate";
    public static final String CREATE_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String DELETE_CERTIFICATE = "DELETE FROM gift_certificate WHERE id = ?";
    public static final String FIND_ALL_CERTIFICATES = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, gct.gift_certificate_id, t.name AS tagName " +
                                                       "FROM gift_certificate gc " +
                                                       "JOIN gift_certificate_tag gct ON gct.gift_certificate_id = gc.id " +
                                                       "JOIN tag t ON gct.tag_id = t.id";
    public static final String FIND_CERTIFICATE_BY_ID = "select * from gift_certificate" + " WHERE id = ?";

    //gift_certificate_tag table
    public static final String CONNECT_TAG_TO_CERTIFICATE = "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
    public static final String DISCONNECT_TAG_TO_CERTIFICATE = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ? AND tag_id = ?";
    public static final String MAKE_TIDED_TAGS_AND_CERTIFICATIONS = "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, (SELECT id from tag WHERE name = ?))";
    public static final String SELECT_TAG_ID_BY_CERTIFICATE_ID = "SELECT tag_id FROM gift_certificate_tag WHERE gift_certificate_id = ?";
    public static final String MAKE_UNTIED_CERTIFICATE = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ?";
    public static final String MAKE_UNTIED_TAG = "DELETE FROM gift_certificate_tag WHERE tag_id = ?";

    //utils
    public static final String SELECT_CERTIFICATE_ID_BY_TAG_NAME = "(SELECT gc.id FROM gift_certificate gc JOIN gift_certificate_tag gct ON gct.gift_certificate_id = gc.id JOIN tag t ON gct.tag_id = t.id WHERE t.name = ";

    public static final String UPDATE_USER_BALANCE = "UPDATE user SET balance = ? WHERE id = ?";
    public static final String CREATE_ORDER = "INSERT INTO orders (user_id, gift_certificate_id, timestamp, price) VALUES (?, ?, ?, ?)";
    public static final String FIND_ORDER_BY_ID = "SELECT * FROM orders WHERE id = ?";
    public static final String FIND_ALL_USER_ORDERS = "SELECT * FROM orders WHERE user_id = ?";
    public static final String COUNT_USER_ORDERS = "SELECT COUNT(*) FROM orders WHERE user_id = ?";

    public static final String GET_MOST_POPULAR_TAG = "select id from user where ";



}
