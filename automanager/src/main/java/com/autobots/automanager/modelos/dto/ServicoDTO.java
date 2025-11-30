package com.autobots.automanager.modelos.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServicoDTO extends RepresentationModel<ServicoDTO> {
    private Long id;
    private String nome;
    private double valor;
    private String descricao;
}