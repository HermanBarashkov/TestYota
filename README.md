# TestYota

## Окружение
1. Java 8
2. Maven 3.9.9
3. Intellij IDEA
4. Docker
5. Docker-compose
6. Jenkins

## Запуск тестовой среды
### Docker-compose
1. Клонировать проект на локальную машину
```
git clone https://github.com/HermanBarashkov/TestYota.git
```
2. Открыть консоль в директории TestYota
3. Выполнить команду
```
sudo docker-compose up -d
```
### Запуск тестов через IDEA
1. Выполнить команду
```
sudo docker-compose up -d
```
2. Открыть проект TestYota в IDEA
3. Открыть консоль и выполнить команду
```
mvn test
```
4. Дождаться окончания тестов
### Запуск тестов через Jenkins
1. Выполнить команду
```
sudo docker-compose up -d
```
2. Выполнить команду
```
sudo docker-compose logs
```
3. Скопировать пароль для входа в Jenkins
![image](https://github.com/user-attachments/assets/9e1398b6-6cb6-4ed9-8c70-0e48dde6830c)
4. Перейти по адесу
```
http://localhost:8080/
```
5. Вставить пароль
6. Пройти регистрацию
7. Установить плагин "Config File Provider Plugin"
![image](https://github.com/user-attachments/assets/10592fde-bc08-4d97-a295-c042cc3f8d82)
8. В Tools установить Maven
![image](https://github.com/user-attachments/assets/55f35dec-5fd1-4948-9255-e07bcf75e1df)
10. Создать new item
11. Добавить ссылку на исходный код
![image](https://github.com/user-attachments/assets/97433556-0f3a-44bd-8a88-4446bb3cef2d)
12. Добавить шаг сборки
![image](https://github.com/user-attachments/assets/2a50f32f-c869-4596-9063-ed1bee97ea45)
13. Сохранить и нажать "Собрать сейчас"
14. Дождаться оканчания сборки

