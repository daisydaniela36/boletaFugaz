package com.example.boletafugaz.Model;

public class Factura {
    String id;
    Integer numero_factura;
    String giro_empresa;
    String fecha;
    String rut_cliente;
    String razon_Social;
    String giro;
    String direccion;
    String region;
    String provincia;
    String comuna;
    Integer iva;
    Integer total;

    public Factura() {
    }

    public Factura(String id,Integer numero_factura,String giro_empresa,String fecha, String rut_cliente, String razon_Social, String giro, String direccion, String region, String provincia, String comuna,Integer iva,Integer total) {
        this.id = id;
        this.numero_factura = numero_factura;
        this.giro_empresa = giro_empresa;
        this.fecha = fecha;
        this.rut_cliente = rut_cliente;
        this.razon_Social = razon_Social;
        this.giro = giro;
        this.direccion = direccion;
        this.region = region;
        this.provincia = provincia;
        this.comuna = comuna;
        this.iva = iva;
        this.total = total;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumero_factura() { return numero_factura; }

    public void setNumero_factura(Integer numero_factura) { this.numero_factura = numero_factura; }

    public String getGiro_empresa() {
        return giro_empresa;
    }

    public void setGiro_empresa(String giro_empresa) {
        this.giro_empresa = giro_empresa;
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

    public Integer getIva() { return iva; }

    public void setIva(Integer iva) { this.iva = iva; }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() { return "fecha: " + fecha+"\n"+"Total: "+total;
    }
}
