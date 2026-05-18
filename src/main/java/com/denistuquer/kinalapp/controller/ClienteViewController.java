package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Cliente;
import com.denistuquer.kinalapp.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteViewController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/lista")
    public String listar(@RequestParam(name = "dpiBusqueda", required = false) String dpiBusqueda, Model model) {
        if (dpiBusqueda != null && !dpiBusqueda.trim().isEmpty()) {
            List<Cliente> lista = clienteService.buscarPorDPI(dpiBusqueda)
                    .map(List::of)
                    .orElse(java.util.Collections.emptyList());
            model.addAttribute("clientes", lista);
            model.addAttribute("dpiBusqueda", dpiBusqueda);
        } else {
            model.addAttribute("clientes", clienteService.listarTodos());
        }
        return "cliente/lista";
    }

    @GetMapping("/nuevo")
    public String formulario(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/formulario";
    }

    //Método @GetMapping para editar un cliente
    @GetMapping("/editar/{dpi}")
    public String editar(@PathVariable String dpi, Model model) {
        // Buscamos el cliente y lo mandamos al formulario
        clienteService.buscarPorDPI(dpi).ifPresent(c -> model.addAttribute("cliente", c));
        return "cliente/formulario";
    }

    //Método @PostMapping para guarda un cliente
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/cliente/lista";
    }

    //Método @GetMapping para eliminar un cliente
    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi) {
        clienteService.eliminar(dpi);
        return "redirect:/cliente/lista";
    }
}