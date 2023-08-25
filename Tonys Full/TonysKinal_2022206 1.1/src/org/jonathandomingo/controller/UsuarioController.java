/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jonathandomingo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.codec.digest.DigestUtils;
import org.jonathandomingo.bean.Usuario;
import org.jonathandomingo.db.Conexion;
import org.jonathandomingo.main.Principal;

/**
 *
 * @author informatica
 */
public class UsuarioController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    
    @FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtPassword;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEliminar.setVisible(false);
    }
    
    public void nuevo(){
        switch (tipoOperacion) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setVisible(true);
                imgNuevo.setImage(new Image("/org/jonathandomingo/images/Guardar.png"));
                imgEliminar.setImage(new Image("/org/jonathandomingo/images/Cancel.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setVisible(false);
                imgNuevo.setImage(new Image("/org/jonathandomingo/images/New.png"));
                imgEliminar.setImage(new Image("/org/jonathandomingo/images/Eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                ventanaLogin();
            break;
                
        }
    }
    
    public void eliminar(){
        switch (tipoOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setVisible(false);
                imgNuevo.setImage(new Image("/org/jonathandomingo/images/New.png"));
                imgEliminar.setImage(new Image("/org/jonathandomingo/images/Eliminar.png"));
              
                tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                throw new AssertionError();
        }
    }
    
    
    public void guardar(){
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setContrasena(txtPassword.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarUsuario(?,?,?,?)");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidoUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContrasena());
            procedimiento.execute();
//            String contra ="";
//            String encript = DigestUtils.md5Hex(contra);
//            System.out.println(encript);    
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    public void desactivarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);
        
    }
    
    public void limpiarControles(){
        txtCodigoUsuario.clear();
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtPassword.clear();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaNuevoUsuario(){
        escenarioPrincipal.ventanaNuevoUsuario();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
}
