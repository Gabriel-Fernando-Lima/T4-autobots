package com.autobots.automanager.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autobots.automanager.modelos.entidades.Usuario;
import com.autobots.automanager.modelos.entidades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

@Service
public class VeiculoService {

    @Autowired
    private RepositorioVeiculo repositorio;

    @Transactional(readOnly = true)
    public List<Veiculo> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Veiculo buscarPorId(Long id) {
        Optional<Veiculo> veiculo = repositorio.findById(id);
        return veiculo.orElse(null);
    }

    @Transactional
    public Veiculo criar(Veiculo veiculo) {
        Usuario proprietario = veiculo.getProprietario();
        if (proprietario != null) {
            proprietario.getVeiculos().add(veiculo);
        }
        return repositorio.save(veiculo);
    }

    @Transactional
    public Veiculo atualizar(Long id, Veiculo veiculoAtualizacao) {
        Veiculo veiculoExistente = buscarPorId(id);
        if (veiculoExistente == null) {
            return null;
        }

        Usuario propietarioAntigo = veiculoExistente.getProprietario();
        Usuario propietarioNovo = veiculoAtualizacao.getProprietario();

        if (propietarioAntigo != null && !propietarioAntigo.equals(propietarioNovo)) {
            propietarioAntigo.getVeiculos().remove(veiculoExistente);
        }

        if (propietarioNovo != null && !propietarioNovo.equals(propietarioAntigo)) {
            if (propietarioNovo.getVeiculos() == null) {
                propietarioNovo.setVeiculos(new HashSet<>());
            }
            propietarioNovo.getVeiculos().add(veiculoExistente);
        }

        veiculoAtualizacao.setId(id);
        atualizarDados(veiculoExistente, veiculoAtualizacao);

        return repositorio.save(veiculoExistente);
    }

    private void atualizarDados(Veiculo existente, Veiculo atualizacao) {
        existente.setTipo(atualizacao.getTipo());
        existente.setModelo(atualizacao.getModelo());
        existente.setPlaca(atualizacao.getPlaca());
        existente.setProprietario(atualizacao.getProprietario());
    }

    @Transactional
    public void excluir(Long id) {
        Veiculo veiculo = buscarPorId(id);
        if (veiculo != null) {
            Usuario proprietario = veiculo.getProprietario();
            if (proprietario != null) {
                proprietario.getVeiculos().remove(veiculo);
            }
            repositorio.delete(veiculo);
        }
    }
}