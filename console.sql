drop table if exists defenseTeaOfGroup,leaderOfGroup,taOfProject,stuViewNotice,stuInGroup,stuAssignment,
    groupAssignment,stuSubmit,groupSubmit,
    chat,comment,submittedAssignment,assignment,groups,notice,users,project;
create table if not exists project
(
    project_id BIGSERIAL  primary key,
    name varchar(200) not null
    );

create table if not exists users
(
    user_id            BIGSERIAL  primary key,
    --暂时仅有0（学生）、1（教师）、2（管理员）三种角色
    identity           integer  not null,
    password           varchar(20)  not null,
    phone              varchar(15)  ,
    mail               varchar(30)  ,
    name               varchar(100) not null,
    --仅有m(male)、f(female)两种
    gender             varchar(1)  not null,
    birthday           date,
    technology_stack   varchar(500),
    programming_skills varchar(500),
    intended_teammates varchar(500)
    );

create table if not exists taOfProject
(
    project_id bigint not null ,
    ta_id bigint not null
);

create table if not exists notice
(
    notice_id  BIGSERIAL  primary key,
    title      varchar(200)  not null,
    content       varchar(5000) not null,
    creator_id bigint        not null
--     foreign key (creator_id) references users (user_id)
    );

create table if not exists stuViewNotice
(
    notice_id bigint  not null,
    stu_id    bigint not null,
--     foreign key (notice_id) references notice (notice_id),
--     foreign key (stu_id) references users (user_id),
    primary key (notice_id, stu_id)
    );

create table if not exists groups
(
    group_id      BIGSERIAL  primary key,
--     leader_id bigint,
--     foreign key (leader_id) references users(user_id),
    group_name    varchar(100) not null,
    --     instructor_id bigint       not null,
--     foreign key (instructor_id) references users (user_id),
    max_size      bigint       not null,

    project_id bigint not null ,
    team_time timestamp ,
    deadline timestamp
    );

create table if not exists leaderOfGroup
(
    group_id bigint not null ,
    leader_id bigint not null
);

create table if not exists defenseTeaOfGroup
(
    defense_teacher_id bigint not null ,
    group_id bigint not null
);

create table if not exists stuInGroup
(
    group_id bigint  not null,
    stu_id   bigint not null,
--     foreign key (group_id) references groups (group_id),
--     foreign key (stu_id) references users (user_id),
    primary key (group_id, stu_id)
    );



create table if not exists assignment
(
--     这里的id代表的是一项作业的id
    assignment_id BIGSERIAL  primary key,
    title         varchar(200)  not null,
    description   varchar(5000) not null,
    --仅有individual、group两种模式
    type          varchar(20)   not null,
    creator_id    bigint        not null
--     foreign key (creator_id) references users (user_id)
    );

create table if not exists stuAssignment
(
    assignment_id bigint not null ,
    stu_id bigint not null ,
--     foreign key (assignment_id) references assignment(assignment_id),
--     foreign key (stu_id) references users(user_id),
    primary key (assignment_id,stu_id)
    );

create table if not exists groupAssignment
(
    assignment_id bigint not null ,
    group_id bigint not null ,
--     foreign key (assignment_id) references assignment(assignment_id),
--     foreign key (group_id) references groups(group_id),
    primary key (assignment_id,group_id)
    );

create table if not exists submittedAssignment
(
--     这里的assignment_id代表某项作业的id，而submit_id则代表学生提交的一份作业id
    submit_id BIGSERIAL  primary key ,
    assignment_id bigint not null ,
--     foreign key (assignment_id) references assignment(assignment_id),
    grade bigint ,
    text varchar(1000),
    comment varchar(1000) ,
    filepath varchar(200)
    );

create table if not exists stuSubmit
(
    submit_id bigint not null ,
    stu_id bigint not null ,
--     foreign key (submit_id) references submittedAssignment(submit_id),
--     foreign key (stu_id) references users(user_id),
    primary key (submit_id,stu_id)
    );

create table if not exists groupSubmit
(
    submit_id bigint not null ,
    group_id bigint not null ,
--     foreign key (submit_id) references submittedAssignment(submit_id),
--     foreign key (group_id) references groups(group_id),
    primary key (submit_id,group_id)
    );

create table if not exists comment
(
    comment_id BIGSERIAL  primary key ,
    comment_group bigint not null ,
--     foreign key (comment_group) references groups(group_id),
    commented_group bigint not null ,
--     foreign key (commented_group) references groups(group_id),
    grade bigint not null ,
    comment_time timestamp not null ,
    content varchar(1000) not null ,
    submit_id BIGINT not null
    );

create table if not exists chat
(
    chat_id BIGSERIAL  primary key ,
    from_id bigint not null ,
--     foreign key (from_id) references users(user_id),
    to_id bigint not null ,
--     foreign key (to_id) references users(user_id),
    chatTime timestamp not null ,
    content varchar(500) not null
    );
