package com.autobots.automanager.modelos.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.autobots.automanager.modelos.dto.EmpresaDTO;
import com.autobots.automanager.modelos.entidades.Empresa;

@Component
public class EmpresaConverter {

    public Empresa dtoParaEntidade(EmpresaDTO dto) {
        Empresa entidade = new Empresa();

        entidade.setId(dto.getId());
        entidade.setRazaoSocial(dto.getRazaoSocial());
        entidade.setNomeFantasia(dto.getNomeFantasia());
        entidade.setCadastro(dto.getCadastro());
        entidade.setTelefones(dto.getTelefones());
        entidade.setEndereco(dto.getEndereco());

        return entidade;
    }

    public EmpresaDTO entidadeParaDto(Empresa entidade) {
        EmpresaDTO dto = new EmpresaDTO();

        dto.setId(entidade.getId());
        dto.setRazaoSocial(entidade.getRazaoSocial());
        dto.setNomeFantasia(entidade.getNomeFantasia());
        dto.setCadastro(entidade.getCadastro());
        dto.setTelefones(entidade.getTelefones());
        dto.setEndereco(entidade.getEndereco());

        return dto;
    }

    public List<EmpresaDTO> entidadeParaDto(List<Empresa> entidades) {
        return entidades.stream()
                .map(this::entidadeParaDto)
                .collect(Collectors.toList());
    }
}