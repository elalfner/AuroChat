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
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author elalf
 */
public class Registro implements Runnable {

    public static ServerSocket miSS;

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

                    String fullname = jobj.getString("fullname");
                    
                    jobj.put("status", "Problema del servidor... Intentalo más tarde");
                    
                    try {
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        String url = "jdbc:mysql://localhost/aurochat";
                        Connection con = DriverManager.getConnection(url, "root", "");

                        String sql = "INSERT INTO `usuario`(`username`, `password`, `fullname`) VALUES ('" + username + "','" + password + "','" + fullname + "')";
                        Statement s = con.createStatement();

                        s.executeUpdate(sql);

                        s.close();
                        con.close();

                        jobj.put("status", "OK");

                        Server.request.append("Se agrego un registro: " + jobj + "\n");


                    } catch (Exception e) {
                        Server.request.append("Error: " + e + "\n");
                    }
                    
                    ObjectOutputStream out = new ObjectOutputStream(conec.getOutputStream());
                    out.writeObject(jobj.toString());
                    
                    System.out.println(jobj);
                    
                    

                } catch (ClassNotFoundException ex) {
                    JSONObject jobje = new JSONObject();
                    jobje.put("status", "Problema del servidor... Intentalo más tarde");
                    
                    ObjectOutputStream out = new ObjectOutputStream(conec.getOutputStream());
                    out.writeObject(jobje.toString());
                }
                conec.close();
                
            } catch (IOException ex) {
                Logger.getLogger(Conexion_server.class.getName()).log(Level.SEVERE, null, ex);
                JSONObject jobje = new JSONObject();
            }

        } while (true);
    }
}
