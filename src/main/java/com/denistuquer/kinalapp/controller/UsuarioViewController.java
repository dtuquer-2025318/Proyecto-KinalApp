package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Usuario;
import com.denistuquer.kinalapp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioViewController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/lista")
    public String listar(@RequestParam(name = "codigoBusqueda", required = false) String codigoBusqueda, Model model) {
        if (codigoBusqueda != null && !codigoBusqueda.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(codigoBusqueda);
                List<Usuario> lista = usuarioService.buscarPorCodigo(id)
                        .map(List::of)
                        .orElse(java.util.Collections.emptyList());
                model.addAttribute("usuarios", lista);
            }catch(NumberFormatException e) {
                model.addAttribute("usuarios", java.util.Collections.emptyList());
            }
            model.addAttribute("codigoBusqueda", codigoBusqueda);
        } else {
            model.addAttribute("usuarios", usuarioService.listarTodos());
        }
        return "usuario/lista";
    }

    //Método @GetMapping para abrir formulario para uno nuevo usuario
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    //Método para abrir formulario para editar un usuario de la lista de usuarios
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, Model model) {
        Optional<Usuario> usuario = usuarioService.buscarPorCodigo(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "registro";
        }
        return "redirect:/usuario/lista";
    }

    //Método @PostMapping para guardar un usuario
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/usuario/lista";
    }

    //Método @GetMapping para eliminar un usuario de la lista de usuarios
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id) {
        usuarioService.eliminar(id);
        return "redirect:/usuario/lista";
    }
}