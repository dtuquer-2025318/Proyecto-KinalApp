package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Producto;
import com.denistuquer.kinalapp.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/producto") // Esta es la base para la VISTA
public class ProductoViewController {

    @Autowired
    private ProductoRepository productoRepository;

    // Ver la lista completa
    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "producto/lista";
    }

    // Abrir formulario para uno nuevo
    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto/formulario";
    }

    // Abrir formulario para EDITAR (Aquí estaba tu error 404)
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            model.addAttribute("producto", producto);
            return "producto/formulario";
        }
        return "redirect:/producto/lista";
    }

    // Acción de ELIMINAR (Usamos GetMapping porque es un enlace <a>)
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id) {
        productoRepository.deleteById(id);
        return "redirect:/producto/lista";
    }

    // Guardar (POST)
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("producto") Producto producto) {
        productoRepository.save(producto);
        return "redirect:/producto/lista";
    }
}