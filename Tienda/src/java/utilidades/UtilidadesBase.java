package utilidades;

import Entidades.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pepo
 * Clase que realiza las peticiones a la base de datos
 */
public class UtilidadesBase {

    private Connection conexion;
    
    //Constructor
    public UtilidadesBase() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        conexion = DriverManager.getConnection("jdbc:postgresql://"
                + "localhost:5432/tienda", "admin_cocotrip", "ds1718a");
    }
    /**
 *
 * @author pepo
 * @param user
 * 
 * Metodo que registra a un usuario
 */
    public long RegistrarUsuario(Usuario user) throws SQLException {
        long id = 0;
        try {
            Statement st = conexion.createStatement();
            String sql = "INSERT INTO USUARIO("
                    + "            id_user, name_user, lname_user, email_user, password_user, bloqueado)"
                    + "    VALUES (DEFAULT, '" + user.getPnombre() + "', '" + user.getSnombre()
                    + "','" + user.getCorreo() + "','" + user.getClave() + "', false) RETURNING(id_user);";
            st.executeQuery(sql);
            st.getResultSet().next();
            id = st.getResultSet().getLong(1);
        } catch (SQLException ex) {
            Logger.getLogger(UtilidadesBase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conexion.close();
            return id;
        }
    }
 /**
 *
 * @author pepo
 * @param user
 * 
 * Metodo que Inicia sesion a un usuario
 */
    public Usuario IniciarSesion(Usuario user) {
        Usuario usuarioBase = new Usuario();
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM USUARIO WHERE email_user='" + user.getCorreo() + "'"
                    + "AND bloqueado=FALSE");
            while (rs.next()) {
                usuarioBase.setId(rs.getLong(1));
                usuarioBase.setPnombre(rs.getString(2));
                usuarioBase.setSnombre(rs.getString(3));
                usuarioBase.setCorreo(rs.getString(4));
                usuarioBase.setClave(rs.getString(5));
            }
            rs.close();
            st.close();
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(UtilidadesBase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return usuarioBase;
        }
    }
     /**
 *
 * @author pepo
 * @param user
 * 
 * Metodo que bloquea a un usuario
 */
    public void bloquearUsuario(Usuario user) {
        try {
            Statement st = conexion.createStatement();
            String sql = "UPDATE public.usuario "
                    + "   SET bloqueado=TRUE "
                    + " WHERE usuario.id_user =" + user.getId();
            st.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(UtilidadesBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
