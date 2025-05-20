package com.example.clientesApi.components;

import com.example.clientesApi.dtos.ClienteResponseDto;
import com.example.clientesApi.dtos.EnderecoResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class RabbitMQConsumerComponent {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JavaMailSender mailSender;


    /*
     * Método para ler e capturar os registros de contas
     * armazenados na fila do RabbitMQ
     */
    @RabbitListener(queues = "apiclientes")
    public void consume(@Payload String message) {

        try {

            //deserializando os dados de JSON para objeto (classe)
            var clienteResponseDto = objectMapper.readValue(message, ClienteResponseDto.class);

            //enviando o email
            sendEmail(clienteResponseDto);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(ClienteResponseDto clienteResponseDto) {



        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(clienteResponseDto.getEmail());
            helper.setSubject("Cadastro realizado com sucesso!");

            StringBuilder html = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            html.append("<html><body>");
            html.append("<div style='font-family:Arial,sans-serif;padding:20px;'>");
            html.append("<h2 style='color:#2c3e50;'>Olá, ").append(clienteResponseDto.getNome()).append("!</h2>");
            html.append("<p>Seu cadastro foi realizado com sucesso. Aqui estão seus dados:</p>");

            html.append("<div style='margin-top:10px;'>");
            html.append("<p><strong>CPF:</strong> ").append(clienteResponseDto.getCpf()).append("</p>");
            html.append("<p><strong>Data de Nascimento:</strong> ").append(sdf.format(clienteResponseDto.getDataNascimento())).append("</p>");
            html.append("</div>");

            html.append("<h3 style='margin-top:20px;color:#34495e;'>Endereços cadastrados:</h3>");
            html.append("<ul style='padding-left:20px;'>");

            for (EnderecoResponseDto e : clienteResponseDto.getEnderecos()) {
                html.append("<li style='margin-bottom:10px;'>")
                        .append("<strong>Logradouro:</strong> ").append(e.getLogradouro()).append("<br>")
                        .append("<strong>Número:</strong> ").append(e.getNumero()).append("<br>")
                        .append("<strong>Bairro:</strong> ").append(e.getBairro()).append("<br>")
                        .append("<strong>Cidade:</strong> ").append(e.getCidade()).append("<br>")
                        .append("<strong>UF:</strong> ").append(e.getUf()).append("<br>")
                        .append("<strong>CEP:</strong> ").append(e.getCep()).append("<br>")
                        .append("</li>");
            }

            html.append("</ul>");
            html.append("<p style='color:#888;'>Este é um e-mail automático. Não responda.</p>");
            html.append("</div></body></html>");

            helper.setText(html.toString(), true); // true para HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            // log de erro ou rethrow de exceção customizada
        }
    }



}
