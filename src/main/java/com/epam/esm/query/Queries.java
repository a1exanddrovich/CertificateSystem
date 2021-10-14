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
    public static final String GET_MOST_POPULAR_TAG = "select *\n" +
            "from tag\n" +
            "where id = (select tag_id\n" +
            "\t    from (select tag_id, count(tag_id) count from\n" +
            "\t\t\t\t\t\t     (select gift_certificate_id\n" +
            "\t\t\t\t\t\t      from user u\n" +
            "\t\t\t\t\t\t      join `order` o\n" +
            "\t\t\t\t\t\t      on u.id=o.user_id\n" +
            "\t\t\t\t\t\t      where user_id = (select id\n" +
            "\t\t\t\t\t\t\t\t\t      from (select u.id, sum(o.price) as sum\n" +
            "\t\t\t\t\t\t\t\t\t\t    from user u\n" +
            "\t\t\t\t\t\t\t\t\t\t    join `order` o\n" +
            "\t\t\t\t\t\t\t\t\t            on u.id=o.user_id\n" +
            "\t\t\t\t\t\t\t\t\t\t    group by u.id) s\n" +
            "\t\t\t\t\t\t\t\t\torder by s.sum desc limit 1)\n" +
            "\t\t\t\t\t\t      ) h\n" +
            "\t    join gift_certificate_tag gct\n" +
            "\t    on gct.gift_certificate_id = h.gift_certificate_id\n" +
            "\t    GROUP BY tag_id limit 1) s\n" +
            "\t   );\n";

    //User
    public static final String COUNT_USERS = "SELECT COUNT(u) FROM User u";
    public static final String GET_ALL_USERS = "SELECT u FROM User u";

}
