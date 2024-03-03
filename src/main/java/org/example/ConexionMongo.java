package org.example;

import Exceptions.MongoConnectException;
import Exceptions.MongoOperationException;
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

    /**
     * Constructor de la clase ConexionMongo*/
    public ConexionMongo(){}

    /**
     * Constructor de la clase ConexionMongo
     * @param usuario Usuario para la conexion a la base de datos de MongoDB
     * @param contraseña Contraseña para la conexion a la base de datos de MongoDB
     * @param servidor Protocolo para la conexion a la base de datos de MongoDB
     * @param cluster Cluster para la conexion a la base de datos de MongoDB
     * @throws MongoConnectException Excepcion para el manejo de errores en la conexion a la base de datos de MongoDB
     */
    public ConexionMongo(String usuario, String contraseña, String servidor, String cluster) throws MongoConnectException {
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.servidor = servidor;
        this.cluster = cluster;
        try{
            this.cliente = MongoClients.create(servidor + "://" + usuario + ":" + contraseña + "@" + cluster + "/?retryWrites=true&w=majority");
        } catch (java.lang.IllegalArgumentException ie) {
            this.cliente = null;
            throw new MongoConnectException("Error al establecer la conexión", ie);
        }
    }

    // Métodos

    /**
     * Metodo para establecer la conexion a la base de datos de MongoDB
     * @param usuario Usuario para la conexion a la base de datos de MongoDB
     * @param contraseña Contraseña para la conexion a la base de datos de MongoDB
     * @param servidor Protocolo para la conexion a la base de datos de MongoDB
     * @param cluster Cluster para la conexion a la base de datos de MongoDB
     * @throws MongoConnectException Excepcion para el manejo de errores en la conexion a la base de datos de MongoDB
     */
    public void setConexion(String usuario, String contraseña, String servidor, String cluster) throws MongoConnectException {
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.servidor = servidor;
        this.cluster = cluster;
        try{
            this.cliente = MongoClients.create(servidor + "://" + usuario + ":" + contraseña + "@" + cluster + "/?retryWrites=true&w=majority");
        } catch (java.lang.IllegalArgumentException ie) {
            this.cliente = null;
            throw new MongoConnectException("Error al establecer la conexión", ie);
        }
    }

    /**
     * Metodo para verificar si la conexion a la base de datos de MongoDB fue exitosa
     * @return Retorna true si la conexion fue exitosa, false en caso contrario
     */
    public boolean Conectado() {
        try{
            // Obtiene la referencia a la base de datos
            this.cliente.listDatabaseNames();
            conexion_establecida = true;
        } catch (Exception ex) {
            conexion_establecida = false;
        }
        return conexion_establecida;
    }

    /**
     * Metodo para desconectar la conexion a la base de datos de MongoDB
     */
    public void Desconectar() {
        this.cliente.close();
    }

    /**
     * Metodo para listar las bases de datos de la conexion a la base de datos de MongoDB
     * @return Retorna un iterable con las bases de datos de la conexion a la base de datos de MongoDB
     */
    public MongoIterable<String> ListarBasesDeDatos() {
        try{
            // Obtiene la referencia a la base de datos
            return this.cliente.listDatabaseNames();
        } catch (MongoException me) {
            return null;
        }
    }

    /**
     * Metodo para listar las colecciones de una base de datos de la conexion a la base de datos de MongoDB
     * @param base Base de datos de la conexion a la base de datos de MongoDB
     * @return Retorna un iterable con las colecciones de la base de datos de la conexion a la base de datos de MongoDB
     */
    public MongoIterable<String> ListarColecciones(String base) {
        try{
            // Obtiene la referencia a la coleccion en la base de datos
            return this.cliente.getDatabase(base).listCollectionNames();
        } catch (MongoException me) {
            return null;
        }
    }

    // Método para crear base de datos y colección

    /**
     * Metodo para crear una base de datos y una coleccion en la conexion a la base de datos de MongoDB
     * @param nombreBaseDatos Nombre de la base de datos a crear
     * @param nombreColeccion Nombre de la coleccion a crear
     */
    public void CrearBaseDeDatosYColeccion(String nombreBaseDatos, String nombreColeccion) {
        // Obtiene la referencia a la base de datos
        MongoDatabase database = this.cliente.getDatabase(nombreBaseDatos);
        // Obtiene la referencia a la colección
        MongoCollection<Document> collection = database.getCollection(nombreColeccion);
        // Realiza alguna operación en la colección para asegurar su creación, como insertar un documento
        collection.insertOne(new Document());
    }

    /**
     * Elimina una base de datos existente en el servidor
     * @param nombreBaseDatos Nombre de la base de datos a eliminar
     * @throws MongoOperationException Si la eliminacion falla*/
    public void EliminarBaseDeDatos(String nombreBaseDatos) throws MongoOperationException {
        try{
            // Obtiene la referencia a la base de datos
            MongoDatabase database = this.cliente.getDatabase(nombreBaseDatos);
            // Elimina la base de datos
            database.drop();
        }catch (MongoException me) {
            throw new MongoOperationException("Error al eliminar la base de datos", me);
        }
    }

    /**
     * Elimina una colección existente en una base de datos
     * @param nombreBaseDatos Nombre de la base de datos
     * @param nombreColeccion Nombre de la colección a eliminar
     * @throws MongoOperationException Si la eliminacion falla*/
    public void EliminarColeccion(String nombreBaseDatos, String nombreColeccion) throws MongoOperationException {
        try{
            // Obtiene la referencia a la base de datos
            MongoDatabase database = this.cliente.getDatabase(nombreBaseDatos);
            // Obtiene la referencia a la colección
            MongoCollection<Document> collection = database.getCollection(nombreColeccion);
            // Elimina la colección
            collection.drop();
        }catch (MongoException me){
            throw new MongoOperationException("Error al eliminar la colección", me);
        }

    }

    // Método para insertar un documento

    /**
     * Metodo para insertar un documento en la coleccion de la base de datos de MongoDB
     * @param nombreBaseDatos Nombre de la base de datos
     * @param nombreColeccion Nombre de la coleccion
     * @param documento Documento a insertar
     */
    public void InsertarDocumento(String nombreBaseDatos, String nombreColeccion, Document documento) {
        // Obtiene la referencia a la base de datos
        MongoDatabase database = this.cliente.getDatabase(nombreBaseDatos);
        // Obtiene la referencia a la colección
        MongoCollection<Document> collection = database.getCollection(nombreColeccion);
        // Inserta el documento en la colección
        collection.insertOne(documento);
    }

    // Método para consultar documentos

    /**
     * Metodo para consultar los documentos de la coleccion de la base de datos de MongoDB
     * @param nombreBaseDatos Nombre de la base de datos
     * @param nombreColeccion Nombre de la coleccion
     * @return Retorna un iterable con los documentos de la coleccion de la base de datos de MongoDB
     */
    public FindIterable<Document> ConsultarDocumentos(String nombreBaseDatos, String nombreColeccion) {
        // Obtiene la referencia a la base de datos
        MongoDatabase database = this.cliente.getDatabase(nombreBaseDatos);
        // Obtiene la referencia a la colección
        MongoCollection<Document> collection = database.getCollection(nombreColeccion);
        // Realiza la consulta de todos los documentos en la colección
        return collection.find();
    }

    // Metodo para eliminar un documento

    /**
     * Metodo para eliminar un documento de la coleccion de la base de datos de MongoDB
     * @param nombreBaseDatos Nombre de la base de datos
     * @param nombreColeccion Nombre de la coleccion
     * @param documento Documento a eliminar
     */
    public void EliminarDocumento(String nombreBaseDatos, String nombreColeccion, Document documento) {
        // Obtiene la referencia a la base de datos
        MongoDatabase database = this.cliente.getDatabase(nombreBaseDatos);
        // Obtiene la referencia a la colección
        MongoCollection<Document> collection = database.getCollection(nombreColeccion);
        // Elimina el documento de la colección
        collection.deleteOne(documento);
    }
}
