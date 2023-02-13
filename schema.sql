CREATE TABLE `posts` (
                         `post_id`	bigint	NOT NULL,
                         `created_date`	datetime	NULL,
                         `modified_date`	datetime	NULL,
                         `title`	varchar(255)	NULL,
                         `author`	varchar(5)	NULL,
                         `content`	TEXT	NULL,
                         `hits`	bigint	NOT NULL	DEFAULT 0,
                         `passwd`	varchar(255)	NULL,
                         `file_flag`	boolean	NOT NULL	DEFAULT false,
                         `category_id`	bigint	NOT NULL
);

ALTER TABLE `posts` ADD CONSTRAINT `PK_POSTS` PRIMARY KEY (
                                                           `post_id`
    );

CREATE TABLE `files` (
                         `file_id`	bigint	NOT NULL,
                         `fileName`	varchar(1000)	NOT NULL,
                         `fileRealName`	varchar(1000)	NOT NULL,
                         `post_id`	bigint	NOT NULL
);

ALTER TABLE `files` ADD CONSTRAINT `PK_FILES` PRIMARY KEY (
                                                           `file_id`
    );

CREATE TABLE `category` (
                            `category_id`	bigint	NOT NULL,
                            `category`	varchar(255)	NOT NULL
);

ALTER TABLE `category` ADD CONSTRAINT `PK_CATEGORY` PRIMARY KEY (
                                                                 `category_id`
    );

CREATE TABLE `comments` (
                            `comment_id`	bigint	NOT NULL,
                            `comment`	varchar(255)	NOT NULL,
                            `created_date`	datetime	NOT NULL,
                            `post_id`	bigint	NOT NULL
);

ALTER TABLE `comments` ADD CONSTRAINT `PK_COMMENTS` PRIMARY KEY (
                                                                 `comment_id`
    );
