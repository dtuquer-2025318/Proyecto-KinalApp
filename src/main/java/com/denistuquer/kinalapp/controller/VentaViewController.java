package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Venta;
import com.denistuquer.kinalapp.repository.ClienteRepository;
import com.denistuquer.kinalapp.repository.UsuarioRepository;
import com.denistuquer.kinalapp.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/venta")
public class VentaViewController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Método @GetMapping para buscar ventas por medio del codigo de venta
    @GetMapping("/lista")
    public String listar(@RequestParam(name = "codigoBusqueda", required = false) String codigoBusqueda, Model model) {
        if (codigoBusqueda != null && !codigoBusqueda.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(codigoBusqueda);
                List<Venta> lista = ventaService.buscarPorCodigo(id)
                        .map(List::of)
                        .orElse(java.util.Collections.emptyList());
                model.addAttribute("ventas", lista);
            }catch(NumberFormatException e){
                model.addAttribute("ventas", java.util.Collections.emptyList());
            }
            model.addAttribute("codigoBusqueda", codigoBusqueda);
        } else {
            model.addAttribute("ventas", ventaService.listarTodos());
        }
        return "venta/lista";
    }

    //Método @GetMapping para agregar una nueva venta
    @GetMapping("/nuevo")
    public String formulario(Model model) {
        Venta venta = new Venta();

        venta.setCliente(new com.denistuquer.kinalapp.entity.Cliente());
        venta.setUsuario(new com.denistuquer.kinalapp.entity.Usuario());

        venta.setTotal(java.math.BigDecimal.ZERO);

        venta.setEstado(1);
        venta.setFechaVenta(java.time.LocalDate.now());

        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "venta/formulario";
    }

    //Método para abrir formulario para editar una venta de la lista de ventas
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, Model model) {
        Optional<Venta> venta = ventaService.buscarPorCodigo(id);
        if (venta.isPresent()) {
            model.addAttribute("usuario", venta.get());
            return "venta/formulario";
        }
        return "redirect:/venta/lista";
    }

    //Método @PostMapping para guardar una nueva venta de la lista de ventas
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("venta") Venta venta) {
        ventaService.crearVenta(venta, venta.getDetalles());
        return "redirect:/venta/lista";
    }

    //Método @GetMapping para eliminar una venta de la lista de ventas
    @GetMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable("id") Integer id) {
        ventaService.eliminarVenta(id);
        return "redirect:/venta/lista";
    }
}