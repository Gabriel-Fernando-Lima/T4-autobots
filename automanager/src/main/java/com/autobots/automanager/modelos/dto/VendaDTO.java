package com.autobots.automanager.modelos.dto;

import java.util.Date;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VendaDTO extends RepresentationModel<VendaDTO> {
    private Long id;
    private Date cadastro;
    private String identificacao;

    private Long clienteId;
    private Long funcionarioId;
    private Long veiculoId;
    private Set<Long> mercadoriasIds;
    private Set<Long> servicosIds;
}