package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.Usuario;
import com.denistuquer.kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 * Anotacion que registra un Bean como un Bean de Spring
 * Que la clase contiene la logica del negocio
 **/
@Service
/*
 * Por defecto todos los metodos de esta clase seran transaccionales
 * Una transaccion es que puede o no ocurrir algo
 **/
@Transactional
public class UsuarioService implements IUsuarioService {
    /*
     * private: Solo es accesible dentro de la misma clase
     * final: No puede cambiar, es constante
     * ClienteRepository: El repositorio para acceder a la BD
     * Inyeccion de Dependencia ya que Spring nos da el repositorio
     **/
    private final UsuarioRepository usuarioRepository;

    /*
     * Constructor: este se ejecuta al crear un objeto
     * Spring pasa el repositorio automaticamente (Inyección de Dependencia)
     **/
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        //Asignar el repositorio a nuestra variable de la clase
    }

    //Indica que se esta implementado un metodo de la interfaz
    @Override
    //Optimizar la consulta, solo lectura, para que no bloquee la BD
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
        //findAll() es un metodo de spring que hace el select * from Usuarios
        //este metodo de JpaRepository
    }

    @Override
    public Usuario guardar(Usuario usuario){
        /*
         * Metodo de guardar, crear un usuario
         * Aca es donde colocamos la logica del negocio. Antes de guardar
         * Primero validamos el dato
         **/
        validarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(int codigo){
        //Buscar un usuario por Codigo
        return usuarioRepository.findById(codigo);
        //Opcional nos evita el nullPointer
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarActivos(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        //if/else para mostrar solo los clientes activos
        List<Usuario> activos = new java.util.ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getEstado() == 1){
                activos .add(usuario);
            }
        }
        return activos;
    }

    @Override
    public Usuario actualizar(int codigo, Usuario usuario){
        //Metodo para actualizar un cliente
        if (!usuarioRepository.existsById(codigo)){
            throw new RuntimeException("El usuario no se encontro con el CODIGO "+codigo);
            //Si no existe se lanza una excepcion (error controlado)
        }
        usuario.setCodigoUsuario(codigo);
        //Asegurarnos que el DPI del objeto coincida con el de la URL
        //Por seguridad usamos el DPI del URL y no el que viene en el JSON
        validarUsuario(usuario);

        return usuarioRepository.save(usuario);
        /*
         * save() este no solo sirve para guardar sino tambien para actualizar Si el dato
         * existe (codigo) entonces hace UPDATE pero si no existe hace un INSERT pero
         * antes verificamos si existe o no el registro
         **/
    }

    @Override
    public void eliminar(int codigo){
        //Eliminar un usuario
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("El usuario no se encontro con el CODIGO "+codigo);
        }
        usuarioRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(int codigo) {
        //Verificar si existe un usuario
        return usuarioRepository.existsById(codigo);
    }

    //Metodo privado(solo puede utilizarse dentro de la clase)
    private void validarUsuario(Usuario usuario) {
        /*
         * Validaciones del negocio: Este metodo se hara privado porque
         * es algo interno del servicio
         **/
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            //Si es username es null o esta vacio despues de quitar espacios
            //Lanza una excepcion con un mensaje
            throw new IllegalArgumentException("El UserName es un dato obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("El password es obligatorio");
        }

    }

}
