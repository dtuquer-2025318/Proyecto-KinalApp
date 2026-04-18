package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.DetalleVenta;
import com.denistuquer.kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Anotacion que registra un Bean como  un Bean de Spring
 * Que la clase contiene la logica del negocio
 */
@Service
/**
 * Por defecto todos los metodos de esta clase seran transaccionales
 * Una transaccion es que pude o no ocurrir algo
 */
@Transactional
public class DetalleVentaService implements IDetalleVentaService{
    /*
     * private: Solo es accesible dentro de la misma clase
     * final: No puede cambiar, es constante
     * DetalleVentaRepository: El repositorio para acceder a la BD
     * Inyeccion de Dependencia ya que Spring nos da el repositorio
     **/
    private final DetalleVentaRepository detalleVentaRepository;

    /**
     * Constructor: este se ejecuta al crear un objeto
     * Spring pasa el repositorio automaticamente (Inyeccion de Dependencia)
     */
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
        //Asignar el repositorio a nuestra variable de la clase
    }

    //Indica que se esta implementado un metodo de la interfaz
    @Override
    //Optimizar la consulta, solo lectura, para que no bloquee la BD
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        validarDetalleVenta(detalleVenta);
        detalleVenta.setSubtotal(
                detalleVenta.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(detalleVenta.getCantidad())));
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorCodigo(int codigo) {
        //Buscar un DetalleVenta por codigo
        return detalleVentaRepository.findById(codigo);
    }

    @Override
    public DetalleVenta actualizar(int codigo, DetalleVenta detalleVenta) {
        //Metodo para actualizar un DetalleVenta
        if (!detalleVentaRepository.existsById(codigo)) {
            throw new RuntimeException("DetalleVenta no encontrado con codigo: " + codigo);
        }
        detalleVenta.setCodigoDetalleVenta(codigo);
        validarDetalleVenta(detalleVenta);

        detalleVenta.setSubtotal(
                detalleVenta.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(detalleVenta.getCantidad())));
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void eliminar(int codigo) {
        //Eliminar un Detalle de Venta
        if (!detalleVentaRepository.existsById(codigo)) {
            throw new RuntimeException("DetalleVenta no encontrado con codigo: " + codigo);
        }
        detalleVentaRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(int codigo) {
        //Verificar si existe un Detalle de la venta
        return detalleVentaRepository.existsById(codigo);
    }

    //Metodo privado(solo puede utilizarse dentro de la clase)
    private void validarDetalleVenta(DetalleVenta detalleVenta) {
        /**
         * Validaciones del negocio: Este metodo se hara privado porque
         * es algo interno del servicio
         **/
        if (detalleVenta.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (detalleVenta.getPrecioUnitario() == null ||
                detalleVenta.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }
        if (detalleVenta.getProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        if (detalleVenta.getVenta() == null) {
            throw new IllegalArgumentException("La venta es obligatoria");
        }
    }
}
