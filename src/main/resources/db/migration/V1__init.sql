create table roles(
    role_id serial not null primary key,
    role_name varchar(150) not null
);

create table users(
    user_id serial not null primary key,
    user_name varchar(150) not null,
    real_name varchar(40),
    password varchar(150),
    dt_birth timestamp null,
    description varchar(150)
);

create table users_roles
(
  user_id bigint,
  role_id bigint,
  primary key(user_id, role_id),
  foreign key (user_id) references users(user_id),
  foreign key (role_id) references roles(role_id)
);


create table statuses (
    status_id serial not null primary key ,
    status_name varchar(150) not null
);

create table categories(
    category_id serial not null primary key,
    category_name varchar(150) not null
);

create table articles(
    article_id serial not null primary key,
    user_id int not null,
    dt_created timestamp not null default now(),
    dt_published timestamp null,
    title varchar(500) not null,
    text text not null,
    status_id smallint not null,
    url varchar(150) null UNIQUE,
    CONSTRAINT articles_fk_user_id FOREIGN KEY(user_id) REFERENCES users(user_id),
    CONSTRAINT articles_fk_status_id FOREIGN KEY(status_id) REFERENCES statuses(status_id)
);

create table article_to_category(
    article_id int,
    category_id int,
    primary key(article_id, category_id ),
    CONSTRAINT ac_fk_category_id FOREIGN KEY(category_id) REFERENCES categories(category_id) on delete cascade,
    CONSTRAINT ac_fk_article_id FOREIGN KEY(article_id) REFERENCES articles(article_id) on delete cascade
);

create table comments(
     comment_id serial not null primary key,
     text varchar(1000) not null,
     user_id int not null,
     article_id int not null,
     parent_comment_id int,
     dt_created timestamp not null default now(),
     CONSTRAINT comments_fk_user_id FOREIGN KEY(user_id) REFERENCES users(user_id) on delete cascade,
     CONSTRAINT comments_fk_article_id FOREIGN KEY(article_id) REFERENCES articles(article_id) on delete cascade
);


create table likes(
    like_id bigserial not null primary key,
    user_id int not null,
    article_id int not null,
    dt_created timestamp not null default now(),
    CONSTRAINT likes_fk_user_id FOREIGN KEY(user_id) REFERENCES users(user_id) on delete cascade,
    CONSTRAINT likes_fk_article_id FOREIGN KEY(article_id) REFERENCES articles(article_id) on delete cascade
);
