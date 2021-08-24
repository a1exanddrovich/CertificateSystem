create table gift_certificate
(
    id bigint auto_increment,
    name varchar(50),
    description varchar(50),
    price decimal(10, 2) default 0 null,
    duration int,
    create_date varchar(50),
    last_update_date varchar(50),

    primary key (id)
);

create table tag
(
    id bigint auto_increment,
    name varchar(50),

    primary key (id)

);

create table gift_certificate_tag
(
    gift_certificate_id int not null,
    tag_id int not null,
    primary key (gift_certificate_id, tag_id),
    constraint fk_gift_certificate_id foreign key (gift_certificate_id) references gift_certificate(id),
    constraint fk_tag_id foreign key (tag_id) references tag(id)
)