package com.autobots.automanager.modelos.dto;

import org.springframework.hateoas.RepresentationModel;

import com.autobots.automanager.enumeracoes.TipoVeiculo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VeiculoDTO extends RepresentationModel<VeiculoDTO> {
    private Long id;
    private TipoVeiculo tipo;
    private String modelo;
    private String placa;
    private Long proprietarioId;
}