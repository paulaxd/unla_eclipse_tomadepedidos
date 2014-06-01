/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Negocio;

import java.util.Date;

/**
 *
 * @author maurogarcia
 */
public class Pedido {
    
    private int codigo;
    private Estado estado;
    private Date fecha;
    private Cliente cliente;
    

    public Pedido(int codigo, Estado estado, Date fecha, Cliente cliente) {
        this.codigo = codigo;
        this.estado = estado;
        this.fecha = fecha;
        this.cliente = cliente;
    }
    
    public Pedido(int codigo){
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Pedido{" + "codigo=" + codigo + ", estado=" + estado + ", fecha=" + fecha + ", cliente=" + cliente;
    }
    
    
}
