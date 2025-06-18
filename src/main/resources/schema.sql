CREATE DATABASE IF NOT EXISTS movieratingdatabase;

USE movieratingdatabase;


create table genres
(
    genre_id    int auto_increment
        primary key,
    genre_name  varchar(50) not null,
    description text        null
);

create table movies
(
    movie_id     int auto_increment
        primary key,
    title        varchar(255) not null,
    release_date date         not null,
    minutes      int          not null,
    image        blob         null
);

create table cast
(
    cast_id    int auto_increment
        primary key,
    cast_name  varchar(255) not null,
    birth_date date         null,
    gender     varchar(10)  null,
    biography  text         null,
    photo      blob         null,
    movie_id   int          null,
    char_name  varchar(255) null,
    constraint cast_ibfk_1
        foreign key (movie_id) references movies (movie_id)
);

create index movie_id
    on cast (movie_id);

create table movie_genres
(
    movie_id int not null,
    genre_id int not null,
    primary key (movie_id, genre_id),
    constraint movie_genres_ibfk_1
        foreign key (movie_id) references movies (movie_id),
    constraint movie_genres_ibfk_2
        foreign key (genre_id) references genres (genre_id)
);

create index genre_id
    on movie_genres (genre_id);

create table users
(
    id       int auto_increment
        primary key,
    name     varchar(255)                          not null,
    email    varchar(255)                          not null,
    password varchar(255)                          not null,
    image    blob                                  null,
    role     enum ('user', 'admin') default 'user' null,
    constraint email
        unique (email)
);

create table ratings
(
    rating_id   int auto_increment
        primary key,
    user_id     int                                   null,
    movie_id    int                                   null,
    rating      int                                   null,
    rating_date timestamp default current_timestamp() not null,
    constraint ratings_ibfk_1
        foreign key (user_id) references users (id),
    constraint ratings_ibfk_2
        foreign key (movie_id) references movies (movie_id)
);

create index movie_id
    on ratings (movie_id);

create index user_id
    on ratings (user_id);

create table reviews
(
    review_id   int auto_increment
        primary key,
    user_id     int                                   null,
    movie_id    int                                   null,
    comment     text                                  not null,
    review_date timestamp default current_timestamp() not null,
    constraint reviews_ibfk_1
        foreign key (user_id) references users (id),
    constraint reviews_ibfk_2
        foreign key (movie_id) references movies (movie_id)
);

create index movie_id
    on reviews (movie_id);

create index user_id
    on reviews (user_id);

create table watchlist
(
    watchlist_id int auto_increment
        primary key,
    user_id      int                                   null,
    movie_id     int                                   null,
    added_date   timestamp default current_timestamp() not null,
    constraint watchlist_ibfk_1
        foreign key (user_id) references users (id),
    constraint watchlist_ibfk_2
        foreign key (movie_id) references movies (movie_id)
);

create index movie_id
    on watchlist (movie_id);

create index user_id
    on watchlist (user_id);