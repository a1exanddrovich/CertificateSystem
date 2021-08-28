package com.epam.esm.entity;

public class SqlQueries {

    private SqlQueries() { }

    //tag table
    public static final String CREATE_TAG = "INSERT INTO tag (name) VALUES (?)";
    public static final String DELETE_TAG = "DELETE FROM tag WHERE id = ?";
    public static final String FIND_ALL_TAGS = "SELECT * FROM tag";
    public static final String FIND_TAG_BY_ID = "SELECT * FROM tag WHERE id = ?";
    public static final String FIND_TAG_ID_BY_NAME = "SELECT id FROM tag WHERE name = ?";
    public static final String FIND_TAG_BY_NAME = "SELECT * FROM tag WHERE name = ?";

    //gift_certificate table
    public static final String CREATE_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String DELETE_CERTIFICATE = "DELETE FROM gift_certificate WHERE id = ?";
    public static final String FIND_ALL_CERTIFICATES = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, gct.gift_certificate_id, t.name AS tagName " +
                                                       "FROM gift_certificate gc " +
                                                       "JOIN gift_certificate_tag gct ON gct.gift_certificate_id = gc.id " +
                                                       "JOIN tag t ON gct.tag_id = t.id";
    public static final String FIND_CERTIFICATE_BY_ID = FIND_ALL_CERTIFICATES + " WHERE gc.id = ?";

    //gift_certificate_tag table
    public static final String CONNECT_TAG_TO_CERTIFICATE = "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
    public static final String DISCONNECT_TAG_TO_CERTIFICATE = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ? AND tag_id = ?";
    public static final String MAKE_TIDED_TAGS_AND_CERTIFICATIONS = "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, (SELECT id from tag WHERE name = ?))";
    public static final String SELECT_TAG_ID_BY_CERTIFICATE_ID = "SELECT tag_id FROM gift_certificate_tag WHERE gift_certificate_id = ?";
    public static final String MAKE_UNTIED_CERTIFICATE = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ?";
    public static final String MAKE_UNTIED_TAG = "DELETE FROM gift_certificate_tag WHERE tag_id = ?";

}
