package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Usuario;
import com.denistuquer.kinalapp.service.IClienteService;
import com.denistuquer.kinalapp.service.IUsuarioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = @Controller + @RequestBody
@RequestMapping("/usuarios")
//Todas las rutas en este controlador deben empezar por /usuarios
public class UsuarioController {
    //Inyectamos el SERVICIO y NO el repositorio
    //El controlador solo debe de tener conexion con el servicio
    private final IUsuarioService usuarioService;
    //Como buena practica la Inyeccion de dependencias debe hacerse por el constructor
    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //Responde peticiones GET
    @GetMapping
    //ResponseEntity nos permite controlar el codigo HTTP y el cuerpo
    public ResponseEntity<List<Usuario>> listar(){
        //delegamos al servicio
        return ResponseEntity.ok(usuarioService.listarTodos());
        // 200 Ok con la lista de clientes
    }

    //{codigo} es una variable de ruta(valor a buscar)
    @GetMapping("{codigo}")
    public ResponseEntity<Usuario> buscarPorCodigo(@PathVariable int codigo) {
        //@PathVariable Toma el valor de la URL y lo asigna al codigo
        return usuarioService.buscarPorCodigo(codigo)
        //Si Optional tiene valor, devuelve 200 ok con el cliente
                .map(ResponseEntity::ok)
                //Si optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    //Get listar usuarios activos
    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> listarActivos(){
        List<Usuario> activos = usuarioService.listarActivos();
        if (activos.isEmpty()) {
            // 204 si no hya usuarios activos
            return ResponseEntity.noContent().build();
        } else {
            // 200 con la lista de activos
            return ResponseEntity.ok(activos);
        }
    }

    //POST crear un nuevo usuario
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario){
        //@RequesBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo usuario
        //<?> significa "tipo generico" puede ser un usuario o un String
        try {
            Usuario nuevoUsuario = usuarioService.guardar(usuario);
            //Intentamos guardar el cliente pero puede lanzar una excepcion
            //de IllegalAArgumentException
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
            //201 CREATED(mucho mas especifico que el 200 para la creacion de un usuario)
        }catch(IllegalArgumentException e){
            //So hay error de validacion
            //400 BAD REQUEST con el mensaje de error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //DELETE elimina un usuario
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable int codigo){
        //ResponseEntity<Void>: No devuelve cuerpo en la respuesta
        try{
            if (!usuarioService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();
                //404 Si no existe
            }
            usuarioService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (se ejecuta correctamente y no devuelve cuerpo)
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }
    }

    //PUT actualiza usuario a traves del codigo
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable int codigo, @RequestBody Usuario usuario){
        try {
            if (!usuarioService.existePorCodigo(codigo)){
                //Verificar si existe antes de poder actualizar
                return ResponseEntity.notFound().build();
                //404 NOT FOUND
            }
            //Actualizamos el cliente pero esto puede lanzar una excepcion
            Usuario usuarioActualizado = usuarioService.actualizar(codigo, usuario);
            return ResponseEntity.ok(usuarioActualizado);
            //200 OK con el usuario ya actualizado
        } catch (IllegalArgumentException e) {
            //Error cuando los datos sean incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e){
            //Posiblemente cualquier otro error como: usuario no encontrado, etc.
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }


}
