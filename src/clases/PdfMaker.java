/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Alfredo
 */
public class PdfMaker {

    public void ventaDelDia(String anno, String mes, String dia) {

        Document documento = new Document();

        try {

            String ruta = System.getProperty("user.home");

            ClassLoader classLoader = PdfMaker.class.getClassLoader();
            URL url = classLoader.getResource("imagenes/ProtoBanner_1_600x200.png");
            System.out.println(url);

            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/Ventas_" + anno + "_" + mes + "_" + dia + ".pdf"));

            com.itextpdf.text.Image header = com.itextpdf.text.Image.getInstance(url.toString());
            header.scaleToFit(200, 600);
            header.setAlignment(Chunk.ALIGN_CENTER);

            Paragraph parrafo = new Paragraph();
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo.add("Ventas del " + dia + "/" + mes + "/" + anno + ": \n \n");
            parrafo.setFont(FontFactory.getFont("Tahoma", 14, Font.BOLD, BaseColor.DARK_GRAY));

            documento.open();
            documento.add(header);
            documento.add(parrafo);

            PdfPTable tablaAlumno = new PdfPTable(3);
            tablaAlumno.addCell("Hora");
            tablaAlumno.addCell("Tasa del dia");
            tablaAlumno.addCell("Factura");

            try {
                Connection cn = Conexion.conetar();
                PreparedStatement pst = cn.prepareStatement(
                        "select * from ventas_" + anno + "_" + mes + " where dia = '" + dia + "' order by hora");
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    do {

                        tablaAlumno.addCell(rs.getString("hora"));
                        tablaAlumno.addCell(rs.getString("tasa_del_dia"));
                        tablaAlumno.addCell(rs.getString("factura"));

                    } while (rs.next());

                    documento.add(tablaAlumno);
                }
            } catch (SQLException s) {
                System.err.println(s.getErrorCode());

            }
            documento.close();
            JOptionPane.showMessageDialog(null, "Documento generado correctamente");
        } catch (DocumentException | IOException e) {
            System.err.println(e);
        }
    }

    public void inventarioActual() {

        Document documento = new Document();

        try {

            String ruta = System.getProperty("user.home");

            ClassLoader classLoader = PdfMaker.class.getClassLoader();
            URL url = classLoader.getResource("imagenes/ProtoBanner_1_600x200.png");
            System.out.println(url);

            PdfWriter.getInstance(documento, new FileOutputStream(
                    ruta + "/Desktop/Inventario_" + Calendario.anno() + "_" + Calendario.mes()
                    + "_" + Calendario.dia() + "_" + Calendario.hora() + Calendario.minuto() + ".pdf"));

            com.itextpdf.text.Image header = com.itextpdf.text.Image.getInstance(url.toString());
            header.scaleToFit(200, 600);
            header.setAlignment(Chunk.ALIGN_CENTER);

            Paragraph parrafo = new Paragraph();
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo.add("Inventario del " + Calendario.dia() + "/" + Calendario.mes() + "/" + Calendario.anno() + ": \n \n");
            parrafo.setFont(FontFactory.getFont("Tahoma", 14, Font.BOLD, BaseColor.DARK_GRAY));

            documento.open();
            documento.add(header);
            documento.add(parrafo);

            PdfPTable tablaAlumno = new PdfPTable(3);
            tablaAlumno.addCell("Nombre");
            tablaAlumno.addCell("Candidad");
            tablaAlumno.addCell("Precio en $");

            try {
                Connection cn = Conexion.conetar();
                PreparedStatement pst = cn.prepareStatement(
                        "select * from inventario order by nombre");
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    do {

                        tablaAlumno.addCell(rs.getString("nombre"));
                        tablaAlumno.addCell(rs.getString("cantidad"));
                        tablaAlumno.addCell(rs.getString("precio_usd"));

                    } while (rs.next());

                    documento.add(tablaAlumno);
                }
            } catch (SQLException s) {
                System.err.println(s.getErrorCode());

            }
            documento.close();
            JOptionPane.showMessageDialog(null, "Documento generado correctamente");
        } catch (DocumentException | IOException e) {
            System.err.println(e);
        }
    }
}
