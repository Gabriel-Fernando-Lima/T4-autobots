package com.autobots.automanager;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.autobots.automanager.enumeracoes.Perfil;
import com.autobots.automanager.modelos.entidades.*;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

    @Autowired
    private RepositorioUsuario repoUsuario;


    public static void main(String[] args) {
        SpringApplication.run(AutomanagerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.getPerfis().add(Perfil.ROLE_ADMIN);

        Credencial credAdmin = new Credencial();
        credAdmin.setNomeUsuario("admin");
        credAdmin.setSenha(encoder.encode("123456"));
        admin.setCredencial(credAdmin);

        repoUsuario.save(admin);

    }
}