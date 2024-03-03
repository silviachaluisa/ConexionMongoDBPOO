package org.example;

import Exceptions.MongoConnectException;

import com.mongodb.client.*;

import org.bson.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ConexionMDB {
    private Document documento;
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
    private JButton buscarButton;
    private JButton crearBaseYColeccionButton;
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

        insertarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (conexion.Conectado()) {
                    String selectedDatabase = base_de_datos.getSelectedItem().toString();
                    String selectedCollection = colecciones.getSelectedItem().toString();
                    if (selectedDatabase != null && !selectedDatabase.isEmpty() && selectedCollection != null && !selectedCollection.isEmpty()) {
                        documento = new Document();
                        documento.append("Nombre", Insercion_nombre.getText());
                        documento.append("Pasatiempo", Insercion_pasatiempo.getText());
                        documento.append("Descripcion", Insercion_descripcion.getText());
                        conexion.InsertarDocumento(selectedDatabase, selectedCollection, documento);
                        JOptionPane.showMessageDialog(VentanaPrincipal, "Documento insertado");
                    } else {
                        // Si no se ha seleccionado una base de datos o una coleccion, muestra un mensaje de error
                        JOptionPane.showMessageDialog(VentanaPrincipal, "Seleccione una base de datos y una colección primero");
                    }
                } else {
                    // Si no se establece adecuadamente la conexion, muestra un mensaje de error
                    JOptionPane.showMessageDialog(VentanaPrincipal, "No se pudo establecer la conexión");
                }
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (conexion.Conectado()) {
                    String selectedDatabase = base_de_datos.getSelectedItem().toString();
                    String selectedCollection = colecciones.getSelectedItem().toString();
                    if (selectedDatabase != null && !selectedDatabase.isEmpty() && selectedCollection != null && !selectedCollection.isEmpty()) {
                        documento = new Document();
                        documento.append("Nombre", Insercion_nombre.getText());
                        documento.append("Pasatiempo", Insercion_pasatiempo.getText());
                        documento.append("Descripcion", Insercion_descripcion.getText());
                        conexion.EliminarDocumento(selectedDatabase, selectedCollection, documento);
                        JOptionPane.showMessageDialog(VentanaPrincipal, "Documento eliminado");
                    } else {
                        // Si no se ha seleccionado una base de datos o una coleccion, muestra un mensaje de error
                        JOptionPane.showMessageDialog(VentanaPrincipal, "Seleccione una base de datos y una colección primero");
                    }
                } else {
                    // Si no se establece adecuadamente la conexion, muestra un mensaje de error
                    JOptionPane.showMessageDialog(VentanaPrincipal, "No se pudo establecer la conexión");
                }
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (conexion.Conectado()) {
                    String selectedDatabase = base_de_datos.getSelectedItem().toString();
                    String selectedCollection = colecciones.getSelectedItem().toString();
                    if (selectedDatabase != null && !selectedDatabase.isEmpty() && selectedCollection != null && !selectedCollection.isEmpty()) {
                        FindIterable<Document>resultado = conexion.ConsultarDocumentos(selectedDatabase, selectedCollection);
                        for (Document doc : resultado) {
                            System.out.println(doc.toJson());
                        }
                    } else {
                        // Si no se ha seleccionado una base de datos o una coleccion, muestra un mensaje de error
                        JOptionPane.showMessageDialog(VentanaPrincipal, "Seleccione una base de datos y una colección primero");
                    }
                } else {
                    // Si no se establece adecuadamente la conexion, muestra un mensaje de error
                    JOptionPane.showMessageDialog(VentanaPrincipal, "No se pudo establecer la conexión");
                }
            }
        });

        crearBaseYColeccionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Ventana2 = new JFrame();
                Ventana2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                Ventana2.setSize(300, 300);
                Ventana2.setContentPane(new CreacionBDDyCL(conexion).VentanaCreacion);
                Ventana2.pack();
                Ventana2.setLocationRelativeTo(null); // Centra la ventana en la pantalla
                Ventana2.setVisible(true);
            }
        });
    }

    private void desabilitarEntradas() { // Desabilita las entradas hasta que se establezca la conexion
        base_de_datos.setEnabled(false);
        colecciones.setEnabled(false);
        Insercion_descripcion.setEnabled(false);
        Insercion_nombre.setEnabled(false);
        Insercion_pasatiempo.setEnabled(false);
        crearBaseYColeccionButton.setEnabled(false);
    }

    private void habilitarEntradas() { // Habilita las entradas para seleccionar la base de datos y la coleccion
        base_de_datos.setEnabled(true);
        colecciones.setEnabled(true);
        Insercion_descripcion.setEnabled(true);
        Insercion_nombre.setEnabled(true);
        Insercion_pasatiempo.setEnabled(true);
        crearBaseYColeccionButton.setEnabled(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        base_de_datos = new JComboBox<String>();
        colecciones = new JComboBox<String>();
    }

}
