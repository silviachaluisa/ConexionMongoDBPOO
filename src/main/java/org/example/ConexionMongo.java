package org.example;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

public class ConexionMongo {
    // Atributos
    private String usuario;
    private String contraseña;
    private String servidor;
    private String cluster;
    private Document documento;
    private MongoClient cliente;
    private boolean conexion_establecida;

    // Constructor
    public ConexionMongo(String usuario, String contraseña, String servidor, String cluster) {
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.servidor = servidor;
        this.cluster = cluster;
        try {
            this.cliente = MongoClients.create(servidor + "://" + usuario + ":" + contraseña + "@" + cluster + "/?retryWrites=true&w=majority");
            this.conexion_establecida = true;
        } catch (MongoException me)
        {
            this.conexion_establecida = false;
        }
    }

    public boolean Conectado() {
        return conexion_establecida;
    }

    public void Desconectar() {
        this.cliente.close();
    }

    public MongoIterable<String> ListarBasesDeDatos() {
        try{
            // Obtiene la referencia a la base de datos
            return this.cliente.listDatabaseNames();
        } catch (MongoException me) {
            return null;
        }
    }

    public MongoIterable<String> ListarColecciones(String base) {
        try{
            // Obtiene la referencia a la coleccion en la base de datos
            return this.cliente.getDatabase(base).listCollectionNames();
        } catch (MongoException me) {
            return null;
        }
    }

    // Método para crear base de datos y colección
    public void CrearBaseDeDatosYColeccion(String nombreBaseDatos, String nombreColeccion) {
        // Obtiene la referencia a la base de datos
        MongoDatabase database = this.cliente.getDatabase(nombreBaseDatos);
        // Obtiene la referencia a la colección
        MongoCollection<Document> collection = database.getCollection(nombreColeccion);
        // Realiza alguna operación en la colección para asegurar su creación, como insertar un documento
        collection.insertOne(new Document());
    }

    // Método para insertar un documento
    public void InsertarDocumento(String nombreBaseDatos, String nombreColeccion, Document documento) {
        // Obtiene la referencia a la base de datos
        MongoDatabase database = this.cliente.getDatabase(nombreBaseDatos);
        // Obtiene la referencia a la colección
        MongoCollection<Document> collection = database.getCollection(nombreColeccion);
        // Inserta el documento en la colección
        collection.insertOne(documento);
    }

    // Método para consultar documentos
    public FindIterable<Document> ConsultarDocumentos(String nombreBaseDatos, String nombreColeccion) {
        // Obtiene la referencia a la base de datos
        MongoDatabase database = this.cliente.getDatabase(nombreBaseDatos);
        // Obtiene la referencia a la colección
        MongoCollection<Document> collection = database.getCollection(nombreColeccion);
        // Realiza la consulta de todos los documentos en la colección
        return collection.find();
    }

    // Metodo para eliminar un documento
    public void EliminarDocumento(String nombreBaseDatos, String nombreColeccion, Document documento) {
        // Obtiene la referencia a la base de datos
        MongoDatabase database = this.cliente.getDatabase(nombreBaseDatos);
        // Obtiene la referencia a la colección
        MongoCollection<Document> collection = database.getCollection(nombreColeccion);
        // Elimina el documento de la colección
        collection.deleteOne(documento);
    }
}
