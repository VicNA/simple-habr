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

insert into articles(user_id,dt_created,dt_published,title,text,status_id, image_path)
select
    (select min(user_id) from users) as user_id,
    now()-1 as dt_created,
    now() as dt_published,
    'Многопоточность в Java' as title,
    'В этой статье я вкратце расскажу вам о процессах, потоках, и об основах многопоточного программирования на языке Java.
     
Наиболее очевидная область применения многопоточности – это программирование интерфейсов. ' ||
    'Многопоточность незаменима тогда, когда необходимо, чтобы графический интерфейс продолжал отзываться на действия пользователя во время выполнения некоторой обработки информации. ' ||
    'Например, поток, отвечающий за интерфейс, может ждать завершения другого потока, загружающего файл из интернета, и в это время выводить некоторую анимацию или обновлять прогресс-бар.' ||
    'Кроме того он может остановить поток загружающий файл, если была нажата кнопка «отмена».
    
Еще одна популярная и, пожалуй, одна из самых хардкорных областей применения многопоточности – игры. ' ||
    'В играх различные потоки могут отвечать за работу с сетью, анимацию, расчет физики и т.п.' as text,
    (select status_id from statuses where status_name = 'published') as status_id,
    'src/main/resources/files/java-tutorials.png' as image_path;

insert into articles(user_id,dt_created,dt_published,title,text,status_id, image_path)
select
    (select max(user_id) from users) as user_id,
    now()-1 as dt_created,
    now() as dt_published,
    'Лучшие практики Android-разработки 2022 за 1 минуту' as title,
    '
В этой статье мы кратко рассмотрим несколько наиболее важных советов, которые сделают вашу повседневную разработку приложений эффективной и легкой.
 
1. Используйте Kotlin — Kotlin является предпочтительным языком для Android-разработки в 2022 году. С момента своего создания Kotlin много развивался и теперь стал зрелым языком.

2. Используйте Jetpack Compose. Декларативная среда создания UI изменила представление о разработке для Android. Просто опишите свой пользовательский интерфейс, и фреймворк позаботится о нем, или, что проще, просто скажите, что вы хотите, и он покажет, что нужно.

3. Выбирайте корутины Kotlin вместо RxJava — это простой и эффективный способ управления потоками.

4. Используйте Hilt для внедрения зависимостей. Он сокращает количество шаблонного кода и позволяет отказаться от ручного внедрения зависимостей в вашем проекте.

5. Используйте Room для локальной базы данных — лучший подход к сохранению данных, чем SQLiteDatabase. Он обеспечивает уровень абстракции над SQLite.

6. Используйте Datastore для хранения пар «ключ-значение». Больше не используйте SharedPreferences! Сохраняйте данные асинхронно, согласованно и транзакционно, преодолевая недостатки SharedPreferences.

7. Используйте Retrofit для работы с сетью — типобезопасный rest клиент для Java и Kotlin.

8. Используйте WorkManager для планирования асинхронных задач — идеальное решение для планирования однократного или многократного запуска задач, даже если приложение закрывается или устройство перезагружается.

9. Всегда измеряйте покрытие кода — JaCoco обладает мощными возможностями для измерения покрытия кода.

10. Тестируйте свои классы с помощью модульных тестов. Вы уже знаете о важности написания модульных тестов. Используйте mockito-kotlin и библиотеку junit, чтобы легко писать тесты. Кроме того, используйте внедрение зависимостей, если это еще не сделано, чтобы разрешить использование классов-заглушек во время тестов.

11. Тестируйте свой UI с помощью Espresso — Android позволяет очень легко писать тесты пользовательского интерфейса. Добавьте их и также запустите на CI.

12. Используйте Coil для загрузки изображений. Знаменитая библиотека загрузки изображений для Android поддерживается корутины Kotlin.

13. Используйте CI/CD для автоматизации разработки — CI/CD необходим для автоматизации процесса разработки. Автоматизируйте все ручные задачи и не беспокойтесь больше о них.

14. Используйте Timber для ведения логов.

15. Используйте Firebase Crashlytics для аналитики сбоев. Этот легкий инструмент для создания отчетов о сбоях в режиме реального времени и он упрощает управление стабильностью вашего приложения.

16. Используйте ktLint для форматирования кода — линтер Kotlin, защищающий от тривиальных проблем, со встроенным форматированием.

17. Используйте архитектуру MVVM — Model-View-ViewModel — это идеальное решение для всех недостатков MVP и MVC, которое делает код тестируемым, несвязанным и простым в обслуживании.

18. Модуляризируйте ваше приложение по функциям — это позволит добиться более быстрой сборки, сокращения boilerplate кода, меньших по размеру и удобных в сопровождении модулей, меньших циклических зависимостей, более простой навигации, разделения ответственности и многих других преимущества.

19. Сделайте безопасность приложения приоритетной. Делая ваше приложение более безопасным, вы помогаете сохранить доверие пользователей и целостность устройства. Ознакомьтесь с этими рекомендациями по обеспечению безопасности, которые оказывают существенное положительное влияние на безопасность вашего приложения.
' as text,
    (select status_id from statuses where status_name = 'published') as status_id,
    'src/main/resources/files/laptop-with-hands.jpg' as image_path;


insert into categories(category_name, category_name_cyr)
values
    ('Marketing', 'Маркетинг'),
    ('Design', 'Дизайн'),
    ('Mobile dev', 'Мобильная разработка'),
    ('Web dev', 'Web разработка');

