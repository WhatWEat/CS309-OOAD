-- drop table if exists taOfProject,stuInProject,stuViewNotice,stuInGroup,stuAssignment,groupAssignment,stuSubmit,groupSubmit,
--     project,users,notice,groups,assignment,submittedAssignment,evaluation,massage,chat;
-- drop table if exists stusubmit;
--
-- drop table if exists groupsubmit;
--


drop table if exists project;
create table if not exists project
(
    projectId BIGSERIAL primary key,
    name       varchar(200) not null,
    description varchar(2000),
    teacherId bigint not null
    );
drop table if exists users;
create table if not exists users
(
    userId            BIGSERIAL primary key,
    --暂时仅有0（学生）、1（教师）、2（管理员）三种角色
    identity           integer      not null,
    password           varchar  not null,
    phone              varchar(15),
    mail               varchar(30),
    name               varchar(100) not null,
    --m(male)、f(female)
    gender             varchar(1)   not null,
    birthday           date,
    technologyStack   varchar(500),
    programmingSkills varchar(500),
    intendedTeammates varchar(500)
    );
drop table if exists taOfProject;
create table if not exists taOfProject
(
    projectId bigint not null,
    taId      bigint not null
);

drop table if exists stuInProject;
create table if not exists stuInProject
(
    projectId bigint not null,
    stuId      bigint not null
);
drop table if exists notice;
create table if not exists notice
(
    noticeId  BIGSERIAL primary key,
    projectId bigint not null ,
    title      varchar(200)  not null,
    content    varchar(5000) not null,
    creatorId bigint        not null
    );
drop table if exists stuViewNotice;
create table if not exists stuViewNotice
(
    noticeId bigint not null,
    stuId    bigint not null,
    primary key (noticeId, stuId)
    );
drop table if exists groups;
create table if not exists groups
(
    groupId   BIGSERIAL primary key,
    leaderId bigint ,
    creatorId bigint not null ,
    instructorId bigint not null ,
    groupName varchar(100) not null,
    maxsize   bigint       not null,
    projectId bigint       not null,
    description varchar(2000),
    teamTime  timestamp,
    reportTime   timestamp
    );
drop table if exists stuInGroup;
create table if not exists stuInGroup
(
    groupId bigint not null,
    stuId   bigint not null,
    primary key (groupId, stuId)
    );
drop table if exists assignment;
create table if not exists assignment
(
--     这里的id代表的是一项作业的id
    assignmentId BIGSERIAL primary key,
    title         varchar(200)  not null,
    projectId    bigint not null ,
    description   varchar(5000) not null,
    --仅有i(individual)、g(group)两种模式
    type          varchar(1)   not null,
    creatorId    bigint        not null,
    fullMark int not null,
    deadline timestamp not null
    );
-- drop table if exists stuAssignment;
-- create table if not exists stuAssignment
-- (
--     assignmentId bigint not null,
--     stuId        bigint not null,
--     primary key (assignmentId, stuId)
-- );
-- drop table if exists groupAssignment;
-- create table if not exists groupAssignment
-- (
--     assignmentId bigint not null,
--     groupId      bigint not null,
--     primary key (assignmentId, groupId)
-- );
drop table if exists submittedAssignment;
create table if not exists submittedAssignment
(
--     这里的assignmentId代表某项作业的id，而submitId则代表学生提交的一份作业id
    submitId     BIGSERIAL primary key,
    assignmentId bigint not null,
    grade         float,
    submitterId bigint not null ,
    text          varchar(1000),
    comment       varchar(1000),
    review        varchar(100),
    filepath      varchar(200),
    submitTime    timestamp not null
    );

-- 这张表与下groupSubmit均代表提交的作业与提交者的关系
-- drop table if exists stuSubmit;
-- create table if not exists stuSubmit
-- (
--     submitId bigint not null,
--     stuId    bigint not null,
--     primary key (submitId, stuId)
-- );
-- drop table if exists groupSubmit;
-- create table if not exists groupSubmit
-- (
--     submitId bigint not null,
--     groupId  bigint not null,
--     primary key (submitId, groupId)
-- );
drop table if exists evaluation;
create table if not exists evaluation
(
    evaluationId      BIGSERIAL primary key,
    projectId bigint not null ,
    commentGroup   bigint        not null,
    commentedGroup bigint        not null,
    grade           float        not null,
    commentTime    timestamp     not null,
    content         varchar(1000) not null,
    submitId       BIGINT        not null
    );
drop table if exists message;
create table if not exists message
(
    massageId bigint not null ,
    chatId bigint not null ,
    fromId bigint not null ,
    toId bigint not null ,
    chatTime timestamp    not null,
    content  varchar(500) not null
    );
drop table if exists chat;
create table if not exists chat
(
    chatId  BIGSERIAL primary key,
--     为了避免(user1Id,user2Id)与(user2Id,user1Id)同时存在导致重复，此处要求user1Id<user2Id
    user1Id  bigint       not null,
    user2Id  bigint       not null
);
