package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.DetalleVenta;

import java.util.List;
import java.util.Optional;

public interface IDetalleVenta {

    /**
     * Interfaz: Es un contrato que dice QUÉ métodos debe tener
     * cualquier servicio de DetalleVentas, No tienes
     * Implementación, solo la definición de los métodos
    */

    //Metodo que devuelve una lista de todos los Detalle de Ventas
    List<DetalleVenta> listarTodos();

    /**
     * List<DetalleVenta> lo que hace es devolver una lista
     * de objetos de la  entidad DetalleVenta
     */

    //Metodo que guarda un DetalleVenta en la BD
    DetalleVenta guardar(DetalleVenta detalleVenta);
    //Parametros: Recibe un objeto DetalleVenta con los datos a guardar

    //Optional - Contenedor que puede o no tener valor
    //evita el error de NullPointerException
    Optional<DetalleVenta> buscarPorCodigo(int codigo);

    //Actualiza un detalle de venta
    DetalleVenta actualizar(int codigo, DetalleVenta detalleVenta);
    /**
     * Parametros - CODIGO: CODIGO del DetalleVenta a actualizar
     * DetalleVenta detalleventa: Objeto con los datos nuevos
     * Retorna un objeto de tipo Cliente ya actualizadoo
     */

    /**
     * Metodo de tipo void para eliminar a un DetalleVenta
     * void: no retorna ningun valor o dato
     * Elimina: un DetalleVentas por su CODIGO
     */
    void eliminar(int codigo);

    //boolean - Retorna tru si existe y false si no existe
    boolean existePorCodigo(int codigo);
}
