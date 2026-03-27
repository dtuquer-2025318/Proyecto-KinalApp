package com.denistuquer.kinalapp.repository;

import com.denistuquer.kinalapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
