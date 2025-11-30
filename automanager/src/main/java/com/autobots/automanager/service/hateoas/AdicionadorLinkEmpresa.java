package com.autobots.automanager.service.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaController;

import com.autobots.automanager.modelos.dto.EmpresaDTO;

@Component
public class AdicionadorLinkEmpresa {

    public void adicionarLinks(EmpresaDTO dto) {
        if (dto == null) return;

        Long id = dto.getId();


        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaController.class).buscarEmpresa(id))
                .withSelfRel();
        dto.add(selfLink);

        Link editLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaController.class).atualizarEmpresa(id, null))
                .withRel("editar");
        dto.add(editLink);

        Link deleteLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaController.class).excluirEmpresa(id))
                .withRel("excluir");
        dto.add(deleteLink);

        Link allEmpresasLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaController.class).listarEmpresas())
                .withRel("empresas");
        dto.add(allEmpresasLink);

        dto.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaController.class).buscarUsuariosDaEmpresa(id))
                .withRel("usuarios"));

    }

    public void adicionarLinks(List<EmpresaDTO> listaDto) {
        if (listaDto == null || listaDto.isEmpty()) return;

        listaDto.forEach(this::adicionarLinks);
    }
}