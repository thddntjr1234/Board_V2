# DROP TABLE IF EXISTS `posts`;

CREATE TABLE `posts` (
                         `id`	bigint	NOT NULL auto_increment PRIMARY KEY ,
                         `created_date`	datetime	NULL,
                         `modified_date`	datetime	NULL,
                         `title`	varchar(255)	NULL,
                         `author`	varchar(255)	NULL,
                         `content`	TEXT	NULL,
                         `hits`	bigint	NOT NULL	DEFAULT 0,
                         `category`	varchar(255)	NULL,
                         `pwd`	varchar(255)	NULL
);

# DROP TABLE IF EXISTS `files`;

CREATE TABLE `files` (
                         `id`	bigint	NOT NULL references posts(id),
                         `file_path_1`	varchar(1000)	NULL,
                         `file_path_2`	varchar(1000)	NULL,
                         `file_path_3`	varchar(1000)	NULL,
                         `file_name_1`	varchar(1000)	NULL,
                         `file_name_2`	varchar(1000)	NULL,
                         `file_name_3`	varchar(1000)	NULL
);

create table comments
(
    created_date timestamp    not null,
    comment      varchar(255) not null,
    post_id      int          null,
    constraint comments_posts_id_fk
        foreign key (post_id) references posts (id)
);



CREATE TABLE category (category varchar(255) NULL primary key );


ALTER TABLE `comments` ADD CONSTRAINT `FK_posts_TO_comments_1` FOREIGN KEY (
                                                                            `id`
    )
    REFERENCES `posts` (
                        `id`
        );