/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import clases.Conexion;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Alfredo
 */
public class Ingresar extends javax.swing.JFrame {

    /**
     * Creates new form Ingresar
     */
    
    @Override
    public Image getIconImage() {
        Image setValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("imagenes/Protologo_3_192x192.png"));
        return setValue;
    }
    
    public static String usuario;
    private String clave = "";

    private Opciones instancia;

    public Opciones getOpciones() {
        if (instancia == null) {
            return instancia = new Opciones();
        } else {
            return instancia;
        }
    }

    public void autenticacion() {
        usuario = ENTRADA_USUARIO.getText().trim();
        clave = ENTRADA_CONTRASEÑA.getText().trim();

        if (!usuario.equals("") || !clave.equals("")) {

            try {
                Connection cn = Conexion.conetar();
                PreparedStatement pst = cn.prepareStatement(
                        "select clave, nombre from usuarios where nombre= '" + usuario
                        + "' and clave = '" + clave + "'");
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    getOpciones().setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "clave o ususario incorrectos");
                    ENTRADA_CONTRASEÑA.setText("");
                    ENTRADA_USUARIO.setText("");
                }
                cn.close();
            } catch (SQLException e) {
                System.err.println("Falla en la clase 'Ingresar.java', linea 59: " + e.getErrorCode() + "\n" + e);
                JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Llena los campos");
        }
    }

    public Ingresar() {
        initComponents();
        this.setLocationRelativeTo(null);
        Conexion.solucionador();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ENTRADA_USUARIO = new javax.swing.JTextField();
        ENTRADA_CONTRASEÑA = new javax.swing.JPasswordField();
        BOTON_ACCEDER = new javax.swing.JButton();
        BOTON_CANCELAR = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Leydi app");
        setBackground(new java.awt.Color(255, 255, 255));
        setIconImage(getIconImage());
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ENTRADA_USUARIO.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ENTRADA_USUARIO.setBorder(null);
        getContentPane().add(ENTRADA_USUARIO, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 266, 244, 20));

        ENTRADA_CONTRASEÑA.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ENTRADA_CONTRASEÑA.setBorder(null);
        getContentPane().add(ENTRADA_CONTRASEÑA, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 330, 244, 20));

        BOTON_ACCEDER.setBackground(new java.awt.Color(0, 0, 0));
        BOTON_ACCEDER.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        BOTON_ACCEDER.setForeground(new java.awt.Color(255, 255, 255));
        BOTON_ACCEDER.setText("Acceder");
        BOTON_ACCEDER.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BOTON_ACCEDER.setBorderPainted(false);
        BOTON_ACCEDER.setFocusable(false);
        BOTON_ACCEDER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BOTON_ACCEDERActionPerformed(evt);
            }
        });
        getContentPane().add(BOTON_ACCEDER, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, 80, 30));

        BOTON_CANCELAR.setBackground(new java.awt.Color(0, 0, 0));
        BOTON_CANCELAR.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        BOTON_CANCELAR.setForeground(new java.awt.Color(255, 255, 255));
        BOTON_CANCELAR.setText("Salir");
        BOTON_CANCELAR.setBorder(null);
        BOTON_CANCELAR.setBorderPainted(false);
        BOTON_CANCELAR.setFocusable(false);
        BOTON_CANCELAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BOTON_CANCELARActionPerformed(evt);
            }
        });
        getContentPane().add(BOTON_CANCELAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 380, 80, 30));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Usuario:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 246, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Clave:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(159, 310, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Protologo_1_192x192.png"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo_1000x1000.png"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, 430));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BOTON_CANCELARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BOTON_CANCELARActionPerformed
        int a = JOptionPane.showConfirmDialog(null, "¿Deseas salir?");
        if (a == 0) {
            this.dispose();
        }
    }//GEN-LAST:event_BOTON_CANCELARActionPerformed

    private void BOTON_ACCEDERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BOTON_ACCEDERActionPerformed
        autenticacion();
    }//GEN-LAST:event_BOTON_ACCEDERActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ingresar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ingresar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ingresar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ingresar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ingresar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BOTON_ACCEDER;
    private javax.swing.JButton BOTON_CANCELAR;
    private javax.swing.JPasswordField ENTRADA_CONTRASEÑA;
    private javax.swing.JTextField ENTRADA_USUARIO;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
