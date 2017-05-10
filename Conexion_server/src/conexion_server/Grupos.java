/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion_server;

import static conexion_server.Contactos.miSS;
import static conexion_server.Login.usuarios;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author elalf
 */
public class Grupos implements Runnable {
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

                String sql = "SELECT * FROM GRUPOS,REL_USU_GRUPO,USUARIO WHERE GRUPOS.id=REL_USU_GRUPO.id_grupo and USUARIO.id=REL_USU_GRUPO.id_usuario and USUARIO.username='" + username + "'";
                PreparedStatement s = con.prepareStatement(sql);
                ResultSet rs = s.executeQuery();
                JSONObject amig = new JSONObject();
                JSONArray nomb = new JSONArray();
                
                while (rs.next()) {
                    nomb.put(rs.getString("grupos.nombre"));
                }

                amig.put("grupo", nomb);

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
