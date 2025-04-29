package com.example.clientesApi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ClienteRequestPostDto {

    @Size(min = 8, max = 100, message = "Por favor, informe um nome de 8 a 100 caracteres.")
    @NotEmpty(message = "Por favor, informe o nome do cliente.")
    private String nome;

    @Email(message = "Email inválido")
    private String email;

    @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 dígitos.")
    private String cpf;

    @NotNull(message = "A data de nascimento é obrigatória")
    private Date dataNascimento;

    private List<EnderecoRequestDTO> enderecos;
    }
