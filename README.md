# Java тестовое задание на позицию Java-разработчика

## Описание проекта
Разработать RESTful API для социальной медиа платформы,
позволяющей пользователям регистрироваться, входить в систему, создавать
посты, переписываться, подписываться на других пользователей и получать
свою ленту активности.

## [Требования](https://docs.yandex.ru/docs/view?url=ya-disk-public%3A%2F%2FObc2qTw61nAUx3n4jZL5jG0yUQHpjOIcOvxwbgQzg3k2r%2BX5iXlAIsTSkseu%2BLOHq%2FJ6bpmRyOJonT3VoXnDag%3D%3D%3A%2F%D0%A2%D0%97%20java.docx&name=%D0%A2%D0%97%20java.docx)

## Реализация
- Написана спецификация [openapi](https://github.com/DariaSuntcova/Social-network/blob/main/OpenApi.yml)
- Приложение написано на Java 17.
- Приложение разработано с использованием Spring Boot 3.
- Аутентификация и авторизация  Spring Security с использованием JSON Web Tokens (JWT).
- Использован сборщик пакетов maven.
- Для запуска используется docker-compose.
- Данные хранятся в БД (Postgres).
- Реализовано создание таблиц и добавление тестовых пользователей в базу данных с помощью Flyway Migration.
- Картинки к постам хранятся в отдельной таблице.
- Код протестирован с помощью Postman.


## Предложения к расширению
- Добавить администрирование(блокировка/разблокировка пользователей и постов)
- Добавить больше информации в профиль пользователя (имя, фамилия, дата рождения, город)
- Добавить поиск пользователей по имени, городу
- Добавить ЧС пользователям
## Запуск приложения
Для запуска приложения
- Загрузить проект на свой компьютер.
- Перейти в папку проекта, где лежит файл docker-compose.yml.
- Вызвать командную строку из этой папки (в адресной строке проводника написать cmd и нажать enter)
- Написать команду docker-compose up -d

Backend запускается на порту 8090.

Postgres запускается на порту 5432.

    bd_name: postgres
    username: postgres
    password: postgres
