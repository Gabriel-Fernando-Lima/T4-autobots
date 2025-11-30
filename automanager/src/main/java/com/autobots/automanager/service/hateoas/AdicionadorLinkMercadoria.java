package com.autobots.automanager.service.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.MercadoriaController;

import com.autobots.automanager.modelos.dto.MercadoriaDTO;

@Component
public class AdicionadorLinkMercadoria {

    public void adicionarLinks(MercadoriaDTO dto) {
        if (dto == null) return;

        Long id = dto.getId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(MercadoriaController.class).buscarMercadoria(id))
                .withSelfRel();
        dto.add(selfLink);

        Link editLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(MercadoriaController.class).atualizarMercadoria(id, null))
                .withRel("editar");
        dto.add(editLink);

        Link deleteLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(MercadoriaController.class).excluirMercadoria(id))
                .withRel("excluir");
        dto.add(deleteLink);

        Link allMercadoriasLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(MercadoriaController.class).listarMercadorias())
                .withRel("mercadorias");
        dto.add(allMercadoriasLink);
    }

    public void adicionarLinks(List<MercadoriaDTO> listaDto) {
        if (listaDto == null || listaDto.isEmpty()) return;

        listaDto.forEach(this::adicionarLinks);
    }
}