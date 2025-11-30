package com.autobots.automanager.controles;

import java.util.List;

import com.autobots.automanager.service.hateoas.AdicionadorLinkServico;
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

import com.autobots.automanager.modelos.converter.ServicoConverter;
import com.autobots.automanager.modelos.dto.ServicoDTO;
import com.autobots.automanager.modelos.entidades.Servico;
import com.autobots.automanager.service.ServicoService;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoServico;

    @Autowired
    private ServicoConverter conversor;

    @Autowired
    private AdicionadorLinkServico adicionadorLink;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<ServicoDTO>> listarServicos() {
        List<Servico> servicos = servicoServico.listarTodos();

        if (servicos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<ServicoDTO> dtos = conversor.entidadeParaDto(servicos);
        adicionadorLink.adicionarLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO> buscarServico(@PathVariable Long id) {
        Servico servico = servicoServico.buscarPorId(id);

        if (servico == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ServicoDTO dto = conversor.entidadeParaDto(servico);
        adicionadorLink.adicionarLinks(dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @PostMapping("/cadastrar")
    public ResponseEntity<ServicoDTO> criarServico(@RequestBody ServicoDTO dto) {
        Servico novoServico = conversor.dtoParaEntidade(dto);
        Servico servicoSalvo = servicoServico.criar(novoServico);
        ServicoDTO dtoSalvo = conversor.entidadeParaDto(servicoSalvo);

        adicionadorLink.adicionarLinks(dtoSalvo);

        return new ResponseEntity<>(dtoSalvo, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ServicoDTO> atualizarServico(@PathVariable Long id, @RequestBody ServicoDTO dto) {
        Servico servicoAtualizacao = conversor.dtoParaEntidade(dto);
        Servico servicoAtualizado = servicoServico.atualizar(id, servicoAtualizacao);

        if (servicoAtualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ServicoDTO dtoAtualizado = conversor.entidadeParaDto(servicoAtualizado);
        adicionadorLink.adicionarLinks(dtoAtualizado);
        return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirServico(@PathVariable Long id) {
        servicoServico.excluir(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}