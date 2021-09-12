/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Alfredo
 */
public class Conexion {

    public static Connection conetar() {
        try {
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/bd_leydi_minimarker", "root", "");
            return cn;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1049) {

                try {
                    Connection cnnn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "");
                    PreparedStatement pst = cnnn.prepareStatement("CREATE DATABASE `bd_leydi_minimarker` CHARSET=utf8 COLLATE utf8_unicode_ci;");
                    pst.execute();
                    Connection cnn = Conexion.conetar();
                    PreparedStatement pst2 = cnn.prepareStatement("CREATE TABLE `bd_leydi_minimarker`.`usuarios`"
                            + "(`ID` INT NOT NULL AUTO_INCREMENT,"
                            + "`nombre` VARCHAR(50) NOT NULL,"
                            + "`clave` VARCHAR(50) NOT NULL,"
                            + "PRIMARY KEY(`ID`)"
                            + ") ENGINE = InnoDB;");
                    pst2.execute();
                    Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/bd_leydi_minimarker", "root", "");
                    return cn;

                } catch (SQLException ex) {
                    System.err.println("Falla en la clase 'Conexion.java', linea 44: " + ex.getErrorCode() + "\n" + ex);
                    JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
                }
            } else {
                System.err.println("Falla en la clase 'Conexion.java', linea 47: " + e.getErrorCode() + "\n" + e);
                JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
            }
        }
        return (null);
    }

    public static void solucionador() {
        try {
            Connection cn = Conexion.conetar();
            PreparedStatement pst = cn.prepareStatement("select * from ventas_" + Calendario.anno() + "_" + Calendario.mes());
            pst.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1146) {
                try {
                    Connection cn = Conexion.conetar();
                    PreparedStatement pst = cn.prepareStatement("CREATE TABLE `bd_leydi_minimarker`.`ventas_" + Calendario.anno() + "_" + Calendario.mes() + "`"
                            + "(`ID` INT NOT NULL AUTO_INCREMENT,"
                            + "`dia` VARCHAR(2) NOT NULL,"
                            + "`hora` VARCHAR(5) NOT NULL,"
                            + "`factura` text NOT NULL,"
                            + "`tasa_del_dia` VARCHAR(60) NOT NULL,"
                            + "PRIMARY KEY(`ID`)"
                            + ") ENGINE = InnoDB;");
                    pst.execute();
                    Connection cn2 = Conexion.conetar();
                    PreparedStatement pst2 = cn2.prepareStatement("insert into usuarios values (?,?,?)");
                    pst2.setString(1, "0");
                    pst2.setString(2, "leydi");
                    pst2.setString(3, "4444");
                    pst2.executeUpdate();
                } catch (SQLException ex) {
                    System.err.println("Falla en la clase 'Conexion.java', linea 79: " + ex.getErrorCode() + "\n" + ex);
                    JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
                }
            } else {
                System.err.println("Falla en la clase 'Conexion.java', linea 83: " + e.getErrorCode() + "\n" + e);
                JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
            }
        }
    }

    public static void inventario() {
        try {
            Connection cn = Conexion.conetar();
            PreparedStatement pst = cn.prepareStatement("select * from inventario");
            pst.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1146) {
                try {
                    Connection cn = Conexion.conetar();
                    PreparedStatement pst = cn.prepareStatement("CREATE TABLE `bd_leydi_minimarker`.`inventario`"
                            + "(`ID` INT NOT NULL AUTO_INCREMENT,"
                            + "`nombre` VARCHAR(100) NOT NULL,"
                            + "`cantidad` VARCHAR(50) NOT NULL,"
                            + "`precio_usd` VARCHAR(20) NOT NULL,"
                            + "PRIMARY KEY(`ID`)"
                            + ") ENGINE = InnoDB;");
                    pst.execute();
                } catch (SQLException ex) {
                    System.err.println("Falla en la clase 'Conexion.java', linea 107: " + ex.getErrorCode() + "\n" + ex);
                    JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
                }
            } else {
                System.err.println("Falla en la clase 'Conexion.java', linea 111: " + e.getErrorCode() + "\n" + e);
                JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
            }
        }
    }
    public static void cuentas() {
        try {
            Connection cn = Conexion.conetar();
            PreparedStatement pst = cn.prepareStatement("select * from cuentas");
            pst.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1146) {
                try {
                    Connection cn = Conexion.conetar();
                    PreparedStatement pst = cn.prepareStatement("CREATE TABLE `bd_leydi_minimarker`.`cuentas`"
                            + "(`ID` INT NOT NULL AUTO_INCREMENT,"
                            + "`nombre` VARCHAR(100) NOT NULL,"
                            + "`saldo_positivo` VARCHAR(50) NOT NULL,"
                            + "`saldo_negativo` VARCHAR(50) NOT NULL,"
                            + "`notas` text NOT NULL,"
                            + "PRIMARY KEY(`ID`)"
                            + ") ENGINE = InnoDB;");
                    pst.execute();
                } catch (SQLException ex) {
                    System.err.println("Falla en la clase 'Conexion.java', linea 135: " + ex.getErrorCode() + "\n" + ex);
                    JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
                }
            } else {
                System.err.println("Falla en la clase 'Conexion.java', linea 139: " + e.getErrorCode() + "\n" + e);
                JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
            }
        }
    }
}
