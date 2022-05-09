package com.example.boletafugaz.Model;

import java.util.ArrayList;

public class Productos {
    ArrayList<Productos> listaProductos = new ArrayList<>();

    String nombre;
    String cantidad;
    String precio;
    Integer total;

    public Productos() {
    }


    public Productos(String nombre,String cantidad, String precio,Integer total) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = total;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String toString() {
        return  nombre;
    }


}
