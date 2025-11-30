package com.autobots.automanager.modelos.entidades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.autobots.automanager.enumeracoes.Perfil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {
        "telefones",
        "endereco",
        "documentos",
        "emails",
        "credencial",
        "veiculos",
        "vendas",
        "mercadorias"
})
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String nomeSocial;

    @Column(nullable = false)
    private Date cadastro = new Date();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Perfil> perfis = new HashSet<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Telefone> telefones = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Documento> documentos = new HashSet<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Email> emails = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Credencial credencial;

    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Veiculo> veiculos = new HashSet<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Venda> vendas = new HashSet<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Mercadoria> mercadorias = new HashSet<>();
}