package com.autobots.automanager.controles;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.modelos.converter.UsuarioConverter;
import com.autobots.automanager.modelos.dto.UsuarioDTO;
import com.autobots.automanager.modelos.entidades.Usuario;
import com.autobots.automanager.service.hateoas.AdicionadorLinkUsuario;
import com.autobots.automanager.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService servicoUsuario;
    @Autowired
    private UsuarioConverter conversor;
    @Autowired
    private AdicionadorLinkUsuario adicionadorLink;

    @GetMapping("/listar")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<Usuario> usuarios = servicoUsuario.listarTodos();
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<UsuarioDTO> dtos = conversor.entidadeParaDto(usuarios);
        adicionadorLink.adicionarLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_CLIENTE')")
    public ResponseEntity<UsuarioDTO> buscarUsuario(@PathVariable Long id) {
        Usuario usuario = servicoUsuario.buscarPorId(id);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UsuarioDTO dto = conversor.entidadeParaDto(usuario);
        adicionadorLink.adicionarLinks(dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody UsuarioDTO dto) {
        Usuario novoUsuario = conversor.dtoParaEntidade(dto);
        Usuario usuarioSalvo = servicoUsuario.criar(novoUsuario);
        UsuarioDTO dtoSalvo = conversor.entidadeParaDto(usuarioSalvo);
        adicionadorLink.adicionarLinks(dtoSalvo);
        return new ResponseEntity<>(dtoSalvo, HttpStatus.CREATED);
    }

    @PutMapping("/atualizar/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        Usuario usuarioAtualizacao = conversor.dtoParaEntidade(dto);
        Usuario usuarioAtualizado = servicoUsuario.atualizar(id, usuarioAtualizacao);
        if (usuarioAtualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UsuarioDTO dtoAtualizado = conversor.entidadeParaDto(usuarioAtualizado);
        adicionadorLink.adicionarLinks(dtoAtualizado);
        return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
    }

    @DeleteMapping("/excluir/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
        servicoUsuario.excluir(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}