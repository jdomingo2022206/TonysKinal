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
import javax.swing.JOptionPane;
import org.jonathandomingo.bean.Empresa;
import org.jonathandomingo.bean.Servicio;
import org.jonathandomingo.db.Conexion;
import org.jonathandomingo.main.Principal;


public class ServiciosController implements Initializable{

    
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR, ELIMINAR, CANCELAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<String> listaHora;
    private ObservableList<String> listaMinuto;
    private ObservableList<Empresa> listaEmpresa;
    private ObservableList<Servicio> listaServicio;
    private DatePicker fecha;
    
    @FXML private TextField txtCodServicio;
    @FXML private TextField txtTipServicio;
    @FXML private TextField txtLugServicio;
    @FXML private TextField txtTelContacto;
    
    @FXML private ComboBox cmbCodEmpresa;
    @FXML private ComboBox cmbHora;
    @FXML private ComboBox cmbMinuto;
    
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colTipServicio;
    @FXML private TableColumn colHoraServicio;
    @FXML private TableColumn colLugServicio;
    @FXML private TableColumn colTelContacto;
    @FXML private TableColumn colCodEmpresa;
    
    @FXML private GridPane grpFecha;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/jonathandomingo/resource/TonysKinal.css");
        grpFecha.add(fecha, 1, 1);
        cmbCodEmpresa.setItems(getEmpresa());
        cmbHora.setItems(getHoras());
        cmbMinuto.setItems(getMinutos());
        desactivarControles();
        
    }
    public void cargarDatos() {
        tblServicios.setItems(getServicio());
        colCodServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("FechaDeServicio"));
        colTipServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Time>("horaServicio"));
        colLugServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelContacto.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
    }
    
    public ObservableList <String> getMinutos(){
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

     public ObservableList <String> getHoras(){
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
     
    public Empresa buscarEmpresa(int codigoEmpresa) {
        Empresa resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
             procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
             while (registro.next()) {
                 resultado = new Empresa(registro.getInt("codigoEmpresa"),
                        registro.getString("nombreEmpresa"),
                        registro.getString("direccion"),
                        registro.getString("telefono"));
            }
        } catch (Exception e) {
            e.printStackTrace();
          }
        return resultado;
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
    
    public ObservableList<Empresa> getEmpresa() {
         ArrayList<Empresa> lista = new ArrayList<Empresa>();
         try {
             PreparedStatement procedimiento = Conexion.getInstance().getConexion()
                 .prepareCall("call sp_ListarEmpresa");
             ResultSet resultado = procedimiento.executeQuery();
             while (resultado.next()) {                
                 lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                         resultado.getString("nombreEmpresa"),
                         resultado.getString("direccion"),
                         resultado.getString("telefono")));
            }
         } catch (Exception e) {
             e.printStackTrace();
         }
         return listaEmpresa = FXCollections.observableArrayList(lista);
    }
     public void nuevo() {
         switch (tipoDeOperacion) {
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
                 tipoDeOperacion = operaciones.GUARDAR;
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

                 tipoDeOperacion = operaciones.NINGUNO;
                 cargarDatos();
                 break;
         }
     }
    
     public void eliminar() {
         switch (tipoDeOperacion) {
             case GUARDAR:
                 limpiarControles();
                 desactivarControles();
                 btnNuevo.setText("Nuevo");
                 btnEliminar.setText("Eliminar");
                 btnEditar.setVisible(true);
                 btnReporte.setVisible(true);
                 imgNuevo.setImage(new Image("/org/jonathandomingo/images/New.png"));
                 imgEliminar.setImage(new Image("/org/jonathandomingo/images/Eliminar.png"));
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
             default:
                 if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                     int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Servicio",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                     if (respuesta == JOptionPane.YES_OPTION) {
                         try {
                              PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarServicios(?)");
                              procedimiento.setInt(1, ((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                              procedimiento.execute();
                              listaServicio.remove(tblServicios.getSelectionModel().getSelectedItem());
                              limpiarControles();
                          } catch (Exception e) {
                             e.printStackTrace();
                          }
                     } 
                 } else {
                     JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento para eliminar");
                 }
        }
    }
    
     
     public void editar() {
         switch (tipoDeOperacion) {
             case NINGUNO:
                 if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                     btnNuevo.setDisable(true);
                     btnEliminar.setDisable(true);
                     btnEditar.setText("Actualizar");
                     btnReporte.setText("Cancelar");
                     imgEditar.setImage(new Image("/org/jonathandomingo/images/Actualizar.png"));
                     imgReporte.setImage(new Image("/org/jonathandomingo/images/Cancel.png"));
                     activarControles();
                     tipoDeOperacion = operaciones.ACTUALIZAR;
                 } else {
                     JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento para editar");
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
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
         }
     }
    
     public void reporte() {
         switch (tipoDeOperacion) {
             case ACTUALIZAR:
//                 actualizar();
                 limpiarControles();
                 desactivarControles();
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("org/jonathandomingo/images/Editar.png"));
                imgReporte.setImage(new Image("org/jonathandomingo/images/Reporte.png"));
                 cargarDatos();
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
         }
     }
     
     
     public void actualizar() {
         try {
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarServicios(?, ?, ?, ?, ?, ?, ?)");
             Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem();
             registro.setCodigoServicio(Integer.parseInt(txtCodServicio.getText()));
             registro.setFechaDeServicio(fecha.getSelectedDate());
             registro.setTipoServicio(txtTipServicio.getText());
             String hora = String.valueOf(cmbHora.getSelectionModel().getSelectedItem());
             String minuto = String.valueOf(cmbMinuto.getSelectionModel().getSelectedItem());
             registro.setHoraServicio(new java.sql.Time(Integer.parseInt(hora), Integer.parseInt(minuto), 0));
             registro.setLugarServicio(txtLugServicio.getText());
             registro.setTelefonoContacto(txtTelContacto.getText());
             procedimiento.setInt(1, registro.getCodigoServicio());
             procedimiento.setDate(2, new java.sql.Date(registro.getFechaDeServicio().getTime()));
             procedimiento.setString(3, registro.getTipoServicio());
             procedimiento.setTime(4, registro.getHoraServicio());
             procedimiento.setString(5, registro.getLugarServicio());
             procedimiento.setString(6, registro.getTelefonoContacto());
             procedimiento.setInt(7, registro.getCodigoEmpresa());
             procedimiento.execute();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
    
    public void guardar() {
        Servicio registro = new Servicio();
        registro.setFechaDeServicio(fecha.getSelectedDate());
        registro.setTipoServicio(txtTipServicio.getText());
        String hora = String.valueOf(cmbHora.getSelectionModel().getSelectedItem());
        String minuto = String.valueOf(cmbMinuto.getSelectionModel().getSelectedItem());
        registro.setHoraServicio(new java.sql.Time(Integer.parseInt(hora), Integer.parseInt(minuto), 0));
        registro.setLugarServicio(txtLugServicio.getText());
        registro.setTelefonoContacto(txtTelContacto.getText());
        registro.setCodigoEmpresa(((Empresa)cmbCodEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarServicios(?, ?, ?, ?, ?, ?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaDeServicio().getTime()));
            procedimiento.setString(2, registro.getTipoServicio());
            procedimiento.setTime(3, registro.getHoraServicio());
            procedimiento.setString(4, registro.getLugarServicio());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setInt(6, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicio.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     
    public void seleccionarElemento() {
        txtCodServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        fecha.selectedDateProperty().set(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaDeServicio());
        txtTipServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
        cmbHora.setValue(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHora());
        cmbMinuto.setValue(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getMinuto());
        txtLugServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
        txtTelContacto.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        cmbCodEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }
    
    public void desactivarControles() {
         txtCodServicio.setEditable(false);
         fecha.setDisable(true);
         txtTipServicio.setEditable(false);
         cmbHora.setDisable(true);
         cmbMinuto.setDisable(true);
         txtLugServicio.setEditable(false);
         txtTelContacto.setEditable(false);
         cmbCodEmpresa.setDisable(true);
    }
    
    public void activarControles() {
         txtCodServicio.setEditable(false);
         fecha.setDisable(false);
         txtTipServicio.setEditable(true);
         cmbHora.setDisable(false);
         cmbMinuto.setDisable(false);
         txtLugServicio.setEditable(true);
         txtTelContacto.setEditable(true);
         cmbCodEmpresa.setDisable(false);
    }
    
    public void limpiarControles() {
         txtCodServicio.clear();
         fecha.selectedDateProperty().set(null);
         txtTipServicio.clear();
         cmbHora.setValue(null);
         cmbMinuto.setValue(null);
         txtLugServicio.clear();
         txtTelContacto.clear();
         cmbCodEmpresa.setValue(null);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
}

