/*
 * Copyright (c) 2023-2024 Tracehub.git
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

--liquibase formatted sql

--changeset hizmailovich:1
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--changeset hizmailovich:2
CREATE SCHEMA IF NOT EXISTS projects;

--changeset hizmailovich:3
CREATE TABLE IF NOT EXISTS projects.tickets
(
    id      uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    project uuid                  NOT NULL REFERENCES projects.projects,
    number  BIGINT                NOT NULL,
    repo    CHARACTER VARYING(64) NOT NULL,
    job     CHARACTER VARYING(64) NOT NULL,
    status  CHARACTER VARYING(16) NOT NULL
);