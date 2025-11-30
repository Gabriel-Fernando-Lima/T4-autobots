package com.autobots.automanager.modelos.dto;

import java.util.Date;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.autobots.automanager.modelos.entidades.Endereco;
import com.autobots.automanager.modelos.entidades.Telefone;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmpresaDTO extends RepresentationModel<EmpresaDTO> {
    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private Date cadastro;

    private Set<Telefone> telefones;
    private Endereco endereco;

}