package com.autobots.automanager.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.modelos.entidades.Empresa;
import com.autobots.automanager.modelos.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.service.UsuarioService;

@Service
public class EmpresaService {

    @Autowired
    private RepositorioEmpresa repositorio;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public Empresa criar(Empresa empresa) {
        if (empresa.getCadastro() == null) {
            empresa.setCadastro(new Date());
        }
        return repositorio.save(empresa);
    }

    @Transactional(readOnly = true)
    public List<Empresa> listarTodas() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Empresa buscarPorId(Long id) {
        return repositorio.findById(id).orElse(null);
    }

    @Transactional
    public Empresa atualizar(Long id, Empresa empresaAtualizacao) {
        Empresa empresaExistente = buscarPorId(id);

        if (empresaExistente == null) {
            return null;
        }

        atualizarDados(empresaExistente, empresaAtualizacao);
        return repositorio.save(empresaExistente);
    }

    private void atualizarDados(Empresa existente, Empresa atualizacao) {

        if (atualizacao.getRazaoSocial() != null) {
            existente.setRazaoSocial(atualizacao.getRazaoSocial());
        }
        if (atualizacao.getNomeFantasia() != null) {
            existente.setNomeFantasia(atualizacao.getNomeFantasia());
        }
        if (atualizacao.getCadastro() != null) {
            existente.setCadastro(atualizacao.getCadastro());
        }

        if (atualizacao.getEndereco() != null) {
            if (existente.getEndereco() != null) {
                atualizacao.getEndereco().setId(existente.getEndereco().getId());
            }
            existente.setEndereco(atualizacao.getEndereco());
        } else {
            existente.setEndereco(null);
        }

        if (atualizacao.getTelefones() != null) {
            existente.getTelefones().clear();
            existente.getTelefones().addAll(atualizacao.getTelefones());
        }
    }

    @Transactional
    public void excluir(Long id) {
        Empresa empresa = buscarPorId(id);
        if (empresa != null) {
            repositorio.delete(empresa);
        }
    }


    @Transactional
    public boolean associar(Long idEmpresa, Long idUsuario) {
        Empresa empresa = buscarPorId(idEmpresa);
        Usuario usuario = usuarioService.buscarPorIdSemAuth(idUsuario);

        if (empresa != null && usuario != null) {
            empresa.getUsuarios().add(usuario);
            repositorio.save(empresa);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Set<Usuario> listarUsuarios(Long idEmpresa) {
        Empresa empresa = buscarPorId(idEmpresa);
        return empresa != null ? empresa.getUsuarios() : null;
    }


    @Transactional
    public boolean desassociar(Long idEmpresa, Long idUsuario) {
        Empresa empresa = buscarPorId(idEmpresa);

        if (empresa != null) {
            boolean removido = empresa.getUsuarios()
                    .removeIf(usuario -> usuario.getId().equals(idUsuario));

            if (removido) {
                repositorio.save(empresa);
                return true;
            }
        }
        return false;
    }
}
