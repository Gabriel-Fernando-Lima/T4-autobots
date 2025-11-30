package com.autobots.automanager.modelos.dto;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MercadoriaDTO extends RepresentationModel<MercadoriaDTO> {
    private Long id;
    private Date validade;
    private Date fabricacao;
    private Date cadastro;
    private String nome;
    private long quantidade;
    private double valor;
    private String descricao;
}