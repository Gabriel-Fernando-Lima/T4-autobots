package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autobots.automanager.modelos.entidades.Venda;

import java.util.List;

public interface RepositorioVenda extends JpaRepository<Venda, Long> {
    List<Venda> findByFuncionarioId(Long funcionarioId);

    List<Venda> findByClienteId(Long clienteId);
}