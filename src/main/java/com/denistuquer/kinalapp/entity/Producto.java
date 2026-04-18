package com.denistuquer.kinalapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")

public class Producto {
    @Id
    @Column(name = "codigo_producto")
    private Integer codigoProducto;
    @Column
    private String nombreProducto;
    @Column(precision = 10, scale = 2)
    private BigDecimal precio;
    @Column
    private int stock;
    @Column
    private int estado;

    //Constructor vacio
    public Producto() {
    }

    //Constructor lleno
    public Producto(Integer codigoProducto, String nombreProducto, BigDecimal precio, int stock, int estado) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
    }

    //Getters and Setters

    public Integer getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Integer codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombre_Producto() {
        return nombreProducto;
    }

    public void setNombre_Producto(String nombre_Producto) {
        this.nombreProducto = nombre_Producto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
