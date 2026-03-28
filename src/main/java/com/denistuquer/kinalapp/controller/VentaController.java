package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Venta;
import com.denistuquer.kinalapp.service.IVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = @Controller + @RequestBody
@RequestMapping("/ventas")
//Todas las rutas en este controlador deben empezar por /ventas
public class VentaController {
    //Inyectamos el SERVICIO y NO el repositorio
    //El controlador solo debe de tener conexion con el Servicio
    private final IVentaService ventaService;
    //Como buena practica la Inyección de dependencias debe hacerse por el constructor
    public VentaController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }

    /**
     * Método para mostrar la lista de las ventas.
     *
     * @return ResponseEntity con la lista de ventas
     */
    @GetMapping
    public ResponseEntity<List<Venta>> listar(){
        List<Venta> ventas = ventaService.listarTodos();
        //delegamos al servicios
        return ResponseEntity.ok(ventas);
        //200 ok con la lista de ventas
    }

    /**
     * Método Get: para buscar una venta por el codigo.
     *
     * @param codigo identificador único de la venta
     * @return ResponseEntity con la venta encontrada o 404 si no existe
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Venta> buscarPorCodigo(@PathVariable int codigo){
        //@PathVariable Toma el valor de la URL y lo asigna a la venta
        return ventaService.buscarPorCodigo(codigo)
                //Si Optional tiene valor, devuelve 200 ok con la venta
                .map(ResponseEntity::ok)
                //Si Optional esta vacío, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Metoodo para la lista de ventas activas.
     *
     * @return ResponseEntity con la lista de ventas activas o sin contenido
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Venta>> listarActivos(){
        List<Venta> activos = ventaService.listarActivos();
        if (activos.isEmpty()) {
            //204 si no hay ventas activas
            return ResponseEntity.noContent().build();
        }else{
            //200 con la lista de activos
            return ResponseEntity.ok(activos);
        }
    }

    /**
     * Método para registrar una nueva venta.
     *
     * Responde a peticiones HTTP POST. Recibe un objeto Venta en formato JSON,
     * lo valida y lo guarda en la base de datos.
     *
     * @param venta objeto Venta recibido en el cuerpo de la petición
     * @return ResponseEntity con la venta creada o mensaje de error
     */
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Venta venta){
        //@RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo venta
        //<?> significa "tipo generico" puede ser una venta o un String
        try{
            Venta nuevaVenta = ventaService.guardar(venta);
            //Intentamos guardar la venta pero puede lanzar una excepcion
            //de IllegalArgumentException
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
            //201 IllegalArgumentException
        }catch(IllegalArgumentException e){
            //Si hay error de validacion
            //400 BAD REQUEST con el mensaje de error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Método para eliminar una venta por medio del código
     * @param codigo identificador único de la venta
     * @return ResponseEntity sin contenido o 404 si no existe
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable int codigo){
        //RequestEntity<Void>: No devuelve cuerpo en la respuesta
        try{
            if (!ventaService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();
                //404 Si no existe
            }
            ventaService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (se ejecuta correctamente y no devuelve cuerpo)

        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }
    }

    /**
     * Método para actualizar una venta existente
     *
     * @param codigo identificador único de la venta
     * @param venta datos actualizados de la venta
     * @return ResponseEntity con la venta actualizada o mensaje de error
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable int codigo, @RequestBody Venta venta){
        try {
            if (!ventaService.existePorCodigo(codigo)) {
                //Verificar si existe antes de poder actualizar
                return ResponseEntity.notFound().build();
                //404 NOT FOUND
            }
            //Actualizamos la venta pero esto puede lanzar una excepcion
            Venta ventaActualizado = ventaService.actualizar(codigo, venta);
            return ResponseEntity.ok(ventaActualizado);
            //200 ok con el cliente ya actualizado

        }catch(IllegalArgumentException e){
            //Error cuando los datos sean incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RuntimeException e){
            //Posiblemente cualquier otro error como: venta no encontrado, etc.
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

}

