package com.example.clientesApi.components;

import com.example.clientesApi.dtos.ClienteResponseDto;
import com.example.clientesApi.entities.Cliente;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Queue;



@Component
public class RabbitMQProducerComponent {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    Queue queue;

    /*
     * Método para gravar os dados da conta
     * na fila do servidor de mensageria
     */
    public void sendMessage(ClienteResponseDto cliente) {
        try {

            //serializar os dados da conta para formato JSON
            String json = objectMapper
                    .writeValueAsString(cliente);

            //enviando a mensagem para a fila
            rabbitTemplate.convertAndSend
                    (queue.getName(), json);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
