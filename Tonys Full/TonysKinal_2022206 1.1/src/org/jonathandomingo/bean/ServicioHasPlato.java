/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jonathandomingo.bean;

/**
 *
 * @author informatica
 */
public class ServicioHasPlato {
   int Servicios_codigoServicio, codigoPlato, codigoServicio;

    public ServicioHasPlato() {}

    public ServicioHasPlato(int Servicios_codigoServicio, int codigoPlato, int codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
        this.codigoPlato = codigoPlato;
        this.codigoServicio = codigoServicio;
    }

    public int getServicios_codigoServicio() {
        return Servicios_codigoServicio;
    }

    public void setServicios_codigoServicio(int Servicios_codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    @Override
    public String toString() {
        return "ServicioHasPlato{" + "Servicios_codigoServicio=" + Servicios_codigoServicio + ", codigoPlato=" + codigoPlato + ", codigoServicio=" + codigoServicio + '}';
    }  
   
}
