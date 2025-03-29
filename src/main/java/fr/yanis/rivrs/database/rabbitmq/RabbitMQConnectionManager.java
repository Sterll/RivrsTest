package fr.yanis.rivrs.database.rabbitmq;

import com.rabbitmq.client.*;
import fr.yanis.rivrs.RMain;
import lombok.Getter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Getter
public class RabbitMQConnectionManager {

    private static final String EXCHANGE_NAME = "scoreExchange";
    private Connection connection;
    private Channel channel;

    public RabbitMQConnectionManager() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(RMain.getInstance().getConfig().getString("rabbitmq.host", "localhost"));
            factory.setPort(RMain.getInstance().getConfig().getInt("rabbitmq.port", 5672));

            String username = RMain.getInstance().getConfig().getString("rabbitmq.username");
            String password = RMain.getInstance().getConfig().getString("rabbitmq.password");

            if (username != null && password != null) {
                factory.setUsername(username);
                factory.setPassword(password);
            }

            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
    /**
     * Publie un message
     */
    public void publishMessage(String message) {
        try {
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ferme la connexion et le channel
     */
    public void close() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
