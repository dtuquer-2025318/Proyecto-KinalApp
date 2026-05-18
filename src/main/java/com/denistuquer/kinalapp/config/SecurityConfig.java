package com.denistuquer.kinalapp.config;

import com.denistuquer.kinalapp.repository.UsuarioRepository;
import com.denistuquer.kinalapp.entity.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**","/login").permitAll()

                        .requestMatchers("/usuario/nuevo", "/usuario/guardar").permitAll()

                        .requestMatchers("/usuario/editar/**", "/usuario/eliminar/**", "/usuario/actualizar/**").hasRole("ADMIN")
                        .requestMatchers("/cliente/editar/**", "/cliente/eliminar/**", "/cliente/actualizar/**").hasRole("ADMIN")
                        .requestMatchers("/producto/editar/**", "/producto/eliminar/**", "/producto/actualizar/**").hasRole("ADMIN")
                        .requestMatchers("/venta/editar/**", "/venta/eliminar/**", "/venta/actualizar/**").hasRole("ADMIN")
                        .requestMatchers("/detalleVenta/eliminar/**", "/detalleVenta/editar/**", "/detalleVenta/actualizar/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/menu", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if ("admin".equals(username)) {
                return User.builder()
                        .username("admin")
                        .password("admin123")
                        .authorities("ROLE_ADMIN")
                        .build();
            }

            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

            Usuario u = usuarioOpt
                    .filter(user -> user.getEstado() == 1)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

            return User.builder()
                    .username(u.getUsername())
                    .password(u.getPassword())
                    .authorities(u.getRol())
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}