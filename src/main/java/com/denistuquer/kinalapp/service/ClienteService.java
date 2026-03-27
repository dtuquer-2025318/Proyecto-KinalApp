package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.Cliente;
import com.denistuquer.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 * Anotacion que registra un Bean como  un Bean de Spring
 * Que la clase contiene la logica del negocio
 **/
@Service
/*
 * Por defecto todos los metodos de esta clase seran transaccionales
 * Una transaccion es que pude o no ocurrir algo
 **/
@Transactional
public class ClienteService implements  IClienteService{
    /*
     * private: Solo es accesible dentro de la misma clase
     * final: No puede cambiar, es constante
     * ClienteRepository: El repositorio para acceder a la BD
     * Inyeccion de Dependencia ya que Spring nos da el repositorio
     **/
    private final ClienteRepository clienteRepository;

    /*
     * Constructor: este se ejecuta al crear un objeto
     * Spring pasa el repositorio automaticamente (Inyeccion de Dependencia)
     * */
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        //Asignar el repositorio a nuestra variable de la clase
    }

    //Indica que se esta implementado un metodo de la interfaz
    @Override
    //Optimizar la consulta, solo lectura, para que no bloquee la BD
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
        //findAll() es un metodo de spring que hace el Select * from Clientes
        //este metodo de JpaRepository
    }



    @Override
    public Cliente guardar(Cliente cliente){
        /*
         * Metodo de guardar, crear un cliente
         * Acá es donde colocamos la logica  del negocio  Antes de guardar
         * Primero validamos el dato
         * */
//        validarCliente(cliente);
//        if (cliente.getEstado() == 0)
//            cliente.setEstado(1);
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        //Buscar un cliente por DPi
        return clienteRepository.findById(dpi);
        //Opcional nos evita el nullPointer
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarActivos() {
        List<Cliente> clientes = clienteRepository.findAll();
        //if/else para mostrar solo los clientes activos
        List<Cliente> activos = new java.util.ArrayList<>();
        for (Cliente cliente : clientes) {
            if (cliente.getEstado() == 1) {
                activos.add(cliente);
            } else {
            }
        }
        return activos;
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        //Metodo para actualizar un cliente
        if (!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El cliente no se encontro con el DPI "+dpi);
            //Si no existe se lanza una excepcion (error controlado)
        }
        cliente.setDPICliente(dpi);
        //Asegurarnos que el DPI del objeto coincida con el de la URL
        //Por seguridad usamos el DPI de la URL y no el que viene en el JSON
        validarCliente(cliente);

        return clienteRepository.save(cliente);
        /*
         * save() este no solo sirve para guardar sino tambien para actualizar Si el dat
         * Existe (dpi) entonces hace UPDATE pero si no existe hace un INSERT pero
         * antes verificamos si existe o no el registro
         **/
    }

    @Override
    public void eliminar(String dpi) {
        //Eliminar un cliente
        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("El cliente no se encontro con el DPI "+dpi);
        }
        clienteRepository.deleteById(dpi);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        //Verificar si existe un cliente
        return clienteRepository.existsById(dpi);
    }

    //Metodo privado(solo puede utilizarse dentro de la clase)
    private void validarCliente(Cliente cliente) {
        /**
         * Validaciones del negocio: Este metodo se hara privado porque
         * es algo interno del servicio
         **/
        if (cliente.getDPICliente() == null || cliente.getDPICliente().trim().isEmpty()) {
            //Si el DPI es null o está vacío despúes de quitar espacios
            //Lanza una excepción con un mensaje
            throw new IllegalArgumentException("El DPI es un dato obligatorio");
        }
        if (cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es un dato obligatorio");
        }
        if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es un dato obligatorio");

        }
    }


}