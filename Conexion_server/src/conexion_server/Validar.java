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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.JSONObject;

/**
 *
 * @author elalf
 */
public class Validar implements Runnable {

    public static ServerSocket miSS;

    public void run() {

        do {
            Socket conec = new Socket();

            try {

                conec = miSS.accept();

                InputStream inn = conec.getInputStream();
                ObjectInputStream in = new ObjectInputStream(inn);

                String username, status;
                
                JSONObject jobj = new JSONObject();

                try {
                    username = (String) in.readObject();
                    int enc=1;
                    String encc;
                    
                    jobj.put("status", "Problema del servidor... Intentalo más tarde");

                    try {
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        String url = "jdbc:mysql://localhost/aurochat";
                        Connection con = DriverManager.getConnection(url, "root", "");

                        String sql = "SELECT * FROM USUARIO WHERE username='" + username + "'";

                        PreparedStatement s = con.prepareStatement(sql);

                        ResultSet rs = s.executeQuery();
                        enc = 0;
                        while (rs.next()) {
                            enc++;
                        }

                        encc = enc + "";
                        s.close();
                        con.close();

                        jobj.put("status", "OK");

                    } catch (Exception e) {
                        Server.request.append("Error: " + e + "\n");
  
                    }
                    
                    encc = enc + "";
                    
                    jobj.put("enc",encc);
                    
                    ObjectOutputStream out = new ObjectOutputStream(conec.getOutputStream());
                    out.writeObject(jobj.toString());
                    
                conec.close();
                } catch (ClassNotFoundException ex) {
                    Server.request.append("Error: " + ex + "\n");
                }
            } catch (IOException ex) {
                Server.request.append("Error: " + ex + "\n");
            }

        } while (true);
    }

}
