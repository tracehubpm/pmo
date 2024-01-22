INSERT INTO projects.projects (id,
                               name,
                               location,
                               description,
                               active)
VALUES ('74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d',
        'Test',
        'github@user/test:master',
        'Description',
        true);
INSERT INTO projects.performers (email,
                                 project,
                                 permission)
VALUES ('user',
        '74bb5ec8-0e6b-4618-bfa4-a0b76b7b312d',
        'READ');