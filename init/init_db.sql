CREATE SCHEMA IF NOT EXISTS dm_schema;

CREATE TABLE IF NOT EXISTS dm_schema.documents (
                                id BIGSERIAL PRIMARY KEY,
                                title TEXT,
                                content TEXT,
                                status TEXT
);

INSERT INTO dm_schema.documents (title, content, status) VALUES
('Project Proposal', 'This document outlines the proposal for the new project, including objectives, scope, and timeline.','processed'),
('Meeting Notes', 'Notes from the weekly team meeting, covering key discussion points and action items.','processed'),
('Research Paper', 'A research paper discussing recent developments in AI and machine learning technologies.','processed');




GRANT CONNECT ON DATABASE dm_db TO dm_user;
GRANT USAGE ON SCHEMA dm_schema TO dm_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA dm_schema TO dm_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA dm_schema GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO dm_user;