package domain;


public class ItemPedido {
private final Producto producto;
private final int cantidad;


public ItemPedido(Producto producto, int cantidad) {
if (cantidad <= 0) throw new IllegalArgumentException("Cantidad debe ser > 0");
this.producto = producto;
this.cantidad = cantidad;
}


public Producto getProducto() { return producto; }
public int getCantidad() { return cantidad; }


public int getPrecioUnitarioAplicado() {
if (producto.hasVolumen() && cantidad >= producto.getUmbralVolumen()) {
return producto.getPrecioVolumen();
}
return producto.getPrecioNormal();
}


public int getSubtotal() {
return getPrecioUnitarioAplicado() * cantidad;
}
}