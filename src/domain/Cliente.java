package domain;


public class Cliente {
private final String nombre;
private final String telefono;


public Cliente(String nombre, String telefono) {
if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("Nombre vacío");
if (telefono == null || telefono.trim().isEmpty()) throw new IllegalArgumentException("Teléfono vacío");
this.nombre = nombre.trim();
this.telefono = telefono.trim();
}


public String getNombre() { return nombre; }
public String getTelefono() { return telefono; }
}