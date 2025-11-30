package com.autobots.automanager.controles;

import java.util.List;

import com.autobots.automanager.service.hateoas.AdicionadorLinkVeiculo;
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

import com.autobots.automanager.modelos.converter.VeiculoConverter;
import com.autobots.automanager.modelos.dto.VeiculoDTO;
import com.autobots.automanager.modelos.entidades.Veiculo;
import com.autobots.automanager.service.VeiculoService;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService servicoVeiculo;

    @Autowired
    private VeiculoConverter conversor;

    @Autowired
    private AdicionadorLinkVeiculo adicionadorLink;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/listar")
    public ResponseEntity<List<VeiculoDTO>> listarVeiculos() {
        List<Veiculo> veiculos = servicoVeiculo.listarTodos();
        if (veiculos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<VeiculoDTO> dtos = conversor.entidadeParaDto(veiculos);
        adicionadorLink.adicionarLinks(dtos);

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> buscarVeiculo(@PathVariable Long id) {
        Veiculo veiculo = servicoVeiculo.buscarPorId(id);
        if (veiculo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VeiculoDTO dto = conversor.entidadeParaDto(veiculo);
        adicionadorLink.adicionarLinks(dto);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/cadastrar")
    public ResponseEntity<VeiculoDTO> criarVeiculo(@RequestBody VeiculoDTO dto) {
        Veiculo novoVeiculo = conversor.dtoParaEntidade(dto);
        Veiculo veiculoSalvo = servicoVeiculo.criar(novoVeiculo);
        VeiculoDTO dtoSalvo = conversor.entidadeParaDto(veiculoSalvo);

        adicionadorLink.adicionarLinks(dtoSalvo);

        return new ResponseEntity<>(dtoSalvo, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<VeiculoDTO> atualizarVeiculo(@PathVariable Long id, @RequestBody VeiculoDTO dto) {
        Veiculo veiculoAtualizacao = conversor.dtoParaEntidade(dto);
        Veiculo veiculoAtualizado = servicoVeiculo.atualizar(id, veiculoAtualizacao);

        if (veiculoAtualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VeiculoDTO dtoAtualizado = conversor.entidadeParaDto(veiculoAtualizado);
        adicionadorLink.adicionarLinks(dtoAtualizado);

        return new ResponseEntity<>(dtoAtualizado, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirVeiculo(@PathVariable Long id) {
        servicoVeiculo.excluir(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}