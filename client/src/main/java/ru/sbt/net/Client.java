package ru.sbt.net;

import java.io.*;
import java.net.Socket;

/* В этом классе тестирую взаимодействие клиент-сервер */

public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket client = new Socket("127.0.0.1", 7500);
             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) { // сокет закрывается при выходе из блока try

            out.println("Сообщение 1 от клиента 7500");   // отправляем
            out.println("Сообщение 2 от клиента 7500");   // отправляем
            out.println("Сообщение 3 от клиента 7500");   // отправляем
            System.out.println(in.readLine()); // получаем

        }
    }
}
