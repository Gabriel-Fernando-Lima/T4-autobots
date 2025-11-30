package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autobots.automanager.modelos.entidades.Email;

public interface RepositorioEmail extends JpaRepository<Email, Long> {
}