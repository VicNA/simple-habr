insert into roles(role_name)
values ('ROLE_USER'), ('ROLE_MODERATOR'), ('ROLE_ADMIN');

insert into users(user_name, real_name, dt_birth, description, password)
values ('First user', null, CAST(CAST('1990-10-12' AS date) as DATE),null,'$2a$10$3YLEE6BaSFyGE6jJWdu9zuh1GmgC4QgaUSN2NXGGkyiATh.SQvR5y'),
       ('bob', 'Петр Смирнов', CAST(CAST('1994-10-12' AS date) as DATE), 'Веселый, общительный','$2a$10$3YLEE6BaSFyGE6jJWdu9zuh1GmgC4QgaUSN2NXGGkyiATh.SQvR5y'),
       ('ali', null, null, null,null),
       ('nil', 'Николай Петрович', null, null,'$2a$10$3YLEE6BaSFyGE6jJWdu9zuh1GmgC4QgaUSN2NXGGkyiATh.SQvR5y');

insert into users_roles (user_id, role_id)
values  (1, 2),
        (2, 3),
        (2, 2),
        (3, 1),
        (4, 1);


insert into statuses(status_name)
values ('hidden'), ('moderating'), ('published');

insert into articles(user_id,dt_created,dt_published,title,text,status_id)
select
    (select min(user_id) from users) as user_id,
    now()-1 as dt_created,
    now() as dt_published,
    'Статья про UX-дизайн мобильных приложений и его влияние на продажи' as title,
    'Aenean sit amet feugiat urna, ac luctus sapien. Etiam scelerisque ultricies sapien varius faucibus. Proin consectetur scelerisque nulla, ut efficitur felis rutrum eget. Nulla hendrerit sagittis egestas. Etiam rutrum fringilla orci, et dapibus ipsum mollis vel. Vestibulum hendrerit massa eget massa sollicitudin, egestas convallis tortor fermentum. Mauris at mauris vitae libero ultrices consequat. Ut blandit nisi felis, sed maximus mauris hendrerit sit amet. Curabitur facilisis neque et rutrum semper. Fusce id laoreet tortor. Nullam ornare, mauris iaculis feugiat laoreet, elit sem gravida urna, ut ullamcorper mauris dui eget justo. Aliquam consequat augue ut pellentesque hendrerit. Proin imperdiet dolor eget interdum posuere. Curabitur mi augue, interdum vitae fermentum sed, tristique a ligula. Donec lorem eros, cursus sit amet leo at, commodo vestibulum neque.' as text,
    (select status_id from statuses where status_name = 'published') as status_id;

insert into articles(user_id,dt_created,dt_published,title,text,status_id, image_path)
select
    (select max(user_id) from users) as user_id,
    now()-1 as dt_created,
    now() as dt_published,
    'Статья про проектирование эргономики НЛО' as title,
    'Aenean sit amet feugiat urna, ac luctus sapien. Etiam scelerisque ultricies sapien varius faucibus. Proin consectetur scelerisque nulla, ut efficitur felis rutrum eget. Nulla hendrerit sagittis egestas. Etiam rutrum fringilla orci, et dapibus ipsum mollis vel. Vestibulum hendrerit massa eget massa sollicitudin, egestas convallis tortor fermentum. Mauris at mauris vitae libero ultrices consequat. Ut blandit nisi felis, sed maximus mauris hendrerit sit amet. Curabitur facilisis neque et rutrum semper. Fusce id laoreet tortor. Nullam ornare, mauris iaculis feugiat laoreet, elit sem gravida urna, ut ullamcorper mauris dui eget justo. Aliquam consequat augue ut pellentesque hendrerit. Proin imperdiet dolor eget interdum posuere. Curabitur mi augue, interdum vitae fermentum sed, tristique a ligula. Donec lorem eros, cursus sit amet leo at, commodo vestibulum neque.' as text,
    (select status_id from statuses where status_name = 'published') as status_id,
    'src/main/resources/files/qbfLJ9Cph_I.jpg' as image_path;

insert into categories(category_name, category_name_cyr)
values
    ('Marketing', 'Маркетинг'),
    ('Design', 'Дизайн'),
    ('Mobile dev', 'Мобильная разработка'),
    ('Web dev', 'Web разработка');

insert into article_to_category(category_id, article_id)
select
    category_id,
    (select article_id from articles where title = 'Статья про UX-дизайн мобильных приложений и его влияние на продажи' )
from categories
where category_name in ('Marketing', 'Design',  'Mobile dev');
