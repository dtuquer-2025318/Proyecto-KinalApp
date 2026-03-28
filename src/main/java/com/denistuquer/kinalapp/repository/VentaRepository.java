package com.denistuquer.kinalapp.repository;

import com.denistuquer.kinalapp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad Venta.
 *
 * Gracias a JpaRepository, ya incluye métodos para guardar, buscar,
 * actualizar y eliminar registros sin necesidad de implementarlos nanualmente.
 */
public interface VentaRepository extends JpaRepository<Venta, Integer> {
}
