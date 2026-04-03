/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.utils.LectorCargaMasiva;
/**
 *
 * @author cesar
 */




public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de carga masiva...");
        LectorCargaMasiva lector = new LectorCargaMasiva();
        
       
        lector.procesarArchivo("datos.txt");
        
        System.out.println("Prueba finalizada. Revisa tu base de datos MySQL.");
    }
}