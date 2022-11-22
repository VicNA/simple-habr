insert into users(user_name, password)
values ('admin', '$2a$10$3YLEE6BaSFyGE6jJWdu9zuh1GmgC4QgaUSN2NXGGkyiATh.SQvR5y'),
       ('moderator', '$2a$10$NxdKV8VEmRy7/zUEHmgtvupjyCdX0CH5xqjWYTShsCyIKdjV8zpFe');
--admin-password
--moderator-password
insert into users_roles (user_id, role_id)
values  (5, 3),
        (5, 2),
        (5, 1),
        (6, 2),
        (6, 1);