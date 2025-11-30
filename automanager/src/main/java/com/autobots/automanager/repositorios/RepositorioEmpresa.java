package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.modelos.entidades.Empresa;

public interface RepositorioEmpresa extends JpaRepository<Empresa, Long> {
	//public Empresa findByRazaoSocial(String nome);
}