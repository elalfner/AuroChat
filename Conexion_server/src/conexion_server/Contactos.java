/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion_server;

import static conexion_server.Login.miSS;
import static conexion_server.Login.usuarios;
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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author elalf
 */
public class Contactos implements Runnable {

    public static ServerSocket miSS;

    public void run() {
        Socket conec = new Socket();
        do {
            try {

                conec = miSS.accept();

                InputStream inn = conec.getInputStream();
                ObjectInputStream in = new ObjectInputStream(inn);

                String json;
                json = (String) in.readObject();
                JSONObject jobj = new JSONObject(json);

                String username = jobj.getString("username");

                Class.forName("com.mysql.jdbc.Driver").newInstance();
                String url = "jdbc:mysql://localhost/aurochat";
                Connection con = DriverManager.getConnection(url, "root", "");

                String sql = "SELECT * FROM AMIGOS WHERE username1='" + username + "'";
                PreparedStatement s = con.prepareStatement(sql);
                ResultSet rs = s.executeQuery();
                JSONObject amig = new JSONObject();
                JSONArray nomb = new JSONArray();
                while (rs.next()) {
                    nomb.put(rs.getString("username2"));
                }

                sql = "SELECT * FROM AMIGOS WHERE username2='" + username + "'";
                s = con.prepareStatement(sql);
                rs = s.executeQuery();
                while (rs.next()) {
                    nomb.put(rs.getString("username1"));
                }

                amig.put("amigos", nomb);

                ObjectOutputStream out = new ObjectOutputStream(conec.getOutputStream());
                out.writeObject(amig.toString());

                s.close();
                con.close();

                conec.close();

            } catch (Exception ex) {
                Server.request.setText(Server.request.getText() + "Error: " + ex + "\n");
            }
        } while (true);
    }
}
