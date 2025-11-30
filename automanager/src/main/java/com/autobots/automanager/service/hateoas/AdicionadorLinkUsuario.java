package com.autobots.automanager.service.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioController;

import com.autobots.automanager.modelos.dto.UsuarioDTO;

@Component
public class AdicionadorLinkUsuario {

    public void adicionarLinks(UsuarioDTO dto) {
        if (dto == null) return;

        Long id = dto.getId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioController.class).buscarUsuario(id))
                .withSelfRel();
        dto.add(selfLink);

        Link editLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioController.class).atualizarUsuario(id, null))
                .withRel("editar");
        dto.add(editLink);

        Link deleteLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioController.class).excluirUsuario(id))
                .withRel("excluir");
        dto.add(deleteLink);

        Link allUsersLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioController.class).listarUsuarios())
                .withRel("usuarios");
        dto.add(allUsersLink);

    }

    public void adicionarLinks(List<UsuarioDTO> listaDto) {
        if (listaDto == null || listaDto.isEmpty()) return;

        listaDto.forEach(this::adicionarLinks);
    }
}