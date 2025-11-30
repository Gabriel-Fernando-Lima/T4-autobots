package com.autobots.automanager.controles;

import java.util.List;

import com.autobots.automanager.service.hateoas.AdicionadorLinkVenda;
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

import com.autobots.automanager.modelos.converter.VendaConverter;
import com.autobots.automanager.modelos.dto.VendaDTO;
import com.autobots.automanager.modelos.entidades.Venda;
import com.autobots.automanager.service.VendaService;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService servicoVenda;

    @Autowired
    private VendaConverter conversor;

    @Autowired
    private AdicionadorLinkVenda adicionadorLink;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR', 'ROLE_CLIENTE')")
    @GetMapping("/listar")
    public ResponseEntity<List<VendaDTO>> listarVendas() {
        List<Venda> vendas = servicoVenda.listarTodos();

        if (vendas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<VendaDTO> dtos = conversor.entidadeParaDto(vendas);
        adicionadorLink.adicionarLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR', 'ROLE_CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<VendaDTO> buscarVenda(@PathVariable Long id) {
        Venda venda = servicoVenda.buscarPorId(id);

        if (venda == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VendaDTO dto = conversor.entidadeParaDto(venda);
        adicionadorLink.adicionarLinks(dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR')")
    @PostMapping("/cadastrar")
    public ResponseEntity<VendaDTO> criarVenda(@RequestBody VendaDTO dto) {
        Venda novaVenda = conversor.dtoParaEntidade(dto);
        Venda vendaSalva = servicoVenda.criar(novaVenda);
        VendaDTO dtoSalvo = conversor.entidadeParaDto(vendaSalva);

        adicionadorLink.adicionarLinks(dtoSalvo);

        return new ResponseEntity<>(dtoSalvo, HttpStatus.CREATED);
    }

     @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<VendaDTO> atualizarVenda(@PathVariable Long id, @RequestBody VendaDTO dto) {
        Venda vendaAtualizacao = conversor.dtoParaEntidade(dto);
        Venda vendaAtualizada = servicoVenda.atualizar(id, vendaAtualizacao);

        if (vendaAtualizada == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VendaDTO dtoAtualizado = conversor.entidadeParaDto(vendaAtualizada);
        adicionadorLink.adicionarLinks(dtoAtualizado);
        return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirVenda(@PathVariable Long id) {
        servicoVenda.excluir(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}