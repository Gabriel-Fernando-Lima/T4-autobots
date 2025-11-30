package com.autobots.automanager.modelos.dto;

import java.util.Date;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.autobots.automanager.modelos.entidades.Credencial;
import com.autobots.automanager.modelos.entidades.Documento;
import com.autobots.automanager.modelos.entidades.Email;
import com.autobots.automanager.modelos.entidades.Endereco;
import com.autobots.automanager.modelos.entidades.Telefone;
import com.autobots.automanager.enumeracoes.Perfil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
    private Long id;
    private String nome;
    private String nomeSocial;
    private Date cadastro;
    private Set<Perfil> perfis;
    private Set<Telefone> telefones;
    private Endereco endereco;
    private Set<Documento> documentos;
    private Set<Email> emails;

    private Credencial credencial;
}