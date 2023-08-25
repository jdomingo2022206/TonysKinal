/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jonathandomingo.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.jonathandomingo.bean.Plato;
import org.jonathandomingo.bean.TipoPlato;
import org.jonathandomingo.db.Conexion;
import org.jonathandomingo.main.Principal;

/**
 *
 * @author Jonwk._.19
 */
public class PlatoController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Plato> listaPlato;
    private ObservableList<TipoPlato> listaTipoPlato;
 
    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtCantidadPlato;
    @FXML private TextField txtPrecioPlato;
    @FXML private TextField txtDescripcionPlato;
    
    @FXML private ComboBox cmbComboBox;
    
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colDescripcionPlato;
    @FXML private TableColumn colCantidadPlato;
    @FXML private TableColumn colPrecioPlato;
    @FXML private TableColumn colTipoPlato;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblPlatos.setItems(getPlato());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato")); 
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato")); 
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcionPlato")); 
        colCantidadPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidad")); 
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Plato, Double>("precioPlato")); 
        colTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato")); 
        cmbComboBox.setItems(getTipoPlato());
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
    
 // ESTO ES PARA EL COMBO BOX
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista= new ArrayList<TipoPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoPlato"); // llamada al procedimiento de sql
            ResultSet resultado = procedimiento.executeQuery(); //obtencion del resulset
            while(resultado.next()){ // el next revisa si el resultado tiene un dato o no
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"), // Instanciar TipoPlato y nombre variable de sql
                            resultado.getString("descripcionTipo"))); // nombre variable de sql
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoPlato(?)");
                procedimiento.setInt(1, codigoTipoPlato);
                ResultSet registro = procedimiento.executeQuery();
                while (registro.next()) {
                    resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
                                            registro.getString("descripcionTipo"));
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
                    btnEditar.setVisible(false);
                    btnReporte.setVisible(false);
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/jonathandomingo/images/Guardar.png"));
                    imgEliminar.setImage(new Image("/org/jonathandomingo/images/Cancel.png"));
                    
                    tipoOperacion= PlatoController.operaciones.GUARDAR;
                break;
            case GUARDAR:
                    guardar();
                    limpiarControles();
                    desactivarControles();

                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setVisible(true);
                    btnReporte.setVisible(true);
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/jonathandomingo/images/New.png"));
                    imgEliminar.setImage(new Image("/org/jonathandomingo/images/Eliminar.png"));

                    tipoOperacion = PlatoController.operaciones.NINGUNO;
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void eliminar(){
        switch (tipoOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                imgNuevo.setImage(new Image("/org/jonathandomingo/images/New.png"));
                imgEliminar.setImage(new Image("/org/jonathandomingo/images/Eliminar.png"));
              
                tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el reguistro?", "Eliminar Plato", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPlatos(?)");
                            procedimiento.setInt(1,((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            procedimiento.execute();
                            listaPlato.remove(tblPlatos.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seccionar un elemento para eliminar");
                }  
        } 
    }
    
    public void editar(){
        switch (tipoOperacion) {
            case NINGUNO:
                if (tblPlatos.getSelectionModel().getSelectedItem() != null){ //dato seleccionado o no
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/jonathandomingo/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/jonathandomingo/images/Cancel.png"));
                    activarControles();
                    tipoOperacion = operaciones.ACTUALIZAR;
                    
                }else{ // sino, pedir que seleccione un elemento
                    JOptionPane.showMessageDialog(null, "Debe de seccionar un elemento para editar");
                }
                    break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("org/jonathandomingo/images/Editar.png"));
                imgReporte.setImage(new Image("org/jonathandomingo/images/Reporte.png"));
                cargarDatos();
                tipoOperacion = operaciones.NINGUNO;
            default:
                throw new AssertionError();
        }
    }
    
    public void reporte(){
        switch (tipoOperacion) {
            case ACTUALIZAR:
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setVisible(true);
                    btnEliminar.setVisible(true);
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    imgEditar.setImage(new Image("org/jonathandomingo/images/Editar.png"));
                    imgReporte.setImage(new Image("org/jonathandomingo/images/Reporte.png"));
                    cargarDatos();
                    tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                
        }
    }
    
    /*
    public void actualizar(){
        Plato registro = (Plato)tblPlatos.getSelectionModel().getSelectedItem();
        //registro.setCodigoPlato(0); // es auto increment
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcionPlato(txtDescripcionPlato.getText());
        registro.setCantidad(Integer.parseInt(txtNombrePlato.getText()));
        registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPlatos(?,?,?,?,?,?)"); 
            //registro.setCodigoEmpresa(((Plato)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoPlato());
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setString(4, registro.getDescripcionPlato());
            procedimiento.setDouble(5, registro.getPrecioPlato());
            procedimiento.setInt(6, registro.getCodigoTipoPlato());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_EditarPlatos(?,?,?,?,?,?)");
            Plato registro = (Plato)tblPlatos.getSelectionModel().getSelectedItem();
            registro.setCantidad(Integer.parseInt(txtCantidadPlato.getText()));
            registro.setNombrePlato(txtNombrePlato.getText());
            registro.setDescripcionPlato(txtDescripcionPlato.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
            registro.setCodigoTipoPlato(((TipoPlato)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setString(4, registro.getDescripcionPlato());
            procedimiento.setDouble(5, registro.getPrecioPlato());
            procedimiento.setInt(6, registro.getCodigoTipoPlato());
            procedimiento.execute();
            listaPlato.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar(){
       Plato registro = new Plato();
       registro.setCantidad(Integer.parseInt(txtCantidadPlato.getText()));
       registro.setNombrePlato(txtNombrePlato.getText());
       registro.setDescripcionPlato(txtDescripcionPlato.getText());
       registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
       registro.setCodigoTipoPlato(((TipoPlato)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
       try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarPlatos(?,?,?,?,?)");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcionPlato());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getCodigoTipoPlato());
            procedimiento.execute();
            listaPlato.add(registro);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
    
    public void seleccionarElemento(){
        txtCodigoPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        txtNombrePlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
        txtCantidadPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
        txtPrecioPlato.setText(Double.toString(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
        txtDescripcionPlato.setText(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
        cmbComboBox.getSelectionModel().select(buscarTipoPlato(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato())); // pasar parametro de la tupla
    }
    
    public void limpiarControles(){
        txtCodigoPlato.clear();
        txtNombrePlato.clear();
        txtCantidadPlato.clear();
        txtPrecioPlato.clear();
        txtDescripcionPlato.clear();
        cmbComboBox.getSelectionModel().select(null);
    }
    
    public void activarControles(){
        txtCodigoPlato.setEditable(false);
        txtNombrePlato.setEditable(true);
        txtCantidadPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
        txtDescripcionPlato.setEditable(true);
    }
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtCantidadPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
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
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    
}


