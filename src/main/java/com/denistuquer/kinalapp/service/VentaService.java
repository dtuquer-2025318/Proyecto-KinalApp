package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.DetalleVenta;
import com.denistuquer.kinalapp.entity.Venta;
import com.denistuquer.kinalapp.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService implements IVentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Venta> listarTodos() {
        List<Venta> lista = ventaRepository.findAll();
        System.out.println("Ventas encontradas: " + lista.size());
        return lista;
    }

    // Método que requiere el VentaViewController para procesar nuevas ventas
    @Override
    @Transactional
    public void crearVenta(Venta venta, List<DetalleVenta> detalles) {
        if (detalles != null && !detalles.isEmpty()) {
            BigDecimal total = detalles.stream()
                    .map(d -> d.getPrecioUnitario().multiply(new BigDecimal(d.getCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            venta.setTotal(total);
            venta.setDetalles(detalles);
            detalles.forEach(d -> d.setVenta(venta));
        }
        ventaRepository.save(venta);
    }

    @Override
    @Transactional
    public Venta guardar(Venta venta) {
        if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
            // Solo si hay detalles, calcula el total automáticamente
            BigDecimal total = venta.getDetalles().stream()
                    .map(d -> d.getPrecioUnitario().multiply(new BigDecimal(d.getCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            venta.setTotal(total);
        }
        // Si no hay detalles, guardará el total que venga del formulario (si quitaste el readonly)
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> buscarPorCodigo(int codigo) {
        return ventaRepository.findById(codigo);
    }

    @Override
    @Transactional
    public Venta actualizar(int codigo, Venta venta) {
        if (ventaRepository.existsById(codigo)) {
            venta.setCodigoVenta(codigo);
            return guardar(venta); // Reutilizamos la lógica de guardado y cálculo
        }
        return null;
    }

    @Override
    public void eliminar(int codigoVenta) {
        ventaRepository.deleteById(codigoVenta);
    }

    @Override
    public boolean existePorCodigo(int codigoVenta) {
        return ventaRepository.existsById(codigoVenta);
    }



    // Método para el funcionamiento del buscador por DPI en la vista
    @Override
    public List<Venta> buscarPorDpi(String dpi) {
        return ventaRepository.findByClienteDPICliente(dpi);
    }

    // Método para compatibilidad con el controlador (si usa este nombre)
    @Override
    public void eliminarVenta(Integer id) {
        ventaRepository.deleteById(id);
    }
}
