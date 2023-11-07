package com.ufcg.psoft.commerce.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import com.ufcg.psoft.commerce.service.pedido.*;
import com.ufcg.psoft.commerce.dto.pedidodto.*;
import com.ufcg.psoft.commerce.model.Pagamento;
import org.springframework.http.*;
import jakarta.validation.*;

@RestController
@RequestMapping(
        value = "/pedidos",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PedidoController {

    @Autowired
    PedidoBodyServiceInterface pedidoBodyService;

    @Autowired
    PedidoGetServiceInterface pedidoGetService;

    @Autowired
    PedidoDeleteServiceInterface pedidoDeleteService;
    
    @PostMapping
    public ResponseEntity<?> pedidoPost(@RequestParam(value = "codigoCliente", required = true) String codigoCliente,
                                        @Valid @RequestBody PedidoBodyDTO pedidoBodyDTO) {
	return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(pedidoBodyService.post(codigoCliente, pedidoBodyDTO));
    }

    @PutMapping("/{idPedido}")
    public ResponseEntity<?> pedidoPut(@PathVariable(value = "idPedido") Long idPedido,
                                    @RequestParam(value = "codigoCliente") String codigoCliente,
                                    @RequestBody @Valid PedidoBodyDTO pedidoBodyDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoBodyService.put(idPedido, codigoCliente, pedidoBodyDTO));
    }

    @PutMapping("/{idPedido}/pedido-pronto")
    public ResponseEntity<?> pedidoPreparoFinalizadoPut(@PathVariable(value = "idPedido") Long idPedido,
                                             @RequestParam(value = "codigoEstabelecimento") String codigoEstabelecimento,
                                             @RequestBody PedidoBodyDTO pedidoBodyDTO) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoBodyService.putStatusPedidoPronto(idPedido, codigoEstabelecimento, pedidoBodyDTO));
    }

    @PutMapping("/{idPedido}/associar-pedido-entregador")
    public ResponseEntity<?> pedidoProntoPut(@PathVariable(value = "idPedido") Long idPedido,
                                       @RequestParam(value = "codigoEstabelecimento") String codigoEstabelecimento,
                                       @RequestBody PedidoBodyDTO pedidoBodyDTO) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoBodyService.putStatusPedidoPronto(idPedido, codigoEstabelecimento, pedidoBodyDTO));
    }

    @PutMapping("/{idPedido}/{idCliente}/cliente-confirmar-entrega")
    public ResponseEntity<?> pedidoEntreguePut(@PathVariable(value = "idPedido") Long idPedido,
                                               @PathVariable(value = "idCliente") Long idCliente,
                                             @RequestParam(value = "codigoCliente") String codigoCliente,
                                             @RequestBody @Valid PedidoBodyDTO pedidoBodyDTO) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoBodyService.putStatusPedidoEntregue(idPedido, idCliente, codigoCliente, pedidoBodyDTO));
    }

    @PatchMapping("/{idPedido}/confirmarPagamento")
    public ResponseEntity<?> confirmarPagamento(@PathVariable(value = "idPedido") Long idPedido,
                                @RequestParam(value = "codigoCliente", required = true) String codigoCliente,
                                @RequestParam Pagamento pagamento) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoBodyService.confirmarPagamento(idPedido, codigoCliente, pagamento));
    }

    @GetMapping("/{idPedido}/{idCliente}")
    ResponseEntity<?> pedidoGetOne(@PathVariable(value = "idPedido") Long idPedido,
                                @PathVariable(value = "idCliente") Long idCliente,
                                @RequestParam(value = "codigoCliente", required = true) String codigoCliente) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.pedidoGetService.getOne(idPedido, idCliente, codigoCliente));
    }

    @GetMapping
    ResponseEntity<?> pedidoGetAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.pedidoGetService.getAll());
    }

    @DeleteMapping("/{idPedido}/cancelar-pedido")
    ResponseEntity<?> cancelarPedido(@PathVariable(value = "idPedido") Long idPedido,
                                      @RequestParam(value = "codigoCliente", required = true) String codigoCliente) {
        this.pedidoDeleteService.deleteOne(idPedido, codigoCliente);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping("/{idPedido}")
    ResponseEntity<?> pedidoDeleteOne(@PathVariable(value = "idPedido") Long idPedido,
                                @RequestParam(value = "codigoCliente", required = true) String codigoCliente) {
        this.pedidoDeleteService.deleteOne(idPedido, codigoCliente);
        return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
    }

    @DeleteMapping
    ResponseEntity<?> pedidoDeleteAll() {
        this.pedidoDeleteService.deleteAll();
        return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
    }
    
    @GetMapping("/{idPedido}/cliente/{idCliente}/estabelecimento/{idEstabelecimento}/visualizarPedido")
    public ResponseEntity<?> clienteVisualizarPedido(@PathVariable(value = "idPedido") Long idPedido,
                                @PathVariable(value = "idCliente") Long idCliente,
                                @PathVariable(value = "idEstabelecimento") Long idEstabelecimento,
                                @RequestParam(value = "codigoCliente", required = true) String codigoCliente) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.pedidoGetService.clienteVisualizarPedido(idPedido, idCliente, idEstabelecimento, codigoCliente));
    }

    @GetMapping("/{idCliente}/estabelecimento/{idEstabelecimento}/historicoPedidos")
    public ResponseEntity<?> clienteHistoricoPedidos(@PathVariable(value = "idCliente") Long idCliente, 
                                @PathVariable(value = "idEstabelecimento") Long idEstabelecimento,
                                @RequestParam(value = "codigoCliente", required = true) String codigoCliente) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.pedidoGetService.clienteHistoricoPedidos(idCliente, idEstabelecimento, codigoCliente));
    }

    @GetMapping("/{idCliente}/estabelecimento/{idEstabelecimento}/historicoPedidos/{statusEntrega}")
    public ResponseEntity<?> clienteHistoricoPedidos(@PathVariable(value = "idCliente") Long idCliente,
                                                     @PathVariable(value = "idEstabelecimento") Long idEstabelecimento,
                                                     @PathVariable(value = "statusEntrega", required = true) String statusEntrega,
                                                     @RequestParam(value = "codigoCliente", required = true) String codigoCliente) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.pedidoGetService.clienteHistoricoPedidosFiltrado(idCliente, idEstabelecimento, codigoCliente, statusEntrega));
    }

    @GetMapping("/{idPedido}/estabelecimento/{idEstabelecimento}/visualizarPedido")
    public ResponseEntity<?> estabelecimentoVisualizarPedido(@PathVariable(value = "idPedido") Long idPedido,
                                @PathVariable(value = "idEstabelecimento") Long idEstabelecimento,
                                @RequestParam(value = "codigoEstabelecimento", required = true) String codigoEstabelecimento) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.pedidoGetService.estabelecimentoVisualizarPedido(idPedido, idEstabelecimento, codigoEstabelecimento));
    }

    @GetMapping("/estabelecimento/{idEstabelecimento}/listarPedidos")
    public ResponseEntity<?> estabelecimentoListarPedidos(@PathVariable(value = "idEstabelecimento") Long idEstabelecimento,
                                @RequestParam(value = "codigoEstabelecimento", required = true) String codigoEstabelecimento) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.pedidoGetService.estabelecimentoListarPedidos(idEstabelecimento, codigoEstabelecimento));
    }

    @DeleteMapping("/{idPedido}/estabelecimento/{idEstabelecimento}/estabelecimentoDeleteOne")
    ResponseEntity<?> pedidoEstabelecimentoDeleteOne(@PathVariable(value = "idPedido") Long idPedido,
                                @PathVariable(value = "idEstabelecimento") Long idEstabelecimento,
                                @RequestParam(value = "codigoEstabelecimento", required = true) String codigoEstabelecimento) {
        this.pedidoDeleteService.estabelecimentoDeleteOne(idPedido, idEstabelecimento, codigoEstabelecimento);
        return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
    }

    @DeleteMapping("/estabelecimentoDeleteAll")
    ResponseEntity<?> pedidoEstabelecimentoDeleteAll() {
        this.pedidoDeleteService.estabelecimentoDeleteAll();
        return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
    }

}
