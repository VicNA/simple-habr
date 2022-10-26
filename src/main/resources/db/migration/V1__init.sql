create table roles(
    role_id serial not null primary key,
    role_name varchar(150) not null
);

create table users(
    user_id serial not null primary key,
    user_name varchar(150) not null,
    role_id int not null,
    CONSTRAINT users_fk_role_id FOREIGN KEY(role_id) REFERENCES roles(role_id)
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
    status varchar(16),
    CONSTRAINT articles_fk_user_id FOREIGN KEY(user_id) REFERENCES users(user_id)
);

create table article_to_category(
    article_id int,
    category_id int,
    primary key(article_id, category_id ),
    CONSTRAINT ac_fk_category_id FOREIGN KEY(category_id) REFERENCES categories(category_id),
    CONSTRAINT ac_fk_article_id FOREIGN KEY(article_id) REFERENCES articles(article_id)
);

create table comments(
     comment_id serial not null primary key,
     text varchar(1000) not null,
     user_id int not null,
     article_id int not null,
     dt_created timestamp not null default now(),
     CONSTRAINT comments_fk_user_id FOREIGN KEY(user_id) REFERENCES users(user_id),
     CONSTRAINT comments_fk_article_id FOREIGN KEY(article_id) REFERENCES articles(article_id)
);


create table likes(
    like_id bigserial not null primary key,
    user_id int not null,
    article_id int not null,
    dt_created timestamp not null default now(),
    CONSTRAINT likes_fk_user_id FOREIGN KEY(user_id) REFERENCES users(user_id),
    CONSTRAINT likes_fk_article_id FOREIGN KEY(article_id) REFERENCES articles(article_id)
);