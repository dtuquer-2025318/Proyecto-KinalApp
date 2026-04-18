package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.Cliente;
import com.denistuquer.kinalapp.entity.Usuario;
import com.denistuquer.kinalapp.entity.Venta;
import com.denistuquer.kinalapp.repository.ProductoRepository;
import com.denistuquer.kinalapp.repository.VentaRepository;
import com.denistuquer.kinalapp.repository.ClienteRepository;
import com.denistuquer.kinalapp.repository.UsuarioRepository;
import com.denistuquer.kinalapp.service.IVentaService;
import com.denistuquer.kinalapp.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/venta")
public class VentaViewController {

    @Autowired
    private IVentaService ventaService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // 1. LISTAR TODAS LAS VENTAS
    @GetMapping("/lista")
    public String listarVentas(Model model) {
        model.addAttribute("ventas", ventaService.listarTodos());
        return "venta/lista"; // Asegúrate de tener templates/venta/lista.html
    }

    // 2. FORMULARIO PARA NUEVA VENTA
    @GetMapping("/nuevo")
    public String formulario(Model model) {
        Venta venta = new Venta();

        // Inicializamos los objetos para evitar errores de puntero nulo
        venta.setCliente(new com.denistuquer.kinalapp.entity.Cliente());
        venta.setUsuario(new com.denistuquer.kinalapp.entity.Usuario());

        // IMPORTANTE: Inicializar el total en 0.00
        venta.setTotal(java.math.BigDecimal.ZERO);

        venta.setEstado(1);
        venta.setFechaVenta(java.time.LocalDate.now());

        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "venta/formulario";
    }

    // 3. GUARDAR LA VENTA
    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute("venta") Venta venta) {
        // Aquí llamaríamos al método crearVenta de tu Service
        // Nota: Los detalles normalmente se capturan mediante un script en el frontend
        // y se envían en la lista de la entidad Venta.
        ventaService.crearVenta(venta, venta.getDetalles());
        return "redirect:/venta/lista";
    }

    // 4. ELIMINAR VENTA
    @GetMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable("id") Integer id) {
        ventaService.eliminarVenta(id);
        return "redirect:/venta/lista";
    }

    // 5. BUSCAR POR DPI (Historial específico)
    @GetMapping("/buscar")
    public String buscarPorDpi(@RequestParam("dpi") String dpi, Model model) {
        model.addAttribute("ventas", ventaService.buscarPorDpi(dpi));
        return "venta/lista";
    }
}