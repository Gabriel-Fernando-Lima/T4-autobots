package com.autobots.automanager.modelos.converter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.modelos.dto.VendaDTO;
import com.autobots.automanager.modelos.entidades.Mercadoria;
import com.autobots.automanager.modelos.entidades.Servico;
import com.autobots.automanager.modelos.entidades.Usuario;
import com.autobots.automanager.modelos.entidades.Veiculo;
import com.autobots.automanager.modelos.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

@Component
public class VendaConverter {

    @Autowired
    private RepositorioUsuario repositorioUsuario;
    @Autowired
    private RepositorioVeiculo repositorioVeiculo;
    @Autowired
    private RepositorioMercadoria repositorioMercadoria;
    @Autowired
    private RepositorioServico repositorioServico;

    public Venda dtoParaEntidade(VendaDTO dto) {
        Venda entidade = new Venda();

        entidade.setId(dto.getId());
        entidade.setCadastro(dto.getCadastro());
        entidade.setIdentificacao(dto.getIdentificacao());

        if (dto.getClienteId() != null) {
            Usuario cliente = repositorioUsuario.findById(dto.getClienteId()).orElse(null);
            entidade.setCliente(cliente);
        }

        if (dto.getFuncionarioId() != null) {
            Usuario funcionario = repositorioUsuario.findById(dto.getFuncionarioId()).orElse(null);
            entidade.setFuncionario(funcionario);
        }

        if (dto.getVeiculoId() != null) {
            Veiculo veiculo = repositorioVeiculo.findById(dto.getVeiculoId()).orElse(null);
            entidade.setVeiculo(veiculo);
        }

        if (dto.getMercadoriasIds() != null) {
            Set<Mercadoria> mercadorias = new HashSet<>(repositorioMercadoria.findAllById(dto.getMercadoriasIds()));
            entidade.setMercadorias(mercadorias);
        }

        if (dto.getServicosIds() != null) {
            Set<Servico> servicos = new HashSet<>(repositorioServico.findAllById(dto.getServicosIds()));
            entidade.setServicos(servicos);
        }

        return entidade;
    }

    public VendaDTO entidadeParaDto(Venda entidade) {
        VendaDTO dto = new VendaDTO();

        dto.setId(entidade.getId());
        dto.setCadastro(entidade.getCadastro());
        dto.setIdentificacao(entidade.getIdentificacao());

    
        if (entidade.getCliente() != null) {
            dto.setClienteId(entidade.getCliente().getId());
        }

        if (entidade.getFuncionario() != null) {
            dto.setFuncionarioId(entidade.getFuncionario().getId());
        }

        if (entidade.getVeiculo() != null) {
            dto.setVeiculoId(entidade.getVeiculo().getId());
        }

        dto.setMercadoriasIds(
                entidade.getMercadorias().stream()
                        .map(Mercadoria::getId)
                        .collect(Collectors.toSet())
        );

        dto.setServicosIds(
                entidade.getServicos().stream()
                        .map(Servico::getId)
                        .collect(Collectors.toSet())
        );

        return dto;
    }

    public List<VendaDTO> entidadeParaDto(List<Venda> entidades) {
        return entidades.stream()
                .map(this::entidadeParaDto)
                .collect(Collectors.toList());
    }
}