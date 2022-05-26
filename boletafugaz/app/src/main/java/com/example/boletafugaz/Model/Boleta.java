package com.example.boletafugaz.Model;

import java.util.Date;

public class Boleta {
    Integer numero_boleta;
    String fecha;
    Integer iva;
    Integer total;

    public Boleta() {
    }

    public Boleta(Integer numero_boleta,String fecha, Integer iva,Integer total) {
        this.numero_boleta = numero_boleta;
        this.fecha = fecha;
        this.iva = iva;
        this.total = total;
    }

    public Integer getNumero_boleta() {
        return numero_boleta;
    }

    public void setNumero_boleta(Integer numero_boleta) {
        this.numero_boleta = numero_boleta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getIva() {
        return iva;
    }

    public void setIva(Integer iva) {
        this.iva = iva;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String toString() {
        return  "Fecha: "+fecha +"\n"+"Total: "+total;
    }
}
