package domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private final Cliente cliente;
    private final List<ItemPedido> items = new ArrayList<>();
    private EstadoPedido estado = EstadoPedido.EN_CREACION;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaConfirmacion;
    
    // Formato de fecha
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItems() { return Collections.unmodifiableList(items); }
    public EstadoPedido getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaConfirmacion() { return fechaConfirmacion; }

    public void agregarItem(ItemPedido item) {
        if (estado == EstadoPedido.CONFIRMADO) throw new IllegalStateException("Pedido ya confirmado; no se puede editar.");
        items.add(item);
    }

    public int calcularTotalBruto() {
        return items.stream().mapToInt(ItemPedido::getSubtotal).sum();
    }

    // Ejemplo de regla de descuento; modifícala según requerimiento del curso
    public double calcularPorcentajeDescuento() {
        int totalBruto = calcularTotalBruto();

        long numSuplementos = items.stream()
                .filter(i -> !i.getProducto().hasVolumen())
                .count();

        long prendas = items.stream()
                .filter(i -> i.getProducto().hasVolumen())
                .mapToInt(ItemPedido::getCantidad)
                .sum();

        // Regla de ejemplo: puedes ajustarla luego
        if (numSuplementos >= 1 && prendas >= 4) return 0.10;
        if (totalBruto > 300000) return 0.07;
        if (totalBruto > 150000) return 0.05;

        return 0.0;
    }

    public int calcularMontoDescuento() {
        return (int) Math.round(calcularTotalBruto() * calcularPorcentajeDescuento());
    }

    public int calcularTotalFinal() {
        return calcularTotalBruto() - calcularMontoDescuento();
    }

    public void confirmar() {
        if (items.isEmpty()) throw new IllegalStateException("No hay ítems en el pedido.");
        this.estado = EstadoPedido.CONFIRMADO;
        this.fechaConfirmacion = LocalDateTime.now();
    }

    public String generarResumen() {
        StringBuilder sb = new StringBuilder();
        
        // Encabezado con fecha
        String fechaStr = fechaCreacion.format(DATE_FORMATTER);
        sb.append("==================================================\n");
        sb.append("PEDIDO WICHO FIT - ").append(fechaStr).append("\n");
        sb.append("==================================================\n\n");
        
        // Información del cliente
        sb.append("CLIENTE:\n");
        sb.append("  Nombre: ").append(cliente.getNombre()).append("\n");
        sb.append("  Teléfono: ").append(cliente.getTelefono()).append("\n");
        sb.append("--------------------------------------------------\n\n");
        
        // Detalles de productos
        sb.append("DETALLES DEL PEDIDO:\n");
        sb.append(String.format("%-20s %-8s %-12s %-10s\n","PRODUCTO","CANT","PRECIO U","SUBTOTAL"));
        sb.append("--------------------------------------------------\n");
        
        for (ItemPedido it : items) {
            sb.append(String.format("%-20s %-8d %-12d %-10d\n",
                it.getProducto().getNombre(), it.getCantidad(), it.getPrecioUnitarioAplicado(), it.getSubtotal()));
        }
        
        sb.append("--------------------------------------------------\n\n");
        
        // Totales
        int bruto = calcularTotalBruto();
        double porc = calcularPorcentajeDescuento() * 100.0;
        int montoDesc = calcularMontoDescuento();
        int finalTotal = calcularTotalFinal();
        
        sb.append("RESUMEN FINANCIERO:\n");
        sb.append(String.format("  Total bruto:     $%,d\n", bruto));
        if (montoDesc > 0) {
            sb.append(String.format("  Descuento (%d%%):  -$%,d\n", (int)porc, montoDesc));
        } else {
            sb.append(String.format("  Descuento:        $0\n"));
        }
        sb.append("  ------------------------------\n");
        sb.append(String.format("  TOTAL FINAL:      $%,d\n", finalTotal));
        
        sb.append("\n==================================================\n");
        sb.append("¡Gracias por su compra en Wicho Fit!\n");
        sb.append("==================================================\n");
        
        return sb.toString();
    }
}