/*
Programador : Jonathan Domingo
Codigo Técnico: IN5BV
Carnet: 2022-206
Fecha de Creación: 14/04/2023
Fecha de Modificación: 
Fecha de Finalización: 
 */
package org.jonathandomingo.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jonathandomingo.controller.EmpleadoController;
import org.jonathandomingo.controller.EmpresaController;
import org.jonathandomingo.controller.LoginController;
import org.jonathandomingo.controller.MenuPrincipalController;
import org.jonathandomingo.controller.PlatoController;
import org.jonathandomingo.controller.PresupuestoController;
import org.jonathandomingo.controller.ProductoController;
import org.jonathandomingo.controller.ProductoHasPlatoController;
import org.jonathandomingo.controller.ProgramadorController;
import org.jonathandomingo.controller.ServicioHasEmpleadoController;
import org.jonathandomingo.controller.ServicioHasPlatoController;
import org.jonathandomingo.controller.ServiciosController;
import org.jonathandomingo.controller.TipoEmpleadoController;
import org.jonathandomingo.controller.TipoPlatoController;
import org.jonathandomingo.controller.UsuarioController;



public class Principal extends Application {
    
    private final String PAQUETE_VISTA = "/org/jonathandomingo/view/";
    // Objeto, Aca sera como un escenario donde apareceran todas las escenas, 
    //este no se diseña aca sera donde Java montara las escenas
    // todos estos metodos deberan ser creados en el main
    private Stage escenarioPrincipal;
    // Estas seran las que apareceran dentro del stage, 
    //las escenas deberan ser mas pequeñas que el escenario principal
    // todos estos metodos deberan ser creados en el main
    private Scene escena;
    
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal=escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tonys Kinal 2023"); //nombre que aparecera arriba en el programa
        this.escenarioPrincipal.getIcons().add(new Image ("/org/jonathandomingo/images/LogoFondo.png"));
                                                            //absoluta o relativa
//       Parent root = FXMLLoader.load(getClass().getResource("/org/jonathandomingo/view/MenuPrincipalView.fxml")); //ruta absoluta
//       Parent root = FXMLLoader.load(getClass().getResource("/org/jonathandomingo/view/ProgramadorView.fxml")); //ruta absoluta
//       Parent root = FXMLLoader.load(getClass().getResource("/org/jonathandomingo/view/EmpresaView.fxml")); //ruta absoluta
//       Scene escena= new Scene(root);
//       escenarioPrincipal.setScene(escena);
        // ponemos la escena a mostrar
        // menuPrincipal();
        ventanaLogin();
        //ventanaServicioHasEmpleado();
        escenarioPrincipal.show();
    }

    public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml",800,700);
            menu.setEsenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try {
            ProgramadorController programador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml",800,700);
            programador.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpresa(){
        try {
            EmpresaController empresa = (EmpresaController) cambiarEscena("EmpresaView.fxml",800,700);
            empresa.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaPresupuesto(){
        try {
            PresupuestoController presupuesto=(PresupuestoController) cambiarEscena("PresupuestoView.fxml",800,700);
            presupuesto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProducto(){
        try {
            ProductoController producto = (ProductoController) cambiarEscena("ProductoView.fxml",800,700);
            producto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoPlato(){
        try {
            TipoPlatoController tipoPlato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml",800,700);
            tipoPlato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoEmpleado(){
        try {
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml",800,700);
            tipoEmpleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpleado(){
        try {
            EmpleadoController Empleado = (EmpleadoController) cambiarEscena("EmpleadoView.fxml",1269,700);
            Empleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaNuevoUsuario(){
        try {
            UsuarioController newUsuario = (UsuarioController) cambiarEscena("UsuarioView.fxml",800,524);
            newUsuario.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaLogin(){
        try {
            LoginController Login = (LoginController) cambiarEscena("LoginView.fxml",800,524);
            Login.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaPlato(){
        try {
            PlatoController Plato = (PlatoController) cambiarEscena("PlatoView.fxml",1012,700);
            Plato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProductoHasPlato(){
        try {
            ProductoHasPlatoController ProductoHasPlato = (ProductoHasPlatoController) cambiarEscena("ProductoHasPlatoView.fxml",800,700);
            ProductoHasPlato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicioHasPlato(){
        try {
            ServicioHasPlatoController ServicioHasPlato = (ServicioHasPlatoController) cambiarEscena("ServicioHasPlatoView.fxml",800,700);
            ServicioHasPlato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicio(){
        try {
            ServiciosController Servicio = (ServiciosController) cambiarEscena("ServicioView.fxml",1199 ,700);
            Servicio.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicioHasEmpleado(){
        try {
            ServicioHasEmpleadoController ServicioHasEmpleado = (ServicioHasEmpleadoController) cambiarEscena("ServicioHasEmpleadoView.fxml",1199 ,700);
            ServicioHasEmpleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado=null;
        FXMLLoader cargadorFXML= new FXMLLoader();
        InputStream archivo= Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado =(Initializable)cargadorFXML.getController();
        return resultado;
    }
    

    
}


