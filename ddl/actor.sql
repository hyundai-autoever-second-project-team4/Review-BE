CREATE TABLE actor
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    character_name    VARCHAR(255),
    profile_path VARCHAR(255),
    movie_id     BIGINT
);
