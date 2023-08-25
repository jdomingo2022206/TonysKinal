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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import org.jonathandomingo.bean.TipoEmpleado;
import org.jonathandomingo.bean.Empleado;
import org.jonathandomingo.db.Conexion;
import org.jonathandomingo.main.Principal;
/**
 *
 * @author informatica
 */
public class EmpleadoController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Empleado> listaEmpleado;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNombreEmpleado;
    @FXML private TextField txtDireccionEmpleado;
    @FXML private TextField txtGradoCocineroEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtApellidoEmpleado;
    @FXML private TextField txtTelefonoEmpleado;
    @FXML private TextField txtBuscarEmpleado;
    
    @FXML private ComboBox cmbComboBox;
    
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colNombreEmpleado;
    @FXML private TableColumn colApellidoEmpleado;
    @FXML private TableColumn colDireccionEmpleado;
    @FXML private TableColumn colTelefonoEmpleado;
    @FXML private TableColumn colGradoCocineroEmpleado;
    @FXML private TableColumn colTipoEmpleado;
    
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
        cmbComboBox.setItems(getTipoEmpleado());
    }
    
    public void cargarDatos(){
    tblEmpleados.setItems(getEmpleado());                         // casteo para cambiar a integer
    colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado")); // elige el value para guardar el valor
    colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado")); // elige el value para guardar el valor
    colNombreEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado")); // elige el value para guardar el valor
    colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado")); // elige el value para guardar el valor
    colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado")); // elige el value para guardar el valor
    colTelefonoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto")); // elige el value para guardar el valor
    colGradoCocineroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero")); // elige el value para guardar el valor
    colTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado")); // elige el value para guardar el valor

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
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista= new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleado"); // llamada al procedimiento de sql
            ResultSet resultado = procedimiento.executeQuery(); //obtencion del resulset
            while(resultado.next()){ // el next revisa si el resultado tiene un dato o no
                lista.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"), // Instanciar TipoEmpleado y nombre variable de sql
                            resultado.getString("descripcion"))); // nombre variable de sql
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }

    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoEmpleado(?)");
                procedimiento.setInt(1, codigoTipoEmpleado);
                ResultSet registro = procedimiento.executeQuery();
                while (registro.next()) {
                    resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                            registro.getString("descripcion"));
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
                    
                    tipoOperacion= EmpleadoController.operaciones.GUARDAR;
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

                    tipoOperacion = EmpleadoController.operaciones.NINGUNO;
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
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el reguistro?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleado(?)");
                            procedimiento.setInt(1,((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedItem());
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
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null){ //dato seleccionado o no
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
        
        Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
        registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registro.setApellidosEmpleado(txtApellidoEmpleado.getText());
        registro.setNombresEmpleado(txtNombreEmpleado.getText());
        registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
        registro.setTelefonoContacto(txtTelefonoEmpleado.getText());
        registro.setGradoCocinero(txtGradoCocineroEmpleado.getText());
        registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        System.out.println(registro.getCodigoEmpleado());
        System.out.println(registro.getNumeroEmpleado());
        System.out.println(registro.getApellidosEmpleado());
        System.out.println(registro.getNombresEmpleado());
        System.out.println(registro.getDireccionEmpleado());
        System.out.println(registro.getTelefonoContacto());
        System.out.println(registro.getGradoCocinero());
        System.out.println(registro.getCodigoTipoEmpleado());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarEmpleado(?,?,?,?,?,?,?,?)");
            //Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
            /*registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidosEmpleado(txtApellidoEmpleado.getText());
            registro.setNombresEmpleado(txtNombreEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoEmpleado.getText());
            registro.setGradoCocinero(txtGradoCocineroEmpleado.getText());
            registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());*/
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4, registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());
            procedimiento.setInt(8, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            //listaEmpleado.add(registro);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void guardar(){
       Empleado registro = new Empleado();
       registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
       registro.setApellidosEmpleado(txtApellidoEmpleado.getText());
       registro.setNombresEmpleado(txtNombreEmpleado.getText());
       registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
       registro.setTelefonoContacto(txtTelefonoEmpleado.getText());
       registro.setGradoCocinero(txtGradoCocineroEmpleado.getText());
       registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbComboBox.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
       try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("Call sp_AgregarEmpleado(?,?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2, registro.getApellidosEmpleado());
            procedimiento.setString(3, registro.getNombresEmpleado());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setString(6, registro.getGradoCocinero());
            procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(registro);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
    
    public void seleccionarElemento(){
        txtCodigoEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
        txtDireccionEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
        txtGradoCocineroEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
        txtNombreEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
        txtApellidoEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
        txtTelefonoEmpleado.setText(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        cmbComboBox.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado())); // pasar parametro de la tupla
    }
    
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(false);
        txtDireccionEmpleado.setEditable(false);
        txtGradoCocineroEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidoEmpleado.setEditable(false);
        txtTelefonoEmpleado.setEditable(false);
        cmbComboBox.getSelectionModel().select(null);
    }
    
    public void activarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNombreEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtGradoCocineroEmpleado.setEditable(true);
        txtNumeroEmpleado.setEditable(true);
        txtApellidoEmpleado.setEditable(true);
        txtTelefonoEmpleado.setEditable(true);
        cmbComboBox.getSelectionModel().select(null);
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.clear();
        txtNombreEmpleado.setText("");
        txtDireccionEmpleado.setText("");
        txtGradoCocineroEmpleado.setText("");
        txtNumeroEmpleado.setText("");
        txtApellidoEmpleado.setText("");
        txtTelefonoEmpleado.setText("");
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
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    
    
    // ... otros métodos del controlador ...

    public void buscarEmpleado() {
        FilteredList<Empleado> filter = new FilteredList<>(listaEmpleado, e -> true);

        txtBuscarEmpleado.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate((Empleado predicateEmpleado) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                // Aplicar el filtro de búsqueda
                if (String.valueOf(predicateEmpleado.getCodigoEmpleado()).contains(searchKey)) {
                    return true;
                } else if (predicateEmpleado.getNombresEmpleado().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmpleado.getApellidosEmpleado().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmpleado.getDireccionEmpleado().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmpleado.getTelefonoContacto().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmpleado.getGradoCocinero().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (String.valueOf(predicateEmpleado.getCodigoTipoEmpleado()).contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Empleado> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(tblEmpleados.comparatorProperty());
        tblEmpleados.setItems(sortList);
    }
    
    
}


