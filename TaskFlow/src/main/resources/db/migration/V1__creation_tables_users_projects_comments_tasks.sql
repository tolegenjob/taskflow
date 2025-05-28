CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     first_name VARCHAR(255) NOT NULL,
                                     last_name VARCHAR(255) NOT NULL,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     active BOOLEAN NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS projects (
                                        id BIGSERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
                                        description TEXT NOT NULL,
                                        status VARCHAR(50) NOT NULL,
                                        owner_id BIGINT NOT NULL REFERENCES users(id),
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users_projects (
                                              project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
                                              user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                              PRIMARY KEY (project_id, user_id)
);

CREATE TABLE IF NOT EXISTS tasks (
                                     id BIGSERIAL PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
                                     description TEXT NOT NULL,
                                     status VARCHAR(50) NOT NULL,
                                     priority VARCHAR(50) NOT NULL,
                                     deadline TIMESTAMP,
                                     assigned_user_id BIGINT NOT NULL REFERENCES users(id),
                                     project_id BIGINT NOT NULL REFERENCES projects(id),
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGSERIAL PRIMARY KEY,
                                        content TEXT NOT NULL,
                                        task_id BIGINT NOT NULL REFERENCES tasks(id),
                                        user_id BIGINT NOT NULL REFERENCES users(id),
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);