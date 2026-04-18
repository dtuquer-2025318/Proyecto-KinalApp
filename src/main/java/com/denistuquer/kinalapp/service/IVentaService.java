package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.DetalleVenta;
import com.denistuquer.kinalapp.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IVentaService {

    /**
     * Interfaz: Es un contrato que dice QUÉ métodos debe tener
     * cualquier servicio de Ventas, No tiene
     * Implementación, solo la definición de los métodos
     */

    //Metodo que devuelve una lista de todos los Ventas


    /**
     * List<Venta> lo que hace es devolver una lista
     * de objetos de la entidad Ventas
     */

    //Metodo que guarda un Venta en la BD
    Venta guardar(Venta venta);
    //Parametros: Recibe un objeto Venta con los datos o guardar

    //Optional - Contenedor que puede o no tener valor
    //Evita el error de NullPointerException
    Optional<Venta> buscarPorCodigo(int codigoVenta);

    /**
     * List<Venta> lo que hace es devolver una lista
     * de objetos de la entidad Venta
     */
    List<Venta> listarTodos();

    //Metodo que actualiza una Venta
    Venta actualizar(int codigoVenta, Venta venta);
    /**
     * Parametros - codigo: CODIGO de la venta a actualizar
     * Codigo codigo: Objeto con los datos nuevos
     * Retorna un objeto de tipo Venta ya actualizado
     */

    /**
     * Metood que tipo de void para eliminar una Venta
     * Void: no retorna ningún valor o dato
     * Elimina: un objeto de tipo Venta ya actualizado
     */
    void eliminar(int codigoVenta);

    //Boolean - Retorna tru si existe y false si no existe
    boolean existePorCodigo(int codigoVenta);

    // Métodos que el VentaViewController necesita para no marcar error en rojo:
    void crearVenta(Venta venta, List<DetalleVenta> detalles);
    void eliminarVenta(Integer id);
    List<Venta> buscarPorDpi(String dpi);
}