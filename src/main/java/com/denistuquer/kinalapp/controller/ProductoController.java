package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Producto;
import com.denistuquer.kinalapp.service.IProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = @Controller + @RequestBody
@RequestMapping("/productos")
//Todas las rutas en ese controlador deben empezar por /clientes

public class ProductoController {
    //Inyectamos el SERVICIO y NO el repositorio
    //El controlador solo debe de tener conexión con el servicio
    private final IProductoService productoService;
    //La inyeccion de dependencias debe hacerse por el constructor
    public ProductoController(IProductoService productoService){
        this.productoService = productoService;
    }

    /**
     * Obtiene una lista de todos los productos.
     *
     * Este endpoint responde a solicitudes HTPP GET y retorna
     * todos los productos registrados en el sistema.
     *
     * @return ResponseEntity: con la lista de productos y estado HTTP 200 (ok)
     */
    @GetMapping
    public ResponseEntity<List<Producto>> listar(){
        List<Producto> productos = productoService.listarTodos();
        //delegamos al servicio
        return ResponseEntity.ok(productos);
    }

    /**
     * Metodo para obtener un producto por su código.
     *
     * @param codigo identificador único del producto
     * @return ResponseEntity: con el producto encontrado o estado 404 si no existe
     */
    @GetMapping("/{codigo}")
    public ResponseEntity<Producto> buscarPorCodigo(@PathVariable int codigo){
        return productoService.buscarPorCodigo(codigo)
                //Si Optional tiene valor, devuelve 200 ok con el producto
                .map(ResponseEntity::ok)
                //Si Optional esta vacio, devuelve un 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Metodo para obtener la lista de productos activos.
     *
     * Este endpoint responde a solicitudes HTTP GET Y retorna únicamente
     * los productos cuyo estado es activo.
     *
     * @return ResponseEntity: con la lista de productos activos o sin contenido
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> listarActivos(){
        List<Producto> activos = productoService.listarActivos();
        if (activos.isEmpty()) {
            //204 si no hay productos activos
            return ResponseEntity.noContent().build();
        }
        //200 con la lista de activos
        return ResponseEntity.ok(activos);
    }

    /**
     * Metodo POST, para crear un nuevo producto.
     *
     * Este método responde a solicitudes HTTP POST y permite registrar
     * un nuevo producto en el sistema.
     *
     * @param producto objeto producto a registrar
     * @return ResponseEntity: con el producto creado o mensaje de error
     */
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Producto producto){
        //@RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo producto
        //<?> significa "tipo generico" puede ser un producto o un String
        try{
            Producto nuevoProducto = productoService.guardar(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
            //201 CREATED(mucho más específico que el 200 para la creación de un producto)
        }catch(IllegalArgumentException e){
            //Si hay error de validación
            //400 BAD REQUEST con el mensaje de error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Metódo DELETE, para eliminar un producto.
     *
     * Este endpoint responde a solicitudes HTTP DELETE y elimina un producto
     * existe en el sistema.
     *
     * @param codigo identificador único del producto
     * @return ResponseEntity: No devuelve cuerpo en la respuesta
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable int codigo){
        try{
            if (!productoService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();
                //404 NOT FOUND
            }
            productoService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (se ejecuta correctamente y no devuelve cuerpo)

        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }
    }

    /**
     * Metodo PUT, para actualizar un producto.
     *
     * Este Endpoint responde peticiones HTTP PUT y permite actualizar
     * los datos de un producto según su código.
     *
     * @param codigo identificador  único del producto
     * @param producto objeto con los nuevos datos
     * @return ResponseEntity: con el producto actualizado o mensaje de error
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable int codigo, @RequestBody Producto producto){
        try{
            if (!productoService.existePorCodigo(codigo)){
                //Verificar si existe antes de poder actualizar
                return ResponseEntity.notFound().build();
                //404 NOT FOUND
            }
            //Actualizamos el producto pero esto puede lanzar una excepcion
            Producto productoActualizado = productoService.actualizar(codigo, producto);
            return ResponseEntity.ok(productoActualizado);
            //200 ok con el cliente ya actualizado

        }catch(IllegalArgumentException e){
            //Error cuando los datos sean correctos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(RuntimeException e) {
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }
}
