package com.autobots.automanager.controles;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.jwt.ProvedorJwt;
import com.autobots.automanager.modelos.dto.LoginDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProvedorJwt provedorJwt;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto, HttpServletResponse response) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getNomeUsuario(), dto.getSenha())
        );

        UserDetails usuario = (UserDetails) auth.getPrincipal();
        String jwt = provedorJwt.proverJwt(usuario.getUsername());

        response.addHeader("Authorization", "Bearer " + jwt);

        return ResponseEntity.ok("Login efetuado com sucesso");
    }

    @GetMapping("/token-test")
    public ResponseEntity<?> tokenTest() {
        return ResponseEntity.ok("Token válido.");
    }
}
