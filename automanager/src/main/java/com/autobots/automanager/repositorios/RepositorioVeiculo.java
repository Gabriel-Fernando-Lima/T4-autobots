package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autobots.automanager.modelos.entidades.Veiculo;

public interface RepositorioVeiculo extends JpaRepository<Veiculo, Long> {
}