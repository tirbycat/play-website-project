# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  name                      varchar(255) not null,
  password                  varchar(255),
  assigned_to_id            integer,
  constraint pk_account primary key (name))
;

create table site_user (
  id                        integer not null,
  login                     varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_site_user primary key (id))
;

create sequence account_seq;

create sequence site_user_seq;

alter table account add constraint fk_account_assignedTo_1 foreign key (assigned_to_id) references site_user (id);
create index ix_account_assignedTo_1 on account (assigned_to_id);



# --- !Downs

drop table if exists account cascade;

drop table if exists site_user cascade;

drop sequence if exists account_seq;

drop sequence if exists site_user_seq;

