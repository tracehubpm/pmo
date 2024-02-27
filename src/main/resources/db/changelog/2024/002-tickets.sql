/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023-2024 Tracehub.git
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
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

--changeset hizmailovich:4
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--changeset hizmailovich:5
CREATE SCHEMA IF NOT EXISTS projects;

--changeset hizmailovich:6
CREATE TABLE IF NOT EXISTS projects.tickets
(
    id      uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    project uuid                  NOT NULL REFERENCES projects.projects,
    number  int                   NOT NULL,
    repo    CHARACTER VARYING(64) NOT NULL,
    job     CHARACTER VARYING(64) NOT NULL,
    status  CHARACTER VARYING(16) NOT NULL
);