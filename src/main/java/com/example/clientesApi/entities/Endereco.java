package com.example.clientesApi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "endereco")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Endereco {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    private UUID id;

    @Column(name = "logradouro", length = 100, nullable = false)
    private String logradouro;

    @Column(name = "complemento", length = 50)
    private String complemento;
    @Column(name = "numero", length = 10, nullable = false)
    private String numero;
    @Column(name = "bairro", length = 50, nullable = false)
    private String bairro;
    @Column(name = "cidade", length = 50, nullable = false)
    private String cidade;
    @Column(name = "uf", length = 2, nullable = false)
    private String uf;
    @Column(name = "cep", length = 10, nullable = false)
    private String cep;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}
