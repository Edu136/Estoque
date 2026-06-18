package com.estoque.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "fornecedores")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do fornecedor é obrigatório")
    @Column(nullable = false)
    private String nome;

    private String contato;
    private String cnpj;

    public Fornecedor(String nome, String contato, String cnpj) {
        this.nome = nome;
        this.contato = contato;
        this.cnpj = cnpj;
    }
}
