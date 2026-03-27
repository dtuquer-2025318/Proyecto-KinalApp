package com.denistuquer.kinalapp.service;

import com.denistuquer.kinalapp.entity.Producto;
import com.denistuquer.kinalapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
* Anotación que registra un Bean como un Bean de Spring
* Que la clase contiene la lógica del negocio
**/
@Service
/**
* Por defecto todos los metodos de esta clase seran transaccionales
* Una transacción es que puede o no ocurrir algo
*/
@Transactional
public class ProductoService implements IProductoService{
    /**
    * Private: Solo es accesible dentro de la misma clase
    * Final: No puede cambiar, es constante
    * ProductoRepository: El repositorio para acceder a la BD
    * Inyección de Dependencia ya que Spring nos da el Repositorio
    */
    private final ProductoRepository productoRepository;

    /**
     * Constructor: Este se ejecuta al crear un objeto
     * Spring pasa el repositorio autómaticamente (Inyección de Dependencia)
     * @param productoRepository repositorio encargado de las operaciones
     *                           de persistencia de la entidad Producto
     */
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
        //Asignar el repositorio a nuestra variable de la clase
    }

    /**
     * Este método realiza una consulta de solo lectura a la base de datos
     * para recuperar todos los productos.
     * @return lista de productos
     */
    //Indica que se esta implementado un método de la interfez
    @Override
    //Optimizar la consulta, solo lectura, para que no bloquee la BD
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
        //findAll() es un método de spring que hace el Select * from productos
        //este método de JpaRepository
    }

    /**
     * Método que guardar, crear un cliente
     * Acá es donde colocamos la Lógica del negocio. Antes de guardar
     * Primero validamos el dato.
     * @param producto objeto Producto a guardar
     * @return producto guardado
     */
    @Override
    public Producto guardar(Producto producto) {
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    /**
     *
     * @param codigo identificador único del producto
     * @return un Optional que contiene
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCodigo(int codigo) {
        //Buscar un cliente por CODIGO
        return productoRepository.findById(codigo);
        //Optional nos evita el NullPointer
    }

    /**
     * Obtiene la lita de productos activos.
     * La consulta se realiza directamente en la base de datos para
     * mejorar el rendimiento.
     * @return lista de productos activos
     */
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        List<Producto> productos = productoRepository.findAll();
        //if/else para mostrar solo los productos activos
        List<Producto> activos = new java.util.ArrayList<>();
            for (Producto producto : productos){
                if (producto.getEstado() == 1){
                    activos.add(producto);
                }
            }
            return activos;
    }

    /**
     *
     * @param codigo identificador único del producto a actualizar
     * @param producto objeto producto con los nuevos datos
     * @return producto a actualizar
     */
    @Override
    public Producto actualizar(int codigo, Producto producto) {
        //Metodo para actualizar un producto
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("El producto no se encontro con el CODIGO " + codigo);
            //Si no existe se lanza una excepción (error controlado)
        }
        producto.setCodigoProducto(codigo);
        //Asegurarnos que el CODIGO del objeto coincida con el de la URL
        //Por seguridad usamos CODIGO de la URL y no el que viene en el JSON
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    /**
     * Elimina un producto de la base de datos.
     * Este método verifica si el producto existe antes de eliminarlo.
     * En caso de no existir, se lanza una excepción.
     *
     * @param codigo identificador único del producto
     */
    @Override
    public void eliminar(int codigo) {
        //  Eliminar un producto
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("El producto no se encontro con el CODIGO " + codigo);
        }
        productoRepository.deleteById(codigo);

    }

    /**
     * Método para verificar si existe un producto por medio del codigo de producto
     * @param codigo idenfiticador único del producto
     * @return true si el producto eixste, false en caso contrario
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(int codigo) {
        return productoRepository.existsById(codigo);
    }

    /**
     * Valida las reglas de negocio de un producto antes de ser persistido.
     * @param producto objeto Producto a validar
     * Método privado(solo puede utilizarse dentro de la clase)
     */
    private void validarProducto(Producto producto){
        /**
         * Validaciones del negocio: Este método se hara privado porque
         * es algo interno del servicio
         */
        if (producto.getNombre_Producto() == null || producto.getNombre_Producto().trim().isEmpty()) {
            //Si el NOMBRE es null o está vacío despúes de quitar espacios
            //Lanza una excepción con un mensaje
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <=0) {
            //Si el precio es null o es menor que o igual a 0
            //CompareTo es usa porque BigDecimal no permite operadores como > o <
            //Lanza una excepción con un mensaje
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (producto.getStock() < 0){
            //Si el STOCK es menor que 0
            //No se permiten valores negativos en el inventario
            //Lanza una excepción con un mensaje
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}
