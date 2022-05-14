package com.example.boletafugaz.Model;

import java.util.Date;

public class Boleta {
    String fecha;
    Integer total;

    public Boleta() {
    }

    public Boleta(String fecha, Integer total) {
        this.fecha = fecha;
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
