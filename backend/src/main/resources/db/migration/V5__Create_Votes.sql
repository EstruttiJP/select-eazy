CREATE TABLE IF NOT EXISTS votes (
    id SERIAL PRIMARY KEY,
    voter_id INTEGER REFERENCES users(id),
    option_id INTEGER REFERENCES options(id),
    topic_id INTEGER REFERENCES topics(id),
    vote_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);