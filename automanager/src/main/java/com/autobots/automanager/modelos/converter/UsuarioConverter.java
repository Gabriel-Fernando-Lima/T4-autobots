package com.autobots.automanager.modelos.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.autobots.automanager.modelos.entidades.Usuario;
import org.springframework.stereotype.Component;

import com.autobots.automanager.modelos.dto.UsuarioDTO;

@Component
public class UsuarioConverter {

    public Usuario dtoParaEntidade(UsuarioDTO dto) {
        Usuario entidade = new Usuario();

        entidade.setId(dto.getId());
        entidade.setNome(dto.getNome());
        entidade.setNomeSocial(dto.getNomeSocial());
        entidade.setCadastro(dto.getCadastro());
        entidade.setPerfis(dto.getPerfis());
        entidade.setTelefones(dto.getTelefones());
        entidade.setEndereco(dto.getEndereco());
        entidade.setDocumentos(dto.getDocumentos());
        entidade.setEmails(dto.getEmails());

        entidade.setCredencial(dto.getCredencial());

        return entidade;
    }

    public UsuarioDTO entidadeParaDto(Usuario entidade) {
        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(entidade.getId());
        dto.setNome(entidade.getNome());
        dto.setNomeSocial(entidade.getNomeSocial());
        dto.setCadastro(entidade.getCadastro());
        dto.setPerfis(entidade.getPerfis());
        dto.setTelefones(entidade.getTelefones());
        dto.setEndereco(entidade.getEndereco());
        dto.setDocumentos(entidade.getDocumentos());
        dto.setEmails(entidade.getEmails());


        return dto;
    }

    public List<UsuarioDTO> entidadeParaDto(List<Usuario> entidades) {
        return entidades.stream()
                .map(this::entidadeParaDto)
                .collect(Collectors.toList());
    }
}