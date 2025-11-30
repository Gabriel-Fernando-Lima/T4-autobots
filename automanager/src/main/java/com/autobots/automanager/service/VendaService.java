package com.autobots.automanager.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.modelos.entidades.Usuario;
import com.autobots.automanager.modelos.entidades.Veiculo;
import com.autobots.automanager.modelos.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVenda;

@Service
public class VendaService {

    @Autowired
    private RepositorioVenda repositorio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Transactional(readOnly = true)
    public List<Venda> listarTodos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = hasRole(auth, "ROLE_ADMIN");
        boolean isGerente = hasRole(auth, "ROLE_GERENTE");
        boolean isVendedor = hasRole(auth, "ROLE_VENDEDOR");
        boolean isCliente = hasRole(auth, "ROLE_CLIENTE");

        if (isAdmin || isGerente) {
            return repositorio.findAll();
        }

        String username = auth.getName();
        Usuario usuarioLogado = repositorioUsuario.findByCredencialNomeUsuario(username);

        if (usuarioLogado == null) {
            throw new AccessDeniedException("Usuário não encontrado.");
        }

        if (isVendedor) {
            return repositorio.findByFuncionarioId(usuarioLogado.getId());
        }

        if (isCliente) {
            return repositorio.findByClienteId(usuarioLogado.getId());
        }

        throw new AccessDeniedException("Perfil não autorizado.");
    }


    @Transactional(readOnly = true)
    public Venda buscarPorId(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Venda venda = repositorio.findById(id).orElse(null);
        if (venda == null) return null;

        boolean isAdmin = hasRole(auth, "ROLE_ADMIN");
        boolean isGerente = hasRole(auth, "ROLE_GERENTE");
        boolean isVendedor = hasRole(auth, "ROLE_VENDEDOR");
        boolean isCliente = hasRole(auth, "ROLE_CLIENTE");

        if (isAdmin || isGerente) {
            return venda;
        }

        String username = auth.getName();
        Usuario usuarioLogado = repositorioUsuario.findByCredencialNomeUsuario(username);

        if (usuarioLogado == null) {
            throw new AccessDeniedException("Usuário não encontrado.");
        }

        if (isVendedor && venda.getFuncionario() != null &&
                venda.getFuncionario().getId().equals(usuarioLogado.getId())) {
            return venda;
        }

        if (isCliente && venda.getCliente() != null &&
                venda.getCliente().getId().equals(usuarioLogado.getId())) {
            return venda;
        }

        throw new AccessDeniedException("Acesso negado.");
    }

    @Transactional
    public Venda criar(Venda venda) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (hasRole(auth, "ROLE_VENDEDOR")) {
            String username = auth.getName();
            Usuario funcionario = repositorioUsuario.findByCredencialNomeUsuario(username);

            if (funcionario == null) throw new RuntimeException("Funcionário não encontrado");

            venda.setFuncionario(funcionario);
        }

        if (venda.getCadastro() == null) {
            venda.setCadastro(new Date());
        }

        Usuario cliente = venda.getCliente();
        if (cliente != null) {
            ensureSet(cliente.getVendas());
            cliente.getVendas().add(venda);
        }

        Veiculo veiculo = venda.getVeiculo();
        if (veiculo != null) {
            ensureSet(veiculo.getVendas());
            veiculo.getVendas().add(venda);
        }

        return repositorio.save(venda);
    }


    @Transactional
    public Venda atualizar(Long id, Venda atualizacao) {

        Venda existente = buscarPorId(id);
        if (existente == null) return null;

        atualizarRelacoes(existente, atualizacao);
        atualizarDados(existente, atualizacao);

        return repositorio.save(existente);
    }

    private void atualizarRelacoes(Venda existente, Venda atualizacao) {

        Usuario clienteAntigo = existente.getCliente();
        Usuario clienteNovo = atualizacao.getCliente();

        if (clienteAntigo != null && !clienteAntigo.equals(clienteNovo)) {
            clienteAntigo.getVendas().remove(existente);
        }

        if (clienteNovo != null && !clienteNovo.equals(clienteAntigo)) {
            ensureSet(clienteNovo.getVendas());
            clienteNovo.getVendas().add(existente);
        }

        Veiculo veiculoAntigo = existente.getVeiculo();
        Veiculo veiculoNovo = atualizacao.getVeiculo();

        if (veiculoAntigo != null && !veiculoAntigo.equals(veiculoNovo)) {
            veiculoAntigo.getVendas().remove(existente);
        }

        if (veiculoNovo != null && !veiculoNovo.equals(veiculoAntigo)) {
            ensureSet(veiculoNovo.getVendas());
            veiculoNovo.getVendas().add(existente);
        }
    }

    private void atualizarDados(Venda existente, Venda atualizacao) {

        existente.setIdentificacao(atualizacao.getIdentificacao());
        existente.setCliente(atualizacao.getCliente());
        existente.setFuncionario(atualizacao.getFuncionario());
        existente.setVeiculo(atualizacao.getVeiculo());

        if (existente.getMercadorias() == null) {
            existente.setMercadorias(new HashSet<>());
        }
        existente.getMercadorias().clear();
        if (atualizacao.getMercadorias() != null) {
            existente.getMercadorias().addAll(atualizacao.getMercadorias());
        }


        if (existente.getServicos() == null) {
            existente.setServicos(new HashSet<>());
        }
        existente.getServicos().clear();
        if (atualizacao.getServicos() != null) {
            existente.getServicos().addAll(atualizacao.getServicos());
        }
    }


    @Transactional
    public void excluir(Long id) {
        Venda venda = buscarPorId(id);
        if (venda == null) return;

        Usuario cliente = venda.getCliente();
        if (cliente != null) {
            cliente.getVendas().remove(venda);
        }

        Veiculo veiculo = venda.getVeiculo();
        if (veiculo != null) {
            veiculo.getVendas().remove(venda);
        }

        repositorio.delete(venda);
    }


    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
    }

    private <T> void ensureSet(Set<T> set) {
        if (set == null) {
            set = new HashSet<>();
        }
    }
}
