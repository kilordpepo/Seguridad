package Entidades;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pepo
 * Clase usuario que posee los atributos de un usuario
 */
public class Usuario {

    private long id;
    private String pnombre;
    private String snombre;
    private String correo;
    private String clave;

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPnombre(String pnombre) {
        this.pnombre = pnombre;
    }

    public void setSnombre(String snombre) {
        this.snombre = snombre;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public long getId() {
        return id;
    }

    public String getPnombre() {
        return pnombre;
    }

    public String getSnombre() {
        return snombre;
    }

    public String getClave() {
        return clave;
    }
}
