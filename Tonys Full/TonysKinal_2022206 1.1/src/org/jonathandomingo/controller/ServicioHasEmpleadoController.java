/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jonathandomingo.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import org.jonathandomingo.bean.Empleado;
import org.jonathandomingo.bean.Servicio;
import org.jonathandomingo.bean.ServicioHasEmpleado;
import org.jonathandomingo.db.Conexion;
import org.jonathandomingo.main.Principal;

/**
 *
 * @author Jonwk._.19
 */
public class ServicioHasEmpleadoController implements Initializable {
    
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ServicioHasEmpleado> listaServiciosHasEmpleados;
    private ObservableList<Servicio> listaServicio;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<String> listaHora;
    private ObservableList<String> listaMinuto;
    private DatePicker fecha;
    
    @FXML private TextField txtServicios_codigoServicio;
    @FXML private TextField txtLugarEvento;
    
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private ComboBox cmbHora;
    @FXML private ComboBox cmbMinuto;
    
    @FXML private GridPane grpFecha;
    
    @FXML private TableView tblServiciosHasEmpleados;
    @FXML private TableColumn colServicios_codigoServicio;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colCodigoServicio;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/jonathandomingo/resource/TonysKinal.css");
        grpFecha.add(fecha, 1, 1);
        cmbHora.setItems(getHora());
        cmbMinuto.setItems(getMinuto());
        cmbCodigoServicio.setItems(getServicio());
        cmbCodigoEmpleado.setItems(getEmpleado());
        desactivarControles();
        btnEliminar.setVisible(false);
    }

    public void cargarDatos() {
        tblServiciosHasEmpleados.setItems(getServiciohasEmpleado());
        colServicios_codigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("Servicios_codigoServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Integer>("codigoEmpleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Date>("fechaDeEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, Time>("horaDeEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServicioHasEmpleado, String>("lugarDeEvento"));
    }
    
    public ObservableList<ServicioHasEmpleado> getServiciohasEmpleado() {
        ArrayList<ServicioHasEmpleado> lista = new ArrayList<ServicioHasEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_Empleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServicioHasEmpleado(resultado.getInt("Servicio_codigoServicio"),
                        resultado.getInt("codigoEmpleado"),
                        resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaDeEvento"),
                        resultado.getTime("horaDeEvento"),
                        resultado.getString("lugarDeEvento")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServiciosHasEmpleados = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> lista= new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleado");
            ResultSet resultado= procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public Empleado buscarEmpleado(int codigoEmpleado) {
         Empleado resultado = null;
         try {
             PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                 .prepareCall("call sp_BuscarEmpleado(?)");
             procedimiento.setInt(1, codigoEmpleado);
             ResultSet registro = procedimiento.executeQuery();
             while (registro.next()) {
                  resultado = new Empleado(registro.getInt("codigoEmpleado"),
                         registro.getInt("numeroEmpleado"),
                         registro.getString("apellidosEmpleado"),
                         registro.getString("nombresEmpleado"),
                         registro.getString("direccionEmpleado"),
                         registro.getString("telefonoContacto"),
                         registro.getString("gradoCocinero"),
                         registro.getInt("codigoTipoEmpleado"));
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         return resultado;
    }
    
    public void nuevo(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setVisible(true);
                imgNuevo.setImage(new Image("/org/jonathandomingo/images/Guardar.png"));
                imgEliminar.setImage(new Image("/org/jonathandomingo/images/Cancel.png"));
                tipoDeOperacion = operaciones.GUARDAR;
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
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
                
        }
    }
    
    public void eliminar(){
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setVisible(false);
                imgNuevo.setImage(new Image("/org/jonathandomingo/images/New.png"));
                imgEliminar.setImage(new Image("/org/jonathandomingo/images/Eliminar.png"));
              
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void guardar() {
         ServicioHasEmpleado registro = new ServicioHasEmpleado();
         registro.setServicios_codigoServicio(Integer.parseInt(txtServicios_codigoServicio.getText()));
         registro.setCodigoServicio(((Servicio)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
         registro.setCodigoEmpleado(((Empleado)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
         registro.setFechaDeEvento(fecha.getSelectedDate());
         String hora = String.valueOf(cmbHora.getSelectionModel().getSelectedItem());
         String minuto = String.valueOf(cmbMinuto.getSelectionModel().getSelectedItem());
         registro.setHoraDeEvento(new java.sql.Time(Integer.parseInt(hora), Integer.parseInt(minuto), 0));
         registro.setLugarDeEvento(txtLugarEvento.getText());
         try {
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicios_has_Empleados (?, ?, ?, ?, ?, ?)");
             procedimiento.setInt(1, registro.getServicios_codigoServicio());
             procedimiento.setInt(2, registro.getCodigoServicio());
             procedimiento.setInt(3, registro.getCodigoEmpleado());
             procedimiento.setDate(4, new java.sql.Date(registro.getFechaDeEvento().getTime()));
             procedimiento.setTime(5, registro.getHoraDeEvento());
             procedimiento.setString(6, registro.getLugarDeEvento());
             procedimiento.execute();
             listaServiciosHasEmpleados.add(registro);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
    
    public ObservableList <String> getMinuto(){
         ArrayList <String> lista = new ArrayList<>();
         for(int i = 0; i<=59;i++){
             if(i<10){
                lista.add("0"+String.valueOf(i)); 
             }else{
                 lista.add(String.valueOf(i));
             }
         }
    	  return listaMinuto = FXCollections.observableArrayList(lista);
    }

    public ObservableList <String> getHora(){
	 ArrayList <String> lista = new ArrayList<>();
	 for(int i = 0; i<=23;i++){
	    if(i<10){
                lista.add("0"+String.valueOf(i));
            }else{
                lista.add(String.valueOf(i));
            }
	 }

	 return listaHora = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento() {
        txtServicios_codigoServicio.setText(String.valueOf(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        fecha.selectedDateProperty().set(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getFechaDeEvento());
        cmbHora.setValue(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getHora());
        cmbMinuto.setValue(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getMinuto());
        txtLugarEvento.setText(((ServicioHasEmpleado)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getLugarDeEvento());
    }
    
    public void desactivarControles() {
        txtServicios_codigoServicio.setEditable(false);
        fecha.setDisable(false);
        cmbHora.setDisable(false);
        cmbMinuto.setDisable(false);
        txtLugarEvento.setEditable(false);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
    }
    
    public void activarControles() {
         txtServicios_codigoServicio.setEditable(true);
         fecha.setDisable(false);
         cmbHora.setDisable(false);
         cmbMinuto.setDisable(false);
         txtLugarEvento.setEditable(true);
         cmbCodigoServicio.setDisable(false);
         cmbCodigoEmpleado.setDisable(false);    
    }
    
    public void limpiarControles() {
         txtServicios_codigoServicio.clear();
         fecha.selectedDateProperty().set(null);
         cmbHora.setValue(null);
         cmbMinuto.setValue(null);
         txtLugarEvento.clear();
         cmbCodigoServicio.setValue(null);
         cmbCodigoEmpleado.setValue(null);
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
    
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
    
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
}
