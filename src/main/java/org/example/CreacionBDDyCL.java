package org.example;

import Exceptions.MongoOperationException;
import com.mongodb.MongoCommandException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreacionBDDyCL {
    private JTextField nombre_BDD;
    private JTextField Nombre_coleccion;
    private JButton confirmarButton;
    JPanel VentanaCreacion;
    private JTabbedPane tabbedPane1;
    private JButton eliminarLaBaseDeButton;
    private JButton eliminarSoloLaColecciónButton;
    private JComboBox<String> BDDComboBox;
    private JComboBox<String> ColeccionComboBox;
    private ConexionMongo conexionMongo;

    public CreacionBDDyCL(ConexionMongo datosConexion) {
        this.conexionMongo = datosConexion;
        confirmarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nombre_BDD.getText().isEmpty() || Nombre_coleccion.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaCreacion, "Por favor, llene todos los campos");
                    return;
                }
                String nombreBDD = nombre_BDD.getText();
                String nombreColeccion = Nombre_coleccion.getText();
                conexionMongo.CrearBaseDeDatosYColeccion(nombreBDD, nombreColeccion);
                JOptionPane.showMessageDialog(VentanaCreacion, "Base de datos y colección creadas correctamente");
            }
        });

        eliminarLaBaseDeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (BDDComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(VentanaCreacion, "Por favor, seleccione una base de datos");
                    return;
                }
                try{
                    String nombreBDD = BDDComboBox.getSelectedItem().toString();
                    conexionMongo.EliminarBaseDeDatos(nombreBDD);
                    JOptionPane.showMessageDialog(VentanaCreacion, "Base de datos eliminada correctamente");
                }
                catch (MongoOperationException ex){
                    JOptionPane.showMessageDialog(VentanaCreacion, "Error al eliminar la base de datos");
                }

            }
        });
        BDDComboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                BDDComboBox.removeAllItems();
                for (String nombreBDD : conexionMongo.ListarBasesDeDatos()) {
                    BDDComboBox.addItem(nombreBDD);
                }
            }
        });

        eliminarSoloLaColecciónButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (BDDComboBox.getSelectedItem() == null || ColeccionComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(VentanaCreacion, "Por favor, seleccione una base de datos y una colección");
                    return;
                }
                String nombreBDD = BDDComboBox.getSelectedItem().toString();
                String nombreColeccion = ColeccionComboBox.getSelectedItem().toString();
                try {
                    conexionMongo.EliminarColeccion(nombreBDD, nombreColeccion);
                    JOptionPane.showMessageDialog(VentanaCreacion, "Colección eliminada correctamente");
                } catch (MongoOperationException ex) {
                    JOptionPane.showMessageDialog(VentanaCreacion, "Error al eliminar la colección");
                }

            }
        });

        ColeccionComboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ColeccionComboBox.removeAllItems();
                for (String nombreColeccion : conexionMongo.ListarColecciones(BDDComboBox.getSelectedItem().toString())) {
                    ColeccionComboBox.addItem(nombreColeccion);
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        BDDComboBox= new JComboBox<String>();
        ColeccionComboBox= new JComboBox<String>();
    }
}
