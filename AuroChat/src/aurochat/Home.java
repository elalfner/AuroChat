package aurochat;

import static aurochat.Login.ip;
import java.awt.Font;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.json.JSONObject;

public class Home extends JFrame implements ActionListener {

    private final JLabel style_bar;
    private JLabel user = new JLabel("Inbox", SwingConstants.CENTER);
    private final JButton add;
    private JButton contacts = new JButton("Contactos");
    private JButton online = new JButton("Conectados");
    private JButton groups = new JButton("Grupos");
    public static JList list = new JList();
    private JTextArea chat_show = new JTextArea();
    private JTextArea message = new JTextArea();
    private JButton send;
    private ImageIcon barra = new ImageIcon("images/barra_inicio.jpg");
    private ImageIcon usricon = new ImageIcon("images/addusr.png");
    private ImageIcon sendicon = new ImageIcon("images/send.png");
    private ImageIcon wpicon = new ImageIcon("images/wp2.png");

    public Home() {
        super("AuroChat");
        this.style_bar = new JLabel(barra);
        this.add = new JButton(usricon);
        this.send = new JButton(sendicon);
        this.add.setOpaque(false);
        this.add.setContentAreaFilled(false);
        this.add.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(27, 34, 52)));
        this.add.setForeground(Color.WHITE);
        this.send.setForeground(Color.WHITE);
        this.send.setBackground(Color.LIGHT_GRAY);
        this.send.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.LIGHT_GRAY));

        this.list.setBackground(new Color(47, 54, 62));
        this.list.setForeground(Color.WHITE);

        this.message.setBackground(Color.LIGHT_GRAY);

        this.user.setFont(new Font("Trebuchet MS", Font.PLAIN, 26));
        this.user.setForeground(Color.WHITE);

        this.contacts.setOpaque(false);
        this.contacts.setContentAreaFilled(false);
        this.contacts.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        this.contacts.setForeground(Color.WHITE);
        this.contacts.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));

        this.groups.setOpaque(false);
        this.groups.setContentAreaFilled(false);
        this.groups.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        this.groups.setForeground(Color.WHITE);
        this.groups.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));

        this.online.setOpaque(false);
        this.online.setContentAreaFilled(false);
        this.online.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        this.online.setForeground(Color.WHITE);
        this.online.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));

        GroupLayout orden = new GroupLayout(this.getContentPane());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(37, 44, 52));

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        send.addActionListener(this);
        //list.addActionListener(this);

        orden.setHorizontalGroup(
                orden.createParallelGroup()
                        .addComponent(style_bar, 800, 800, 800)
                        .addGroup(
                                orden.createSequentialGroup()
                                        .addGroup(
                                                orden.createParallelGroup()
                                                        .addGroup(
                                                                orden.createSequentialGroup()
                                                                        .addComponent(user, 100, 100, 100)
                                                                        .addComponent(add)
                                                        )
                                                        .addComponent(contacts, 150, 150, 150)
                                                        .addComponent(online, 150, 150, 150)
                                                        .addComponent(groups, 150, 150, 150)
                                        )
                                        .addComponent(list, 250, 250, 250)
                                        .addGroup(
                                                orden.createParallelGroup()
                                                        .addComponent(chat_show, 400, 400, 400)
                                                        .addGroup(
                                                                orden.createSequentialGroup()
                                                                        .addComponent(message)
                                                                        .addComponent(send)
                                                        )
                                        )
                        )
        );

        orden.setVerticalGroup(
                orden.createSequentialGroup()
                        .addComponent(style_bar, 15, 15, 15)
                        .addGroup(
                                orden.createParallelGroup()
                                        .addGroup(
                                                orden.createSequentialGroup()
                                                        .addGroup(
                                                                orden.createParallelGroup()
                                                                        .addComponent(user, 70, 70, 70)
                                                                        .addComponent(add, 70, 70, 70)
                                                        )
                                                        .addComponent(contacts, 50, 50, 50)
                                                        .addComponent(online, 50, 50, 50)
                                                        .addComponent(groups, 50, 50, 50)
                                        )
                                        .addComponent(list, 350, 350, 350)
                                        .addGroup(
                                                orden.createSequentialGroup()
                                                        .addComponent(chat_show, 300, 300, 300)
                                                        .addGroup(
                                                                orden.createParallelGroup()
                                                                        .addComponent(message, 50, 50, 50)
                                                                        .addComponent(send, 50, 50, 50)
                                                        )
                                        )
                        )
        );

        setLayout(orden);
        this.pack();

        DefaultListModel modelo = new DefaultListModel();
        modelo.addElement("Hola");
        list.setModel(modelo);

        Thread ObtConectados;
        ObtConectados = new Thread(new ObtConectados());
        ObtConectados.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == send) {
            Socket socket;

            try {

                socket = new Socket(Login.ip, 2225);

                String mensaje;
                mensaje = message.getText();
                chat_show.append(mensaje + "\n");
                message.setText(" ");

                Object destinatario = list.getSelectedValue();
                destinatario.toString();

                JSONObject json = new JSONObject();
                json.put("destinatario", destinatario);

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(json.toString());

                InputStream inn = socket.getInputStream();
                ObjectInputStream in = new ObjectInputStream(inn);
                String jobj = (String) in.readObject();
                json = new JSONObject(jobj);

                String hora = json.getString("hora");

                chat_show.append(hora + "\n");

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        JOptionPane.showMessageDialog(null, "Cerrando sesión");
        Socket socket;
        try {
            socket = new Socket(ip, 2230);

            JSONObject json = new JSONObject();
            json.put("username", Login.usernom);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(json.toString());

            InputStream inn = socket.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inn);

            String jsonn = (String) in.readObject();
            JSONObject jsonnn = new JSONObject(jsonn);

        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listMouseClicked(java.awt.event.MouseEvent evt) {
        Cambio();
        Object usuario;
        chat_show.setText("");
        usuario = list.getSelectedValue();
        usuario.toString();
        //chat_selected.setText(usuario);
        FileReader documento = null;
        try {
            String Lineas;
            //chat_selected.getText()
            documento = new FileReader(usuario + ".txt");
            BufferedReader buffer = new BufferedReader(documento);
            while ((Lineas = buffer.readLine()) != null) {
                chat_show.append(Lineas + "\n");
            }
            buffer.close();
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
    }

    void Cambio() {
        String texto;
        texto = chat_show.getText();
        Object usuario;
        usuario = list.getSelectedValue();
        usuario.toString();
        try {
            File ArchivoTexto = new File(usuario + ".txt");
            FileWriter Escribir = new FileWriter(ArchivoTexto);
            BufferedWriter Buffer = new BufferedWriter(Escribir);
            PrintWriter Imprimir = new PrintWriter(Buffer);
            Escribir.write(texto);
            Escribir.close();
        } catch (Exception E) {
            System.out.println(E.getMessage());
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatProvisional.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatProvisional.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatProvisional.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatProvisional.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }
}
