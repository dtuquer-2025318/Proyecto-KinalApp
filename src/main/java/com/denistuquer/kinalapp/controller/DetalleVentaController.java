package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.DetalleVenta;
import com.denistuquer.kinalapp.service.IDetalleVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle_ventas")
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;

    // Inyección de dependencias por constructor
    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    /**
     * Método para listar todos los detalles de venta
     * @return ResponseEntity con la lista de detalles de venta
     */
    @GetMapping
    public ResponseEntity<List<DetalleVenta>> listar() {
        List<DetalleVenta> detalles = detalleVentaService.listarTodos();
        return ResponseEntity.ok(detalles);
        // 200 OK con la lista de detalles
    }

    /**
     * Método para buscar un detalle de venta por codigo
     * @param codigo identificador único del detalle de venta
     * @return ResponseEntity con el detalle de venta o 404
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<DetalleVenta> buscarPorCodigo(@PathVariable int codigo) {
        return detalleVentaService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Método para crear un nuevo detalle de venta
     * @param detalleVenta objeto recibido en el cuerpo de la petición
     * @return ResponseEntity con el detalle creado o mensaje de error
     */
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalleVenta) {
        try {
            DetalleVenta nuevoDetalle = detalleVentaService.guardar(detalleVenta);
            return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
            // 201 CREATED
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST
        }
    }

    /**
     * Método para actualizar un detalle de venta
     * @param codigo identificador único del detalle de venta
     * @param detalleVenta datos actualizados
     * @return ResponseEntity con el detalle actualizado o mensaje de error
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable int codigo, @RequestBody DetalleVenta detalleVenta) {
        try {
            if (!detalleVentaService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
                // 404 NOT FOUND
            }
            DetalleVenta detalleActualizado = detalleVentaService.actualizar(codigo, detalleVenta);
            return ResponseEntity.ok(detalleActualizado);
            // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
            // 400 BAD REQUEST
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
            // 404 NOT FOUND
        }
    }

    /**
     * Método para eliminar un detalle de venta
     * @param codigo identificador único del detalle de venta
     * @return ResponseEntity sin contenido o 404
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable int codigo) {
        try {
            if (!detalleVentaService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
                // 404 NOT FOUND
            }
            detalleVentaService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            // 204 NO CONTENT
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
            // 404 NOT FOUND
        }
    }
}
