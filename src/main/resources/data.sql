insert into roles(role_name)
values ( 'Usual user'), ('moderator'), ('administrator');

insert into users(user_name,role_id)
select 'First user', role_id from roles where role_name = 'Usual user';


insert into statuses(status_name)
values ('created'), ('moderating'), ('published'), ('hidden');

insert into articles(user_id,dt_created,dt_published,title,text,status_id,url)
select
    (select min(user_id) from users) as user_id,
    now()-1 as dt_created,
    now() as dt_published,
    'Статья про UX-дизайн мобильных приложений и его влияние на продажи' as title,
    'Aenean sit amet feugiat urna, ac luctus sapien. Etiam scelerisque ultricies sapien varius faucibus. Proin consectetur scelerisque nulla, ut efficitur felis rutrum eget. Nulla hendrerit sagittis egestas. Etiam rutrum fringilla orci, et dapibus ipsum mollis vel. Vestibulum hendrerit massa eget massa sollicitudin, egestas convallis tortor fermentum. Mauris at mauris vitae libero ultrices consequat. Ut blandit nisi felis, sed maximus mauris hendrerit sit amet. Curabitur facilisis neque et rutrum semper. Fusce id laoreet tortor. Nullam ornare, mauris iaculis feugiat laoreet, elit sem gravida urna, ut ullamcorper mauris dui eget justo. Aliquam consequat augue ut pellentesque hendrerit. Proin imperdiet dolor eget interdum posuere. Curabitur mi augue, interdum vitae fermentum sed, tristique a ligula. Donec lorem eros, cursus sit amet leo at, commodo vestibulum neque.' as text,
    (select status_id from statuses where status_name = 'published') as status_id,
    'my-first_article' as url ;



insert into categories(category_name, url)
values
    ('Marketing', 'marketing'),
    ('Design', 'design'),
    ('Mobile dev', 'mobile-dev'),
    ('Web dev', 'web-dev');

insert into article_to_category(category_id, article_id)
select
    category_id,
    (select article_id from articles where title = 'Статья про UX-дизайн мобильных приложений и его влияние на продажи' )
from categories
where category_name in ('Marketing', 'Design',  'Mobile dev');

--comment, likes