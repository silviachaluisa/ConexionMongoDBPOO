package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConexionMDB {
    private JTextField Insercion_descripcion;
    private JTextField Insercion_nombre;
    private JTextField Insercion_pasatiempo;
    private JButton insertarButton;
    private JButton eliminarButton;
    private JTextField Campo_usuario;
    private JTextField Campo_contraseña;
    private JTextField Campo_cluster;
    private JComboBox servidor;
    private JComboBox <String> base_de_datos;
    private JComboBox <String> colecciones;
    JPanel VentanaPrincipal;
    private ConexionMongo conexion;

    public ConexionMDB() {
        conexion = new ConexionMongo("esfot", "esfot2024", "mongodb+srv", "cluster0.xzffuex.mongodb.net");
        boolean conn = conexion.Conectado();
        base_de_datos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                base_de_datos.removeAllItems();
                if (conn){
                    for (String base : conexion.ListarBasesDeDatos()) {
                        base_de_datos.addItem(base);
                        System.out.println(base);
                    }
                } else {
                    base_de_datos.addItem("No se pudo establecer la conexión");
                }
            }
        });
        colecciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (conn){
                    for (String coleccion : conexion.ListarColecciones(base_de_datos.getSelectedItem().toString())) {
                        colecciones.addItem(coleccion);
                        System.out.println(coleccion);
                    }
                } else {
                    colecciones.addItem("No se pudo establecer la conexión");
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        base_de_datos = new JComboBox<String>();
        colecciones = new JComboBox<String>();
    }

}
