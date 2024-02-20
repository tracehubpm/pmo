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