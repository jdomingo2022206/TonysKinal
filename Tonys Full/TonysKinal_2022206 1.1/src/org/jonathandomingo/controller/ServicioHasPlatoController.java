/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jonathandomingo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jonathandomingo.bean.Plato;
import org.jonathandomingo.bean.Servicio;
import org.jonathandomingo.bean.ServicioHasPlato;
import org.jonathandomingo.db.Conexion;

import org.jonathandomingo.main.Principal;

/**
 *
 * @author informatica
 */
public class ServicioHasPlatoController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<ServicioHasPlato> listaServicioHasPlato;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Plato> listaPlato;
    
    @FXML private TextField txtServicioCodigoServicio;
    @FXML private ComboBox cmbComboBoxPlato;
    @FXML private ComboBox cmbComboBoxServicio;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    
    @FXML private TableView tblServiciosHasPlatos;
    @FXML private TableColumn colServicioCodigoServicio;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoServicio;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        btnEliminar.setVisible(false);
    }
    
    public void cargarDatos(){
        tblServiciosHasPlatos.setItems(getServicioHasPlato());
        colServicioCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasPlato, Integer>("Servicios_codigoServicio"));  
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasPlato, Integer>("codigoServicio")); 
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServicioHasPlato, Integer>("codigoPlato")); 
        
        cmbComboBoxServicio.setItems(getServicio());
        cmbComboBoxPlato.setItems(getPlato());
    }
    
    public ObservableList<ServicioHasPlato> getServicioHasPlato(){
        ArrayList<ServicioHasPlato> lista= new ArrayList<ServicioHasPlato>();
        try {
            PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Platos");
            ResultSet resultado= procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServicioHasPlato(resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoServicio")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicioHasPlato = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Servicio> getServicio() {
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                 .prepareCall("call sp_ListarServicios");
             ResultSet resultado = procedimiento.executeQuery();
             while (resultado.next()) {
                 lista.add(new Servicio(resultado.getInt("codigoServicio"),
                         resultado.getDate("fechaDeServicio"),
                         resultado.getString("tipoServicio"),
                         resultado.getTime("horaServicio"),
                         resultado.getString("lugarServicio"),
                         resultado.getString("telefonoContacto"),
                         resultado.getInt("codigoEmpresa")));
             }
         } catch (Exception e) {
            e.printStackTrace();
        }
         return listaServicio = FXCollections.observableArrayList(lista);
    }
    
    /*public ObservableList <Servicio> getServicio(){
        ArrayList <Servicio> lista = new ArrayList();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio(resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getTime("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
    }*/


    /*public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicio(registro.getByte("codigoServicio"),
                        registro.getDate("fechaServicio"),
                        registro.getString("tipoServicio"),
                        registro.getTime("horaServicio"),
                        registro.getString("lugarServicio"),
                        registro.getString("telefonoContacto"),
                        registro.getByte("codigoEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }*/
    
    public Servicio buscarServicio(int codigoServicio) {
         Servicio resultado = null;
         try {
             PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                 .prepareCall("call sp_BuscarServicios(?)");
             procedimiento.setInt(1, codigoServicio);
             ResultSet registro = procedimiento.executeQuery();
             while (registro.next()) {
                 resultado = new Servicio(registro.getInt("codigoServicio"),
                         registro.getDate("fechaDeServicio"),
                         registro.getString("tipoServicio"),
                         registro.getTime("horaServicio"),
                         registro.getString("lugarServicio"),
                         registro.getString("telefonoContacto"),
                         registro.getInt("codigoEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Plato> getPlato(){
        ArrayList<Plato> lista= new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_ListarPlatos"); // llamada al procedimiento de sql
            ResultSet resultado = procedimiento.executeQuery(); //obtencion del resulset
            while(resultado.next()){ // el next revisa si el resultado tiene un dato o no
                lista.add(new Plato(resultado.getInt("codigoPlato"), // Instanciar Plato y nombre variable de sql
                            resultado.getString("nombrePlato"), // nombre variable de sql
                            resultado.getString("descripcionPlato"), // nombre variable de sql
                            resultado.getInt("cantidad"),
                            resultado.getDouble("precioPlato"),
                            resultado.getInt("codigoTipoPlato"))); // nombre variable de sql
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }

    public Plato buscarPlato(int codigoPlato){
        Plato resultado = null;
        
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlatos(?)");
                procedimiento.setInt(1, codigoPlato);
                ResultSet registro = procedimiento.executeQuery();
                while (registro.next()) {
                    resultado = new Plato(registro.getInt("codigoPlato"),
                                            registro.getString("nombrePlato"),
                                            registro.getString("descripcionPlato"),
                                            registro.getInt("cantidad"),
                                            registro.getDouble("precioPlato"),
                                            registro.getInt("codigoTipoPlato"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        return resultado;
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
                cargarDatos();
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
        ServicioHasPlato registro = new ServicioHasPlato();
        registro.setServicios_codigoServicio(Integer.parseInt(txtServicioCodigoServicio.getText()));
        registro.setCodigoServicio(((Servicio)cmbComboBoxServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoPlato(((Plato)cmbComboBoxPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarServicios_has_Platos(?,?,?)");
            procedimiento.setInt(1, registro.getServicios_codigoServicio());
            procedimiento.setInt(2, registro.getCodigoServicio());
            procedimiento.setInt(3, registro.getCodigoPlato());
            procedimiento.execute();   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void seleccionarElemento(){
        txtServicioCodigoServicio.setText(String.valueOf(((ServicioHasPlato)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cmbComboBoxServicio.getSelectionModel().select(buscarServicio(((ServicioHasPlato)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio())); // pasar parametro de la tupla
        cmbComboBoxPlato.getSelectionModel().select(buscarPlato(((ServicioHasPlato)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato())); // pasar parametro de la tupla
    }
    
    public void desactivarControles(){
        txtServicioCodigoServicio.setEditable(false);
        cmbComboBoxPlato.getSelectionModel().select(null);
        cmbComboBoxServicio.getSelectionModel().select(null);
    }

    public void activarControles(){
        txtServicioCodigoServicio.setEditable(true);
        cmbComboBoxPlato.getSelectionModel().select(null);
        cmbComboBoxServicio.getSelectionModel().select(null);
    }

    public void limpiarControles(){
        txtServicioCodigoServicio.clear();
        cmbComboBoxPlato.getSelectionModel().select(null);
        cmbComboBoxServicio.getSelectionModel().select(null);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaServicio(){
       // escenarioPrincipal.ventanaServicio();
    }
    
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    
}
