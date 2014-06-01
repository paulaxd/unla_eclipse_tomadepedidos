/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Negocio;

import java.util.Date;

/**
 * Clase para manejar articulos
 * @author m_pau_000
 */
public class Articulo {
    private int codigo;
    private double precio;
    private String nombre;
    private int cantidadPorBulto;
    private String caracteristicas;
    private String fotografiaURL;
    private Date fechaActualizacion;
    private Date vigenciaPrecio;

    public Articulo(int codigo, double precio, String nombre, int cantidadPorBulto, String caracteristicas, String fotografiaURL, Date fechaActualizacion, Date vigenciaPrecio) {
        this.codigo = codigo;
        this.precio = precio;
        this.nombre = nombre;
        this.cantidadPorBulto = cantidadPorBulto;
        this.caracteristicas = caracteristicas;
        this.fotografiaURL = fotografiaURL;
        this.fechaActualizacion = fechaActualizacion;
        this.vigenciaPrecio = vigenciaPrecio;
    }
    
    public Articulo(int codigo){
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadPorBulto() {
        return cantidadPorBulto;
    }

    public void setCantidadPorBulto(int cantidadPorBulto) {
        this.cantidadPorBulto = cantidadPorBulto;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getFotografiaURL() {
        return fotografiaURL;
    }

    public void setFotografiaURL(String fotografiaURL) {
        this.fotografiaURL = fotografiaURL;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Date getVigenciaPrecio() {
        return vigenciaPrecio;
    }

    public void setVigenciaPrecio(Date vigenciaPrecio) {
        this.vigenciaPrecio = vigenciaPrecio;
    }

    @Override
    public String toString() {
        return "Articulo{" + "codigo=" + codigo + ", precio=" + precio + ", nombre=" + nombre + ", cantidadPorBulto=" + cantidadPorBulto + ", caracteristicas=" + caracteristicas + ", fotografiaURL=" + fotografiaURL + ", fechaActualizacion=" + fechaActualizacion + ", vigenciaPrecio=" + vigenciaPrecio + '}';
    }
}
