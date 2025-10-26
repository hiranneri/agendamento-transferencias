package br.com.agendamentotransferencias.controller;

import br.com.agendamentotransferencias.controller.dto.TransferenciaDTO;
import br.com.agendamentotransferencias.controller.dto.TransferenciaResponseDTO;
import br.com.agendamentotransferencias.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("transferencia")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<TransferenciaResponseDTO> agendarTransferencia(@Valid @RequestBody TransferenciaDTO dto) {
        TransferenciaResponseDTO response = transferenciaService.agendarTransferencia(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/extrato")
    public ResponseEntity<Page<TransferenciaResponseDTO>> listarExtrato(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataAgendamento,desc") String[] sort) {

        // Configurar ordenação
        Sort.Direction direction = sort[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Sort sorting = Sort.by(direction, sort[0]);

        // Criar objeto Pageable
        Pageable pageable = PageRequest.of(page, size, sorting);

        // Buscar dados paginados
        Page<TransferenciaResponseDTO> extrato = transferenciaService.listarExtrato(pageable);

        return ResponseEntity.ok(extrato);
    }
}
