package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Producto;
import com.denistuquer.kinalapp.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/producto")
public class ProductoViewController {

    @Autowired
    private ProductoService productoService;

    //Método @GetMapping para ver la lista completa de producto
    @GetMapping("/lista")
    public String listar(@RequestParam(name = "codigoBusqueda", required = false) String codigoBusqueda, Model model) {
        if (codigoBusqueda != null && !codigoBusqueda.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(codigoBusqueda);
                List<Producto> lista = productoService.buscarPorCodigo(id)
                        .map(List::of)
                        .orElse(java.util.Collections.emptyList());
                model.addAttribute("productos", lista);
            }catch(NumberFormatException e){
                model.addAttribute("productos", java.util.Collections.emptyList());
            }
            model.addAttribute("codigoBusqueda", codigoBusqueda);
        }else {
            model.addAttribute("productos", productoService.listarTodos());
    }
        return "producto/lista";
    }

    //Método @GetMapping para abrir formulario para uno nuevo producto
    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto/formulario";
    }

    //Método para abrir formulario para editar un producto de la lista de productos
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, Model model) {
        Optional<Producto> producto = productoService.buscarPorCodigo(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            return "producto/formulario";
        }
        return "redirect:/producto/lista";
    }

    //Método @PostMapping para guardar un producto
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("producto") Producto producto) {
        productoService.guardar(producto);
        return "redirect:/producto/lista";
    }

    //Método @GetMapping para eliminar un producto de la lista de productos
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id) {
        productoService.eliminar(id);
        return "redirect:/producto/lista";
    }
}