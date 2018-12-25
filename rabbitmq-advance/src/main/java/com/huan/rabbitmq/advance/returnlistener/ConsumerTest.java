package com.huan.rabbitmq.advance.returnlistener;


import com.rabbitmq.client.*;

public class ConsumerTest {

    public static void main(String[] args)throws Exception {
        // 1、创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setPort(AMQP.PROTOCOL.PORT);
        factory.setHost("39.104.169.209");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("/");
        // 2、创建连接
        Connection connection = factory.newConnection();
        // 3、获取通道
        Channel channel = connection.createChannel();

        String exchangeName = "return_exchange";
        String routingKey = "return.key";
        String exchangeType = "direct";
        String queueName = "return_queue";

        channel.exchangeDeclare(exchangeName, exchangeType, true, false, null);

        channel.queueDeclare(queueName, true, false, false, null);

        channel.queueBind(queueName, exchangeName, routingKey);

        QueueingConsumer consumer = new QueueingConsumer(channel);

        channel.basicConsume(queueName, true, consumer);

        while (true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            byte[] body = delivery.getBody();
            String msg = new String(body);
            System.out.println("消费者收到生产者生产的消息是： " + msg);
        }
    }

}
