-- Insert Genres
INSERT INTO genre (name) VALUES ('Drama');
INSERT INTO genre (name) VALUES ('Biography');
INSERT INTO genre (name) VALUES ('Science Fiction');
INSERT INTO genre (name) VALUES ('Adventure');
INSERT INTO genre (name) VALUES ('Historical Drama');
INSERT INTO genre (name) VALUES ('Comedy');

-- Insert Movies
INSERT INTO movie (movie_name, running_time_in_minutes, description, age_restriction, release_year, photo, genre_names) VALUES
                                                                                                                            ('One Life', 110, '', 'PG13', 2023, '', 'Drama, Biography'),
                                                                                                                            ('Dune: Part Two', 166, '', 'PG', 2023, '', 'Science Fiction, Adventure'),
                                                                                                                            ('Oppenheimer', 180, '', 'PG13', 2023, '', 'Biography, Historical Drama'),
                                                                                                                            ('Anyone But You', 104, '', 'PG13', 2023, '', 'Comedy');

-- Insert Relationships between Movies and Genres
INSERT INTO movie_genre (movie_id, genre_id) SELECT m.id, g.id FROM movie m, genre g WHERE m.genre_names LIKE CONCAT('%', g.name, '%');
