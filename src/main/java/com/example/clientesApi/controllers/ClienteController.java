package com.example.clientesApi.controllers;

import com.example.clientesApi.dtos.ClienteRequestPostDto;
import com.example.clientesApi.dtos.ClienteResponseDto;
import com.example.clientesApi.dtos.EnderecoResponseDto;
import com.example.clientesApi.entities.Cliente;
import com.example.clientesApi.entities.Endereco;
import com.example.clientesApi.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController("api/clientes")
public class ClienteController {

    @Autowired
    ClienteRepository clienteRepository;

    @PostMapping("/criar")
    public ClienteResponseDto post(@RequestBody ClienteRequestPostDto clienteRequestPostDto) {

        Cliente cliente = new Cliente();
        cliente.setNome(clienteRequestPostDto.getNome());
        cliente.setEmail(clienteRequestPostDto.getEmail());
        cliente.setCpf(clienteRequestPostDto.getCpf());
        cliente.setDataNascimento(clienteRequestPostDto.getDataNascimento());

        List<Endereco> enderecos = clienteRequestPostDto.getEnderecos().stream().map(enderecoRequestDTO -> {
            Endereco endereco = new Endereco();
            endereco.setLogradouro(enderecoRequestDTO.getLogradouro());
            endereco.setComplemento(enderecoRequestDTO.getComplemento());
            endereco.setNumero(enderecoRequestDTO.getNumero());
            endereco.setBairro(enderecoRequestDTO.getBairro());
            endereco.setCidade(enderecoRequestDTO.getCidade());
            endereco.setUf(enderecoRequestDTO.getUf());
            endereco.setCep(enderecoRequestDTO.getCep());

            endereco.setCliente(cliente); // relacionamento
            return endereco;
        }).collect(Collectors.toList());

        cliente.setEnderecos(enderecos);

        Cliente clienteSalvo = clienteRepository.save(cliente);

        // Criar o objeto de resposta
        ClienteResponseDto clienteResponseDto = new ClienteResponseDto();

        clienteResponseDto.setNome(cliente.getNome());
        clienteResponseDto.setEmail(cliente.getEmail());
        clienteResponseDto.setCpf(cliente.getCpf());
        clienteResponseDto.setDataNascimento(cliente.getDataNascimento());

        List<EnderecoResponseDto> enderecosReponse = clienteSalvo.getEnderecos().stream().map(endereco -> {
            EnderecoResponseDto enderecoResponse = new EnderecoResponseDto();
            enderecoResponse.setLogradouro(endereco.getLogradouro());
            enderecoResponse.setComplemento(endereco.getComplemento());
            enderecoResponse.setNumero(endereco.getNumero());
            enderecoResponse.setBairro(endereco.getBairro());
            enderecoResponse.setCidade(endereco.getCidade());
            enderecoResponse.setUf(endereco.getUf());
            enderecoResponse.setCep(endereco.getCep());

            return enderecoResponse;
        }).collect(Collectors.toList());

        clienteResponseDto.setEnderecos(enderecosReponse);

        return clienteResponseDto;
    }


}
