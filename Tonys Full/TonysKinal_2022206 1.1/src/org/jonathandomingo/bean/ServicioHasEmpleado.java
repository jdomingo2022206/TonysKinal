/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jonathandomingo.bean;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Jonwk._.19
 */
public class ServicioHasEmpleado {
    
    private int Servicios_codigoServicio;
    private int codigoServicio;
    private int codigoEmpleado;
    private Date fechaDeEvento;
    private Time horaDeEvento ;
    private String lugarDeEvento;

    public ServicioHasEmpleado() {
    }

    public ServicioHasEmpleado(int Servicios_codigoServicio, int codigoServicio, int codigoEmpleado, Date fechaDeEvento, Time horaDeEvento, String lugarDeEvento) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
        this.codigoServicio = codigoServicio;
        this.codigoEmpleado = codigoEmpleado;
        this.fechaDeEvento = fechaDeEvento;
        this.horaDeEvento = horaDeEvento;
        this.lugarDeEvento = lugarDeEvento;
    }

    public int getServicios_codigoServicio() {
        return Servicios_codigoServicio;
    }

    public void setServicios_codigoServicio(int Servicios_codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public Date getFechaDeEvento() {
        return fechaDeEvento;
    }

    public void setFechaDeEvento(Date fechaDeEvento) {
        this.fechaDeEvento = fechaDeEvento;
    }

    public Time getHoraDeEvento() {
        return horaDeEvento;
    }

    public void setHoraDeEvento(Time horaDeEvento) {
        this.horaDeEvento = horaDeEvento;
    }

    public String getLugarDeEvento() {
        return lugarDeEvento;
    }

    public void setLugarDeEvento(String lugarDeEvento) {
        this.lugarDeEvento = lugarDeEvento;
    }
    
    public String getHora(){
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        String hora = formato.format(horaDeEvento);
        return hora.substring(0, 2);
    }
    
    public String getMinuto(){
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        String minuto = formato.format(horaDeEvento);
        return minuto.substring(3, 5);
    }
}
