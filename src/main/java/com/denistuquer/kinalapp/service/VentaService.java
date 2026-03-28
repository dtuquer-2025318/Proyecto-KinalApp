package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.Venta;
import com.denistuquer.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Anotación que registra un Bean como un Bean de Spring
 * Que la clase contiene la lógica del negocio
 */
@Service
/**
 * Por defecto todos los métodos de esta clase seran transccionales
 * Una transcción es que puede o no ocurrir algo
 */
@Transactional
public class VentaService implements IVentaService {
    /**
     * private: Solo es accesible dentro de la misma clase
     * final: No puede cambiar, es constante
     * VentaRepository: El repositorio para acceder a la BD
     * Inyección de Dependencia ya que Spring nos da el repositorio
     */

    private final VentaRepository ventaRepository;

    /**
     * Constructor del servicio de ventas.
     * Spring pasa el repositorio automaticamente (Inyección de Dependencia)
     *
     * @param ventaRepository repositorio de acceso a datos de Venta
     */
    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
        //Asignar el repositorio a nuestra variable de la clase
    }

    /**
     * Obtiene la lista de todas las ventas registradas.
     *
     * @return lista de ventas
     */
    //Indica que se esta implementando un método de la interfaz
    @Override
    //Optimiza la consulta, solo lectura, para que no bloquee la BD
    @Transactional(readOnly = true)
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
        //findAll() es un método de Spring que hace el Select * from Ventas
        //este método de JpaRepository
    }

    /**
     * Metodo que guarda una nueva venta en el sistema.
     *
     * @param venta objeto Venta a guardar
     * @return venta guarda en la base de datos
     */
    @Override
    public Venta guardar(Venta venta) {
        validarVenta(venta);
        return ventaRepository.save(venta);
    }

    /**
     * Busca una venta por su código identificador.
     *
     * @param codigo identificador único de la venta
     * @return Optional con la venta encontrada, o vacío si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorCodigo(int codigo) {
        return ventaRepository.findById(codigo);
        //Opcional nos evitá el NullPointer
    }

    /**
     * Método que obtiene la lista de ventas activas.
     *
     * @return listas de ventas activa  s
     */
    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarActivos() {
        List<Venta> ventas = ventaRepository.findAll();
        //if para mostrar solo los clientes activos
        List<Venta> activos = new java.util.ArrayList<>();
        for (Venta venta : ventas){
            if (venta.getEstado() == 1) {
                activos.add(venta);
            }
        }
        return activos;
    }

    /**
     * Método que actualiza una venta existente.
     *
     * @param codigo identificador único de la venta
     * @param venta datos actualizados de la venta
     * @return venta actualizada
     */
    @Override
    public Venta actualizar(int codigo, Venta venta) {
        if (!ventaRepository.existsById(codigo)){
            throw new RuntimeException("La venta no se encontro con el CODIGO " + codigo );
        }
        venta.setCodigoVenta(codigo);
        //Aseguramos que el CODIGO del objeto coincida con el de la URL
        //Por seguridad usamos CODIGO de la URL y no el que viene en el JSON
        validarVenta(venta);

        return ventaRepository.save(venta);
        /**
         * save() este no solo sirve para guardar si no también para actualizar, si el dato
         * Existe (codigo) entonces hace UPDATE pero si no existe hace un INSERT pero
         * antes verificamos si existe o no el registro
         */
    }

    /**
     * Método para eliminar una venta existente con el código.
     *
     * Verifica si la venta existe antes de proceder con la eliminación.
     * @param codigo identificador único de la venta a eliminar
     */
    @Override
    public void eliminar(int codigo) {
        if (!ventaRepository.existsById(codigo)) {
            throw new RuntimeException("La venta no se encontro con el CODIGO " + codigo);
        }
        ventaRepository.deleteById(codigo);
    }

    /**
     * Método para verifica si existe una venta por medio del código.
     *
     * @param codigo identificador único de la venta
     * @return true si la venta existe, false en caso contrario
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(int codigo) {
        return ventaRepository.existsById(codigo);
    }

    /**
     * Metodo privado(realiza validaciones de negocio sobre el objeto Venta.
     *
     * @param venta objeto Venta a validar
     */
    private void validarVenta(Venta venta) {
       //Valida que la fecha de venta no sea nula
        if (venta.getFechaVenta() == null) {
            throw new IllegalArgumentException("La fecha de venta es obligatoria");
        }
        //Valida que el total no sea nulo
        if (venta.getTotal() == null) {
            throw new IllegalArgumentException("El total es obligatorio");
        }
        //Valida que existe un cliente asociado
        if (venta.getCliente() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }
        //Valida que existe un usuario asociado
        if (venta.getUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
    }
}
