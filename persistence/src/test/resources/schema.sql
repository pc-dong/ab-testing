CREATE TABLE experiment
(
    id          VARCHAR(64) not null primary key,
    description VARCHAR(128),
    version BIGINT
);

CREATE TABLE bucket
(
    id            VARCHAR(64) not null primary key,
    experiment_id VARCHAR(64) not null,
    bucket_key           VARCHAR(64) not null,
    name          VARCHAR(64) not null,
    config        VARCHAR(512),
    version BIGINT
);

CREATE TABLE member_criteria_condition
(
    id            VARCHAR(64) not null primary key,
    experiment_id VARCHAR(64) not null,
    segment_id    VARCHAR(64) not null,
    version BIGINT
);

CREATE TABLE assignment
(
    id            VARCHAR(64) not null primary key,
    experiment_id VARCHAR(64) not null,
    bucket_key    VARCHAR(64) not null,
    bucket_config    VARCHAR(64) not null,
    customer_id    VARCHAR(64) not null,
    version BIGINT
);