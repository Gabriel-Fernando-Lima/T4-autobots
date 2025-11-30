package com.autobots.automanager.service.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoController;

import com.autobots.automanager.modelos.dto.ServicoDTO;

@Component
public class AdicionadorLinkServico {
    
    public void adicionarLinks(ServicoDTO dto) {
        if (dto == null) return;

        Long id = dto.getId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoController.class).buscarServico(id))
                .withSelfRel();
        dto.add(selfLink);

        Link editLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoController.class).atualizarServico(id, null))
                .withRel("editar");
        dto.add(editLink);

        Link deleteLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoController.class).excluirServico(id))
                .withRel("excluir");
        dto.add(deleteLink);

        Link allServicosLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoController.class).listarServicos())
                .withRel("servicos");
        dto.add(allServicosLink);
    }


    public void adicionarLinks(List<ServicoDTO> listaDto) {
        if (listaDto == null || listaDto.isEmpty()) return;

        listaDto.forEach(this::adicionarLinks);
    }
}