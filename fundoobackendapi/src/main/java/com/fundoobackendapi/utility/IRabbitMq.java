package com.fundoobackendapi.utility;
import com.fundoobackendapi.dto.RabbitMqDto;

public interface IRabbitMq {
    void sendMessageToQueue(RabbitMqDto rabbitMqDto);
    void receiveMessage(RabbitMqDto rabbitMqDto);
    void sendMessage(RabbitMqDto rabbitMqDto);
}
