package com.autobots.automanager.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.enumeracoes.Perfil;
import com.autobots.automanager.modelos.entidades.Documento;
import com.autobots.automanager.modelos.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UsuarioService {

    @Autowired
    private RepositorioUsuario repositorio;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isGerente = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_GERENTE"));

        if (isGerente) {
            return repositorio.findAll().stream()
                    .filter(usuario -> usuario.getPerfis().contains(Perfil.ROLE_CLIENTE))
                    .collect(Collectors.toList());
        }

        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameAutenticado = authentication.getName();

        Usuario usuarioAlvo = repositorio.findById(id).orElse(null);
        if (usuarioAlvo == null) {
            return null;
        }

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
            return usuarioAlvo;
        }

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_GERENTE"))) {
            boolean isAlvoCliente = usuarioAlvo.getPerfis().contains(Perfil.ROLE_CLIENTE);
            if (isAlvoCliente) {
                return usuarioAlvo;
            }
        }

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_CLIENTE"))) {
            Usuario usuarioAutenticado = repositorio.findByCredencialNomeUsuario(usernameAutenticado);

            if (usuarioAutenticado != null && usuarioAlvo.getId().equals(usuarioAutenticado.getId())) {
                return usuarioAlvo;
            }
        }

        throw new AccessDeniedException("Acesso negado: Você não tem permissão para acessar este usuário.");
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorIdSemAuth(Long id) {
        Optional<Usuario> usuario = repositorio.findById(id);
        return usuario.orElse(null);
    }

    @Transactional
    public Usuario criar(Usuario usuario) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isGerente = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_GERENTE"));

        if (isGerente) {
            boolean isApenasCliente = usuario.getPerfis() != null &&
                    usuario.getPerfis().size() == 1 &&
                    usuario.getPerfis().contains(Perfil.ROLE_CLIENTE);

            if (!isApenasCliente) {
                throw new AccessDeniedException(
                        "Acesso negado: Gerentes só podem criar usuários com o perfil ROLE_CLIENTE.");
            }

            if (usuario.getPerfis() == null || usuario.getPerfis().isEmpty()) {
                usuario.setPerfis(Set.of(Perfil.ROLE_CLIENTE));
            }

        }

        if (usuario.getCredencial() != null && usuario.getCredencial().getSenha() != null) {
            String senhaCodificada = passwordEncoder.encode(usuario.getCredencial().getSenha());
            usuario.getCredencial().setSenha(senhaCodificada);
        }

        if (usuario.getCadastro() == null) {
            usuario.setCadastro(new Date());
        }

        return repositorio.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario usuarioAtualizacao) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isGerente = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_GERENTE"));

        Usuario usuarioExistente = buscarPorIdSemAuth(id);
        if (usuarioExistente == null) {
            return null;
        }

        if (isGerente) {
            boolean isAlvoCliente = usuarioExistente.getPerfis().contains(Perfil.ROLE_CLIENTE);
            if (!isAlvoCliente) {
                throw new AccessDeniedException(
                        "Acesso negado: Gerentes só podem editar usuários com o perfil ROLE_CLIENTE.");
            }
        }

        usuarioAtualizacao.setId(id);
        atualizarDados(usuarioExistente, usuarioAtualizacao);

        return repositorio.save(usuarioExistente);
    }

    private void atualizarDados(Usuario existente, Usuario atualizacao) {

        if (atualizacao.getNome() != null) {
            existente.setNome(atualizacao.getNome());
        }
        if (atualizacao.getNomeSocial() != null) {
            existente.setNomeSocial(atualizacao.getNomeSocial());
        }
        if (atualizacao.getCadastro() != null) {

            existente.setCadastro(atualizacao.getCadastro());
        }
        if (atualizacao.getPerfis() != null && !atualizacao.getPerfis().isEmpty()) {
            existente.setPerfis(atualizacao.getPerfis());
        }

        if (atualizacao.getEndereco() != null) {
            if (existente.getEndereco() != null) {
                atualizacao.getEndereco().setId(existente.getEndereco().getId());
            }
            existente.setEndereco(atualizacao.getEndereco());
        } else {
            existente.setEndereco(null);
        }

        if (atualizacao.getCredencial() != null && atualizacao.getCredencial().getSenha() != null
                && !atualizacao.getCredencial().getSenha().isEmpty()) {

            String senhaCodificada = passwordEncoder.encode(atualizacao.getCredencial().getSenha());
            existente.getCredencial().setSenha(senhaCodificada);
        }

        if (atualizacao.getTelefones() != null) {
            existente.getTelefones().clear();
            existente.getTelefones().addAll(atualizacao.getTelefones());
        }

        if (atualizacao.getEmails() != null) {
            existente.getEmails().clear();
            existente.getEmails().addAll(atualizacao.getEmails());
        }

        if (atualizacao.getDocumentos() != null) {
            Set<String> numerosDocAtualizacao = atualizacao.getDocumentos().stream()
                    .map(Documento::getNumero)
                    .collect(Collectors.toSet());

            existente.getDocumentos().removeIf(
                    docExistente -> !numerosDocAtualizacao.contains(docExistente.getNumero()));

            for (Documento docAtualizacao : atualizacao.getDocumentos()) {
                boolean jaExiste = existente.getDocumentos().stream()
                        .anyMatch(docExistente -> docExistente.getNumero().equals(docAtualizacao.getNumero()));

                if (!jaExiste) {
                    existente.getDocumentos().add(docAtualizacao);
                } else {
                    existente.getDocumentos().stream()
                            .filter(docExistente -> docExistente.getNumero().equals(docAtualizacao.getNumero()))
                            .findFirst()
                            .ifPresent(docParaAtualizar -> {
                                docParaAtualizar.setTipo(docAtualizacao.getTipo());
                                docParaAtualizar.setDataEmissao(docAtualizacao.getDataEmissao());
                            });
                }
            }
        }
    }

    @Transactional
    public void excluir(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isGerente = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_GERENTE"));

        Usuario usuario = buscarPorIdSemAuth(id);
        if (usuario == null) {
            return;
        }

        if (isGerente) {
            boolean isAlvoCliente = usuario.getPerfis().contains(Perfil.ROLE_CLIENTE);
            if (!isAlvoCliente) {
                throw new AccessDeniedException(
                        "Acesso negado: Gerentes só podem excluir usuários com o perfil ROLE_CLIENTE.");
            }
        }

        repositorio.delete(usuario);
    }
}