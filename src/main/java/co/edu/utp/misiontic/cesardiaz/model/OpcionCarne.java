package co.edu.utp.misiontic.cesardiaz.model;

public class OpcionCarne {
    private String nombre;

    public OpcionCarne(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
