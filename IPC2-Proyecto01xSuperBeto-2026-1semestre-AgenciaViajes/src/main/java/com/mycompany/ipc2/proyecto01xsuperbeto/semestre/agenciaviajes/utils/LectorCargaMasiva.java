/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author cesar
 */
public class LectorCargaMasiva {


    // Se captura lo que está entre paréntesis (group) ignorando los espacios y las comillas.
    
    private static final String REGEX_USUARIO = "^USUARIO\\(\"([^\"]+)\",\\s*\"([^\"]+)\",\\s*(\\d+)\\)$";
    private static final String REGEX_DESTINO = "^DESTINO\\(\"([^\"]+)\",\\s*\"([^\"]+)\",\\s*\"([^\"]+)\"\\)$";
    private static final String REGEX_PROVEEDOR = "^PROVEEDOR\\(\"([^\"]+)\",\\s*(\\d+),\\s*\"([^\"]+)\"\\)$";
    private static final String REGEX_PAQUETE = "^PAQUETE\\(\"([^\"]+)\",\\s*\"([^\"]+)\",\\s*(\\d+),\\s*([0-9.]+),\\s*(\\d+)\\)$";
    private static final String REGEX_SERVICIO_PAQUETE = "^SERVICIO_PAQUETE\\(\"([^\"]+)\",\\s*\"([^\"]+)\",\\s*\"([^\"]+)\",\\s*([0-9.]+)\\)$";
    private static final String REGEX_CLIENTE = "^CLIENTE\\(\"([^\"]+)\",\\s*\"([^\"]+)\",\\s*\"([^\"]+)\",\\s*\"([^\"]+)\",\\s*\"([^\"]+)\",\\s*\"([^\"]+)\"\\)$";
    
    // En grupo 2 ([^,]+) capturamos el agente (ej. jperez) que no trae comillas en el PDF
    private static final String REGEX_RESERVACION = "^RESERVACION\\(\"([^\"]+)\",\\s*([^,]+),\\s*\"([^\"]+)\",\\s*\"([^\"]+)\"\\)$";
    private static final String REGEX_PAGO = "^PAGO\\(\"([^\"]+)\",\\s*([0-9.]+),\\s*(\\d+),\\s*\"([^\"]+)\"\\)$";

    // Compilar los patrones para mayor eficiencia
    private final Pattern pUsuario = Pattern.compile(REGEX_USUARIO);
    private final Pattern pDestino = Pattern.compile(REGEX_DESTINO);
    private final Pattern pProveedor = Pattern.compile(REGEX_PROVEEDOR);
    private final Pattern pPaquete = Pattern.compile(REGEX_PAQUETE);
    private final Pattern pServicio = Pattern.compile(REGEX_SERVICIO_PAQUETE);
    private final Pattern pCliente = Pattern.compile(REGEX_CLIENTE);
    private final Pattern pReservacion = Pattern.compile(REGEX_RESERVACION);
    private final Pattern pPago = Pattern.compile(REGEX_PAGO);

    public void procesarArchivo(String rutaArchivo) {
        int registrosProcesados = 0;
        int erroresEncontrados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numLinea = 0;

            while ((linea = br.readLine()) != null) {
                numLinea++;
                linea = linea.trim();

                if (linea.isEmpty()) continue;

                if (linea.startsWith("USUARIO")) {
                    Matcher m = pUsuario.matcher(linea);
                    if (m.matches()) {
                        String username = m.group(1);
                        String password = m.group(2);
                        int rol = Integer.parseInt(m.group(3));
                        System.out.println("Cargando Usuario: " + username);
                 
                        registrosProcesados++;
                    } else {
                        erroresEncontrados++;
                        System.err.println("Error de Formato Lógico en línea " + numLinea + ": " + linea);
                    }

                } else if (linea.startsWith("DESTINO")) {
                    Matcher m = pDestino.matcher(linea);
                    if (m.matches()) {
                        String nombre = m.group(1);
                        String pais = m.group(2);
                        String descripcion = m.group(3);
                        System.out.println("Cargando Destino: " + nombre);
                        
                        registrosProcesados++;
                    } else {
                        erroresEncontrados++;
                        System.err.println("Error de Formato Lógico en línea " + numLinea + ": " + linea);
                    }

                } else if (linea.startsWith("PROVEEDOR")) {
                    Matcher m = pProveedor.matcher(linea);
                    if (m.matches()) {
                        String nombre = m.group(1);
                        int tipo = Integer.parseInt(m.group(2));
                        String pais = m.group(3);
                        System.out.println("Cargando Proveedor: " + nombre);
                       
                        registrosProcesados++;
                    } else {
                        erroresEncontrados++;
                        System.err.println("Error de Formato Lógico en línea " + numLinea + ": " + linea);
                    }

                } 
              
                
                else {
                    erroresEncontrados++;
                    System.err.println("Instrucción desconocida en línea " + numLinea + ": " + linea);
                }
            }

            
            System.out.println("--- RESUMEN DE CARGA ---");
            System.out.println("Registros procesados con éxito: " + registrosProcesados);
            System.out.println("Errores encontrados: " + erroresEncontrados);

        } catch (Exception e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
