create table giftcertificate
(
    id bigint auto_increment,
    name varchar(50),
    description varchar(100),
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
    constraint fk_gift_certificate_id foreign key (gift_certificate_id) references giftcertificate(id),
    constraint fk_tag_id foreign key (tag_id) references tag(id)
);

insert into tag(name)
values ('Coffee');
insert into tag(name)
values ('Music');
insert into tag(name)
values ('Relax');
insert into tag(name)
values ('Shopping');
insert into tag(name)
values ('Movie');
insert into tag(name)
values ('Retailing');
insert into tag(name)
values ('Recreation');
insert into tag(name)
values ('New');

insert into giftcertificate (name, description, price, duration, create_date, last_update_date)
values ('iTunes', 'Take a peek inside one of the state''s most award-winning songs, located in iTunes.', 120.00, 50, '2021-08-21T06:11:43.547Z', '2021-08-21T06:11:43.547Z');
insert into giftcertificate (name, description, price, duration, create_date, last_update_date)
values ('Starbucks', 'Taste more than a thousand a kinds of coffee in Starbucks.', 157.00, 90, '2021-08-20T06:11:43.547Z', '2021-08-21T06:11:43.547Z');
insert into giftcertificate (name, description, price, duration, create_date, last_update_date)
values ('Walmart', 'There should be kinda a description', 22, 30, '2021-08-19T06:11:43.547Z', '2021-08-19T06:11:43.547Z');
insert into giftcertificate (name, description, price, duration, create_date, last_update_date)
values ('Target', 'There should be kinda a description', 87.00, 58, '2021-08-18T06:11:43.547Z', '2021-08-18T06:11:43.547Z');
insert into giftcertificate (name, description, price, duration, create_date, last_update_date)
values ('Amazon', 'There should be kinda a description', 89.00, 59, '2021-08-17T06:11:43.547Z', '2021-08-17T06:11:43.547Z');
insert into giftcertificate (name, description, price, duration, create_date, last_update_date)
values ('Best Buy', 'There should be kinda a description', 170.00, 54, '2021-08-16T06:11:43.547Z', '2021-08-16T06:11:43.547Z');
insert into giftcertificate (name, description, price, duration, create_date, last_update_date)
values ('Fandago', 'There should be kinda a description', 190.00, 23, '2021-08-15T06:11:43.547Z', '2021-08-15T06:11:43.547Z');

insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (1, 3);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (1, 4);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (1, 8);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (2, 1);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (2, 4);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (2, 8);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (3, 5);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (3, 7);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (4, 5);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (4, 7);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (5, 5);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (5, 8);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (6, 5);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (6, 8);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (7, 4);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (7, 6);
insert into gift_certificate_tag (gift_certificate_id, tag_id)
values (7, 8);

