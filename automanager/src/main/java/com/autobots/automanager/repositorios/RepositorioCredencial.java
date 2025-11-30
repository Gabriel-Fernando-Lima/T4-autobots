package com.autobots.automanager.repositorios;

import com.autobots.automanager.modelos.entidades.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioCredencial extends JpaRepository<Credencial, Long> {
}