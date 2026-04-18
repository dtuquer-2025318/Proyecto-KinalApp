package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Cliente;
import com.denistuquer.kinalapp.repository.ClienteRepository;
import com.denistuquer.kinalapp.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
public class ClienteViewController {

    @Autowired
    private ClienteService clienteService;

    // Listar clientes
    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "cliente/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/formulario";
    }

    // Guardar el cliente y regresar a la lista
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("cliente") Cliente cliente) {
        // Validación: Si es un cliente nuevo, le ponemos estado 1 (Activo)
//            if (cliente.getEstado() == 0) {
//                cliente.setEstado(1);
//            }
        clienteService.guardar(cliente);
        return "redirect:/cliente/lista";
    }

    @GetMapping("/editar/{dpi}")
    public String editar(@PathVariable String dpi, Model model) {
        // Buscamos el cliente y lo mandamos al formulario
        clienteService.buscarPorDPI(dpi).ifPresent(c -> model.addAttribute("cliente", c));
        return "cliente/formulario";
    }

    // Eliminar
    @GetMapping("/eliminar/{dpi}")
    public String eliminar(@PathVariable String dpi) {
        clienteService.eliminar(dpi);
        return "redirect:/cliente/lista";
    }
}