package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {

    /*
     * Interfaz: Es un contrato que dice QUÉ métodos debe tener
     * cualquier servicio de Clientes, No tienes
     * Implementación, solo la definición de los métodos
     **/

    //Metodo que devuelve una lista de todos los Clientes
    List<Cliente> listarTodos();

    /*
     * List<Cliente> lo que hace es devolver una lista
     * de objetos de la  entidad Clientes
     **/

    //Metodo que guarda un Cliente en la BD
    Cliente guardar(Cliente cliente);
    //Parametros: Recibe un objeto Cliente con los datos a
    //guardar

    //Optional - Contenedor que puede o no tener valor
    //evita el error de NullPointerException
    Optional<Cliente> buscarPorDPI(String dpi);

    List<Cliente> listarActivos();


    //Metodo que actualiza un Cliente
    Cliente actualizar(String dpi, Cliente cliente);
    /*
     * Parametros - dpi: DPI del cliente a actualizar
     * Cliente cliente: Objeto con los datos nuevos
     * Retorna un objeto de tipo Cliente ya actualizadoo
     **/

    /*
     * Metodo de tipo void para eliminar a un Cliente
     * void: no retorna ningun valor o dato
     * Elimina: un Cliente por su DPI
     **/
    void eliminar(String dpi);

    //boolean - Retorna tru si existe y false si no existe
    boolean existePorDPI (String dpi);

}

