# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "Role" ("id" SERIAL NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL,"description" VARCHAR(254));
create table "UserRole" ("userId" BIGINT NOT NULL,"roleId" BIGINT NOT NULL);
alter table "UserRole" add constraint "UserRole_pk" primary key("userId","roleId");
create table "User" ("id" SERIAL NOT NULL PRIMARY KEY,"username" VARCHAR(254) NOT NULL,"password" VARCHAR(254) NOT NULL,"email" VARCHAR(254) NOT NULL);
alter table "UserRole" add constraint "User_fk" foreign key("userId") references "User"("id") on update NO ACTION on delete NO ACTION;
alter table "UserRole" add constraint "Role_fk" foreign key("roleId") references "Role"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "UserRole" drop constraint "User_fk";
alter table "UserRole" drop constraint "Role_fk";
drop table "Role";
alter table "UserRole" drop constraint "UserRole_pk";
drop table "UserRole";
drop table "User";

