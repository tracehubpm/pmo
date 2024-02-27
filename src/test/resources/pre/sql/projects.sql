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

INSERT INTO projects.project (id,
                              name,
                              location,
                              description,
                              active)
VALUES ('74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d',
        'Test',
        'github@user/test:master',
        'Description',
        true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO projects.performer (login,
                                project,
                                permission)
VALUES ('user',
        '74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d',
        'READ');

INSERT INTO projects.ticket(id,
                            project,
                            number,
                            repo,
                            job,
                            status)
VALUES ('04986038-6e38-4928-b12e-644c99f9cadc',
        '74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d',
        1,
        'user/test',
        'path/to/job',
        'OPENED')
ON CONFLICT (id) DO NOTHING;

INSERT INTO projects.secret(project,
                            key,
                            value)
VALUES ('74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d',
        'key',
        'Iohe7UjU5MjHcxqsKk9vMLTlYHFCXHCLl4q0EjfBbfzmiYtXI1Vfw3KRUQEmbeVh')
ON CONFLICT (project, key) DO NOTHING;