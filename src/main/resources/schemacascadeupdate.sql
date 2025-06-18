ALTER TABLE ratings DROP FOREIGN KEY ratings_ibfk_2;
ALTER TABLE ratings
    ADD CONSTRAINT ratings_ibfk_2
        FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
            ON DELETE CASCADE;

ALTER TABLE reviews DROP FOREIGN KEY reviews_ibfk_2;
ALTER TABLE reviews
    ADD CONSTRAINT reviews_ibfk_2
        FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
            ON DELETE CASCADE;

ALTER TABLE watchlist DROP FOREIGN KEY watchlist_ibfk_2;
ALTER TABLE watchlist
    ADD CONSTRAINT watchlist_ibfk_2
        FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
            ON DELETE CASCADE;

ALTER TABLE cast DROP FOREIGN KEY cast_ibfk_1;
ALTER TABLE cast
    ADD CONSTRAINT cast_ibfk_1
        FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
            ON DELETE CASCADE;

ALTER TABLE movie_genres DROP FOREIGN KEY movie_genres_ibfk_1;
ALTER TABLE movie_genres
    ADD CONSTRAINT movie_genres_ibfk_1
        FOREIGN KEY (movie_id) REFERENCES movies (movie_id)
            ON DELETE CASCADE;
