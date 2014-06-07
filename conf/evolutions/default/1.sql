# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "COURSE" ("id" VARCHAR NOT NULL PRIMARY KEY,"code" VARCHAR,"title" VARCHAR,"level" VARCHAR,"teacher1" VARCHAR,"teacher2" VARCHAR,"comment" VARCHAR,"start" BIGINT,"finish" BIGINT,"isOpen" BOOLEAN,"days" VARCHAR,"hours" VARCHAR);
create table "CourseStudent" ("id" VARCHAR NOT NULL PRIMARY KEY,"courseId" VARCHAR NOT NULL,"isPaid" BOOLEAN,"comment" VARCHAR);
create table "PROFILE" ("id" VARCHAR NOT NULL PRIMARY KEY,"email" VARCHAR NOT NULL,"phone" VARCHAR NOT NULL,"firstName" VARCHAR NOT NULL,"lastName" VARCHAR NOT NULL,"address" VARCHAR,"position" VARCHAR,"birthday" BIGINT,"created" BIGINT);
create table "USER" ("id" VARCHAR NOT NULL PRIMARY KEY,"username" VARCHAR NOT NULL,"password" VARCHAR NOT NULL,"created" BIGINT NOT NULL);

# --- !Downs

drop table "COURSE";
drop table "CourseStudent";
drop table "PROFILE";
drop table "USER";

