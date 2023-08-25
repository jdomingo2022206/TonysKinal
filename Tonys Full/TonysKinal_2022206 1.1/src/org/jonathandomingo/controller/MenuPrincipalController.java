/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jonathandomingo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javax.swing.JOptionPane;
import org.jonathandomingo.main.Principal;


//abre 
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
//cierra
/**
 *
 * @author informatica
 */
public class MenuPrincipalController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    //abre
    @FXML private MenuBar mbar;
    @FXML private Menu Modulos;
    @FXML private Menu Reportes;
    @FXML private Menu Info;
    @FXML private Menu Has;
    //cierra
    
    //abre
    // Método para mostrar el menú "Modulos" del MenuBar al presionar una tecla
    @FXML
    private void mostrarMenus(KeyEvent event) {
        if (null != event.getCode()) switch (event.getCode()) {
            case DIGIT1:
                Modulos.show();
                break;
            case DIGIT2:
                Reportes.show();
                break;
            case DIGIT3:
                Info.show();
                break;
            case DIGIT4:
                Has.show();
                break;
            default:
                break;
        }
           
    }
    //cierra
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //borrar metodo para poder implementar
        
    }

    public Principal getEsenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEsenarioPrincipal(Principal esenarioPrincipal) {
        this.escenarioPrincipal = esenarioPrincipal;
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
    public void ventanaProducto(){
        escenarioPrincipal.ventanaProducto();
    }
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
    
    
    public void ventanaLogin(){
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de salir?", "Log out", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(respuesta == JOptionPane.YES_OPTION){
            escenarioPrincipal.ventanaLogin();
        }
    }
    
    
//    public void ventanaLogin(){
//        escenarioPrincipal.ventanaLogin();
//    }
    
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    
    public void ventanaProductoHasPlato(){
        escenarioPrincipal.ventanaProductoHasPlato();
    }
    
    public void ventanaServicioHasPlato(){
        escenarioPrincipal.ventanaServicioHasPlato();
    }
    
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
    
        public void ventanaServicioHasEmpleado(){
        escenarioPrincipal.ventanaServicioHasEmpleado();
    }
}
