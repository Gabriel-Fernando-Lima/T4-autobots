package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autobots.automanager.modelos.entidades.Endereco;

public interface RepositorioEndereco extends JpaRepository<Endereco, Long> {
}