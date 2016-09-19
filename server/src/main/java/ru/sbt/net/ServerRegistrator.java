package ru.sbt.net;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/*
* Client:
* createProxy
*     send method name + args to server
 *    receive return value from server and return it
* */

/*
* server:
* listen host + port
* read methodName + args
* invoke method via reflection
* send return value to client
*
* */

/* Сервер работающий с одним портом */
public class ServerRegistrator {

    public static void listen(String host, int port, Object impl) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            System.out.println("\nСервер ожидает сообщение: ");
            try (Socket client = serverSocket.accept();
                 ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream())) {

                System.out.println("Сервер получил сообщени от клиента " + client + " и начал его обработку.");

                Object result;
                try {
                    result = getResult(in, impl);
                } catch (Exception e) {
                    result = new Exception("В методе произошла ошибка " + e.getMessage(), e);
                }
                out.writeObject(result);
            }
        }
    }

    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        int port = 5555;
        ServerRegistrator.listen("localhost", port, new CalculatorImpl());
        System.out.println("Сервер завершил свою работу на порте " + port);
    }

    private static Object getResult(ObjectInputStream in, Object impl) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = (String) in.readObject();
        Class[] paramsType = (Class[]) in.readObject();
        Object[] args = (Object[]) in.readObject();
        Method method = impl.getClass().getMethod(methodName, paramsType);

        return method.invoke(impl, args);
    }
}