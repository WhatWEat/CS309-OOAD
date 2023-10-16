drop table if exists taOfProject,stuViewNotice,stuInGroup,stuAssignment,groupAssignment,stuSubmit,groupSubmit,
    project,users,notice,groups,assignment,submittedAssignment,evaluation,massage,chat;
create table if not exists project
(
    project_id BIGSERIAL primary key,
    name       varchar(200) not null,
    description varchar(2000)
    );

create table if not exists users
(
    user_id            BIGSERIAL primary key,
    --暂时仅有0（学生）、1（教师）、2（管理员）三种角色
    identity           integer      not null,
    password           varchar(20)  not null,
    phone              varchar(15),
    mail               varchar(30),
    name               varchar(100) not null,
    --m(male)、f(female)
    gender             varchar(1)   not null,
    birthday           date,
    technology_stack   varchar(500),
    programming_skills varchar(500),
    intended_teammates varchar(500)
    );

create table if not exists taOfProject
(
    project_id bigint not null,
    ta_id      bigint not null
);

create table if not exists notice
(
    notice_id  BIGSERIAL primary key,
    project_id bigint not null ,
    title      varchar(200)  not null,
    content    varchar(5000) not null,
    creator_id bigint        not null
    );

create table if not exists stuViewNotice
(
    notice_id bigint not null,
    stu_id    bigint not null,
    primary key (notice_id, stu_id)
    );

create table if not exists groups
(
    group_id   BIGSERIAL primary key,
    leader_id bigint ,
    instructor_id bigint not null ,
    group_name varchar(100) not null,
    max_size   bigint       not null,
    project_id bigint       not null,
    description varchar(2000),
    team_time  timestamp,
    report_time   timestamp
    );

create table if not exists stuInGroup
(
    group_id bigint not null,
    stu_id   bigint not null,
    primary key (group_id, stu_id)
    );

create table if not exists assignment
(
--     这里的id代表的是一项作业的id
    assignment_id BIGSERIAL primary key,
    title         varchar(200)  not null,
    project_id    bigint not null ,
    description   varchar(5000) not null,
    --仅有i(individual)、g(group)两种模式
    type          varchar(1)   not null,
    creator_id    bigint        not null
    );

create table if not exists stuAssignment
(
    assignment_id bigint not null,
    stu_id        bigint not null,
    primary key (assignment_id, stu_id)
    );

create table if not exists groupAssignment
(
    assignment_id bigint not null,
    group_id      bigint not null,
    primary key (assignment_id, group_id)
    );

create table if not exists submittedAssignment
(
--     这里的assignment_id代表某项作业的id，而submit_id则代表学生提交的一份作业id
    submit_id     BIGSERIAL primary key,
    assignment_id bigint not null,
    grade         float,
    project_id bigint not null ,
    text          varchar(1000),
    comment       varchar(1000),
    filepath      varchar(200)
    );

-- 这张表与下groupSubmit均代表提交的作业与提交者的关系
create table if not exists stuSubmit
(
    submit_id bigint not null,
    stu_id    bigint not null,
    primary key (submit_id, stu_id)
    );

create table if not exists groupSubmit
(
    submit_id bigint not null,
    group_id  bigint not null,
    primary key (submit_id, group_id)
    );

create table if not exists evaluation
(
    evaluation_id      BIGSERIAL primary key,
    project_id bigint not null ,
    comment_group   bigint        not null,
    commented_group bigint        not null,
    grade           float        not null,
    comment_time    timestamp     not null,
    content         varchar(1000) not null,
    submit_id       BIGINT        not null
    );

create table if not exists massage
(
    massage_id bigint not null ,
    chat_id bigint not null ,
    from_id bigint not null ,
    to_id bigint not null ,
    chatTime timestamp    not null,
    content  varchar(500) not null
    );

create table if not exists chat
(
    chat_id  BIGSERIAL primary key,
--     为了避免(user1_id,user2_id)与(user2_id,user1_id)同时存在导致重复，此处要求user1_id<user2_id
    user1_id  bigint       not null,
    user2_id  bigint       not null
);
