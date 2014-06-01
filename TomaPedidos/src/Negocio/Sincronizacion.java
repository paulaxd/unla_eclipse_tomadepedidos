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
public class Sincronizacion {
    
    private Date fechaUltimaSincronizacion;
    private int vendedorDni;

    public Sincronizacion(Date fechaUltimaSincronizacion, int vendedorDni) {
        this.fechaUltimaSincronizacion = fechaUltimaSincronizacion;
        this.vendedorDni = vendedorDni;
    }

    public Date getFechaUltimaSincronizacion() {
        return fechaUltimaSincronizacion;
    }

    public void setFechaUltimaSincronizacion(Date fechaUltimaSincronizacion) {
        this.fechaUltimaSincronizacion = fechaUltimaSincronizacion;
    }

    public int getVendedorDni() {
        return vendedorDni;
    }

    public void setVendedorDni(int vendedorDni) {
        this.vendedorDni = vendedorDni;
    }

    @Override
    public String toString() {
        return "Sincronizacion{" + "fechaUltimaSincronizacion=" + fechaUltimaSincronizacion + ", vendedorDni=" + vendedorDni + '}';
    }
    
    
}
