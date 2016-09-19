package ru.sbt.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/* В этом классе тестирую взаимодействие клиент-сервер */
public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(5555);
        while (true) {
            System.out.print("\nСервер ожидает сообщение: ");
            try (Socket client = serverSocket.accept();
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) { // начинает слушать сокет

                System.out.println(in.readLine()); // получаем
                System.out.println(in.readLine()); // получаем
                System.out.println(in.readLine()); // получаем
                Thread.sleep(1000); // типа долгая обработка
                out.println("Сервер получил сообщение! Клиент молодец!"); // отправляем
        }
        }
        // System.out.println("Сервер завершил свою работу");
    }
}
