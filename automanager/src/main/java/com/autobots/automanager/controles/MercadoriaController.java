package com.autobots.automanager.controles;

import java.util.List;

import com.autobots.automanager.service.hateoas.AdicionadorLinkMercadoria;
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

import com.autobots.automanager.modelos.converter.MercadoriaConverter;
import com.autobots.automanager.modelos.dto.MercadoriaDTO;
import com.autobots.automanager.modelos.entidades.Mercadoria;
import com.autobots.automanager.service.MercadoriaService;

@RestController
@RequestMapping("/mercadorias")
public class MercadoriaController {

    @Autowired
    private MercadoriaService servicoMercadoria;

    @Autowired
    private MercadoriaConverter conversor;

    @Autowired
    private AdicionadorLinkMercadoria adicionadorLink;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<MercadoriaDTO>> listarMercadorias() {
        List<Mercadoria> mercadorias = servicoMercadoria.listarTodos();

        if (mercadorias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<MercadoriaDTO> dtos = conversor.entidadeParaDto(mercadorias);
        adicionadorLink.adicionarLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<MercadoriaDTO> buscarMercadoria(@PathVariable Long id) {
        Mercadoria mercadoria = servicoMercadoria.buscarPorId(id);

        if (mercadoria == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        MercadoriaDTO dto = conversor.entidadeParaDto(mercadoria);
        adicionadorLink.adicionarLinks(dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @PostMapping("/cadastrar")
    public ResponseEntity<MercadoriaDTO> criarMercadoria(@RequestBody MercadoriaDTO dto) {
        Mercadoria novaMercadoria = conversor.dtoParaEntidade(dto);
        Mercadoria mercadoriaSalva = servicoMercadoria.criar(novaMercadoria);
        MercadoriaDTO dtoSalvo = conversor.entidadeParaDto(mercadoriaSalva);

        adicionadorLink.adicionarLinks(dtoSalvo);

        return new ResponseEntity<>(dtoSalvo, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<MercadoriaDTO> atualizarMercadoria(@PathVariable Long id, @RequestBody MercadoriaDTO dto) {
        Mercadoria mercadoriaAtualizacao = conversor.dtoParaEntidade(dto);
        Mercadoria mercadoriaAtualizada = servicoMercadoria.atualizar(id, mercadoriaAtualizacao);

        if (mercadoriaAtualizada == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        MercadoriaDTO dtoAtualizado = conversor.entidadeParaDto(mercadoriaAtualizada);
        adicionadorLink.adicionarLinks(dtoAtualizado);
        return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirMercadoria(@PathVariable Long id) {
        servicoMercadoria.excluir(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}