/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import clases.Calendario;
import clases.Conexion;
import clases.PdfMaker;
import clases.Ticked;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alfredo
 */
public class Vender extends javax.swing.JFrame {

    Integer productoSelecionado_carrito = 1;
    Boolean deseaImpresion = false;
    Float dolar;
    String productoSelecionado_inventario = "", monto = "", montoVerde = "", factura = "";
    HashMap<String, Float> restadores = new HashMap<>();
    DefaultTableModel modeloInventario = new DefaultTableModel();
    DefaultTableModel modeloCarrito = new DefaultTableModel();
    PdfMaker p = new PdfMaker();

    /**
     * Creates new form Vender
     *
     * @return
     */
    @Override
    public Image getIconImage() {
        Image setValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("imagenes/Protologo_3_192x192.png"));
        return setValue;
    }

    private Opciones instancia;

    public Opciones getOpciones() {
        if (instancia == null) {
            return instancia = new Opciones();
        } else {
            return instancia;
        }
    }

    public void crearTablaInventario() {
        TABLA_DE_INVENTARIO = new JTable(modeloInventario);
        SCROLL_DE_INVENTARIO.setViewportView(TABLA_DE_INVENTARIO);

        modeloInventario.addColumn("Nombre");
        modeloInventario.addColumn("Existencia");
        modeloInventario.addColumn("Precio en Dólar");

    }

    public void crearTablaCarrito() {
        TABLA_DE_CARRITO = new JTable(modeloCarrito);
        SCROLL_DE_CARRITO.setViewportView(TABLA_DE_CARRITO);

        modeloCarrito.addColumn("Nombre");
        modeloCarrito.addColumn("Cantidad");
        modeloCarrito.addColumn("Precio en Dólar");
    }

    public void actualizarTablaInventario() {
        try {
            Connection cn = Conexion.conetar();
            PreparedStatement pst = cn.prepareStatement(
                    "select nombre, cantidad, precio_usd from inventario order by nombre");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[3];

                for (int i = 0; i < 3; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                modeloInventario.addRow(fila);
            }

            cn.close();

        } catch (SQLException e) {
            System.err.println("Falla en la clase 'Vender.java', linea 86: " + e.getErrorCode() + "\n" + e);
            JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
        }
    }

    public void actualizarTablaInventarioFiltrada(String filtro) {
        try {
            Connection cn = Conexion.conetar();
            PreparedStatement pst = cn.prepareStatement(
                    "select nombre, cantidad, precio_usd from inventario where nombre like '" + filtro + "'");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[3];

                for (int i = 0; i < 3; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                modeloInventario.addRow(fila);
            }

            cn.close();

        } catch (SQLException e) {
            System.err.println("Falla en la clase 'Vender.java', linea 87: " + e.getErrorCode() + "\n" + e);
            JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
        }
    }

    public void llenarCarrito(String filtro, Float cantidad) {
        crearBajasDeInventario();
        try {
            Connection cn = Conexion.conetar();
            PreparedStatement pst = cn.prepareStatement(
                    "select nombre, cantidad, precio_usd from inventario where nombre = '" + filtro + "'");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[3];

                for (int i = 0; i < 3; i++) {
                    if (i == 1) {
                        if (cantidad < rs.getInt(2)) {
                            fila[1] = cantidad;
                            continue;
                        } else /*if (cantidad > restadores.get(rs.getString(1)))*/ {
                            fila[1] = rs.getInt(2);
                            continue;
                        }
                    }
                    fila[i] = rs.getObject(i + 1);

                }
                modeloCarrito.addRow(fila);
            }

            cn.close();

        } catch (SQLException e) {
            System.err.println("Falla en la clase 'Vender.java', linea 87: " + e.getErrorCode() + "\n" + e);
            JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
        }
    }

    public void restarDelCarrito() {

        if (modeloCarrito.getRowCount() > 0 && productoSelecionado_carrito >= 0) {
            modeloCarrito.removeRow(productoSelecionado_carrito);
            productoSelecionado_carrito = -1;
        }

    }

    public void precioDePoductoUnidad(float precio) {
        float fTemp = precio * dolar;
        String s = String.format("%,.2f", fTemp);
        TEXTO_PRECIO_BS_P_INVENTARIO.setText(s);

    }

    public void precioMontoTotal() {
        float fPrecioUsd = 0.0f, fmonto = 0.0f, fCantidad = 0.0f, fmontoVerde = 0.0f;
        for (int i = 0; i < TABLA_DE_CARRITO.getRowCount(); i++) {
            fPrecioUsd = Float.parseFloat(String.valueOf(modeloCarrito.getValueAt(i, 2)));
            fCantidad = restadores.get((String) modeloCarrito.getValueAt(i, 0));

            fmonto += fCantidad * fPrecioUsd * dolar;
            fmontoVerde += fCantidad * fPrecioUsd;
        }
        monto = String.format("%,.2f Bs", fmonto);
        montoVerde = String.format("%,.2f $", fmontoVerde);
    }

    public void crearBajasDeInventario() {
        for (int i = 0; i < TABLA_DE_CARRITO.getRowCount(); i++) {

            restadores.put(String.valueOf(modeloCarrito.getValueAt(i, 0)), Float.parseFloat(String.valueOf(modeloCarrito.getValueAt(i, 1))));

        }

    }

    public void eliminarDuplicadosDelCarrito() {
        HashMap<String, Float> producto = new HashMap<>();
        for (int i = 0; i < TABLA_DE_CARRITO.getRowCount(); i++) {
            producto.put(String.valueOf(modeloCarrito.getValueAt(i, 0)), Float.parseFloat(String.valueOf(modeloCarrito.getValueAt(i, 1))));
        }
        modeloCarrito.setRowCount(0);
        try {
            for (Map.Entry<String, Float> entry : producto.entrySet()) {
                String key = entry.getKey();
                Float value = entry.getValue();

                Connection cn = Conexion.conetar();
                PreparedStatement pst = cn.prepareStatement(
                        "select nombre, cantidad, precio_usd from inventario where nombre = '" + key + "'");

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Object[] fila = new Object[3];

                    for (int i = 0; i < 3; i++) {
                        if (i == 1) {

                            fila[1] = value;
                            continue;

                        }
                        fila[i] = rs.getObject(i + 1);

                    }
                    modeloCarrito.addRow(fila);
                }

                restadores = producto;
                crearBajasDeInventario();
                cn.close();
            }

        } catch (SQLException e) {
            System.err.println("Falla en la clase 'Vender.java', linea 87: " + e.getErrorCode() + "\n" + e);
            JOptionPane.showMessageDialog(null, "Error, contactar al programador.");
        }
    }

    public void actualizarBDInventario() {
        HashMap<String, Float> temporal = new HashMap<>();
        try {
            for (Map.Entry<String, Float> entry : restadores.entrySet()) {
                String key = entry.getKey();

                Connection cn = Conexion.conetar();
                PreparedStatement pst = cn.prepareStatement(
                        "select cantidad from inventario where nombre = '" + key + "'");

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    temporal.put(key, rs.getFloat(1));
                }
                cn.close();
            }
            for (Map.Entry<String, Float> entry : restadores.entrySet()) {
                String key = entry.getKey();
                Float value = entry.getValue();

                Connection cn = Conexion.conetar();
                PreparedStatement pst = cn.prepareStatement(
                        "update inventario set cantidad = ? where nombre = '" + key + "'");
                Float restita = temporal.get(key) - value;

                pst.setString(1, String.valueOf(restita));

                pst.executeUpdate();
            }
        } catch (SQLException sQLException) {
        }
    }

    public void crearFactura() {
        Ticked tk = new Ticked();
        String precio = "";

        for (Map.Entry<String, Float> entry : restadores.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            try {
                Connection cn = Conexion.conetar();
                PreparedStatement pst = cn.prepareStatement(
                        "select precio_usd from inventario where nombre = '" + key + "'");
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    precio = rs.getString("precio_usd");
                }
                cn.close();
            } catch (Exception e) {
                System.err.println(e);
            }
            factura += key + "\n" + value + " x " + precio + "$\n";

        }
        System.out.println(factura);
        tk.setArticulos(factura);
        tk.setDolar(String.valueOf(montoVerde));
        tk.setTotal(monto);

        try {
            tk.print(true);
        } catch (Exception e) {
            System.err.println(e);
        }

        factura = "";

    }

    public Vender() {
        initComponents();
        this.setLocationRelativeTo(null);
        crearTablaInventario();
        crearTablaCarrito();
        actualizarTablaInventario();
        do {
            ENTRADA_DOLAR.setText(JOptionPane.showInputDialog("DOLAR:"));
        } while (ENTRADA_DOLAR.getText().equals(""));
        dolar = Float.parseFloat(ENTRADA_DOLAR.getText().trim());

        TABLA_DE_INVENTARIO.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_poin = TABLA_DE_INVENTARIO.rowAtPoint(e.getPoint());

                if (fila_poin > -1) {

                    dolar = Float.parseFloat(ENTRADA_DOLAR.getText().trim());
                    productoSelecionado_inventario = (String) modeloInventario.getValueAt(fila_poin, 0);
                    float a = Float.parseFloat((String) modeloInventario.getValueAt(fila_poin, 2));
                    precioDePoductoUnidad(a);

                }
            }
        });
        TABLA_DE_CARRITO.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_poin = TABLA_DE_CARRITO.rowAtPoint(e.getPoint());

                if (fila_poin > -1) {

                    dolar = Float.parseFloat(ENTRADA_DOLAR.getText().trim());
                    productoSelecionado_carrito = fila_poin;

                }
            }
        });

        Mostrar_Monto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Mostrar_Monto.setText(monto);
                if (e.getClickCount() == 2) {
                    dolar = Float.parseFloat(ENTRADA_DOLAR.getText().trim());
                    precioMontoTotal();
                    JOptionPane.showMessageDialog(null, montoVerde, "Precio TOTAL en dolares:", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        ENTRADA_DOLAR = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        SELECTOR_FILTRO = new javax.swing.JComboBox<>();
        SCROLL_DE_INVENTARIO = new javax.swing.JScrollPane();
        TABLA_DE_INVENTARIO = new javax.swing.JTable();
        SCROLL_DE_CARRITO = new javax.swing.JScrollPane();
        TABLA_DE_CARRITO = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        TEXTO_PRECIO_BS_P_INVENTARIO = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        Mostrar_Monto = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        jMenuItem3.setText("jMenuItem3");

        jMenu5.setText("jMenu5");

        jMenuItem5.setText("jMenuItem5");

        jMenuItem6.setText("jMenuItem6");

        jMenuItem7.setText("jMenuItem7");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Leydi Minimarket");
        setIconImage(getIconImage());
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ENTRADA_DOLAR.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        ENTRADA_DOLAR.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ENTRADA_DOLAR.setBorder(null);
        getContentPane().add(ENTRADA_DOLAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(379, 11, 458, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Dólar:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, -1, 40));

        SELECTOR_FILTRO.setBackground(new java.awt.Color(0, 0, 0));
        SELECTOR_FILTRO.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        SELECTOR_FILTRO.setForeground(new java.awt.Color(255, 255, 255));
        SELECTOR_FILTRO.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TODOS", "0 - 9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" }));
        SELECTOR_FILTRO.setBorder(null);
        SELECTOR_FILTRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SELECTOR_FILTROActionPerformed(evt);
            }
        });
        getContentPane().add(SELECTOR_FILTRO, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, -1, -1));

        SCROLL_DE_INVENTARIO.setBorder(null);

        TABLA_DE_INVENTARIO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TABLA_DE_INVENTARIO.setEnabled(false);
        SCROLL_DE_INVENTARIO.setViewportView(TABLA_DE_INVENTARIO);

        getContentPane().add(SCROLL_DE_INVENTARIO, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 78, 387, -1));

        SCROLL_DE_CARRITO.setBorder(null);

        TABLA_DE_CARRITO.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        SCROLL_DE_CARRITO.setViewportView(TABLA_DE_CARRITO);

        getContentPane().add(SCROLL_DE_CARRITO, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 78, 387, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Filtrar por:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 55, -1, -1));

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText(">");
        jButton1.setBorder(null);
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(403, 237, 40, 40));

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("<");
        jButton2.setBorder(null);
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(403, 291, 40, 40));

        jButton4.setBackground(new java.awt.Color(0, 0, 0));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Vender");
        jButton4.setBorder(null);
        jButton4.setFocusable(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 530, 120, 38));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Monto total:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 539, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Precio en bolivares del producto:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 511, -1, -1));

        TEXTO_PRECIO_BS_P_INVENTARIO.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        TEXTO_PRECIO_BS_P_INVENTARIO.setForeground(new java.awt.Color(255, 255, 255));
        TEXTO_PRECIO_BS_P_INVENTARIO.setText("000.000.000,00");
        getContentPane().add(TEXTO_PRECIO_BS_P_INVENTARIO, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 510, 260, -1));

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 51, 51));
        jButton3.setText("Cancelar");
        jButton3.setBorder(null);
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 530, 130, 38));

        Mostrar_Monto.setBackground(new java.awt.Color(0, 0, 0));
        Mostrar_Monto.setFont(new java.awt.Font("Tahoma", 3, 24)); // NOI18N
        Mostrar_Monto.setForeground(new java.awt.Color(255, 255, 255));
        Mostrar_Monto.setText("0,00 Bs");
        Mostrar_Monto.setBorder(null);
        Mostrar_Monto.setFocusable(false);
        Mostrar_Monto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Mostrar_Monto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Mostrar_MontoActionPerformed(evt);
            }
        });
        getContentPane().add(Mostrar_Monto, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 540, 350, 30));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondo_1000x1000.png"))); // NOI18N
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 580));

        jMenuBar1.setBackground(new java.awt.Color(0, 0, 0));
        jMenuBar1.setForeground(new java.awt.Color(255, 255, 255));
        jMenuBar1.setBorderPainted(false);

        jMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("Herramientas");
        jMenu1.add(jSeparator1);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem8.setText("Cuentas de clientes");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);
        jMenu1.add(jSeparator2);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Volver a la ventana anterior");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Consultar ventas en pdf");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setForeground(new java.awt.Color(255, 255, 255));
        jMenu2.setText("Acerca de");

        jMenuItem4.setText("Acerca de...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SELECTOR_FILTROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SELECTOR_FILTROActionPerformed
        switch (SELECTOR_FILTRO.getSelectedIndex()) {
            case 0:
                modeloInventario.setRowCount(0);
                actualizarTablaInventario();
                break;
            case 1:
                modeloInventario.setRowCount(0);
                for (int i = 0; i < 10; i++) {
                    actualizarTablaInventarioFiltrada(i + "%");
                }
                break;
            case 2:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("A%");
                break;
            case 3:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("B%");
                break;
            case 4:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("C%");
                break;
            case 5:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("D%");
                break;
            case 6:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("E%");
                break;
            case 7:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("F%");
                break;
            case 8:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("G%");
                break;
            case 9:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("H%");
                break;
            case 10:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("I%");
                break;
            case 11:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("J%");
                break;
            case 12:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("K%");
                break;
            case 13:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("L%");
                break;
            case 14:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("M%");
                break;
            case 15:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("N%");
                break;
            case 16:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("Ñ%");
                break;
            case 17:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("O%");
                break;
            case 18:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("P%");
                break;
            case 19:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("Q%");
                break;
            case 20:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("R%");
                break;
            case 21:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("S%");
                break;
            case 22:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("T%");
                break;
            case 23:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("U%");
                break;
            case 24:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("V%");
                break;
            case 25:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("W%");
                break;
            case 26:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("X%");
                break;
            case 27:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("Y%");
                break;
            case 28:
                modeloInventario.setRowCount(0);
                actualizarTablaInventarioFiltrada("Z%");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Increiblemente ingresaste un valor inválido");
        }
    }//GEN-LAST:event_SELECTOR_FILTROActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        dolar = Float.parseFloat(ENTRADA_DOLAR.getText().trim());
        Float cantidad = 0.0f;
        try {
            if (!productoSelecionado_inventario.equals("")) {
                cantidad = Float.parseFloat(JOptionPane.showInputDialog("Cantidad:"));
                if (cantidad > 0) {
                    llenarCarrito(productoSelecionado_inventario, cantidad);
                }
            }
        } catch (Exception e) {
            if (e.toString().equals("java.lang.NumberFormatException: empty String")) {
                llenarCarrito(productoSelecionado_inventario, 1.0f);
            } else {
                JOptionPane.showMessageDialog(null, "Solo ingrese valores NUMERICOS validos");
                System.err.println(e.toString());
            }
        }
        eliminarDuplicadosDelCarrito();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dolar = Float.parseFloat(ENTRADA_DOLAR.getText().trim());
        restarDelCarrito();
        eliminarDuplicadosDelCarrito();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int sure = JOptionPane.showConfirmDialog(null, "¿Deseas CANCELAR la venta?");
        if (sure == 0) {
            modeloCarrito.setRowCount(0);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        crearBajasDeInventario();
        precioMontoTotal();
        Mostrar_Monto.setText(monto);
        if (!restadores.isEmpty()) {
            String precio = "";
            try {
                Connection cn = Conexion.conetar();
                PreparedStatement pst = cn.prepareStatement("insert into ventas_" + Calendario.anno() + "_" + Calendario.mes() + " values (?,?,?,?,?)");

                pst.setString(1, "0");
                pst.setString(2, Calendario.dia());
                pst.setString(3, Calendario.hora() + ":" + Calendario.minuto());
                for (Map.Entry<String, Float> entry : restadores.entrySet()) {
                    String key = entry.getKey();
                    Float value = entry.getValue();

                    try {
                        Connection cn2 = Conexion.conetar();
                        PreparedStatement pst2 = cn2.prepareStatement(
                                "select precio_usd from inventario where nombre = '" + key + "'");
                        ResultSet rs2 = pst2.executeQuery();
                        if (rs2.next()) {
                            precio = rs2.getString("precio_usd");
                        }
                        cn2.close();
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    factura += key + "\n" + value + " x " + precio + "$\n\n";

                }
                pst.setString(4, factura);
                pst.setString(5, dolar.toString());
                pst.executeUpdate();

                cn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "ERROR EN LA CONEXION AL BASE DE DATOS:\n" + e);
            }
            precio = "";
            factura = "";
        }
        if (Mostrar_Monto.getText().equals("0,00 Bs")) {
            restadores.clear();
        }

        int facturator = JOptionPane.showConfirmDialog(null, "¿Imprimir Factura?");

        if (!restadores.isEmpty() && facturator == 0) {

            crearFactura();

        }

        actualizarBDInventario();
        modeloInventario.setRowCount(0);
        actualizarTablaInventario();
        modeloCarrito.setRowCount(0);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void Mostrar_MontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Mostrar_MontoActionPerformed
        precioMontoTotal();
        Mostrar_Monto.setText(monto);
    }//GEN-LAST:event_Mostrar_MontoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.dispose();
        getOpciones().setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        String year = JOptionPane.showInputDialog("Año: XXXX", Calendario.anno()),
                month = JOptionPane.showInputDialog("Mes: XX", Calendario.mes()),
                day = JOptionPane.showInputDialog("Dia: XX", Calendario.dia());
        if (year.length() == 4 && month.length() == 2 && day.length() == 2) {
            p.ventaDelDia(year, month, day);
        } else {
            JOptionPane.showMessageDialog(null, "Ingresaste un valor incorrecto");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        JOptionPane.showMessageDialog(null,
                  "Desarrollado por Alfredo Sánchez:\n\n"
                + "alfredo29732366@gmail.com\n\n\n"
                + "-2021");
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        CuentasDeClientes cuentasDeClientes = new CuentasDeClientes();
        cuentasDeClientes.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

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
            java.util.logging.Logger.getLogger(Vender.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vender.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vender.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vender.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Vender().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ENTRADA_DOLAR;
    private javax.swing.JButton Mostrar_Monto;
    private javax.swing.JScrollPane SCROLL_DE_CARRITO;
    private javax.swing.JScrollPane SCROLL_DE_INVENTARIO;
    private javax.swing.JComboBox<String> SELECTOR_FILTRO;
    private javax.swing.JTable TABLA_DE_CARRITO;
    private javax.swing.JTable TABLA_DE_INVENTARIO;
    private javax.swing.JLabel TEXTO_PRECIO_BS_P_INVENTARIO;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
