package com.example.clientesApi.dtos;


import lombok.Data;


@Data
public class EnderecoRequestDTO {

    private String logradouro;
    private String complemento;
    private String numero;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;


}
