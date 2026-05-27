package com.estoque.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

// SRP: Fornecedor representa apenas os dados de um fornecedor
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

    public Fornecedor() {}

    public Fornecedor(String nome, String contato, String cnpj) {
        this.nome = nome;
        this.contato = contato;
        this.cnpj = cnpj;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
}
