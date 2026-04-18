package com.denistuquer.kinalapp.repository;

import com.denistuquer.kinalapp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findByClienteDPICliente(String dpi);
}
