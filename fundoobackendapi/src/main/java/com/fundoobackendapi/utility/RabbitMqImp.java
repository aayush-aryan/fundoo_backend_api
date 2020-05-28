package com.fundoobackendapi.utility;
import com.fundoobackendapi.dto.RabbitMqDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class RabbitMqImp implements IRabbitMq{
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMessageToQueue(RabbitMqDto rabbitMqDto){
        final String exchange = "rabbitExchange";
        final String routingKey = "RoutingKey";
        System.out.println("Mail exchanging Successfully");
        rabbitTemplate.convertAndSend(exchange, routingKey, rabbitMqDto);
        receiveMessage(rabbitMqDto);
    }

    @Override
    public void sendMessage(RabbitMqDto email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getTo());
        message.setFrom(email.getFrom());
        message.setSubject(email.getSubject());
        message.setText(email.getBody());
        javaMailSender.send(message);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void receiveMessage(RabbitMqDto email) {
        System.out.println("Mail Received Successfully");
        sendMessage(email);
    }

}
