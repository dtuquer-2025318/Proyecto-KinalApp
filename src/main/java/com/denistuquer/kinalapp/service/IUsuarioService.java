package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    /*
     * Interfaz: Es un contrato que dice QUÉ métodos debe tener
     * cualquier servicio de Usuarios, No tiene
     * Implementación, solo la definición de los métodos
     **/

    //Metodo que devuelve una lista de todos los Usuarios
    List<Usuario> listarTodos();

    /*
     * List<Usuario> lo que hace es devolver una lista
     * de objetos de la entidad Usuarios
     **/


    //Metodo que guarda un Usuario en la BD
    Usuario guardar(Usuario usuario);
    //Parametros: Recibe un objeto Usuario con los datos a guardar

    //Optional - Contenedor que puede o no tener valor
    //evita el error de NullPointerException
    Optional<Usuario> buscarPorCodigo(int codigo);

    //List - Muestra una lista de usuario
    //que usuarios estan activos
    List<Usuario> listarActivos();


    //Metodo que actualiza un usuario
    Usuario actualizar(int codigo, Usuario usuario);
    /*
     * Parametros - codigo: CODIGO del usuario a actualizar
     * Usuario usuario: Objeto con los datos nuevos
     * Retorna un objeto de tipo Usuario ya actualizado
     **/

    /*
     * Metodo de tipo void para eliminar a un usuario
     * void: no retorna ningun valor o dato
     * Elimina: un Usuario por su Codigo
     **/
    void eliminar(int codigo);

    //Boolean - Retorna tru si existe y false si no existe
    boolean existePorCodigo (int codigo);
}
