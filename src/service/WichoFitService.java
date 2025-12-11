package service;

import domain.*;

public class WichoFitService {
    private Pedido pedido; // Pedido actual en memoria

    // Crea un nuevo pedido para un cliente
    public void crearPedido(Cliente cliente) {
        this.pedido = new Pedido(cliente);
    }

    // Obtiene el pedido actual
    public Pedido getPedido() { return pedido; }

    // Agrega un item al pedido actual
    public void agregarItem(Producto producto, int cantidad) {
        if (pedido == null) throw new IllegalStateException("No hay pedido en creación.");
        ItemPedido it = new ItemPedido(producto, cantidad);
        pedido.agregarItem(it);
    }

    // Confirma el pedido actual (cambia estado a CONFIRMADO)
    public void confirmarPedido() {
        if (pedido == null) throw new IllegalStateException("No hay pedido a confirmar.");
        pedido.confirmar();
    }

    // Obtiene el resumen formateado del pedido
    public String obtenerResumen() {
        if (pedido == null) throw new IllegalStateException("No hay pedido.");
        return pedido.generarResumen();
    }
}