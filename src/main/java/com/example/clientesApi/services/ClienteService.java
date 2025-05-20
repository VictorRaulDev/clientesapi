package com.example.clientesApi.services;


import com.example.clientesApi.components.RabbitMQProducerComponent;
import com.example.clientesApi.dtos.ClienteRequestDto;
import com.example.clientesApi.dtos.ClienteRequestPostDto;
import com.example.clientesApi.dtos.ClienteResponseDto;
import com.example.clientesApi.dtos.EnderecoResponseDto;
import com.example.clientesApi.entities.Cliente;
import com.example.clientesApi.entities.Endereco;
import com.example.clientesApi.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    private RabbitMQProducerComponent rabbitMQProducerComponent;


    public ClienteResponseDto cadastrar(ClienteRequestPostDto clienteRequestPostDto) {

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

        clienteResponseDto.setId(clienteSalvo.getId());
        clienteResponseDto.setNome(cliente.getNome());
        clienteResponseDto.setEmail(cliente.getEmail());
        clienteResponseDto.setCpf(cliente.getCpf());
        clienteResponseDto.setDataNascimento(cliente.getDataNascimento());

        List<EnderecoResponseDto> enderecosReponse = clienteSalvo.getEnderecos().stream().map(endereco -> {
            EnderecoResponseDto enderecoResponse = new EnderecoResponseDto();

            enderecoResponse.setId(endereco.getId());
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

        //enviando os dados da conta para o RabbitMQ
        rabbitMQProducerComponent.sendMessage(clienteResponseDto);

        return clienteResponseDto;
    }
    public ClienteResponseDto atualizar(ClienteRequestDto clienteRequestDto) {

        //verificar se o cliente ja existe no banco de dados

        Cliente cliente = new Cliente();

        cliente.setId(clienteRequestDto.getId());
        cliente.setNome(clienteRequestDto.getNome());
        cliente.setEmail(clienteRequestDto.getEmail());
        cliente.setCpf(clienteRequestDto.getCpf());
        cliente.setDataNascimento(clienteRequestDto.getDataNascimento());

        List<Endereco> enderecos = clienteRequestDto.getEnderecos().stream().map(enderecoRequestDTO -> {

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

        Cliente clienteAtualizado = clienteRepository.save(cliente);

        // Criar o objeto de resposta
        ClienteResponseDto clienteResponseDto = new ClienteResponseDto();

        clienteResponseDto.setId(clienteAtualizado.getId());
        clienteResponseDto.setNome(cliente.getNome());
        clienteResponseDto.setEmail(cliente.getEmail());
        clienteResponseDto.setCpf(cliente.getCpf());
        clienteResponseDto.setDataNascimento(cliente.getDataNascimento());

        List<EnderecoResponseDto> enderecosReponse = clienteAtualizado.getEnderecos().stream().map(endereco -> {
            EnderecoResponseDto enderecoResponse = new EnderecoResponseDto();

            enderecoResponse.setId(endereco.getId());
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
    public List<ClienteResponseDto> listar() {

        List<Cliente> clientes = clienteRepository.listarOrderByNome();

        List<ClienteResponseDto> clientesResponse = clientes.stream().map(cliente ->{

            ClienteResponseDto clienteResponseDto = new ClienteResponseDto();
            clienteResponseDto.setId(cliente.getId());
            clienteResponseDto.setNome(cliente.getNome());
            clienteResponseDto.setEmail(cliente.getEmail());
            clienteResponseDto.setCpf(cliente.getCpf());
            clienteResponseDto.setDataNascimento(cliente.getDataNascimento());

            List<EnderecoResponseDto> enderecosReponse = cliente.getEnderecos().stream().map(endereco -> {
                EnderecoResponseDto enderecoResponse = new EnderecoResponseDto();

                enderecoResponse.setId(endereco.getId());
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
        }).collect(Collectors.toList());

        return clientesResponse;
    }
    public ClienteResponseDto listarPorID(UUID id) {

            Cliente cliente = clienteRepository.findById(id).orElse(null);

            ClienteResponseDto clienteResponseDto = new ClienteResponseDto();
            clienteResponseDto.setId(cliente.getId());
            clienteResponseDto.setNome(cliente.getNome());
            clienteResponseDto.setEmail(cliente.getEmail());
            clienteResponseDto.setCpf(cliente.getCpf());
            clienteResponseDto.setDataNascimento(cliente.getDataNascimento());
            List<EnderecoResponseDto> enderecosReponse = cliente.getEnderecos().stream().map(endereco -> {
                EnderecoResponseDto enderecoResponse = new EnderecoResponseDto();

                enderecoResponse.setId(endereco.getId());
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
    public ResponseEntity<?> deletar(UUID id) {
        try {
            clienteRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente deletado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }
    }

}
