package service;


import domain.*;
import java.util.Optional;


public class WichoFitService {
private Pedido pedido;


public void crearPedido(Cliente cliente) {
this.pedido = new Pedido(cliente);
}


public Pedido getPedido() { return pedido; }


public void agregarItem(Producto producto, int cantidad) {
if (pedido == null) throw new IllegalStateException("No hay pedido en creación.");
ItemPedido it = new ItemPedido(producto, cantidad);
pedido.agregarItem(it);
}


public void confirmarPedido() {
if (pedido == null) throw new IllegalStateException("No hay pedido a confirmar.");
pedido.confirmar();
}


public String obtenerResumen() {
if (pedido == null) throw new IllegalStateException("No hay pedido.");
return pedido.generarResumen();
}
}