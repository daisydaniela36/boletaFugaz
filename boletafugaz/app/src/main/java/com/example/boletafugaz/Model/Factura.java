package com.example.boletafugaz.Model;

public class Factura {
    String empresa;
    String giro_Empresa;
    String rut;
    String razon_Social;
    String giro;
    String direccion;
    String region;
    String provincia;
    String comuna;

    public Factura() {
    }

    public Factura(String empresa, String giro_Empresa, String rut, String razon_Social, String giro, String direccion, String region, String provincia, String comuna) {
        this.empresa = empresa;
        this.giro_Empresa = giro_Empresa;
        this.rut = rut;
        this.razon_Social = razon_Social;
        this.giro = giro;
        this.direccion = direccion;
        this.region = region;
        this.provincia = provincia;
        this.comuna = comuna;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getGiro_Empresa() {
        return giro_Empresa;
    }

    public void setGiro_Empresa(String giro_Empresa) {
        this.giro_Empresa = giro_Empresa;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
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
}
