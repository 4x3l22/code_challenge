CREATE TABLE customer
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    create_at datetime     NOT NULL,
    update_at datetime     NOT NULL,
    delete_at datetime NULL,
    name      VARCHAR(500) NOT NULL,
    last_name VARCHAR(500) NOT NULL,
    age       INT          NOT NULL,
    birt_date datetime     NOT NULL,
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE user
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    create_at   datetime     NOT NULL,
    update_at   datetime     NOT NULL,
    delete_at   datetime NULL,
    username    VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    rol         VARCHAR(100) NOT NULL,
    customer_id BIGINT       NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (username);

ALTER TABLE user
    ADD CONSTRAINT FK_USER_ON_CUSTOMERID FOREIGN KEY (customer_id) REFERENCES customer (id);