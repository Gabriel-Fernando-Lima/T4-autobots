package com.autobots.automanager.modelos.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.modelos.dto.VeiculoDTO;
import com.autobots.automanager.modelos.entidades.Usuario;
import com.autobots.automanager.modelos.entidades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Component
public class VeiculoConverter {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public Veiculo dtoParaEntidade(VeiculoDTO dto) {
        Veiculo entidade = new Veiculo();

        entidade.setId(dto.getId());
        entidade.setTipo(dto.getTipo());
        entidade.setModelo(dto.getModelo());
        entidade.setPlaca(dto.getPlaca());

        if (dto.getProprietarioId() != null) {
            Usuario proprietario = repositorioUsuario.findById(dto.getProprietarioId()).orElse(null);
            entidade.setProprietario(proprietario);
        }

        return entidade;
    }

    public VeiculoDTO entidadeParaDto(Veiculo entidade) {
        VeiculoDTO dto = new VeiculoDTO();

        dto.setId(entidade.getId());
        dto.setTipo(entidade.getTipo());
        dto.setModelo(entidade.getModelo());
        dto.setPlaca(entidade.getPlaca());

        if (entidade.getProprietario() != null) {
            dto.setProprietarioId(entidade.getProprietario().getId());
        }

        return dto;
    }

    public List<VeiculoDTO> entidadeParaDto(List<Veiculo> entidades) {
        return entidades.stream()
                .map(this::entidadeParaDto)
                .collect(Collectors.toList());
    }
}