package ru.sbt.net;

import java.lang.reflect.Proxy;
import java.util.List;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.reflect.Proxy.newProxyInstance;

/*
Здесь создается клиент и прокси для него
 */

public class NetClientFactory {
    private final String host;
    private final int port;

    public NetClientFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public <T> T createClient(Class<T> interfaceClass) {
        return (T) newProxyInstance(getSystemClassLoader(), new Class[]{interfaceClass}, new ClientInvocationHandler(host, port));
    }

    public static void main(String[] args) {
        NetClientFactory factory = new NetClientFactory("localhost", 5555); // 127.0.0.1
        Calculator proxyClient = factory.createClient(Calculator.class);
        double calculate = proxyClient.calculate(1, 2);
        System.out.println("Ответ: " + calculate);

        NetClientFactory factory2 = new NetClientFactory("localhost", 7777); // 127.0.0.1
        Calculator proxyClient2 = factory2.createClient(Calculator.class);
        double calculate2 = proxyClient2.calculate(4, 3);
        System.out.println("Ответ: " + calculate2);

        factory2 = new NetClientFactory("localhost", 7777); // 127.0.0.1
         proxyClient2 = factory2.createClient(Calculator.class);
        double calculate3 = proxyClient2.calculate(4, 3);
        System.out.println("Ответ: " + calculate3);

    }
}