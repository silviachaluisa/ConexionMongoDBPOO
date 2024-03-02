package org.example;

import Exceptions.MongoConnectException;
import com.mongodb.client.MongoIterable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private JButton conectarButton;
    private ConexionMongo conexion;
    private boolean conn;

    public ConexionMDB() {
        // Creacion de una instancia para la conexion a MongoDB
        conexion = new ConexionMongo();
        desabilitarEntradas(); // Desabilita las entradas hasta que se establezca la conexion
        conectarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Si existen campos vacios, muestra un mensaje de error
                if (Campo_usuario.getText().isEmpty() || Campo_contraseña.getText().isEmpty() || Campo_cluster.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaPrincipal, "Por favor llene todos los campos");
                    return;
                }
                // Establece la conexion con los datos ingresados
                try {
                    conexion.setConexion(Campo_usuario.getText(), Campo_contraseña.getText(), servidor.getSelectedItem().toString(), Campo_cluster.getText());
                    boolean conn = conexion.Conectado(); // Verifica si la conexion fue exitosa
                    if (conn) {
                        JOptionPane.showMessageDialog(VentanaPrincipal, "Conexión establecida");
                        habilitarEntradas(); // Habilita las entradas para seleccionar la base de datos y la coleccion
                    } else {
                        JOptionPane.showMessageDialog(VentanaPrincipal, "No se pudo establecer la conexión");
                        desabilitarEntradas();
                    }
                } catch (MongoConnectException ex) {
                    desabilitarEntradas(); // Desabilita las entradas hasta que se establezca la conexion
                    JOptionPane.showMessageDialog(VentanaPrincipal, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        base_de_datos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                base_de_datos.removeAllItems(); // Limpia el combobox
                if (conexion.Conectado()) {
                    MongoIterable<String> bases = conexion.ListarBasesDeDatos(); // Obtiene las bases de datos de la conexion
                    if (bases != null) {
                        for (String base : bases) {
                            base_de_datos.addItem(base); // Agrega las bases de datos al combobox
                        }
                    } else {
                        // Si no existen bases de datos, muestra un mensaje de error
                        base_de_datos.addItem("No se pudo obtener la lista de bases de datos");
                    }
                } else {
                    // Si no se establece adecuadamente la conexion, muestra un mensaje de error
                    base_de_datos.addItem("No se pudo establecer la conexión");
                }
            }
        });

        colecciones.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                colecciones.removeAllItems(); // Limpia el combobox
                String selectedDatabase = base_de_datos.getSelectedItem().toString();
                if (conexion.Conectado()) {
                    if (selectedDatabase != null && !selectedDatabase.isEmpty()) {
                        MongoIterable<String> coleccionesBDD = conexion.ListarColecciones(selectedDatabase); // Obtiene las colecciones de la base de datos seleccionada
                        if (colecciones != null) {
                            for (String coleccion : coleccionesBDD) {
                                colecciones.addItem(coleccion); // Agrega las colecciones al combobox
                            }
                        } else {
                            // Si no existen colecciones, muestra un mensaje de error
                            colecciones.addItem("No se pudo obtener la lista de colecciones");
                        }
                    } else {
                        // Si no se ha seleccionado una base de datos, muestra un mensaje de error
                        colecciones.addItem("Seleccione una base de datos primero");
                    }
                } else {
                    // Si no se establece adecuadamente la conexion, muestra un mensaje de error
                    colecciones.addItem("No se pudo establecer la conexión");
                }
            }
        });

    }

    private void desabilitarEntradas() { // Desabilita las entradas hasta que se establezca la conexion
        base_de_datos.setEnabled(false);
        colecciones.setEnabled(false);
        Insercion_descripcion.setEnabled(false);
        Insercion_nombre.setEnabled(false);
        Insercion_pasatiempo.setEnabled(false);
    }

    private void habilitarEntradas() { // Habilita las entradas para seleccionar la base de datos y la coleccion
        base_de_datos.setEnabled(true);
        colecciones.setEnabled(true);
        Insercion_descripcion.setEnabled(true);
        Insercion_nombre.setEnabled(true);
        Insercion_pasatiempo.setEnabled(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        base_de_datos = new JComboBox<String>();
        colecciones = new JComboBox<String>();
    }

}
