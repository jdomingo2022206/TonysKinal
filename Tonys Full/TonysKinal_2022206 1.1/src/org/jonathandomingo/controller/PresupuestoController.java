/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jonathandomingo.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import org.jonathandomingo.bean.Presupuesto;
import org.jonathandomingo.db.Conexion;
import org.jonathandomingo.main.Principal;
import org.jonathandomingo.report.GenerarReporte;

/**
 *
 * @author informatica
 */
public class PresupuestoController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Presupuesto> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    
    @FXML private GridPane grpFecha;
    @FXML private ComboBox cmbComboBox;
    
    @FXML private TableView tblPresupuestos;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
    
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
        fecha =new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")); //se le da el formato ala Fechatiene menú contextual
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(true);
        fecha.getStylesheets().add("/org/jonathandomingo/resource/TonysKinal.css");
        grpFecha.add(fecha, 3, 0);
        cmbComboBox.setItems(getEmpresa());
    }
    
    public void cargarDatos(){
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto")); 
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaDeSolicitud")); 
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto")); 
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa")); 
    }
    
    public void seleccionarElemento(){
        txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fecha.selectedDateProperty().set(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaDeSolicitud());
        txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cmbComboBox.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa())); // pasar parametro de la tupla
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
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
    
    
    
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista= new ArrayList<Presupuesto>();
        try {
            PreparedStatement procedimiento= Conexion.getInstance().getConexion().prepareCall("call sp_ListarPresupuesto");
            ResultSet resultado= procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                        resultado.getDate("fechaDeSolicitud"),
                        resultado.getDouble("cantidadPresupuesto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    
    
    // ESTO ES PARA EL COMBO BOX
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista= new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresa"); // llamada al procedimiento de sql
            ResultSet resultado = procedimiento.executeQuery(); //obtencion del resulset
            while(resultado.next()){ // el next revisa si el resultado tiene un dato o no
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"), // Instanciar empresa y nombre variable de sql
                            resultado.getString("nombreEmpresa"), // nombre variable de sql
                            resultado.getString("direccion"), // nombre variable de sql
                            resultado.getString("telefono"))); // nombre variable de sql
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
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
                    
                    tipoOperacion= PresupuestoController.operaciones.GUARDAR;
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

                    tipoOperacion = PresupuestoController.operaciones.NINGUNO;
                    cargarDatos();
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
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el reguistro?", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                            procedimiento.setInt(1,((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedItem());
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
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null){ //dato seleccionado o no
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
    
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarPresupuesto(?,?,?,?)");
            Presupuesto registro = (Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setFechaDeSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            registro.setCodigoPresupuesto(Integer.parseInt(txtCodigoPresupuesto.getText()));
            //registro.setCodigoEmpresa(((Empresa)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            registro.setCodigoEmpresa(((Empresa)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date (registro.getFechaDeSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.setInt(4, registro.getCodigoEmpresa());
            procedimiento.execute();
            // procedimiento.setInt(4, registro.getCodigoEmpresa());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarPresupuestos(?,?,?,?)");
            Presupuesto registro = (Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setFechaDeSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            registro.setCodigoPresupuesto(Integer.parseInt(txtCodigoPresupuesto.getText()));
            registro.setCodigoEmpresa(((Presupuesto)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date (registro.getFechaDeSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.setInt(4, registro.getCodigoEmpresa());
            procedimiento.execute();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    
    
    // abre
    
     public void generarReporte(){
        switch (tipoOperacion) {
            case NINGUNO:
                    imprimirReporte();
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
                break;
            default:
                
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        File fichero2 = new File("src/org/jonathandomingo/report");
        String path2 = fichero2.getAbsolutePath();
        parametros.put("SUBREPORT_DIR", path2);
        GenerarReporte.mostrarReporte("ReportePresupuestos.jasper", "Reporte de Presupuesto", parametros);
    }
    
    
    /*
    public void imprimirReporteFactura() {
        if (tblEmpresas.getSelectionModel().isEmpty()){
            return;
        }

        Empresa emSeleccion = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
            int codEmpresa = emSeleccion.getCodigoEmpresa();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("codEmpresa", codEmpresa);

        GenerarReporte.mostrarReporte("ReporteFactura.jasper", "Reporte Factura", parametros);
    }
    */
    // cierra
    
    
    
    
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        //registro.setCantidadPresupuesto(0); // es auto increment
        registro.setFechaDeSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarPresupuesto(?,?,?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaDeSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
    
    public void desactivarControles(){
        txtCodigoPresupuesto.setEditable(false);
        fecha.selectedDateProperty().set(null);
        txtCantidadPresupuesto.setEditable(false);
        cmbComboBox.getSelectionModel().select(null);
    }
    
    public void activarControles(){
        txtCodigoPresupuesto.setEditable(false);
        fecha.selectedDateProperty().set(null);
        txtCantidadPresupuesto.setEditable(true);
        cmbComboBox.getSelectionModel().select(null);
    }
    
    public void limpiarControles(){
        txtCodigoPresupuesto.clear();
        fecha.selectedDateProperty().set(null);
        txtCantidadPresupuesto.clear();
        cmbComboBox.getSelectionModel().select(null);
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
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
}
