CREATE TABLE REVIEW (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        movie_id BIGINT,
                        member_id BIGINT,
                        star_rate DOUBLE,
                        content TEXT,
                        spoiler BOOLEAN,
                        created_at TIMESTAMP,
                        deleted BOOLEAN NOT NULL DEFAULT false
);
