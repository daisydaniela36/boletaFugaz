package com.example.boletafugaz.Model;

public class Factura {
    String fecha;
    String rut_cliente;
    String razon_Social;
    String giro;
    String direccion;
    String region;
    String provincia;
    String comuna;

    public Factura() {
    }

    public Factura(String fecha, String rut_cliente, String razon_Social, String giro, String direccion, String region, String provincia, String comuna) {
        this.fecha = fecha;
        this.rut_cliente = rut_cliente;
        this.razon_Social = razon_Social;
        this.giro = giro;
        this.direccion = direccion;
        this.region = region;
        this.provincia = provincia;
        this.comuna = comuna;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getRut_cliente() {
        return rut_cliente;
    }

    public void setRut_cliente(String rut_cliente) {
        this.rut_cliente = rut_cliente;
    }

    public String getRazon_Social() {
        return razon_Social;
    }

    public void setRazon_Social(String razon_Social) {
        this.razon_Social = razon_Social;
    }

    public String getGiro() {
        return giro;
    }

    public void setGiro(String giro) {
        this.giro = giro;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    @Override
    public String toString() { return "fecha: " + fecha;
    }
}
