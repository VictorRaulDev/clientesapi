package com.example.clientesApi.dtos;



import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ClienteResponseDto {

    private String nome;
    private String email;
    private String cpf;
    private Date dataNascimento;
    private List<EnderecoResponseDto> enderecos;

}
