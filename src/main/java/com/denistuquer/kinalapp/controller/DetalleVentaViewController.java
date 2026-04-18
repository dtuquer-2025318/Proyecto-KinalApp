package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.DetalleVenta;
import com.denistuquer.kinalapp.entity.Producto;
import com.denistuquer.kinalapp.entity.Venta;
import com.denistuquer.kinalapp.service.IDetalleVentaService;
import com.denistuquer.kinalapp.service.IProductoService;
import com.denistuquer.kinalapp.service.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/detalleVenta")
public class DetalleVentaViewController {

    @Autowired
    private IDetalleVentaService detalleVentaService;

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IVentaService ventaService;

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("detalles", detalleVentaService.listarTodos());
        return "detalleVenta/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        DetalleVenta dv = new DetalleVenta();
        dv.setVenta(new Venta());    // Esto evita que venta sea null
        dv.setProducto(new Producto()); // Esto evita que producto sea null

        model.addAttribute("detalleVenta", dv);
        model.addAttribute("ventas", ventaService.listarTodos());
        model.addAttribute("productos", productoService.listarTodos());
        return "detalleVenta/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        DetalleVenta detalleVenta = detalleVentaService.buscarPorCodigo(id).orElse(null);

        if (detalleVenta == null) return "redirect:/detalleVenta/lista";

        if (detalleVenta.getVenta() == null) detalleVenta.setVenta(new Venta());
        if (detalleVenta.getProducto() == null) detalleVenta.setProducto(new Producto());

        model.addAttribute("detalleVenta", detalleVenta);
        model.addAttribute("productos", productoService.listarTodos());
        // CORREGIDO: ahora usa listarTodos()
        model.addAttribute("ventas", ventaService.listarTodos());
        return "detalleVenta/formulario";
    }

    @PostMapping("/guardar") // Debe coincidir con th:action="@{/detalleVenta/guardar}"
    public String guardar(@ModelAttribute("detalleVenta") DetalleVenta detalleVenta) {
        detalleVentaService.guardar(detalleVenta);
        return "redirect:/detalleVenta/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        detalleVentaService.eliminar(id);
        return "redirect:/detalleVenta/lista";
    }
}