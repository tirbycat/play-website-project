# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  id                        integer not null,
  user_id                   integer not null,
  name                      VARCHAR(40),
  url                       varchar(255),
  password                  varchar(255),
  constraint pk_account primary key (id))
;

create table admin_role (
  id                        integer not null,
  role_name                 varchar(255),
  user_rights               varchar(255),
  constraint pk_admin_role primary key (id))
;

create table admin_user (
  id                        integer not null,
  login                     VARCHAR(40),
  email                     varchar(255),
  password                  VARCHAR(40),
  role_id                   integer,
  constraint pk_admin_user primary key (id))
;

create table site_user (
  id                        integer not null,
  login                     VARCHAR(40),
  email                     varchar(255),
  password                  VARCHAR(40),
  constraint pk_site_user primary key (id))
;

create sequence account_seq;

create sequence admin_role_seq;

create sequence admin_user_seq;

create sequence site_user_seq;

alter table account add constraint fk_account_site_user_1 foreign key (user_id) references site_user (id);
create index ix_account_site_user_1 on account (user_id);
alter table admin_user add constraint fk_admin_user_role_2 foreign key (role_id) references admin_role (id);
create index ix_admin_user_role_2 on admin_user (role_id);



# --- !Downs

drop table if exists account cascade;

drop table if exists admin_role cascade;

drop table if exists admin_user cascade;

drop table if exists site_user cascade;

drop sequence if exists account_seq;

drop sequence if exists admin_role_seq;

drop sequence if exists admin_user_seq;

drop sequence if exists site_user_seq;

