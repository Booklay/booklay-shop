-- 2023 01 09 14:03

drop table if exists access_log cascade;

drop table if exists ask_chat cascade;

drop table if exists blocked_member_detail cascade;

drop table if exists book_subscribe cascade;

drop table if exists cart cascade;

drop table if exists category_product cascade;

drop table if exists delivery_destination cascade;

drop table if exists delivery_detail cascade;

drop table if exists delivery_status_code cascade;

drop table if exists member_authority cascade;

drop table if exists authority cascade;

drop table if exists member_grade cascade;

drop table if exists order_coupon cascade;

drop table if exists category cascade;

drop table if exists order_subscribe cascade;

drop table if exists owned_ebook cascade;

drop table if exists point_history cascade;

drop table if exists product_ask_comment cascade;

drop table if exists post cascade;

drop table if exists post_type cascade;

drop table if exists product_author cascade;

drop table if exists author cascade;

drop table if exists product_coupon cascade;

drop table if exists coupon cascade;

drop table if exists coupon_type cascade;

drop table if exists order_product cascade;

drop table if exists `order` cascade;

drop table if exists order_status_code cascade;

drop table if exists product_detail cascade;

drop table if exists product_relation cascade;

drop table if exists product_tag cascade;

drop table if exists restocking_notification cascade;

drop table if exists review cascade;

drop table if exists subscribe cascade;

drop table if exists tag cascade;

drop table if exists view_count cascade;

drop table if exists wishlist cascade;

drop table if exists member cascade;

drop table if exists gender cascade;

drop table if exists product cascade;

drop table if exists image cascade;

create table access_log
(
    access_log_no bigint not null
        primary key auto_increment,
    ip char(12) not null,
    domain varchar(10) null,
    browser varchar(25) not null,
    access_device varchar(10) not null,
    log_date datetime null
);

create table authority
(
    authority_no tinyint not null
        primary key,
    authority varchar(15) not null
);

create table category
(
    category_no bigint not null
        primary key,
    parent_category_no bigint null,
    name varchar(50) not null,
    depth tinyint not null,
    is_exposure boolean default 1 not null,
    constraint FK_category_TO_category_1
        foreign key (parent_category_no) references category (category_no)
);

create table coupon_type
(
    code tinyint not null
        primary key,
    name varchar(10) not null
);

create table delivery_status_code
(
    code tinyint not null
        primary key,
    name varchar(10) not null
);

create table gender
(
    gender_no boolean not null
        primary key,
    gender varchar(5) not null
);

create table image
(
    image_no bigint not null
        primary key auto_increment,
    address varchar(255) not null,
    ext varchar(5) not null
);

create table member
(
    member_no  bigint      not null
        primary key auto_increment,
    gender_no  boolean     not null,
    member_id  varchar(30) not null,
    password   char(60)    not null,
    nickname   varchar(30) not null,
    name       varchar(50) not null,
    birthday   date        not null,
    phone_no   char(11)    not null,
    email      varchar(30) not null,
    created_at datetime    not null,
    updated_at datetime null,
    deleted_at datetime null,
    is_blocked boolean     not null,
    constraint FK_gender_TO_member_1
        foreign key (gender_no) references gender (gender_no)
);

create table ask_chat
(
    ask_chat_no bigint not null
        primary key auto_increment,
    member_no bigint not null,
    content text not null,
    constraint FK_member_TO_ask_chat_1
        foreign key (member_no) references member (member_no)
);

create table author
(
    author_no bigint not null
        primary key auto_increment,
    member_no bigint null,
    name varchar(50) not null,
    constraint FK_member_TO_author_1
        foreign key (member_no) references member (member_no)
);

create table blocked_member_detail
(
    blocked_member_detail_no bigint not null
        primary key auto_increment,
    member_no bigint not null,
    reason varchar(100) not null,
    blocked_at datetime not null,
    released_at datetime null,
    constraint FK_member_TO_blocked_member_detail_1
        foreign key (member_no) references member (member_no)
);

create table delivery_destination
(
    delivery_destination_no bigint not null
        primary key auto_increment,
    member_no bigint not null,
    name varchar(50) not null,
    zip_code char(5) not null,
    address varchar(50) not null,
    is_default_destination boolean null,
    constraint FK_member_TO_delivery_destination_1
        foreign key (member_no) references member (member_no)
);

create table member_authority
(
    member_no bigint not null,
    authority_no tinyint not null,
    primary key (member_no, authority_no),
    constraint FK_authority_TO_member_authority_1
        foreign key (authority_no) references authority (authority_no),
    constraint FK_member_TO_member_authority_1
        foreign key (member_no) references member (member_no)
);

create table member_grade
(
    member_grade_no bigint not null
        primary key auto_increment,
    member_no bigint not null,
    name varchar(15) not null,
    date date not null,
    constraint FK_member_TO_member_grade_1
        foreign key (member_no) references member (member_no)
);

create table order_status_code
(
    code tinyint not null
        primary key,
    name varchar(5) not null
);

create table `order`
(
    order_no bigint not null
        primary key auto_increment,
    member_no bigint null,
    code tinyint not null,
    ordered_at datetime not null,
    product_price_sum int not null,
    delivery_price int not null,
    discount_price int not null,
    point_use_price int not null,
    payment_price int not null,
    payment_method int not null,
    gift_wrapping_price int not null,
    is_blinded boolean not null,
    constraint FK_member_TO_order_1
        foreign key (member_no) references member (member_no),
    constraint FK_order_status_code_TO_order_1
        foreign key (code) references order_status_code (code)
);

create table delivery_detail
(
    delivery_detail_no bigint not null
        primary key auto_increment,
    order_no bigint not null,
    code tinyint not null,
    zip_code char(5) not null,
    address varchar(50) not null,
    sender varchar(50) not null,
    sender_phone_no varchar(15) not null,
    receiver varchar(50) not null,
    receiver_phone_no varchar(15) not null,
    memo varchar(50) null,
    invoice_no varchar(20) null,
    delivery_started_at datetime not null,
    delivery_completed_at datetime null,
    constraint FK_delivery_status_code_TO_delivery_detail_1
        foreign key (code) references delivery_status_code (code),
    constraint FK_order_TO_delivery_detail_1
        foreign key (order_no) references `order` (order_no)
);

create table post_type
(
    post_type_no tinyint not null
        primary key,
    type varchar(20) not null
);

create table product
(
    product_no bigint not null
        primary key auto_increment,
    thumbnail_no bigint not null,
    title varchar(255) not null,
    registed_at datetime not null,
    price int not null,
    point_rate int not null,
    short_description text not null,
    long_description text not null,
    is_selling boolean not null,
    point_method boolean not null,
    constraint FK_image_TO_product_1
        foreign key (thumbnail_no) references image (image_no)
);

create table coupon
(
    coupon_no bigint not null
        primary key auto_increment,
    image_no bigint not null,
    code tinyint not null,
    product_no bigint null,
    category_no bigint null,
    name varchar(100) not null,
    amount int not null,
    minimum_use_amount int not null,
    maximum_discount_amount int null,
    issuance_deadline_at datetime not null,
    is_duplicatable boolean not null,
    is_limited boolean not null,
    constraint FK_product_TO_coupon_1
        foreign key (product_no) references product (product_no),
    constraint FK_category_TO_coupon_1
        foreign key (category_no) references category (category_no),
    constraint FK_coupon_type_TO_coupon_1
        foreign key (code) references coupon_type (code),
    constraint FK_image_TO_coupon_1
        foreign key (image_no) references image (image_no)
);

create table order_coupon
(
    order_coupon_no bigint not null
        primary key auto_increment,
    coupon_no bigint not null,
    member_no bigint null,
    order_no bigint null,
    coupon_code char(36) not null,
    issued_at datetime null,
    expired_at datetime null,
    constraint FK_coupon_TO_order_coupon_1
        foreign key (coupon_no) references coupon (coupon_no),
    constraint FK_member_TO_order_coupon_1
        foreign key (member_no) references member (member_no),
    constraint FK_order_TO_order_coupon_1
        foreign key (order_no) references `order` (order_no)
);

create table point_history
(
    point_history_no bigint not null
        primary key auto_increment,
    member_no bigint not null,
    point int not null,
    total_point int not null,
    updated_at datetime not null,
    updated_detail varchar(30) not null,
    constraint FK_member_TO_point_history_1
        foreign key (member_no) references member (member_no)
);

create table cart
(
    member_no bigint not null,
    product_no bigint not null,
    count smallint not null,
    primary key (member_no, product_no),
    constraint FK_member_TO_cart_1
        foreign key (member_no) references member (member_no),
    constraint FK_product_TO_cart_1
        foreign key (product_no) references product (product_no)
);

create table category_product
(
    product_no bigint not null,
    category_no bigint not null,
    primary key (product_no, category_no),
    constraint FK_category_TO_category_product_1
        foreign key (category_no) references category (category_no),
    constraint FK_product_TO_category_product_1
        foreign key (product_no) references product (product_no)
);

create table order_product
(
    order_product_no bigint not null
        primary key auto_increment,
    order_no bigint not null,
    product_no bigint not null,
    count smallint not null,
    price int not null,
    constraint FK_order_TO_order_product_1
        foreign key (order_no) references `order` (order_no),
    constraint FK_product_TO_order_product_1
        foreign key (product_no) references product (product_no)
);

create table owned_ebook
(
    owned_ebook_no bigint not null
        primary key auto_increment,
    member_no bigint not null,
    product_no bigint not null,
    constraint FK_member_TO_owned_ebook_1
        foreign key (member_no) references member (member_no),
    constraint FK_product_TO_owned_ebook_1
        foreign key (product_no) references product (product_no)
);

create table post
(
    post_no bigint not null
        primary key auto_increment,
    post_type_no tinyint not null,
    member_no bigint not null,
    product_no bigint null,
    group_post_no bigint null,
    group_order int not null,
    depth int not null,
    title varchar(255) not null,
    content text not null,
    created_at datetime not null,
    updated_at datetime null,
    is_view_public boolean not null,
    is_answered boolean null,
    constraint FK_member_TO_post_1
        foreign key (member_no) references member (member_no),
    constraint FK_post_TO_post_1
        foreign key (group_post_no) references post (post_no),
    constraint FK_post_type_TO_post_1
        foreign key (post_type_no) references post_type (post_type_no),
    constraint FK_product_TO_post_1
        foreign key (product_no) references product (product_no)
);

create table product_ask_comment
(
    comment_no bigint not null
        primary key auto_increment,
    post_no bigint not null,
    member_no bigint not null,
    group_comment_no bigint null,
    content text not null,
    created_at datetime not null,
    updated_at datetime null,
    group_order int not null,
    depth int not null,
    constraint FK_member_TO_product_ask_comment_1
        foreign key (member_no) references member (member_no),
    constraint FK_post_TO_product_ask_comment_1
        foreign key (post_no) references post (post_no),
    constraint FK_product_ask_comment_TO_product_ask_comment_1
        foreign key (group_comment_no) references product_ask_comment (comment_no)
);

create table product_coupon
(
    product_coupon_no bigint not null
        primary key auto_increment,
    coupon_no bigint not null,
    member_no bigint null,
    order_product_no bigint null,
    coupon_code char(36) not null,
    issued_at datetime null,
    expired_at datetime null,
    constraint FK_coupon_TO_product_coupon_1
        foreign key (coupon_no) references coupon (coupon_no),
    constraint FK_member_TO_product_coupon_1
        foreign key (member_no) references member (member_no),
    constraint FK_order_product_TO_product_coupon_1
        foreign key (order_product_no) references order_product (order_product_no)
);

create table product_detail
(
    book_no bigint not null
        primary key auto_increment,
    product_no bigint not null,
    isbn varchar(20) not null,
    page int not null,
    publisher varchar(100) not null,
    published_date date not null,
    ebook_address varchar(255) null,
    storage int null,
    constraint FK_product_TO_product_detail_1
        foreign key (product_no) references product (product_no)
);

create table product_author
(
    book_no bigint not null,
    author_no bigint not null,
    primary key (book_no, author_no),
    constraint FK_author_TO_product_author_1
        foreign key (author_no) references author (author_no),
    constraint FK_product_detail_TO_product_author_1
        foreign key (book_no) references product_detail (book_no)
);

create table product_relation
(
    product_relation_no bigint not null
        primary key auto_increment,
    base_product_no bigint not null,
    related_product_no bigint not null,
    is_deleted boolean default 0 not null,
    constraint FK_product_TO_product_relation_1
        foreign key (base_product_no) references product (product_no),
    constraint FK_product_TO_product_relation_2
        foreign key (related_product_no) references product (product_no)
);

create table restocking_notification
(
    member_no bigint not null,
    product_no bigint not null,
    primary key (member_no, product_no),
    constraint FK_member_TO_restocking_notification_1
        foreign key (member_no) references member (member_no),
    constraint FK_product_TO_restocking_notification_1
        foreign key (product_no) references product (product_no)
);

create table review
(
    review_no bigint not null
        primary key auto_increment,
    product_no bigint not null,
    member_no bigint not null,
    image_no bigint not null,
    score int not null,
    content text not null,
    created_at datetime not null,
    modified_at datetime null,
    constraint FK_image_TO_review_1
        foreign key (image_no) references image (image_no),
    constraint FK_member_TO_review_1
        foreign key (member_no) references member (member_no),
    constraint FK_product_TO_review_1
        foreign key (product_no) references product (product_no)
);

create table subscribe
(
    subscribe_no bigint not null
        primary key auto_increment,
    product_no bigint not null,
    subscribe_week int not null,
    subscribe_day int not null,
    publisher varchar(100) null,
    constraint FK_product_TO_subscribe_1
        foreign key (product_no) references product (product_no)
);

create table book_subscribe
(
    subscribe_no bigint not null,
    book_no bigint not null,
    release_date date not null,
    primary key (subscribe_no, book_no),
    constraint FK_product_detail_TO_book_subscribe_1
        foreign key (book_no) references product_detail (book_no),
    constraint FK_subscribe_TO_book_subscribe_1
        foreign key (subscribe_no) references subscribe (subscribe_no)
);

create table order_subscribe
(
    order_subscribe_no bigint not null
        primary key auto_increment,
    subscribe_no bigint not null,
    order_no bigint not null,
    amounts smallint not null,
    price int not null,
    start_at datetime not null,
    finish_at datetime not null,
    constraint FK_order_TO_order_subscribe_1
        foreign key (order_no) references `order` (order_no),
    constraint FK_subscribe_TO_order_subscribe_1
        foreign key (subscribe_no) references subscribe (subscribe_no)
);

create table tag
(
    tag_no bigint not null
        primary key auto_increment,
    tag varchar(20) not null
);

create table product_tag
(
    product_no bigint not null,
    tag_no bigint not null,
    primary key (product_no, tag_no),
    constraint FK_product_TO_product_tag_1
        foreign key (product_no) references product (product_no),
    constraint FK_tag_TO_product_tag_1
        foreign key (tag_no) references tag (tag_no)
);

create table view_count
(
    view_count_no bigint not null
        primary key auto_increment,
    product_no bigint not null,
    saved_date date not null,
    count int not null,
    constraint FK_product_TO_view_count_1
        foreign key (product_no) references product (product_no)
);

create table wishlist
(
    member_no bigint not null,
    product_no bigint not null,
    listed_at datetime not null,
    primary key (member_no, product_no),
    constraint FK_member_TO_wishlist_1
        foreign key (member_no) references member (member_no),
    constraint FK_product_TO_wishlist_1
        foreign key (product_no) references product (product_no)
);

create table object_file
(
    file_no bigint not null
        primary key auto_increment,
    address varchar(255) not null,
    file_name varchar(255) not null
);

