package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.Usuario;
import com.denistuquer.kinalapp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        validarUsuario(usuario);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(int codigo, Usuario usuario) {
        Usuario existente = usuarioRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(existente.getPassword())) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        usuario.setCodigoUsuario(codigo);
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() { return usuarioRepository.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(int codigo) { return usuarioRepository.findById(codigo); }

    @Override
    public void eliminar(int codigo) { usuarioRepository.deleteById(codigo); }

    @Override
    public boolean existePorCodigo(int codigo) { return usuarioRepository.existsById(codigo); }

    @Override
    public List<Usuario> listarActivos() {
        return usuarioRepository.findAll().stream().filter(u -> u.getEstado() == 1).toList();
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().isBlank())
            throw new IllegalArgumentException("Username es obligatorio");
        if (usuario.getPassword() == null || usuario.getPassword().isBlank())
            throw new IllegalArgumentException("Password es obligatorio");
    }
}