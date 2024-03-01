package org.example;

import com.mongodb.client.FindIterable;
import org.bson.Document;

public class Main {
    public static void main(String[] args) {
        ConexionMongo conexion = new ConexionMongo("esfot", "esfot2024", "mongodb+srv", "cluster0.xzffuex.mongodb.ne");
        if (conexion.Conectado()){
            System.out.println("Conexión establecida");
            System.out.println("Bases de datos:");
            for (String base : conexion.ListarBasesDeDatos()) {
                System.out.println(base);
                System.out.println("Colecciones:");
                for (String coleccion : conexion.ListarColecciones(base)) {
                    System.out.println(coleccion);
                }
            }
            conexion.Desconectar();
        } else {
            System.out.println("No se pudo establecer la conexión");
        }
    }
}