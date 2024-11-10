CREATE TABLE movie
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id           BIGINT,
    title              VARCHAR(255) NOT NULL,
    overview           TEXT,
    poster_path        VARCHAR(255),
    backdrop_path      VARCHAR(255),
    adult              BOOLEAN,
    release_date       DATE,
    runtime            INT,
    origin_country     VARCHAR(255),
    total_review_count BIGINT DEFAULT 0,
    total_star_rate    DOUBLE DEFAULT 0.0
);
