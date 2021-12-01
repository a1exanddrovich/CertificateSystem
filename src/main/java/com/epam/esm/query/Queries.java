package com.epam.esm.query;

public class Queries {

    private Queries() { }

    //Gift certificate
    public static final String COUNT_CERTIFICATES = "SELECT COUNT(c) FROM GiftCertificate c";

    //Order
    public static final String COUNT_ORDERS_BY_USER_ID = "SELECT COUNT(o) FROM Order o WHERE o.user=:user";
    public static final String GET_ORDERS_BY_USER_ID = "SELECT o FROM Order o WHERE o.user=:user";

    //Tag
    public static final String GET_ALL_TAGS = "SELECT t FROM Tag t";
    public static final String GET_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name=:tagName";
    public static final String COUNT_TAGS = "SELECT COUNT(t) FROM Tag t";
    public static final String GET_MOST_POPULAR_TAG = "select * " +
            "from tag " +
            "where id = (select tag_id " +
            "from (select tag_id, count(tag_id) count from " +
            "(select gift_certificate_id " +
            "from user u " +
            "join `order` o " +
            "on u.id=o.user_id " +
            "where user_id = (select id " +
            "from (select u.id, sum(o.price) as sum " +
            "from user u " +
            "join `order` o " +
            "on u.id=o.user_id " +
            "group by u.id) s " +
            "order by s.sum desc limit 1) " +
            ") h " +
            "join gift_certificate_tag gct " +
            "on gct.gift_certificate_id = h.gift_certificate_id " +
            "GROUP BY tag_id limit 1) s " +
            "); ";

    //User
    public static final String COUNT_USERS = "SELECT COUNT(u) FROM User u";
    public static final String GET_ALL_USERS = "SELECT u FROM User u";

}
