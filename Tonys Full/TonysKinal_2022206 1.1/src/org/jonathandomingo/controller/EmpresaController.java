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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.jonathandomingo.bean.Empresa;
import org.jonathandomingo.db.Conexion;
import org.jonathandomingo.main.Principal;
import org.jonathandomingo.report.GenerarReporte;

/**
 *
 * @author informatica
 */
public class EmpresaController implements Initializable {
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO}; // pensar bien pq luego de utilizar no podremos cambiarlo para cambiar funcionalidad de los botones
    private operaciones tipoDeOperacion=operaciones.NINGUNO; // regresar al estado original del boton
    private ObservableList<Empresa> listaEmpresa;
    private Principal escenarioPrincipal;
    
    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private TextField txtTelefonoEmpresa;
    
    @FXML private TableView tblEmpresas;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccionEmpresa;
    @FXML private TableColumn colTelefonoEmpresa;
    
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
        tblEmpresas.setItems(getEmpresa());                         // casteo para cambiar a integer
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa")); // elige el value para guardar el valor
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa")); // elige el value para guardar el valor
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion")); // elige el value para guardar el valor
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono")); // elige el value para guardar el valor
    }
 
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
        switch (tipoDeOperacion) {
            case NINGUNO:
                    limpiarControles();
                    activarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    // btnEditar.setDisable(true); // desabilitar
                    btnEditar.setVisible(false);
                    btnReporte.setVisible(false);
                    imgNuevo.setImage(new Image("/org/jonathandomingo/images/Guardar.png"));
                    imgEliminar.setImage(new Image("/org/jonathandomingo/images/Cancel.png"));
                    
                    /*btnEliminar.setText("Guardar");
                    btnEditar.setText("Cancelar");
                    // btnEditar.setDisable(true); // desabilitar
                    btnNuevo.setVisible(false);
                    btnReporte.setVisible(false);
                    imgEliminar.setImage(new Image("/org/jonathandomingo/images/Guardar.png"));
                    imgEditar.setImage(new Image("/org/jonathandomingo/images/Cancel.png"));*/
                    
                    tipoDeOperacion= operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                // btnEditar.setDisable(true); // desabilitar
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                imgNuevo.setImage(new Image("/org/jonathandomingo/images/New.png"));
                imgEliminar.setImage(new Image("/org/jonathandomingo/images/Eliminar.png"));
                
                /*btnEliminar.setText("Eliminar");
                btnEditar.setText("Editar");
                // btnEditar.setDisable(true); // desabilitar
                btnNuevo.setVisible(true);
                btnReporte.setVisible(true);
                imgEliminar.setImage(new Image("/org/jonathandomingo/images/Eliminar.png"));
                imgEditar.setImage(new Image("/org/jonathandomingo/images/Editar.png"));*/
                
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                
                break;
            default:
                throw new AssertionError();
        }
    }
    
    public void eliminar(){
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                // btnEditar.setDisable(true); // desabilitar
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                imgNuevo.setImage(new Image("/org/jonathandomingo/images/New.png"));
                imgEliminar.setImage(new Image("/org/jonathandomingo/images/Eliminar.png"));
                
                /*btnEliminar.setText("Eliminar");
                btnEditar.setText("Editar");
                // btnEditar.setDisable(true); // desabilitar
                btnNuevo.setVisible(true);
                btnReporte.setVisible(true);
                imgEliminar.setImage(new Image("/org/jonathandomingo/images/Eliminar.png"));
                imgEditar.setImage(new Image("/org/jonathandomingo/images/Editar.png"));*/
                
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el reguistro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpresa(?)");
                            procedimiento.setInt(1,((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                            procedimiento.execute();
                            listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedItem());
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
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null){ //dato seleccionado o no
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/jonathandomingo/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/jonathandomingo/images/Cancel.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
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
                tipoDeOperacion = operaciones.NINGUNO;
            default:
                throw new AssertionError();
        }
    }
    
    public void reporte(){
        switch (tipoDeOperacion) {
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
            default:
                
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarEmpresa (?,?,?,?)");
            Empresa registro = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText()); // obtener datos del txt
            registro.setDireccion(txtDireccionEmpresa.getText()); // obtener datos del txt
            registro.setTelefono(txtTelefonoEmpresa.getText()); // obtener datos del txt
            procedimiento.setInt(1, registro.getCodigoEmpresa());
            procedimiento.setString(2, registro.getNombreEmpresa());
            procedimiento.setString(3, registro.getDireccion());
            procedimiento.setString(4, registro.getTelefono());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar(){
        Empresa registro = new Empresa();
        // registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));  // tiene valor nulo
        registro.setNombreEmpresa(txtNombreEmpresa.getText());  
        registro.setDireccion(txtDireccionEmpresa.getText());
        registro.setTelefono(txtTelefonoEmpresa.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpresa (?, ?, ?)");
            procedimiento.setString(1, registro.getNombreEmpresa());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getTelefono());
            procedimiento.execute();
            listaEmpresa.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    // abre
    
     public void generarReporte(){
        switch (tipoDeOperacion) {
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
                    tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresas", null);
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
    }
    
    
    
    public void imprimirReporteGeneral() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpresas.getSelectionModel().isEmpty()){
                    return;
                }

                Empresa emSeleccion = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
                    int codEmpresa = emSeleccion.getCodigoEmpresa();

                Map<String, Object> parametros = new HashMap<>();
                parametros.put("codEmpresa", codEmpresa);

                GenerarReporte.mostrarReporte("ReporteGeneral.jasper", "Reporte General", parametros);
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
            default:
                
        }
        
        
        
        
    }
    
    // cierra
    
    
    
    
    
    
    
    public void seleccionarElemento(){
        txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
        txtDireccionEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
        txtTelefonoEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
    }
        
    public void desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }
    public void activarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }
    public void limpiarControles(){
        txtCodigoEmpresa.clear();
        txtNombreEmpresa.clear();
        txtDireccionEmpresa.clear();
        txtTelefonoEmpresa.clear();
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
    
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    };
    
    
    /*public void BuscarId(){
    
        String input = JOptionPane.showInputDialog(null, "Ingrese el ID:", "Buscar por ID", JOptionPane.QUESTION_MESSAGE);
        if (input == null) {
            return; // Salir del método si no se ingresó ningún valor
        }
        try {
            int id = Integer.parseInt(input);
            boolean bandera = false;
            for (int i = 0; i < tblEmpresas.getItems().size(); i++) {
                Empresa item = (Empresa) tblEmpresas.getItems().get(i);
                if (item.getCodigoEmpresa() == id) {
                    tblEmpresas.getSelectionModel().select(i);
                    seleccionarElementoBuscar(i);
                    bandera = true;
                    break;
                }
            }
            if (bandera == false){
                JOptionPane.showMessageDialog(null, "ID inexistente o no encontrado.", "No Encontrado", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido. Por favor, ingrese un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        } 
    }*/
    
    
    
    public void BuscarId(){

        boolean reintentar = false;
        do {
            String input = JOptionPane.showInputDialog(null, "Ingrese el ID:", "Buscar por ID", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                return; // Salir del método si no se ingresó ningún valor
            }
            try {
                int id = Integer.parseInt(input);
                boolean bandera = false;
                for (int i = 0; i < tblEmpresas.getItems().size(); i++) {
                    Empresa item = (Empresa) tblEmpresas.getItems().get(i);
                    if (item.getCodigoEmpresa() == id) {
                        tblEmpresas.getSelectionModel().select(i);
                        seleccionarElementoBuscar(i);
                        bandera = true;
                        reintentar = false;
                        break;
                    }
                }
                if (bandera == false){
                    JOptionPane.showMessageDialog(null, "ID inexistente o no encontrado.", "No Encontrado", JOptionPane.ERROR_MESSAGE);
                    reintentar = true;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID inválido. Por favor, ingrese un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                reintentar = true;
            }
        }while(reintentar==true);  
    }
    
    /*public void BuscarId(){

        boolean reintentar = true;
        while(reintentar==true){
            String input = JOptionPane.showInputDialog(null, "Ingrese el ID:", "Buscar por ID", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                return; // Salir del método si no se ingresó ningún valor
            }
            try {
                int id = Integer.parseInt(input);
                boolean bandera = false;
                for (int i = 0; i < tblEmpresas.getItems().size(); i++) {
                    Empresa item = (Empresa) tblEmpresas.getItems().get(i);
                    if (item.getCodigoEmpresa() == id) {
                        tblEmpresas.getSelectionModel().select(i);
                        seleccionarElementoBuscar(i);
                        bandera = true;
                        reintentar = false;
                        break;
                    }
                }
                if (bandera == false){
                    JOptionPane.showMessageDialog(null, "ID inexistente o no encontrado.", "No Encontrado", JOptionPane.ERROR_MESSAGE);
                    reintentar = true;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID inválido. Por favor, ingrese un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                reintentar = true;
            }
        };  
    }*/
    
    public void seleccionarElementoBuscar(int i){
        tblEmpresas.getSelectionModel().select(i);
        Empresa empresaSeleccionada = (Empresa) tblEmpresas.getSelectionModel().getSelectedItem();
    
        txtCodigoEmpresa.setText(String.valueOf(empresaSeleccionada.getCodigoEmpresa()));
        txtNombreEmpresa.setText(String.valueOf(empresaSeleccionada.getNombreEmpresa()));
        txtDireccionEmpresa.setText(String.valueOf(empresaSeleccionada.getDireccion()));
        txtTelefonoEmpresa.setText(String.valueOf(empresaSeleccionada.getTelefono()));
    }
    
}












