package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {

    /*
     * Interfaz: Es un contrato que dice QUÉ métodos debe tener
     * cualquier servicio de Productos, No tiene
     * Implementación, solo la definición de los métodos
     **/

    //Metodo que devuelve una lista de todos los Productos
    List<Producto> listarTodos();

    /*
     * List<Producto> lo que hace es devolver una lista
     * de objetos de la entidad Productos
     **/

    //Metodo que guarda un Producto en la BD
    Producto guardar(Producto producto);
    //Parametros: Recibe un objeto Producto con los datos a guardar

    //Optional - Contenedor que puede o no tener valor
    //evita el error de NullPointerException
    Optional<Producto> buscarPorCodigo(int codigo);

    //Lis<Producto> lo que hace es devolver una lista
    //Metodo que muestra una lista de activos
    List<Producto> listarActivos();

    //Metodo que actualiza un Producto
    Producto actualizar(int codigo, Producto producto);
    /*
     * Parametros - Codigo: CODIGO del producto a actualizar
     * Producto producto: Objeto con los datos nuevos
     * Retorna un objeto de tipo Producto ya actualizado
     **/

    /*
     * Metodo de tipo void para eliminar a un Producto
     * Void: no retorna ningun valor o dato
     * Elimina: un Producto por su CODIGO
     **/
    void eliminar(int codigo);

    //Boolean - Retorna tru si existe y false si no existe
    boolean existePorCodigo(int codigo);
}
