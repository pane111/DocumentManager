CREATE SCHEMA IF NOT EXISTS dm_schema;

CREATE TABLE IF NOT EXISTS dm_schema.documents (
                                id BIGSERIAL PRIMARY KEY,
                                title TEXT,
                                content TEXT,
                                path TEXT,
                                status TEXT
);
CREATE TABLE IF NOT EXISTS dm_schema.comments (
                                id BIGSERIAL PRIMARY KEY,
                                doc_id BIGSERIAL,
                                author TEXT,
                                content TEXT
);

INSERT INTO dm_schema.comments VALUES (1,1,'max_mustermann','I really like this document');
INSERT INTO dm_schema.comments VALUES (2,1,'jane_doe11','This is very interesting.');
GRANT CONNECT ON DATABASE dm_db TO dm_user;
GRANT USAGE ON SCHEMA dm_schema TO dm_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA dm_schema TO dm_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA dm_schema GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO dm_user;