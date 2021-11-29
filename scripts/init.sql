create table gift_certificate
(
    id bigint auto_increment,
    name varchar(50),
    description varchar(100),
    price decimal(10, 2) default 0 null,
    duration int,
    create_date timestamp,
    last_update_date timestamp,

    primary key (id)
);

create table tag
(
    id bigint auto_increment,
    name varchar(50) unique,

    primary key (id)

);

create table gift_certificate_tag
(
    gift_certificate_id int not null,
    tag_id int not null,
    primary key (gift_certificate_id, tag_id),
    constraint fk_gift_certificate_id foreign key (gift_certificate_id) references gift_certificate(id),
    constraint fk_tag_id foreign key (tag_id) references tag(id)
);

create table user
(
    id bigint auto_increment,

    primary key (id)
);

create table `order`
(
    id bigint auto_increment,
    order_timestamp timestamp,
    price decimal(10, 2),
    user_id bigint,
    gift_certificate_id bigint,

    constraint order_gift_certificate_id_fk foreign key (gift_certificate_id) references gift_certificate(id),
    constraint order_user_id_fk foreign key (user_id) references user(id),

    primary key (id)
);

create table audit
(
    id bigint auto_increment,
    entity_id bigint,
    operation_type varchar(50),
    entity_type varchar(50),
    operation_timestamp timestamp,

    primary key (id)
);