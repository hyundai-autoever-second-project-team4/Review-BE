CREATE TABLE DIRECTOR
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    profile_path VARCHAR(255),
    movie_id     BIGINT
);
