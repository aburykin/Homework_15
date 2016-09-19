package ru.sbt.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ABurykin on 19.09.2016.
 */
public class MultyServerRegistrator implements Runnable{

    private final String host;
    private final int port;
    private final Object impl;
    private final ServerSocket serverSocket;


    public MultyServerRegistrator(String host, int port, Object impl) throws IOException {
        this.host = host;
        this.port = port;
        this.impl = impl;
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("\nСервер " + port + " ожидает сообщение: ");
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void listen(String host, int port, Object impl) throws IOException, ClassNotFoundException {
        MultyServerRegistrator server = new MultyServerRegistrator(host, port, impl);
        Thread thread1 = new Thread(server);
        thread1.start();
    }

    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        MultyServerRegistrator.listen("localhost", 5555, new CalculatorImpl());
        MultyServerRegistrator.listen("localhost", 7777, new CalculatorImpl());
    }

    private static Object getResult(ObjectInputStream in, Object impl) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = (String) in.readObject();
        Class[] paramsType = (Class[]) in.readObject();
        Object[] args = (Object[]) in.readObject();
        Method method = impl.getClass().getMethod(methodName, paramsType);

        return method.invoke(impl, args);
    }
}
