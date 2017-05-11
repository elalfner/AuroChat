/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion_server;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 *
 * @author Departamento
 */
public class Conexion_server {

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {

        try {
            ServerSocket miSS2 = new ServerSocket(2222);
            ServerSocket miSS3 = new ServerSocket(2223);
            ServerSocket miSS4 = new ServerSocket(2224);
            ServerSocket miSS5 = new ServerSocket(2225);
            ServerSocket miSS6 = new ServerSocket(2226);
            ServerSocket miSS7 = new ServerSocket(2227);
            ServerSocket miSS8 = new ServerSocket(2228);
            ServerSocket miSS30 = new ServerSocket(2230);

            CerrarSesion.miSS = miSS30;
            Contactos.miSS = miSS8;
            Grupos.miSS = miSS7;
            Conectados.miSS = miSS6;
            Mensajes.miSS = miSS5;
            Validar.miSS = miSS4;
            Registro.miSS = miSS3;
            Login.miSS = miSS2;
            

            Server ventana = new Server();
            ventana.setVisible(true);

            Thread login;
            Thread registro;
            Thread validar;
            Thread mensajes;
            Thread contactos;
            Thread grupos;
            Thread conectados;
            Thread cerrar;

            contactos = new Thread(new Contactos());
            login = new Thread(new Login());
            registro = new Thread(new Registro());
            validar = new Thread(new Validar());
            mensajes = new Thread(new Mensajes());
            grupos = new Thread(new Grupos());
            conectados = new Thread(new Conectados());
            cerrar = new Thread(new CerrarSesion());

            login.start();
            registro.start();
            validar.start();
            mensajes.start();
            contactos.start();
            grupos.start();
            conectados.start();
            cerrar.start();
            
            try {

                Runtime obj = Runtime.getRuntime();
                //obj.exec("C:\\wamp64\\wampmanager.exe");

                Process p = Runtime.getRuntime().exec("cmd /c start C:/xampp/xampp-control.exe");

                JOptionPane.showMessageDialog(null, "Debes aceptar la ejecucion de XamppServer\no el programa no funcionara correctamente\n\nSi arroja un error significa que no se encontro el XamppServer\nVuelva a instalar Aurochat con XamppServer integrado");

                int checkwamp = 1;
                do {
                    try {

                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        String url = "jdbc:mysql://localhost/test";
                        Connection con = DriverManager.getConnection(url, "root", "");
                        
                        String sql = "create database IF NOT EXISTS aurochat;";
                        Statement s = con.createStatement();
                        s.executeUpdate(sql);
                        
                        url = "jdbc:mysql://localhost/aurochat";
                        con = DriverManager.getConnection(url, "root", "");
                        
                        
                        sql = "CREATE TABLE IF NOT EXISTS `usuario` (`id` int(11) NOT NULL AUTO_INCREMENT,`username` text NOT NULL,`password` text NOT NULL,`fullname` text NOT NULL,PRIMARY KEY (id)) ENGINE=MyISAM DEFAULT CHARSET=latin1;";
                        s = con.createStatement();
                        s.executeUpdate(sql);
                        
                        s.close();
                        con.close();

                        Server.request.append("Xampp Server Status: OK\n");
                        
                        checkwamp = 1;
                        try {

                            String ips;

                            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
                            while (n.hasMoreElements()) {
                                NetworkInterface e = n.nextElement();

                                Enumeration<InetAddress> a = e.getInetAddresses();

                                while (a.hasMoreElements()) {
                                    InetAddress addr = a.nextElement();
                                    int indexOf = addr.getHostAddress().indexOf(':');

                                    if (indexOf == -1) {
                                        //Server.lbl_ips.setText(addr.getHostAddress());
                                        Server.ips.setText(Server.ips.getText() + addr.getHostAddress() + "\n");
                                    }
                                }

                            }
                            
                            
                        } catch (SocketException ex) {
                            Logger.getLogger(Conexion_server.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } catch (Exception ex) {
                        Server.request.append("Error: " + ex + "\n");
                        Server.request.append("Error: No se ha iniciado XamppServer\n");

                        checkwamp = 0;
                    }

                } while (checkwamp == 0);

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, "No se ha encontrado xamppserver en el sistema\nDesinstale AuroChatServer y vuelvalo a instalar con\nXamppServer incluido");
                //System.out.println(ex);
                ventana.dispatchEvent(new WindowEvent(ventana, WindowEvent.WINDOW_CLOSING));
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Ya se está ejecutando AuroChatServer");
        }

    }

}
