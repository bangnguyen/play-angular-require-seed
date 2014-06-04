# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "COURSE" ("id" VARCHAR NOT NULL PRIMARY KEY,"code" VARCHAR NOT NULL,"title" VARCHAR NOT NULL,"level" VARCHAR,"comment" VARCHAR);
create table "PROFILE" ("id" VARCHAR NOT NULL PRIMARY KEY,"email" VARCHAR NOT NULL,"phone" VARCHAR NOT NULL,"firstName" VARCHAR,"lastName" VARCHAR,"address" VARCHAR,"birthday" BIGINT,"created" BIGINT);
create table "USER" ("id" VARCHAR NOT NULL PRIMARY KEY,"username" VARCHAR NOT NULL,"password" VARCHAR NOT NULL,"created" BIGINT NOT NULL);

# --- !Downs

drop table "COURSE";
drop table "PROFILE";
drop table "USER";

