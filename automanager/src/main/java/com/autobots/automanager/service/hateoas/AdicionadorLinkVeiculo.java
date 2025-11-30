package com.autobots.automanager.service.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioController;
import com.autobots.automanager.controles.VeiculoController;
import com.autobots.automanager.modelos.dto.VeiculoDTO;

@Component
public class AdicionadorLinkVeiculo {

    public void adicionarLinks(VeiculoDTO dto) {
        if (dto == null) return;

        Long id = dto.getId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VeiculoController.class).buscarVeiculo(id))
                .withSelfRel();
        dto.add(selfLink);

        Link editLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VeiculoController.class).atualizarVeiculo(id, null))
                .withRel("editar");
        dto.add(editLink);

        Link deleteLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VeiculoController.class).excluirVeiculo(id))
                .withRel("excluir");
        dto.add(deleteLink);

        Link allVeiculosLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VeiculoController.class).listarVeiculos())
                .withRel("veiculos");
        dto.add(allVeiculosLink);

        if (dto.getProprietarioId() != null) {
            Link proprietarioLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(UsuarioController.class).buscarUsuario(dto.getProprietarioId()))
                    .withRel("proprietario");
            dto.add(proprietarioLink);
        }

    }

    public void adicionarLinks(List<VeiculoDTO> listaDto) {
        if (listaDto == null || listaDto.isEmpty()) return;

        listaDto.forEach(this::adicionarLinks);
    }
}