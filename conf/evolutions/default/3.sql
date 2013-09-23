# --- !Ups
  create table news (
    id                        integer not null,
    title                     varchar(255),
    date                      timestamp,
    short_text                varchar(4000),
    full_text                 varchar(10000),
    small_pict                varchar(255),
    big_pict                  varchar(255),
    constraint pk_news primary key (id)
  );

CREATE SEQUENCE news_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

# --- !Downs

DROP TABLE news;
DROP SEQUENCE news_seq;