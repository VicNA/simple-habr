insert into articles (user_id, title, text, status_id, image_path)
values (2, 'Security Week 2243: кража паролей с помощью тепловизора',
           'Насколько реально восстановить пароль по остаточному нагреву кнопок на клавиатуре?' ||
           'Исследователи из Университета Глазго в Великобритании подробно отвечают на этот вопрос в свежей публикации.' ||
           ' Использование тепловизора для кражи паролей исследовано достаточно подробно. Одна из первых работ по этой теме' ||
           ' — американская публикация 2011 года, в которой оценивалась (положительно) возможность кражи ПИН-кодов с цифровой ' ||
           ' клавиатуры банкомата. В новой публикации эта достаточно экзотическая атака по сторонним каналам выводится ' ||
           ' на новый уровень — с применением технологий машинного обучения.', 1
           ,'src/main/resources/files/laptop-with-hands.jpg'),
         (2, 'Книга «Python без проблем: решаем реальные задачи и пишем полезный код»',
           'Привет, Хаброжители! ' ||
           'Компьютер способен решить практически любую задачу, если ему дать правильные инструкции. С этого и начинается ' ||
           'программирование. Даниэль Зингаро создал книгу для начинающих, чтобы вы сразу учились решать интересные задачи, ' ||
           'которые использовались на олимпиадах по программированию, и развивали мышление программиста.', 2
           ,'src/main/resources/files/select_lang.png'),
         (3, 'Заставим производителей раскрыть дату смерти электроники',
           'Наш анализ 14 популярных потребительских устройств показал, что они могут прекратить работать через 3-4 года ' ||
           ' из-за незаменяемых аккумуляторов. В этой статье мы расскажем, как заставить отрасль технологий проектировать' ||
           ' продукты, способные проработать дольше и наносить меньше ущерба окружающей среде.' ||
           ' Если у вас есть наушники Apple AirPods, то они умрут, и, наверно, раньше, чем вы могли бы предположить.',3
           ,'src/main/resources/files/ustroistva.png'),
         (4,'Собираю умный дом с Марусей',
         'Привет, Хабр! Я уже давно интересуюсь темой личной эффективности и перепробовал много способов её увеличить:'||
         ' тайм-менеджмент, физические нагрузки, питание и другие приёмы и методики. В том числе я затронул тему сна,' ||
         ' а особенно то, как именно я просыпаюсь ежедневно.' ||
         ' По утрам большинство людей либо резко встаёт под громкую мелодию будильника, либо по несколько раз' ||
         ' откладывает пробуждение, успевая погрузиться в прерывистый сон. Оба варианта негативно влияют на наше' ||
         ' здоровье – как физическое, так и ментальное. Поэтому я решил найти формулу идеального пробуждения.'||
         ' С этого началось моё погружение в тему умного дома и в то, как грамотно его организовать.' ,3
         ,'src/main/resources/files/shtuki.png'),
         (2, 'Военные суперкомпьютеры и научные роты: сведения из первых рук',
         'Через два дня, 29 ноября 2022 года, в городе Переславле открывается XI Национальный Суперкомпьютерный Форум.'||
         ' Он ежегодно проходит на базе Института программных систем РАН. Вы можете принять участие очно, через Zoom' ||
         ' или послушать трансляцию докладов.' ||
         ' Год назад на Форуме прозвучал маленький доклад о военном IT и военных суперкомпьютерах. Выступали Николай' ||
         ' Владимирович Перфилов и Василий Иванович Мишин из ЦНИИ-12 Министерства обороны. Их институт работает на ' ||
         ' окраине города Сергиев Посад, их доклад прозвучал 2 декабря 2021 года.',2
          ,'src/main/resources/files/sinie-ustroistva.png'),
         (2, 'Войти в IT: с чего начать и как продолжить? Лайфхаки от Skillaz',
         ' В Сети ну очень много статей о том, как человек, у которого нет IT-бэкграунда, может легко "перейти в айти",' ||
         ' причем без особого труда. На самом деле, все обстоит не совсем так. Дело в том, что большинство подобных' ||
         ' статей публикуются с подачи разного рода курсов, которые обещают сделать из гуманитария Python-программиста' ||
         ' за месяц, ну или два, причем с трудоустройством и большой зарплатой.'||
         '  Меня зовут Марина Рыбакова, я HR директор в компании Skillaz. Мы занимаемся, в том числе, наймом' ||
         ' IT-профессионалов, поэтому прекрасно понимаем, что зеленый новичок после курсов вряд ли получит все то, что' ||
         ' ему обещают многочисленные статьи в интернете. Исключения, конечно, есть, но их не так много. Давайте лучше' ||
         ' поговорим о том, с чего стоит начать человеку, который очень хочет попасть в IT. ',2
        ,'src/main/resources/files/ruki-i-shtuki.png'),
         (2, 'Разбираемся с Docker: как создаются образы',
         'От любого инструмента, который внедряем в проект, мы ждём стабильной работы. Docker не исключение. Чтобы' ||
         ' иметь возможность оперативно выявлять потенциальные проблемы и избегать сбоев, необходимо понимать' ||
         ' внутренние особенности технологии. Эта статья — сборник заметок, которые помогут разобраться, как создаются'||
         ' образы контейнеров. ',2
         ,'src/main/resources/files/docker.png'),
        (3,'Дизайн мобильных приложений: полный гайд по UX/UI',
        'Дизайн мобильных приложений — это по сути создание мобильной версии сайта с дополнительными возможностями. При этом главная задача разработчиков заключается в создании удобной экосистемы с совершенным UX.
        Скачивая какое-либо приложение, юзер по умолчанию лоялен: он уже совершил целевое действие, и если он сможет решить свою проблему с помощью вашего сервиса — то будет пользоваться им на регулярной основе. Однако если хотя бы один раздел на пути клиента не работает или неудобен — человек просто удалит ваше приложение – и не вернется к нему никогда.
        В этом заключается принципиальное отличие UX-дизайна сайтов и приложений: как правило, пользователь оценивает удобство нескольких веб-продуктов, и даже если при первом визите человек не совершил целевое действие, всегда есть вероятность, что спустя некоторое время он вернется на ваш сайт. При этом повторное скачивание приложений — это скорее исключение, чем распространенная практика.'
        ,3,'src/main/resources/files/html-code.png');


insert into likes (user_id, article_id)
values (1,1),
       (1,2),
       (2,1),
       (3,1),
       (2,3),
       (2,4);


insert into comments (text, user_id, article_id)
values ('Отличная статья', 1, 1),
       ('Статья не понравилась. Совсем', 1, 2),
       ('Статья не очень', 1 , 3),
       ('Статья супер', 2 , 4),
       ('Потрясающе!!!', 1, 4);


insert into article_to_category (article_id, category_id)
values (2, 1),
       (3, 2),
       (4, 3),
       (5, 3),
       (6, 4),
       (10,2),
       (7,1),
       (8,2),
       (9,3);

