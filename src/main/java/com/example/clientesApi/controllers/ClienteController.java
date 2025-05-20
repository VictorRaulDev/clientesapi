package com.example.clientesApi.controllers;

import com.example.clientesApi.dtos.ClienteRequestDto;
import com.example.clientesApi.dtos.ClienteRequestPostDto;
import com.example.clientesApi.dtos.ClienteResponseDto;
import com.example.clientesApi.dtos.EnderecoResponseDto;
import com.example.clientesApi.entities.Cliente;
import com.example.clientesApi.entities.Endereco;
import com.example.clientesApi.repositories.ClienteRepository;
import com.example.clientesApi.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RestController("api/clientes")
public class ClienteController {

    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ClienteService clienteService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody ClienteRequestPostDto clienteRequestPostDto) {

        try {
            var clienteResponseDto = clienteService.cadastrar(clienteRequestPostDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteResponseDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizar(@RequestBody ClienteRequestDto clienteRequestDto) {

        try {
            var clienteResponseDto = clienteService.atualizar(clienteRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(clienteResponseDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar(){

        try {
            var clientesResponse = clienteService.listar();
            return ResponseEntity.status(HttpStatus.OK).body(clientesResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        }

    @GetMapping("/Listar/{id}")
    private ResponseEntity<?> listarPorID(@PathVariable UUID id){

        ClienteResponseDto clienteResponseDto = clienteService.listarPorID(id);

        if(clienteResponseDto == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }else
            return ResponseEntity.status(HttpStatus.OK).body(clienteResponseDto);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(@PathVariable UUID id) {
        try {
            return clienteService.deletar(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
