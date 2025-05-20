package com.example.clientesApi.dtos;



import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class ClienteResponseDto {

    private UUID id;
    private String nome;
    private String email;
    private String cpf;
    private Date dataNascimento;
    private List<EnderecoResponseDto> enderecos;

}
