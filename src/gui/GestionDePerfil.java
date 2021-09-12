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
public class GestionDePerfil extends javax.swing.JFrame {

    private String usuario = Opciones.usuario;

    /**
     * Creates new form GUI_EditarAlumno
     */
    public GestionDePerfil() {
        initComponents();
        this.setLocationRelativeTo(null);
        try {
            Connection cn = Conexion.conetar();
            PreparedStatement pst = cn.prepareStatement(
                    "select * from usuarios where nombre = '" + usuario + "'");

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                TEXTO_USUARIO.setText(usuario);
                TEXTO_CLAVE.setText(rs.getString("clave"));

            }

            cn.close();

        } catch (SQLException e) {
            System.err.println("ERROR al llamar al alumno:\n" + e);

            JOptionPane.showMessageDialog(null, "Error, contacte con el desarrollador del software");
        }

    }
    @Override
    public Image getIconImage() {
        Image setValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("imagenes/Protologo_3_192x192.png"));
        return setValue;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TEXTO_USUARIO = new javax.swing.JTextField();
        TEXTO_CLAVE = new javax.swing.JTextField();
        ACTUALIZAR = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar usuario");
        setIconImage(getIconImage());
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Usuario:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 11, -1, 20));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Contraseña:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 44, -1, 20));

        TEXTO_USUARIO.setBorder(null);
        getContentPane().add(TEXTO_USUARIO, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 11, 228, 20));

        TEXTO_CLAVE.setBorder(null);
        getContentPane().add(TEXTO_CLAVE, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 41, 228, 20));

        ACTUALIZAR.setBackground(new java.awt.Color(0, 0, 0));
        ACTUALIZAR.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ACTUALIZAR.setForeground(new java.awt.Color(255, 255, 255));
        ACTUALIZAR.setText("ACTUALIZAR");
        ACTUALIZAR.setBorder(null);
        ACTUALIZAR.setFocusable(false);
        ACTUALIZAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ACTUALIZARActionPerformed(evt);
            }
        });
        getContentPane().add(ACTUALIZAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 110, 20));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo_1000x1000.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 100));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ACTUALIZARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ACTUALIZARActionPerformed

        int validacion = 0;

        String ususario, clave;

        ususario = TEXTO_USUARIO.getText().trim();
        clave = TEXTO_CLAVE.getText().trim();

        if (ususario.equals("")) {
            JOptionPane.showMessageDialog(null, "Usuario no puede estar vacio");
            validacion++;
        } else {
            validacion = 0;
        }
        if (clave.equals("")) {
            JOptionPane.showMessageDialog(null, "La clave es necesaria");
            validacion++;
        } else {
            validacion = 0;
        }

        if (validacion == 0) {
            try {
                Connection cn = Conexion.conetar();
                PreparedStatement pst = cn.prepareStatement(
                        "update usuarios set nombre =?, clave=? where nombre = '" + usuario + "'");

                pst.setString(1, ususario);
                pst.setString(2, clave);

                pst.executeUpdate();
                cn.close();

                JOptionPane.showMessageDialog(null, "Actualizacion exitosa");

                TEXTO_CLAVE.setText("");
                TEXTO_USUARIO.setText("");

                this.dispose();
                
            } catch (SQLException e) {
                System.err.println("ERROR al actualizar al especialista:\n" + e);

                JOptionPane.showMessageDialog(null, "Error, contacte con el desarrollador del software");
            }
        }

    }//GEN-LAST:event_ACTUALIZARActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GestionDePerfil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestionDePerfil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestionDePerfil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestionDePerfil.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestionDePerfil().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ACTUALIZAR;
    private javax.swing.JTextField TEXTO_CLAVE;
    private javax.swing.JTextField TEXTO_USUARIO;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
