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
import org.jonathandomingo.bean.Producto;
import org.jonathandomingo.bean.ProductoHasPlato;
import org.jonathandomingo.db.Conexion;
import org.jonathandomingo.main.Principal;

/**
 *
 * @author informatica
 */
public class ProductoHasPlatoController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<ProductoHasPlato> listaProductoHasPlato;
    private ObservableList<Producto> listaProducto;
    private ObservableList<Plato> listaPlato;
    
    @FXML private TextField txtProductoCodigoProducto;
    @FXML private ComboBox cmbComboBoxPlato;
    @FXML private ComboBox cmbComboBoxProducto;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    
    @FXML private TableView tblProductosHasPlatos;
    @FXML private TableColumn colProductoCodigoProducto;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoProducto;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        btnEliminar.setVisible(false);
    }
    
    public void cargarDatos(){
        tblProductosHasPlatos.setItems(getProductoHasPlato());
        colProductoCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductoHasPlato, Integer>("Productos_codigoProducto"));  
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductoHasPlato, Integer>("codigoProducto")); 
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductoHasPlato, Integer>("codigoPlato")); 
        
        cmbComboBoxProducto.setItems(getProducto());
        cmbComboBoxPlato.setItems(getPlato());
    }
    
    public ObservableList<ProductoHasPlato> getProductoHasPlato(){
        ArrayList<ProductoHasPlato> lista= new ArrayList<ProductoHasPlato>();
        try {
            PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos_has_Platos");
            ResultSet resultado= procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ProductoHasPlato(resultado.getInt("Productos_codigoProducto"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoProducto")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductoHasPlato = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista= new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProducto");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                                        resultado.getString("nombreProducto"),
                                        resultado.getInt("cantidad")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista);
    }
    
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
        
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProducto(?)");
                procedimiento.setInt(1, codigoProducto);
                ResultSet registro = procedimiento.executeQuery();
                while (registro.next()) {
                    resultado = new Producto(registro.getInt("codigoProducto"),
                                            registro.getString("nombreProducto"),
                                            registro.getInt("cantidad"));
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
        ProductoHasPlato registro = new ProductoHasPlato();
        registro.setProductos_codigoProducto(Integer.parseInt(txtProductoCodigoProducto.getText()));
        registro.setCodigoProducto(((Producto)cmbComboBoxProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        registro.setCodigoPlato(((Plato)cmbComboBoxPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarProductos_has_Platos(?,?,?)");
            procedimiento.setInt(1, registro.getProductos_codigoProducto());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.execute();   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void seleccionarElemento(){
        txtProductoCodigoProducto.setText(String.valueOf(((ProductoHasPlato)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
        cmbComboBoxProducto.getSelectionModel().select(buscarProducto(((ProductoHasPlato)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto())); // pasar parametro de la tupla
        cmbComboBoxPlato.getSelectionModel().select(buscarPlato(((ProductoHasPlato)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato())); // pasar parametro de la tupla
    }
    
    public void desactivarControles(){
        txtProductoCodigoProducto.setEditable(false);
        cmbComboBoxPlato.getSelectionModel().select(null);
        cmbComboBoxProducto.getSelectionModel().select(null);
    }

    public void activarControles(){
        txtProductoCodigoProducto.setEditable(true);
        cmbComboBoxPlato.getSelectionModel().select(null);
        cmbComboBoxProducto.getSelectionModel().select(null);
    }

    public void limpiarControles(){
        txtProductoCodigoProducto.clear();
        cmbComboBoxPlato.getSelectionModel().select(null);
        cmbComboBoxProducto.getSelectionModel().select(null);
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
    
    public void ventanaProducto(){
        escenarioPrincipal.ventanaProducto();
    }
    
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
    
}
