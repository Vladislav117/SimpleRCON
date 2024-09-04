# SimpleRCON

SimpleRCON - java-библиотека с минимальным функционалом для общения с сервером по протоколу RCON.

> Пакет `ru.vladislav117.simplercon.kr5chrcon` содержит классы из репозитория [kr5ch/rkon-core](https://github.com/kr5ch/rkon-core). Автор всего внутреннего функционала [kr5ch](https://github.com/kr5ch).

> The package `ru.vladislav117.simplercon.kr5chrcon` contains classes from the [kr5ch/rkon-core](https://github.com/kr5ch/rkon-core) repository. The author of all internal functionality [kr5ch](https://github.com/kr5ch).

## Добавление зависимости

### Gradle

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Vladislav117:SimpleRCON:1.0'
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.Vladislav117</groupId>
    <artifactId>SimpleRCON</artifactId>
    <version>1.0</version>
</dependency>
```

## Использование

Пример отправки сообщения "hello" на сервер:

```java
// Подключение к серверу.
SimpleRCON rcon = SimpleRCON.connect("127.0.0.1", "7777", "password");

// Если не удалось подключиться, метод connect вернёт null.
if (rcon == null) return;

// Отправка сообщения на сервер и получение ответа.
String response = rcon.send("hello");

// Если не удалось отправить сообщение, то в ответе будет null.
if (response == null) return;

// Отключение от сервера.
rcon.disconnect();
```

Если вы хотите отключить вывод исключений или изменить метод вывода, воспользуйтесь этим:

```java
// Отключение вывода исключений.
SimpleRCON.setDisplayExceptions(false);

// Изменение метода вывода исключений.
SimpleRCON.setExceptionDisplay(exception -> exception.printStackTrace());
```
