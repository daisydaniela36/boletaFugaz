package com.example.boletafugaz.Model;

public class Productos {
    String nombre;
    String cantidad;
    String precio;

    public Productos() {
    }


    public Productos(String nombre,String cantidad, String precio) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
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

    public String toString() {
        return  nombre;
    }
}
