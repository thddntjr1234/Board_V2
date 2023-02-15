CREATE TABLE `posts` (
                         `post_id`	bigint	NOT NULL,
                         `created_date`	datetime	NULL,
                         `modified_date`	datetime	NULL,
                         `title`	varchar(100)	NULL,
                         `author`	varchar(5)	NULL,
                         `content`	TEXT	NULL,
                         `hits`	bigint	NOT NULL	DEFAULT 0,
                         `passwd`	varchar(255)	NULL,
                         `file_flag`	boolean	NOT NULL	DEFAULT false,
                         `category_id`	bigint	NOT NULL
);

CREATE TABLE `files` (
                         `file_id`	bigint	NOT NULL,
                         `fileName`	varchar(100)	NOT NULL,
                         `fileRealName`	varchar(100)	NOT NULL,
                         `post_id`	bigint	NOT NULL
);

CREATE TABLE `category` (
                            `category_id`	bigint	NOT NULL,
                            `category`	varchar(25)	NOT NULL
);

CREATE TABLE `comments` (
                            `comment_id`	bigint	NOT NULL,
                            `comment`	varchar(255)	NOT NULL,
                            `created_date`	datetime	NOT NULL,
                            `post_id`	bigint	NOT NULL
);

ALTER TABLE `posts` ADD CONSTRAINT `PK_POSTS` PRIMARY KEY (
                                                           `post_id`
    );

ALTER TABLE `files` ADD CONSTRAINT `PK_FILES` PRIMARY KEY (
                                                           `file_id`
    );

ALTER TABLE `category` ADD CONSTRAINT `PK_CATEGORY` PRIMARY KEY (
                                                                 `category_id`
    );

ALTER TABLE `comments` ADD CONSTRAINT `PK_COMMENTS` PRIMARY KEY (
                                                                 `comment_id`
    );

ALTER TABLE `posts` ADD CONSTRAINT `FK_category_TO_posts_1` FOREIGN KEY (
                                                                         `category_id`
    )
    REFERENCES `category` (
                           `category_id`
        );

ALTER TABLE `files` ADD CONSTRAINT `FK_posts_TO_files_1` FOREIGN KEY (
                                                                      `post_id`
    )
    REFERENCES `posts` (
                        `post_id`
        );

ALTER TABLE `comments` ADD CONSTRAINT `FK_posts_TO_comments_1` FOREIGN KEY (
                                                                            `post_id`
    )
    REFERENCES `posts` (
                        `post_id`
        );

