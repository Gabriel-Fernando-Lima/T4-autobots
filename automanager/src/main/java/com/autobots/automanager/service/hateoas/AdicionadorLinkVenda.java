package com.autobots.automanager.service.hateoas;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoController;
import com.autobots.automanager.controles.MercadoriaController;
import com.autobots.automanager.controles.UsuarioController;
import com.autobots.automanager.controles.VeiculoController;
import com.autobots.automanager.controles.VendaController;

import com.autobots.automanager.modelos.dto.VendaDTO;

@Component
public class AdicionadorLinkVenda {

    public void adicionarLinks(VendaDTO dto) {
        if (dto == null) return;

        Long id = dto.getId();


        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaController.class).buscarVenda(id))
                .withSelfRel();
        dto.add(selfLink);


        Link editLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaController.class).atualizarVenda(id, null))
                .withRel("editar");
        dto.add(editLink);


        Link deleteLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaController.class).excluirVenda(id))
                .withRel("excluir");
        dto.add(deleteLink);


        Link allVendasLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaController.class).listarVendas())
                .withRel("vendas");
        dto.add(allVendasLink);



        if (dto.getClienteId() != null) {
            Link clienteLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(UsuarioController.class).buscarUsuario(dto.getClienteId()))
                    .withRel("cliente");
            dto.add(clienteLink);
        }

        if (dto.getFuncionarioId() != null) {
            Link funcionarioLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(UsuarioController.class).buscarUsuario(dto.getFuncionarioId()))
                    .withRel("funcionario");
            dto.add(funcionarioLink);
        }

        if (dto.getVeiculoId() != null) {
            Link veiculoLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(VeiculoController.class).buscarVeiculo(dto.getVeiculoId()))
                    .withRel("veiculo");
            dto.add(veiculoLink);
        }


        dto.add(WebMvcLinkBuilder.linkTo(MercadoriaController.class).withRel("mercadorias"));
        dto.add(WebMvcLinkBuilder.linkTo(ServicoController.class).withRel("servicos"));
    }

    public void adicionarLinks(List<VendaDTO> listaDto) {
        if (listaDto == null || listaDto.isEmpty()) return;

        listaDto.forEach(this::adicionarLinks);
    }
}