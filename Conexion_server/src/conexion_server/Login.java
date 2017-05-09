/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author elalf
 */
public class Login implements Runnable {

    public static ServerSocket miSS;
    public static ArrayList<Usuario> usuarios = new ArrayList<>();

    public void run() {

        do {
            Socket conec = new Socket();

            try {

                conec = miSS.accept();
                
                InputStream inn = conec.getInputStream();
                ObjectInputStream in = new ObjectInputStream(inn);

                String json;

                try {
                    json = (String) in.readObject();
                    JSONObject jobj = new JSONObject(json);

                    String username = jobj.getString("username");
                    String password = jobj.getString("password");

                    String fullname = "";                    
                    jobj.put("status", "Problema del servidor... Intentalo más tarde");

                    try {
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        String url = "jdbc:mysql://localhost/aurochat";
                        Connection con = DriverManager.getConnection(url, "root", "");

                        String sql = "SELECT * FROM USUARIO WHERE username='" + username + "' AND PASSWORD = '" + password + "'";

                        PreparedStatement s = con.prepareStatement(sql);
                        ResultSet rs = s.executeQuery();
                        
                        jobj.put("status", "Usuario o Contraseña incorrectos");
                        while (rs.next()) {
                            fullname = rs.getString("fullname");
                            jobj.put("status", "OK");
                            Usuario us= new Usuario(username,conec);
                            usuarios.add(us);
                        }
                        s.close();
                        con.close();
                        Server.request.append("Peticion para ingresar: " + jobj + "\n");                        

                    } catch (Exception e) {
                        Server.request.append("Error: " + e + "\n");
                        Server.request.append("Error: No se ha iniciado XamppServer\n");
                    }
                    
                    jobj.put("fullname", fullname);
                    
                    ObjectOutputStream out = new ObjectOutputStream(conec.getOutputStream());
                    out.writeObject(jobj.toString());
                    
                    conec.close();

                } catch (Exception ex) {
                    Server.request.setText(Server.request.getText() + "Error: " + ex + "\n");
                    System.out.println(ex);
                }
                
                
            } catch (Exception ex) {
                Server.request.setText(Server.request.getText() + "Error: " + ex + "\n");
                System.out.println(ex+"1");
            }
            

        } while (true);
    }

}
