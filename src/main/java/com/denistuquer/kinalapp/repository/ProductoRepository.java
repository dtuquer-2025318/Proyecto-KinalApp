package com.denistuquer.kinalapp.repository;

import com.denistuquer.kinalapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la entidad Producto
 * proporciona operaciones CRUD mediante JpaRepository
 */
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
