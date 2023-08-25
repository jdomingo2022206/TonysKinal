/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jonathandomingo.controller;

import java.sql.PreparedStatement;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javax.swing.JOptionPane;
import org.jonathandomingo.bean.Login;
import org.jonathandomingo.bean.Usuario;
import org.jonathandomingo.db.Conexion;
import org.jonathandomingo.main.Principal;

/**
 *
 * @author Jonwk._.19
 */
public class LoginController implements Initializable{
    private Principal escenarioPrincipal;
    private ObservableList<Usuario> listaUsuario;
    
    @FXML private TextField txtUsuarioLogin;
    @FXML private TextField txtPasswordLogin;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public ObservableList<Usuario> getUsuario(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarUsuario()"); // llamada al procedimiento de sql
            ResultSet resultado = procedimiento.executeQuery(); //obtencion del resulset
            while(resultado.next()){ // el next revisa si el resultado tiene un dato o no
                lista.add(new Usuario(resultado.getInt("codigoUsuario"), // Instanciar empresa y nombre variable de sql
                            resultado.getString("nombreUsuario"), // nombre variable de sql
                            resultado.getString("apellidoUsuario"), // nombre variable de sql
                            resultado.getString("usuarioLogin"), // nombre variable de sql
                            resultado.getString("contrasena"))); // nombre variable de sql
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         return listaUsuario = FXCollections.observableArrayList(lista);
    }
    
    @FXML
    private void Login(){
        Login login = new Login();
        int x =0;
        boolean bandera = false;
        login.setUsuarioMaster(txtUsuarioLogin.getText());
        login.setPasswordLogin(txtPasswordLogin.getText());
        
        if (txtUsuarioLogin==null) {
            JOptionPane.showMessageDialog(null, "Llena los campos");
        } else if(txtPasswordLogin==null) {
            JOptionPane.showMessageDialog(null, "Llena los campos");
        }
        
        while (x < getUsuario().size()) {
            String user = getUsuario().get(x).getUsuarioLogin();
            String pass = getUsuario().get(x).getContrasena();
            if (user.equals(login.getUsuarioMaster())&& pass.equals(login.getPasswordLogin())) {
                JOptionPane.showMessageDialog(null, "Sesión iniciada \n"
                                                + getUsuario().get(x).getNombreUsuario()+" "+
                                                getUsuario().get(x).getApellidoUsuario()+ "\n"+
                                                "Bienvenido");
                escenarioPrincipal.menuPrincipal();
                x = getUsuario().size();
                bandera =true;
            }
            x++;
        }
        
        if (bandera==false){
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
        }
    }
    
    public void exit(){
        System.exit(0);
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

    
    public void enter(){
        txtPasswordLogin.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                Login();
            }
        });
        
        txtUsuarioLogin.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                Login();
            }
        });
    }
    
}
