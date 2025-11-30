package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autobots.automanager.modelos.entidades.Servico;

public interface RepositorioServico extends JpaRepository<Servico, Long> {
}