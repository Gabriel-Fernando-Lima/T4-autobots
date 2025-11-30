package com.autobots.automanager.controles;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.autobots.automanager.service.EmpresaService;
import com.autobots.automanager.service.hateoas.AdicionadorLinkEmpresa;
import com.autobots.automanager.service.hateoas.AdicionadorLinkUsuario;

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

import com.autobots.automanager.modelos.converter.EmpresaConverter;
import com.autobots.automanager.modelos.converter.UsuarioConverter;
import com.autobots.automanager.modelos.dto.EmpresaDTO;
import com.autobots.automanager.modelos.dto.UsuarioDTO;
import com.autobots.automanager.modelos.entidades.Empresa;
import com.autobots.automanager.modelos.entidades.Usuario;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService servicoEmpresa;

    @Autowired
    private EmpresaConverter conversor;

    @Autowired
    private AdicionadorLinkEmpresa adicionadorLink;

    @Autowired
    private UsuarioConverter usuarioConverter;

    @Autowired
    private AdicionadorLinkUsuario adicionadorLinkUsuario;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/listar")
    public ResponseEntity<List<EmpresaDTO>> listarEmpresas() {
        List<Empresa> empresas = servicoEmpresa.listarTodas();
        if (empresas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<EmpresaDTO> dtos = conversor.entidadeParaDto(empresas);
        adicionadorLink.adicionarLinks(dtos);

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> buscarEmpresa(@PathVariable Long id) {
        Empresa empresa = servicoEmpresa.buscarPorId(id);
        if (empresa == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        EmpresaDTO dto = conversor.entidadeParaDto(empresa);
        adicionadorLink.adicionarLinks(dto);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/cadastrar")
    public ResponseEntity<EmpresaDTO> criarEmpresa(@RequestBody EmpresaDTO dto) {
        Empresa novaEmpresa = conversor.dtoParaEntidade(dto);
        Empresa empresaSalva = servicoEmpresa.criar(novaEmpresa);
        EmpresaDTO dtoSalvo = conversor.entidadeParaDto(empresaSalva);

        adicionadorLink.adicionarLinks(dtoSalvo);

        return new ResponseEntity<>(dtoSalvo, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<EmpresaDTO> atualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaDTO dto) {
        Empresa empresaAtualizacao = conversor.dtoParaEntidade(dto);
        Empresa empresaAtualizada = servicoEmpresa.atualizar(id, empresaAtualizacao);

        if (empresaAtualizada == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        EmpresaDTO dtoAtualizado = conversor.entidadeParaDto(empresaAtualizada);
        adicionadorLink.adicionarLinks(dtoAtualizado);

        return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirEmpresa(@PathVariable Long id) {
        servicoEmpresa.excluir(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/associar/{idEmpresa}/usuarios/{idUsuario}")
    public ResponseEntity<Void> associarUsuario(@PathVariable Long idEmpresa, @PathVariable Long idUsuario) {

        boolean associado = servicoEmpresa.associar(idEmpresa, idUsuario);

        if (associado) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{idEmpresa}/usuarios")
    public ResponseEntity<List<UsuarioDTO>> buscarUsuariosDaEmpresa(@PathVariable Long idEmpresa) {

        Set<Usuario> usuarios = servicoEmpresa.listarUsuarios(idEmpresa);

        if (usuarios == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<UsuarioDTO> dtos = usuarios.stream()
                .map(usuarioConverter::entidadeParaDto)
                .collect(Collectors.toList());

        adicionadorLinkUsuario.adicionarLinks(dtos);

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}