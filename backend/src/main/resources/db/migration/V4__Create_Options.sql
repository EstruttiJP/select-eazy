CREATE TABLE IF NOT EXISTS options (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    topic_id INTEGER REFERENCES topics(id)
);
