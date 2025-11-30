package com.autobots.automanager.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.modelos.entidades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@Service
public class MercadoriaService {

    @Autowired
    private RepositorioMercadoria repositorio;

    @Transactional(readOnly = true)
    public List<Mercadoria> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Mercadoria buscarPorId(Long id) {
        Optional<Mercadoria> mercadoria = repositorio.findById(id);
        return mercadoria.orElse(null);
    }

        @Transactional
    public Mercadoria criar(Mercadoria mercadoria) {
        if (mercadoria.getCadastro() == null) {
            mercadoria.setCadastro(new Date());
        }
        return repositorio.save(mercadoria);
    }

    @Transactional
    public Mercadoria atualizar(Long id, Mercadoria mercadoriaAtualizacao) {
        Mercadoria mercadoriaExistente = buscarPorId(id);
        if (mercadoriaExistente == null) {
            return null;
        }

        mercadoriaAtualizacao.setId(id);
        atualizarDados(mercadoriaExistente, mercadoriaAtualizacao);

        return repositorio.save(mercadoriaExistente);
    }

    private void atualizarDados(Mercadoria existente, Mercadoria atualizacao) {
        existente.setValidade(atualizacao.getValidade());
        existente.setFabricacao(atualizacao.getFabricacao());
        existente.setCadastro(atualizacao.getCadastro());
        existente.setNome(atualizacao.getNome());
        existente.setQuantidade(atualizacao.getQuantidade());
        existente.setValor(atualizacao.getValor());
        existente.setDescricao(atualizacao.getDescricao());


    }

    @Transactional
    public void excluir(Long id) {
        Mercadoria mercadoria = buscarPorId(id);
        if (mercadoria != null) {
            repositorio.delete(mercadoria);
        }
    }
}