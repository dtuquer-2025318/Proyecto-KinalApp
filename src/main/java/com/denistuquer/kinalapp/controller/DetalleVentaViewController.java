package com.denistuquer.kinalapp.controller;

import com.denistuquer.kinalapp.entity.DetalleVenta;
import com.denistuquer.kinalapp.entity.Producto;
import com.denistuquer.kinalapp.entity.Venta;
import com.denistuquer.kinalapp.service.DetalleVentaService;
import com.denistuquer.kinalapp.service.IProductoService;
import com.denistuquer.kinalapp.service.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/detalleVenta")
public class DetalleVentaViewController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private IProductoService productoService;

    @Autowired
    private IVentaService ventaService;

    @GetMapping("/lista")
    public String listar(@RequestParam(name = "codigoBusqueda", required = false) String codigoBusqueda, Model model) {
        if (codigoBusqueda != null && !codigoBusqueda.trim().isEmpty()) {
            try{
                int id = Integer.parseInt(codigoBusqueda);
                List<DetalleVenta> lista = detalleVentaService.buscarPorCodigo(id)
                        .map(List::of)
                        .orElse(java.util.Collections.emptyList());
                model.addAttribute("detalleVentas", lista);
            }catch(NumberFormatException e) {
                model.addAttribute("detalleVentas", java.util.Collections.emptyList());
            }
            model.addAttribute("codigoBusqueda", codigoBusqueda);
        } else {
            model.addAttribute("detalleVentas", detalleVentaService.listarTodos());
        }
        return "detalleVenta/lista";
    }

    //Método @GetMapping para abrir formulario para uno nuevo detalleventa
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        DetalleVenta dv = new DetalleVenta();
        dv.setVenta(new Venta());
        dv.setProducto(new Producto());

        model.addAttribute("detalleVenta", dv);
        model.addAttribute("ventas", ventaService.listarTodos());
        model.addAttribute("productos", productoService.listarTodos());
        return "detalleVenta/formulario";
    }

    //Método @GetMapping para formulario para editar un detalleventa de la lista de detalleventas
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        DetalleVenta detalleVenta = detalleVentaService.buscarPorCodigo(id).orElse(null);

        if (detalleVenta == null) return "redirect:/detalleVenta/lista";

        if (detalleVenta.getVenta() == null) detalleVenta.setVenta(new Venta());
        if (detalleVenta.getProducto() == null) detalleVenta.setProducto(new Producto());

        model.addAttribute("detalleVenta", detalleVenta);
        model.addAttribute("productos", productoService.listarTodos());

        model.addAttribute("ventas", ventaService.listarTodos());
        return "detalleVenta/formulario";
    }

    //Método @PostMapping para formulario para guardar un detalleventa de la lista de detalleventas
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("detalleVenta") DetalleVenta detalleVenta) {
        detalleVentaService.guardar(detalleVenta);
        return "redirect:/detalleVenta/lista";
    }

    //Método @GetMapping para formulario para eliminar un detalleventa de la lista de detalleventas
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        detalleVentaService.eliminar(id);
        return "redirect:/detalleVenta/lista";
    }
}