package org.example;

import Exceptions.MongoConnectException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.MongoIterable;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Filter;


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
    private JButton buscarButton;
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
                MongoClient mongoclient=MongoClients.create("mongodb+srv://<username>:<password>@cluster0.xzffuex.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
                MongoClient database=mongoclient.getDatabase("ConexionMongoDB");
                MongoClient<Document> collection=database.getCollection("Integrantes");

                String Nombre=Insercion_nombre.getText();
                String Pasatiempo=Insercion_pasatiempo.getText();
                String Descripcion=Insercion_descripcion.getText();

                Document documento=new Document();
                documento.append("Nombre",Nombre);
                documento.append("Pasatiempo",Pasatiempo);
                documento.append("Descripcion",Descripcion);

                collection.insertOne(documento);

                System.out.println("Datos insertados exitosamente");
                mongoclient.close();

            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (collection != null) {
                    String nombreToDelete = JOptionPane.showInputDialog(Ventana1.this, "Ingrese el Nombre del documento a eliminar:");

                    if (nombreToDelete != null && !nombreToDelete.isEmpty()) {
                        collection.deleteOne(Filters.eq("Nombre", nombreToDelete));
                        System.out.println("Documento eliminado exitosamente");
                    } else {
                        System.out.println("Operación de eliminación cancelada. El Nombre no puede estar vacío.");
                    }
                } else {
                    System.out.println("No hay conexión establecida. No se puede realizar la eliminación.");
                }
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (collection != null) {
                    String nombreToSearch = JOptionPane.showInputDialog(Ventana1.this, "Ingrese el Nombre del documento a buscar:");

                    if (nombreToSearch != null && !nombreToSearch.isEmpty()) {
                        Document resultado = collection.find(Filters.eq("Nombre", nombreToSearch)).first();
                        if (resultado != null) {
                            System.out.println("Documento encontrado:");
                            System.out.println(resultado.toJson());
                        } else {
                            System.out.println("No se encontraron documentos con el Nombre proporcionado.");
                        }
                    } else {
                        System.out.println("Operación de búsqueda cancelada. El Nombre no puede estar vacío.");
                    }
                } else {
                    System.out.println("No hay conexión establecida. No se puede realizar la búsqueda.");
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
