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
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.jonathandomingo.bean.Producto;
import org.jonathandomingo.db.Conexion;
import org.jonathandomingo.main.Principal;


import java.io.FileOutputStream;
import javafx.scene.control.TablePosition;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Ci5
 */
public class ProductoController implements Initializable{

    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion=operaciones.NINGUNO;
    private ObservableList<Producto> listaProducto;
    private Principal escenarioPrincipal;
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidadProducto;
    
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidadProducto;
    
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
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidadProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
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
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/jonathandomingo/images/Guardar.png"));
                    imgEliminar.setImage(new Image("/org/jonathandomingo/images/Cancel.png"));
                    
                    tipoDeOperacion= ProductoController.operaciones.GUARDAR;
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

                tipoDeOperacion = ProductoController.operaciones.NINGUNO;
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
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/jonathandomingo/images/New.png"));
                imgEliminar.setImage(new Image("/org/jonathandomingo/images/Eliminar.png"));
                
                tipoDeOperacion = ProductoController.operaciones.NINGUNO;
                break;
            default:
                if (tblProductos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el reguistro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProducto(?)");
                            procedimiento.setInt(1, ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProductos.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento para eliminar");
                }
        }
    }
    
    public void editar(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() != null){ //dato seleccionado o no
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/jonathandomingo/images/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/jonathandomingo/images/Cancel.png"));
                    activarControlesEditar();
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ActualizarProducto(?,?, ?)");
            Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtNombreProducto.getText()); // obtener datos del txt
            registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText())); // obtener datos del txt
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar(){
        Producto registro =new Producto();
        registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));  // tiene valor del id  
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProducto(?,?,?)");
            procedimiento.setString(1, Integer.toString(registro.getCodigoProducto()));
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setString(3, Integer.toString(registro.getCantidad()));

            procedimiento.execute();
            listaProducto.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void seleccionarElemento(){
        txtCodigoProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtNombreProducto.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
        txtCantidadProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
    }
    
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidadProducto.setEditable(false);
    }
    public void activarControles(){
        txtCodigoProducto.setEditable(true);
        txtNombreProducto.setEditable(true);
        txtCantidadProducto.setEditable(true);
    }
    
    public void activarControlesEditar(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(true);
        txtCantidadProducto.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtCantidadProducto.clear();
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
    
    
    
    /*
    // Método para exportar los datos de la TableView a un archivo Excel
    public void exportToExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Productos");

            // Crea la fila del encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Código Producto");
            headerRow.createCell(1).setCellValue("Nombre Producto");
            headerRow.createCell(2).setCellValue("Cantidad");

            // Llena el archivo Excel con los datos de la TableView
            ObservableList<TablePosition> selectedCells = tblProductos.getSelectionModel().getSelectedCells();
            for (TablePosition tablePosition : selectedCells) {
                int row = tablePosition.getRow();
                Producto producto = (Producto) tblProductos.getItems().get(row);
                Row excelRow = sheet.createRow(row + 1);
                excelRow.createCell(0).setCellValue(producto.getCodigoProducto());
                excelRow.createCell(1).setCellValue(producto.getNombreProducto());
                excelRow.createCell(2).setCellValue(producto.getCantidad());
            }

            // Ajusta el ancho de las columnas automáticamente
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guarda el archivo Excel
            try (FileOutputStream fileOut = new FileOutputStream("productos.xlsx")) {
                workbook.write(fileOut);
            }

            JOptionPane.showMessageDialog(null, "Archivo Excel creado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al exportar los datos a Excel.");
        }
    }*/
    
        // Método para exportar los datos de la TableView a un archivo Excel
    public void exportToExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Productos");

            // Crea la fila del encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Código Producto");
            headerRow.createCell(1).setCellValue("Nombre Producto");
            headerRow.createCell(2).setCellValue("Cantidad");

            // Llena el archivo Excel con los datos de la TableView
            int rowIndex = 1; // Iniciamos en 1 para omitir la fila del encabezado
            for (Producto producto : listaProducto) {
                Row excelRow = sheet.createRow(rowIndex);
                excelRow.createCell(0).setCellValue(producto.getCodigoProducto());
                excelRow.createCell(1).setCellValue(producto.getNombreProducto());
                excelRow.createCell(2).setCellValue(producto.getCantidad());
                rowIndex++;
            }

            // Ajusta el ancho de las columnas automáticamente
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guarda el archivo Excel
            try (FileOutputStream fileOut = new FileOutputStream("productos.xlsx")) {
                workbook.write(fileOut);
            }

            JOptionPane.showMessageDialog(null, "Archivo Excel creado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al exportar los datos a Excel.");
        }
    }

    
}
