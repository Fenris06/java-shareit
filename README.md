# java-shareit
Этот сервис предназначен для аренды вещей. Он дает возможсть пользователям сдавть и брать в аренду нужные им вещи. А также оставлять отзывы о взятых вещах. Сервис имеет многомодульную архитектуру и состоит из двух модулей.
* **gateway** сервис предназначен для первичной валидации входящих запросов. Если валидация устпешна, то он передает запрос в основной сервис через клиет restTemplate. Если запрос не прошол валидацию сервис генерирюет исключение и возвращает ответ с указание причины ошибки.
* **server** основной сервис приложения. Он содержит бизнеслонику и взаимодействует с базой данных.
* # Technologies Used
* Java 11
* Springboot 7.7.9
* Hibernate
* Lombok
* PostgreSql
* H2 database for test
* Spring restTemplate
* Maven
* Mockito
* JUnit 5
* Docker
# Database diagram
![database diagram](https://github.com/Fenris06/java-shareit/blob/main/shareit%20-%20public.png)
