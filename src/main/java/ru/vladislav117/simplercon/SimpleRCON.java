package ru.vladislav117.simplercon;

import org.jetbrains.annotations.Nullable;
import ru.vladislav117.simplercon.kr5chrkon.Kr5chRcon;

import java.util.function.Consumer;

/**
 * Объект связи с сервером по RCON.
 */
public class SimpleRCON {
    static boolean displayExceptions = true;
    static Consumer<Exception> exceptionDisplay = Throwable::printStackTrace;

    /**
     * Получение статуса отображения исключений. По умолчанию отображение включено.
     *
     * @return Статус отображения исключений.
     */
    public static boolean isDisplayExceptions() {
        return displayExceptions;
    }

    /**
     * Установка статуса отображения исключений. По умолчанию отображение включено.
     *
     * @param displayExceptions Статус отображения исключений.
     */
    public static void setDisplayExceptions(boolean displayExceptions) {
        SimpleRCON.displayExceptions = displayExceptions;
    }

    /**
     * Установка метода отображения исключений. По умолчанию это printStackTrace().
     *
     * @param exceptionDisplay Метод отображения исключений.
     */
    public static void setExceptionDisplay(Consumer<Exception> exceptionDisplay) {
        SimpleRCON.exceptionDisplay = exceptionDisplay;
    }

    /**
     * Отображение исключения. Если отображение отключено, то ничего не произойдёт.
     *
     * @param exception Исключение.
     */
    static void displayException(Exception exception) {
        if (displayExceptions) exceptionDisplay.accept(exception);
    }

    protected Kr5chRcon rcon;

    /**
     * Создание объекта связи по RCON.
     *
     * @param rcon RCON-объект от kr5ch.
     */
    protected SimpleRCON(Kr5chRcon rcon) {
        this.rcon = rcon;
    }

    /**
     * Создание соединения с сервером.
     *
     * @param address  Адрес сервера
     * @param port     RCON-порт
     * @param password RCON-пароль
     * @return Созданный объект связи с сервером или null, если произошла какая-либо ошибка.
     */
    public static @Nullable SimpleRCON connect(String address, String port, String password) {
        int portNumber;
        try {
            portNumber = Integer.parseInt(port);
        } catch (NumberFormatException exception) {
            displayException(exception);
            return null;
        }

        Kr5chRcon rcon;
        try {
            rcon = new Kr5chRcon(address, portNumber, password.getBytes());
        } catch (Exception exception) {
            displayException(exception);
            return null;
        }

        return new SimpleRCON(rcon);
    }

    /**
     * Отправка сообщения серверу.
     *
     * @param message Сообщение
     * @return Ответ сервера или null, если произошла какая-либо ошибка.
     */
    public @Nullable String send(String message) {
        String response = null;
        try {
            response = rcon.command(message);
        } catch (Exception exception) {
            displayException(exception);
        }
        return response;
    }

    /**
     * Отключение соединения с сервером.
     */
    public void disconnect() {
        try {
            rcon.disconnect();
        } catch (Exception exception) {
            displayException(exception);
        }
    }
}
