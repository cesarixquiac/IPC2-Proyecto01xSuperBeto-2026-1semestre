/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.utils;

import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.ClienteDAO;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.DestinoDAO;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.PaqueteDAO;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.ProveedorDAO;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.dao.UsuarioDAO;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Cliente;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Destino;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Paquete;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Proveedor;
import com.mycompany.ipc2.proyecto01xsuperbeto.semestre.agenciaviajes.models.Usuario;
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

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final DestinoDAO destinoDAO = new DestinoDAO();
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private final PaqueteDAO paqueteDAO = new PaqueteDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

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

                if (linea.isEmpty()) {
                    continue;
                }

                if (linea.startsWith("USUARIO")) {
                    Matcher m = pUsuario.matcher(linea);
                    if (m.matches()) {
                        String username = m.group(1);
                        String password = m.group(2);
                        int rol = Integer.parseInt(m.group(3));
                        Usuario nuevoUsuario = new Usuario(username, password, rol);
                        boolean exito = usuarioDAO.insertarUsuario(nuevoUsuario);
                        if (exito) {
                            System.out.println("Éxito: Usuario '" + username + "' guardado en la Base de Datos.");
                            registrosProcesados++;
                        } else {
                            System.err.println("Error de Base de Datos en línea " + numLinea + ": No se pudo guardar el usuario '" + username + "' (Revisa si está duplicado).");
                            erroresEncontrados++;
                        }

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

                        Destino nuevoDestino = new Destino(nombre, pais, descripcion);
                        boolean exito = destinoDAO.insertarDestino(nuevoDestino);

                        if (exito) {
                            System.out.println("Éxito: Destino '" + nombre + "' guardado.");
                            registrosProcesados++;
                        } else {
                            System.err.println("Error DB en línea " + numLinea + ": Destino '" + nombre + "' no guardado (¿Duplicado?).");
                            erroresEncontrados++;
                        }
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

                        Proveedor nuevoProveedor = new Proveedor(nombre, tipo, pais);
                        boolean exito = proveedorDAO.insertarProveedor(nuevoProveedor);

                        if (exito) {
                            System.out.println("Éxito: Proveedor '" + nombre + "' guardado.");
                            registrosProcesados++;
                        } else {
                            System.err.println("Error DB en línea " + numLinea + ": Proveedor '" + nombre + "' no guardado (¿Duplicado?).");
                            erroresEncontrados++;
                        }
                    } else {
                        erroresEncontrados++;
                        System.err.println("Error de Formato Lógico en línea " + numLinea + ": " + linea);
                    }
                } else if (linea.startsWith("PAQUETE")) {
                    Matcher m = pPaquete.matcher(linea);
                    if (m.matches()) {
                        String nombrePaquete = m.group(1);
                        String nombreDestino = m.group(2); // Ej: "Cancun"
                        int duracion = Integer.parseInt(m.group(3));
                        double precio = Double.parseDouble(m.group(4));
                        int capacidad = Integer.parseInt(m.group(5));

                        // Buscamos el ID del destino usando el nombre
                        int idDestinoEncontrado = destinoDAO.obtenerIdPorNombre(nombreDestino);

                        if (idDestinoEncontrado != -1) {
                            // Si existe creamos el paquete con el ID numero
                            Paquete nuevoPaquete = new Paquete(nombrePaquete, idDestinoEncontrado, duracion, precio, capacidad);
                            boolean exito = paqueteDAO.insertarPaquete(nuevoPaquete);

                            if (exito) {
                                System.out.println("Éxito: Paquete '" + nombrePaquete + "' guardado.");
                                registrosProcesados++;
                            } else {
                                System.err.println("Error DB en línea " + numLinea + ": No se guardó el paquete (¿Duplicado?).");
                                erroresEncontrados++;
                            }
                        } else {
                            // Error El archivo intento asignar un destino que no ha sido creado
                            System.err.println("Error Lógico en línea " + numLinea + ": El destino '" + nombreDestino + "' no existe en la base de datos.");
                            erroresEncontrados++;
                        }
                    } else {
                        erroresEncontrados++;
                        System.err.println("Error de Formato Lógico en línea " + numLinea + ": " + linea);
                    }
                } else if (linea.startsWith("CLIENTE")) {
                    Matcher m = pCliente.matcher(linea);
                    if (m.matches()) {
                        Cliente nuevo = new Cliente(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m.group(6));
                        if (clienteDAO.insertarCliente(nuevo)) {
                            System.out.println("Éxito: Cliente " + nuevo.getNombreCompleto() + " cargado.");
                            registrosProcesados++;
                        } else {
                            erroresEncontrados++;
                        }
                    }
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
