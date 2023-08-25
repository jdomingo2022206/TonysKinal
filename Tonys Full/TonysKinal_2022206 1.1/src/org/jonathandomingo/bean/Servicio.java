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
 * @author informatica
 */
public class Servicio {
    int codigoServicio, codigoEmpresa;
    String  tipoServicio,  lugarServicio, telefonoContacto; 
    Date fechaDeServicio; 
    Time horaServicio;

    public Servicio() {}
    public Servicio(int codigoServicio, Date fechaDeServicio, String tipoServicio, Time horaServicio, String lugarServicio, String telefonoContacto, int codigoEmpresa ) {
        this.codigoServicio = codigoServicio;
        this.codigoEmpresa = codigoEmpresa;
        this.tipoServicio = tipoServicio;
        this.lugarServicio = lugarServicio;
        this.telefonoContacto = telefonoContacto;
        this.fechaDeServicio = fechaDeServicio;
        this.horaServicio = horaServicio;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public String getLugarServicio() {
        return lugarServicio;
    }

    public void setLugarServicio(String lugarServicio) {
        this.lugarServicio = lugarServicio;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public Date getFechaDeServicio() {
        return fechaDeServicio;
    }

    public void setFechaDeServicio(Date fechaDeServicio) {
        this.fechaDeServicio = fechaDeServicio;
    }

    public Time getHoraServicio() {
        return horaServicio;
    }

    public void setHoraServicio(Time horaServicio) {
        this.horaServicio = horaServicio;
    }    
    
    public String getHora(){
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        String hora = formato.format(horaServicio);
        return hora.substring(0, 2);
    }
    
    public String getMinuto(){
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        String minuto = formato.format(horaServicio);
        return minuto.substring(3, 5);
    }

    @Override
    public String toString() {
        return codigoServicio + " | " + tipoServicio;
    }
    
    
}
