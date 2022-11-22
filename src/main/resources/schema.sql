
CREATE TABLE schools
(
    id         VARCHAR(8)   NOT NULL PRIMARY KEY,
    version    INT          NOT NULL DEFAULT 0,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE book_categories
(
    id                   INT          NOT NULL PRIMARY KEY,
    version              INT          NOT NULL DEFAULT 0,
    letter               VARCHAR(1)   NOT NULL,
    name                 VARCHAR(256) NOT NULL,
    starting_book_id     INT          NOT NULL DEFAULT 0,
    ending_book_id       INT          NOT NULL DEFAULT 0,
    description          VARCHAR(32)  NOT NULL DEFAULT '',
    school_id            VARCHAR(8)   NOT NULL DEFAULT '',
    ccode_content_digits VARCHAR(256) NOT NULL DEFAULT '',
    created_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uc_book_categories__letter UNIQUE KEY (letter),
    CONSTRAINT fk_book_categories__school_id FOREIGN KEY (school_id) REFERENCES schools (id)
);


CREATE TABLE books
(
    book_id       int          not null,
    title         varchar(256) not null,
    title_kana    varchar(256),
    author        varchar(256) not null,
    author_kana   varchar(256),
    isbn          varchar(32)  not null,
    comments      varchar(511),
    publisher     varchar(256) not null,
    date_added    timestamp             default current_timestamp,
    is_missing    boolean,
    num_checkouts int          not null default 0,
    date_deleted  date,
    date_lost     date,
    version INT NOT NULL DEFAULT 0,
    book_category_id INT NOT NULL,
    school_id VARCHAR (8) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key (book_id),
    FOREIGN KEY fk_books__book_category_id (book_category_id) REFERENCES book_categories (id),
    FOREIGN KEY fk_books__schools_id (school_id) REFERENCES schools (id)
);


CREATE TABLE members
(
    member_id    int         not null,
    date_added   timestamp            default current_timestamp not null,
    active       boolean     not null default true,
    class_number int,
    grade        INT NOT NULL DEFAULT 99,
    firstname_en varchar(64) not null,
    lastname_en  varchar(64) not null,
    firstname    varchar(64),
    lastname     varchar(64),
    school       varchar(8),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    primary key (member_id)
);


CREATE TABLE checkouts
(
    member_id     int  not null,
    book_id       int  not null,
    checkout_date date not null,
    due_date      date not null,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    foreign key fk_checkouts__member_id (member_id) references members (member_id),
    foreign key fk_checkouts__book_id (book_id) references books (book_id),
    unique key uc_checkouts__book_id (book_id),
    primary key (member_id, book_id)
);


-- CREATE TABLE checkoutHistory
-- (
--     book_id       int  not null,
--     member_id     int  not null,
--     checkin_date  date not null,
--     checkout_date date not null,
--     foreign key (member_id) references members (member_id),
--     foreign key (book_id) references books (book_id)
-- );

CREATE TABLE limits
(
    role     varchar(8)  not null,
    property varchar(16) not null,
    value    varchar(64) not null,
    primary key (role, property)
);

CREATE TABLE schedule
(
    date                 varchar(16) not null,
    school               VARCHAR(8)  not null,
    k_max_books          int         not null,
    k_max_checkout_weeks int         not null,
    e_max_books          int         not null,
    e_max_checkout_weeks int         not null,
    m_max_books          int         not null,
    m_max_checkout_weeks int         not null,
    h_max_books          int         not null,
    h_max_checkout_weeks int         not null,
    o_max_books          int         not null,
    o_max_checkout_weeks int         not null,
    primary key (date, school)
);


CREATE TABLE activities
(
    id          INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    version     INT         NOT NULL DEFAULT 0,
    book_id     INT         NOT NULL,
    member_id   INT,
    action_type VARCHAR(30) NOT NULL,
    created_at  TIMESTAMP   not null DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE checkout_limit_defaults
(
    id         INT        NOT NULL PRIMARY KEY,
    version    INT        NOT NULL DEFAULT 0,
    school_id  VARCHAR(8) NOT NULL,
    grade      INT        NOT NULL,
    max_books  INT        NOT NULL,
    max_weeks  INT        NOT NULL,
    created_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uc_checkout_limit_defaults__school_grade UNIQUE (school_id, grade),
    CONSTRAINT fk_checkout_limit_defaults__schools_id FOREIGN KEY (school_id) REFERENCES schools (id)
);

CREATE TABLE checkout_limit_schedules
(
    id            INT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    version       INT        NOT NULL DEFAULT 0,
    school_id     VARCHAR(8) NOT NULL,
    grade         INT        NOT NULL,
    schedule_date DATE       NOT NULL,
    max_books     INT        NOT NULL,
    max_weeks     INT        NOT NULL,
    created_at    TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uc_checkout_limit_schedules__school_grade_date UNIQUE (school_id, grade, schedule_date),
    CONSTRAINT fk_checkout_limit_schedules__schools_id FOREIGN KEY (school_id) REFERENCES schools (id)
);


-- TODO: for existing tables:
--  * Add version column for ones that app updates
--  * Rename to id
