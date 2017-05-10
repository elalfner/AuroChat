/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion_server;

import java.net.Socket;

/**
 *
 * @author elalf
 */


public class Usuario {
    protected String nombre;
    protected Socket socket; 

    public Usuario() {
    }

    public Usuario(String nombre, Socket socket) {
        this.nombre = nombre;
        this.socket = socket;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    
}
