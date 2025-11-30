package com.autobots.automanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.modelos.entidades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;

@Service
public class ServicoService {

    @Autowired
    private RepositorioServico repositorio;

    @Transactional(readOnly = true)
    public List<Servico> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Servico buscarPorId(Long id) {
        Optional<Servico> servico = repositorio.findById(id);
        return servico.orElse(null);
    }

    @Transactional
    public Servico criar(Servico servico) {
        return repositorio.save(servico);
    }

    @Transactional
    public Servico atualizar(Long id, Servico servicoAtualizacao) {
        Servico servicoExistente = buscarPorId(id);
        if (servicoExistente == null) {
            return null;
        }

        servicoAtualizacao.setId(id);
        atualizarDados(servicoExistente, servicoAtualizacao);

        return repositorio.save(servicoExistente);
    }

    private void atualizarDados(Servico existente, Servico atualizacao) {
        existente.setNome(atualizacao.getNome());
        existente.setValor(atualizacao.getValor());
        existente.setDescricao(atualizacao.getDescricao());
    }

    @Transactional
    public void excluir(Long id) {
        Servico servico = buscarPorId(id);
        if (servico != null) {
            repositorio.delete(servico);
        }
    }
}