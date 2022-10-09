CREATE TABLE app_role_entity_permissions
(
    app_role_entity_id BIGINT NOT NULL,
    permissions        VARCHAR(255)
);

CREATE TABLE book_author
(
    author_id BIGINT NOT NULL,
    book_id   BIGINT NOT NULL
);

CREATE TABLE book_genre
(
    book_id  BIGINT NOT NULL,
    genre_id BIGINT NOT NULL
);

CREATE TABLE books
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255),
    tagline     VARCHAR(30),
    description VARCHAR(200),
    rating      DOUBLE PRECISION,
    CONSTRAINT pk_books PRIMARY KEY (id)
);

CREATE TABLE genres
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(200),
    CONSTRAINT pk_genres PRIMARY KEY (id)
);

CREATE TABLE images
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    image_name VARCHAR(255),
    location   VARCHAR(255),
    CONSTRAINT pk_images PRIMARY KEY (id)
);

CREATE TABLE role_requests
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id     BIGINT                                  NOT NULL,
    role_id     BIGINT                                  NOT NULL,
    time_stamp  TIMESTAMP WITHOUT TIME ZONE,
    state       VARCHAR(255),
    description VARCHAR(200),
    CONSTRAINT pk_role_requests PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    role_name  VARCHAR(255)                            NOT NULL,
    is_default BOOLEAN                                 NOT NULL,
    time_stamp TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name          VARCHAR(255),
    user_name     VARCHAR(255),
    password      VARCHAR(255),
    role_id       BIGINT,
    user_image_id BIGINT,
    time_stamp    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_rolename UNIQUE (role_name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (user_name);

ALTER TABLE role_requests
    ADD CONSTRAINT FK_ROLE_REQUESTS_ON_ROLE FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE role_requests
    ADD CONSTRAINT FK_ROLE_REQUESTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ROLE FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_USERIMAGE FOREIGN KEY (user_image_id) REFERENCES images (id);

ALTER TABLE app_role_entity_permissions
    ADD CONSTRAINT fk_approleentity_permissions_on_app_role_entity FOREIGN KEY (app_role_entity_id) REFERENCES roles (id);

ALTER TABLE book_author
    ADD CONSTRAINT fk_booaut_on_app_user_entity FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE book_author
    ADD CONSTRAINT fk_booaut_on_book_entity FOREIGN KEY (book_id) REFERENCES books (id);

ALTER TABLE book_genre
    ADD CONSTRAINT fk_boogen_on_book_entity FOREIGN KEY (book_id) REFERENCES books (id);

ALTER TABLE book_genre
    ADD CONSTRAINT fk_boogen_on_genre_entity FOREIGN KEY (genre_id) REFERENCES genres (id);