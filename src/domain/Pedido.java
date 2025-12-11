package domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private final Cliente cliente; // Cliente asociado al pedido
    private final List<ItemPedido> items = new ArrayList<>(); // Lista de items del pedido
    private EstadoPedido estado = EstadoPedido.EN_CREACION; // Estado actual del pedido
    private final LocalDateTime fechaCreacion; // Fecha de creación del pedido
    
    // Formato para mostrar fechas
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Constructor: crea pedido con cliente y fecha actual
    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.fechaCreacion = LocalDateTime.now();
    }

    // --- GETTERS (ENCAPSULACIÓN) ---
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItems() { return Collections.unmodifiableList(items); }
    public EstadoPedido getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    // --- MÉTODOS PARA GESTIONAR ITEMS ---
    public void agregarItem(ItemPedido item) {
        if (estado == EstadoPedido.CONFIRMADO) 
            throw new IllegalStateException("Pedido ya confirmado; no se puede editar.");
        items.add(item);
    }

    // --- CÁLCULOS FINANCIEROS ---
    // Calcula total bruto (suma de subtotales)
    public int calcularTotalBruto() {
        return items.stream().mapToInt(ItemPedido::getSubtotal).sum();
    }

    // Calcula porcentaje de descuento según reglas de negocio
    public double calcularPorcentajeDescuento() {
        int totalBruto = calcularTotalBruto();

        // Contar suplementos (productos sin volumen)
        long numSuplementos = items.stream()
                .filter(i -> !i.getProducto().hasVolumen())
                .count();

        // Sumar cantidad de prendas (productos con volumen)
        long prendas = items.stream()
                .filter(i -> i.getProducto().hasVolumen())
                .mapToInt(ItemPedido::getCantidad)
                .sum();

        // Reglas de descuento:
        // 1. 10% si tiene al menos 1 suplemento y 4+ prendas
        // 2. 7% si total > $300,000
        // 3. 5% si total > $150,000
        if (numSuplementos >= 1 && prendas >= 4) return 0.10;
        if (totalBruto > 300000) return 0.07;
        if (totalBruto > 150000) return 0.05;

        return 0.0;
    }

    // Calcula monto del descuento
    public int calcularMontoDescuento() {
        return (int) Math.round(calcularTotalBruto() * calcularPorcentajeDescuento());
    }

    // Calcula total final (bruto - descuento)
    public int calcularTotalFinal() {
        return calcularTotalBruto() - calcularMontoDescuento();
    }

    // --- CONFIRMAR PEDIDO (CAMBIO DE ESTADO) ---
    public void confirmar() {
        if (items.isEmpty()) 
            throw new IllegalStateException("No hay ítems en el pedido.");
        this.estado = EstadoPedido.CONFIRMADO;
    }

    // --- GENERAR RESUMEN FORMATEADO DEL PEDIDO ---
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
        
        // Detalles de productos (tabla formateada)
        sb.append("DETALLES DEL PEDIDO:\n");
        sb.append(String.format("%-20s %-8s %-12s %-10s\n","PRODUCTO","CANT","PRECIO U","SUBTOTAL"));
        sb.append("--------------------------------------------------\n");
        
        for (ItemPedido it : items) {
            sb.append(String.format("%-20s %-8d %-12d %-10d\n",
                it.getProducto().getNombre(), it.getCantidad(), it.getPrecioUnitarioAplicado(), it.getSubtotal()));
        }
        
        sb.append("--------------------------------------------------\n\n");
        
        // Resumen financiero
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