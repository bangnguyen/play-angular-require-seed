# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "USER" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
   "username" VARCHAR NOT NULL,
   "username" VARCHAR NOT NULL,
   "created" BIGINT
   );

# --- !Downs

drop table "USER";

