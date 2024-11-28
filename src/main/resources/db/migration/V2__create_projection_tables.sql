create table if not exists users_projections
(
    user_id uuid primary key,
    name text not null,
    nickname text not null
);
create unique index if not exists users_projections_nickname_indx on users_projections(nickname);
create unique index if not exists users_projections_name_indx on users_projections(name);


create table if not exists project_info
(
    project_id uuid primary key,
    project_title text not null
);
create index if not exists project_info_project_title_indx on project_info(project_title);


create table if not exists project_participants
(
    project_id uuid not null,
    user_id uuid not null,
    primary key (project_id, user_id)
);
create table if not exists project_users
(
    user_id uuid primary key,
    username text not null
);
create index if not exists project_participants_project_id_indx on project_participants(project_id);


create table if not exists task_infos
(
    task_id uuid primary key,
    project_id uuid not null,
    task_name text not null,
    task_status_id uuid
);
create table if not exists task_executors
(
    task_id uuid primary key,
    user_id uuid not null
);
create table if not exists task_users
(
    user_id uuid primary key,
    username text not null
);
create table if not exists task_statuses
(
    status_id uuid primary key,
    project_id uuid not null,
    status_name text not null,
    hex_color varchar(6) not null
);
create index if not exists task_statuses_project_id_indx on task_statuses(project_id);

