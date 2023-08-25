/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jonathandomingo.controller;
import javax.swing.JOptionPane;
/**
 *
 * @author Jonwk._.19
 */
public class BusquedaController {
    
    int idBusqueda;
    String proceso;

    public BusquedaController(int idBusqueda, String proceso) {
        this.idBusqueda = idBusqueda;
        this.proceso = proceso;
    }
    
    
    


//    public void BuscarId(){
//    
//        String input = JOptionPane.showInputDialog(null, "Ingrese el ID:", "Buscar por ID", JOptionPane.QUESTION_MESSAGE);
//        if (input != null) {
//            try {
//                int id = Integer.parseInt(input);
//                for (int i = 0; i < tableView.getItems().size(); i++) {
//                    YourObject item = tableView.getItems().get(i);
//                    if (item.getId() == id) {
//                        tableView.getSelectionModel().select(i);
//                        // Aquí puedes realizar otras acciones relacionadas con el elemento encontrado
//                        break;
//                    }
//                }
//            } catch (NumberFormatException e) {
//                JOptionPane.showMessageDialog(null, "ID inválido. Por favor, ingrese un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }  
//    }
 

  
}
