package app;

import domain.*;
import service.WichoFitService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainModern {
    // Colores (opción C)
    private static final Color BG = new Color(15, 15, 15);
    private static final Color SIDEBAR = new Color(26, 26, 26);
    private static final Color ACCENT_BLUE = new Color(0x00, 0x51, 0xA2);
    private static final Color ACCENT_PURPLE = new Color(0x5E, 0x2B, 0x97);
    private static final Color CARD = new Color(31, 31, 31);
    private static final Color TEXT_SECOND = new Color(184, 184, 184);
    private static final Color TEXT_WHITE = new Color(255, 255, 255);
    
    // Patrones de validación
    private static final Pattern NOMBRE_PATTERN = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$");
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^\\d{10}$");
    
    // Formato de fecha y hora
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // URLs confiables de Unsplash y otros servicios
    private static final Map<Producto, String> PRODUCT_IMAGE_URLS = new HashMap<>();
    static {
        // URLs confiables para todos los productos
        PRODUCT_IMAGE_URLS.put(Producto.POLERA_BASICA, "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=300&fit=crop&auto=format");
        PRODUCT_IMAGE_URLS.put(Producto.SUDADERA, "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=400&h=300&fit=crop&auto=format");
        PRODUCT_IMAGE_URLS.put(Producto.CREATINA_300G, "https://images.unsplash.com/photo-1599161144150-7c6ddefc2bf9?w=400&h=300&fit=crop&auto=format");
        PRODUCT_IMAGE_URLS.put(Producto.PRE_ENTRENO_200G, "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop&auto=format");
        
        // URLs para nuevos productos - todas de Unsplash (confiables)
        PRODUCT_IMAGE_URLS.put(Producto.GORRA_DEPORTIVA, "https://images.unsplash.com/photo-1521369909029-2afed882baee?w=400&h=300&fit=crop&auto=format");
        PRODUCT_IMAGE_URLS.put(Producto.PANTALON_DEPORTIVO, "https://images.unsplash.com/photo-1552902865-b72c031ac5ea?w=400&h=300&fit=crop&auto=format");
        PRODUCT_IMAGE_URLS.put(Producto.BANDAS_RESISTENCIA, "https://images.unsplash.com/photo-1598974357801-cbca100e5d10?w=400&h=300&fit=crop&auto=format");
        PRODUCT_IMAGE_URLS.put(Producto.BOTELLA_AGUA, "https://images.unsplash.com/photo-1523362628745-0c100150b504?w=400&h=300&fit=crop&auto=format");
        PRODUCT_IMAGE_URLS.put(Producto.PROTEINA_WHEY, "https://images.unsplash.com/photo-1594736797933-d0e49c3d8f88?w=400&h=300&fit=crop&auto=format");
        PRODUCT_IMAGE_URLS.put(Producto.GLUTAMINA_200G, "https://images.unsplash.com/photo-1599161144150-7c6ddefc2bf9?w=400&h=300&fit=crop&auto=format");
        PRODUCT_IMAGE_URLS.put(Producto.BCAAS_300G, "https://images.unsplash.com/photo-1599161144150-7c6ddefc2bf9?w=400&h=300&fit=crop&auto=format");
        PRODUCT_IMAGE_URLS.put(Producto.VITAMINAS_COMPLEJO_B, "https://images.unsplash.com/photo-1559757148-5c350d0d3c56?w=400&h=300&fit=crop&auto=format");
    }

    // Ruta del logo local
    private static final String LOGO_PATH = "src/assets/logo.png";

    private final WichoFitService service = new WichoFitService();

    private JFrame frame;
    private JPanel productsPanel;
    private JPanel ordersPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    
    // Para controlar si ya se creó el panel de productos
    private boolean productosPanelCreado = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainModern().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("WichoFit — Tienda");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 750);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG);

        // layout principal: sidebar + main
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerSize(2);
        split.setDividerLocation(220);
        split.setBorder(null);
        split.setBackground(BG);

        split.setLeftComponent(createSidebar());
        split.setRightComponent(createMainContent());

        frame.getContentPane().add(split);
        frame.setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel left = new JPanel();
        left.setBackground(SIDEBAR);
        left.setLayout(new BorderLayout());
        left.setBorder(new EmptyBorder(12,12,12,12));

        // Logo top
        JLabel logo = new JLabel();
        ImageIcon logoIcon = ImagenUtil.cargarDesdeArchivo(LOGO_PATH, 160, 80);
        if (logoIcon == null || logoIcon.getIconWidth() <= 0) {
            logoIcon = ImagenUtil.cargarDesdeRecurso("assets/logo.png", 160, 80);
        }
        logo.setIcon(logoIcon);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(logo, BorderLayout.NORTH);

        // Menu
        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnInicio = makeSidebarButton("Inicio", "🏠");
        btnInicio.addActionListener(e -> mostrarInicio());
        
        JButton btnProductos = makeSidebarButton("Productos", "🛍️");
        btnProductos.addActionListener(e -> mostrarProductos());
        
        JButton btnNuevoPedido = makeSidebarButton("Nuevo pedido", "➕");
        btnNuevoPedido.addActionListener(e -> {
            if (service.getPedido() == null) {
                JOptionPane.showMessageDialog(frame, 
                    "Primero navega a 'Productos' y agrega artículos a tu pedido.", 
                    "Nuevo pedido", JOptionPane.INFORMATION_MESSAGE);
                mostrarProductos();
            } else {
                mostrarProductos(); // Mostrar productos para continuar agregando
            }
        });
        
        JButton btnPedidosRealizados = makeSidebarButton("Pedidos realizados", "📦");
        btnPedidosRealizados.addActionListener(e -> mostrarPedidosRealizados());

        menu.add(btnInicio);
        menu.add(Box.createVerticalStrut(8));
        menu.add(btnProductos);
        menu.add(Box.createVerticalStrut(8));
        menu.add(btnNuevoPedido);
        menu.add(Box.createVerticalStrut(8));
        menu.add(btnPedidosRealizados);
        menu.add(Box.createVerticalGlue());

        left.add(menu, BorderLayout.CENTER);

        // Footer (pequeño)
        JLabel footer = new JLabel("WichoFit • v1.2");
        footer.setForeground(TEXT_SECOND);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setBorder(new EmptyBorder(8,0,4,0));
        left.add(footer, BorderLayout.SOUTH);

        return left;
    }

    private JButton makeSidebarButton(String text, String iconText) {
        JButton b = new JButton(String.format("%s   %s", iconText, text));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setFocusPainted(false);
        b.setBackground(SIDEBAR);
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        return b;
    }

    private JPanel createMainContent() {
        // Panel principal con CardLayout para cambiar entre vistas
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BG);
        
        // Crear las diferentes vistas
        mainContentPanel.add(createInicioPanel(), "inicio");
        mainContentPanel.add(createPedidosRealizadosPanel(), "pedidos");
        
        // Nota: El panel de productos se creará la primera vez que se acceda a él
        mainContentPanel.add(new JPanel(), "productos"); // Placeholder
        
        // Mostrar inicio por defecto
        cardLayout.show(mainContentPanel, "inicio");
        
        return mainContentPanel;
    }

    private JPanel createInicioPanel() {
        JPanel inicioPanel = new JPanel(new BorderLayout());
        inicioPanel.setBackground(BG);
        inicioPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        // Título
        JLabel titulo = new JLabel("Bienvenido a WichoFit");
        titulo.setForeground(TEXT_WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titulo);
        content.add(Box.createVerticalStrut(30));
        
        // Subtítulo
        JLabel subtitulo = new JLabel("Tu tienda fitness de confianza");
        subtitulo.setForeground(ACCENT_BLUE);
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 18));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(subtitulo);
        content.add(Box.createVerticalStrut(40));
        
        // Contenido
        String[] textos = {
            "✨ Sobre Nosotros ✨",
            "En WichoFit nos especializamos en proveer productos fitness de la más alta calidad.",
            "Desde ropa deportiva hasta suplementos nutricionales, tenemos todo lo que necesitas",
            "para alcanzar tus metas fitness.",
            "",
            "🎯 Nuestro Objetivo 🎯", 
            "Empoderar a cada persona en su journey fitness, proporcionando productos",
            "que combinen calidad, estilo y funcionalidad a precios competitivos.",
            "",
            "🛒 ¿Cómo Comprar? 🛒",
            "1. Navega por nuestros productos en la sección 'Productos'",
            "2. Agrega los items que desees a tu carrito",
            "3. Completa tus datos personales",
            "4. Confirma tu pedido y ¡listo!",
            "",
            "💪 ¡Tu transformación comienza aquí! 💪"
        };
        
        for (String texto : textos) {
            JLabel label = new JLabel(texto);
            label.setForeground(TEXT_SECOND);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(label);
            content.add(Box.createVerticalStrut(8));
        }
        
        // Logo grande en el centro
        content.add(Box.createVerticalStrut(40));
        JLabel logoGrande = new JLabel();
        ImageIcon logoIcon = ImagenUtil.cargarDesdeArchivo(LOGO_PATH, 200, 100);
        if (logoIcon == null || logoIcon.getIconWidth() <= 0) {
            logoIcon = ImagenUtil.cargarDesdeRecurso("assets/logo.png", 200, 100);
        }
        logoGrande.setIcon(logoIcon);
        logoGrande.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(logoGrande);
        
        inicioPanel.add(content, BorderLayout.CENTER);
        
        return inicioPanel;
    }

    private JPanel createProductosPanel() {
        // Solo crear el panel de productos una vez
        if (productosPanelCreado) {
            return null; // Ya existe, no crear de nuevo
        }
        
        productosPanelCreado = true;
        
        JPanel productosPanelContainer = new JPanel(new BorderLayout());
        productosPanelContainer.setBackground(BG);
        
        // Panel izquierdo: productos (grid)
        productsPanel = new JPanel();
        productsPanel.setBackground(BG);
        productsPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 16, 16));
        productsPanel.setBorder(new EmptyBorder(12,12,12,12));

        // Llena productos desde el enum Producto
        for (Producto p : Producto.values()) {
            String url = PRODUCT_IMAGE_URLS.getOrDefault(p, "");
            productsPanel.add(makeProductCard(p, url));
        }

        JScrollPane productsScroll = new JScrollPane(productsPanel);
        productsScroll.setBorder(null);
        productsScroll.getVerticalScrollBar().setUnitIncrement(12);
        productsScroll.setBackground(BG);

        // Panel derecho: pedidos (tickets)
        ordersPanel = new JPanel();
        ordersPanel.setBackground(BG);
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBorder(new EmptyBorder(12,12,12,12));

        JLabel ordersTitle = new JLabel("Pedido (tickets)");
        ordersTitle.setForeground(TEXT_WHITE);
        ordersTitle.setFont(ordersTitle.getFont().deriveFont(Font.BOLD, 16f));
        ordersPanel.add(ordersTitle);
        ordersPanel.add(Box.createVerticalStrut(8));

        JScrollPane ordersScroll = new JScrollPane(ordersPanel);
        ordersScroll.setPreferredSize(new Dimension(380, 600));
        ordersScroll.setBorder(null);
        ordersScroll.getVerticalScrollBar().setUnitIncrement(12);
        ordersScroll.setBackground(BG);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, productsScroll, ordersScroll);
        rightSplit.setDividerLocation(750);
        rightSplit.setDividerSize(2);
        rightSplit.setBorder(null);

        productosPanelContainer.add(rightSplit, BorderLayout.CENTER);
        
        return productosPanelContainer;
    }

    private JPanel createPedidosRealizadosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        // Título
        JLabel titulo = new JLabel("Pedidos Realizados");
        titulo.setForeground(TEXT_WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titulo);
        content.add(Box.createVerticalStrut(20));
        
        // Información
        JLabel info = new JLabel("<html><div style='text-align: center; color: #b8b8b8;'>"
                + "Aquí podrás ver el historial de todos tus pedidos confirmados.<br><br>"
                + "Por ahora, esta funcionalidad está en desarrollo.<br>"
                + "Próximamente podrás ver:<br><br>"
                + "• Historial completo de pedidos<br>"
                + "• Estado de envíos<br>"
                + "• Facturas digitales<br>"
                + "• Sistema de seguimiento<br><br>"
                + "¡Vuelve pronto para esta nueva funcionalidad!</div></html>");
        info.setFont(new Font("Arial", Font.PLAIN, 14));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(info);
        
        // Icono
        content.add(Box.createVerticalStrut(30));
        JLabel icono = new JLabel("📋");
        icono.setFont(new Font("Arial", Font.PLAIN, 60));
        icono.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(icono);
        
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }

    // Métodos para cambiar entre vistas
    private void mostrarInicio() {
        if (cardLayout != null && mainContentPanel != null) {
            cardLayout.show(mainContentPanel, "inicio");
        }
    }
    
    private void mostrarProductos() {
        if (cardLayout != null && mainContentPanel != null) {
            // Crear el panel de productos si no existe
            if (!productosPanelCreado) {
                JPanel productosPanel = createProductosPanel();
                if (productosPanel != null) {
                    // Reemplazar el placeholder con el panel real
                    mainContentPanel.add(productosPanel, "productos");
                }
            }
            
            cardLayout.show(mainContentPanel, "productos");
            
            // Refrescar el panel de pedidos si hay un pedido en curso
            if (service.getPedido() != null) {
                refreshOrdersPanel();
            }
        }
    }
    
    private void mostrarPedidosRealizados() {
        if (cardLayout != null && mainContentPanel != null) {
            cardLayout.show(mainContentPanel, "pedidos");
        }
    }

    private JPanel makeProductCard(Producto producto, String imageUrl) {
        JPanel card = new RoundedPanel(14, CARD);
        card.setPreferredSize(new Dimension(200, 260));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(10,10,10,10));

        // Imagen - usar la versión mejorada de ImagenUtil
        JLabel img = new JLabel();
        img.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Cargar imagen usando la utilidad mejorada
        ImageIcon imagenFinal = ImagenUtil.cargar(imageUrl, 160, 110);
        img.setIcon(imagenFinal);
        card.add(img, BorderLayout.NORTH);

        // Info medio
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(producto.getNombre());
        name.setForeground(TEXT_WHITE);
        name.setFont(name.getFont().deriveFont(Font.BOLD, 14f));
        
        // Mostrar precio de volumen si aplica
        String precioTexto;
        if (producto.hasVolumen()) {
            precioTexto = String.format("$%,d / $%,d vol", producto.getPrecioNormal(), producto.getPrecioVolumen()).replace(',', '.');
        } else {
            precioTexto = String.format("$%,d", producto.getPrecioNormal()).replace(',', '.');
        }
        
        JLabel price = new JLabel(precioTexto);
        price.setForeground(TEXT_SECOND);
        price.setFont(price.getFont().deriveFont(Font.PLAIN, 12f));
        info.add(name);
        info.add(Box.createVerticalStrut(6));
        info.add(price);
        
        // Mostrar umbral de volumen si aplica
        if (producto.hasVolumen() && producto.getUmbralVolumen() > 0) {
            JLabel volInfo = new JLabel("Vol: " + producto.getUmbralVolumen() + "+ unidades");
            volInfo.setForeground(new Color(150, 150, 200));
            volInfo.setFont(volInfo.getFont().deriveFont(Font.PLAIN, 10f));
            info.add(Box.createVerticalStrut(2));
            info.add(volInfo);
        }
        
        card.add(info, BorderLayout.CENTER);

        // Footer con boton agregar
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        foot.setOpaque(false);

        JButton add = new JButton("Agregar");
        add.setBackground(ACCENT_BLUE);
        add.setForeground(Color.WHITE);
        add.setFocusPainted(false);
        add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add.addActionListener(e -> onAgregarProducto(producto));

        foot.add(add);
        card.add(foot, BorderLayout.SOUTH);

        return card;
    }

    private void onAgregarProducto(Producto producto) {
        // Primero, asegurarnos de que estamos en la vista de productos
        mostrarProductos();
        
        // Pequeña pausa para que se cargue la vista
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Ignorar
        }
        
        // Ahora pedir la cantidad
        String qtyStr = JOptionPane.showInputDialog(frame, "Cantidad para " + producto.getNombre() + ":", "1");
        if (qtyStr == null) return;
        
        int qty;
        try {
            qty = Integer.parseInt(qtyStr.trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Ingrese una cantidad válida (>0).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear pedido si no existe (pedimos nombre y teléfono si faltan)
        if (service.getPedido() == null) {
            while (true) { // Loop hasta que los datos sean válidos
                JPanel input = new JPanel(new GridLayout(3, 2, 8, 8));
                JTextField nombre = new JTextField();
                JTextField telefono = new JTextField();
                
                input.add(new JLabel("Nombre:"));
                input.add(nombre);
                input.add(new JLabel("Teléfono (10 dígitos):"));
                input.add(telefono);
                input.add(new JLabel(""));
                input.add(new JLabel("<html><small>Solo letras y espacios</small></html>"));

                int res = JOptionPane.showConfirmDialog(frame, input, "Datos del cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (res != JOptionPane.OK_OPTION) return;
                
                String nombreStr = nombre.getText().trim();
                String telefonoStr = telefono.getText().trim();
                
                // Validar nombre (solo letras y espacios)
                if (!NOMBRE_PATTERN.matcher(nombreStr).matches()) {
                    JOptionPane.showMessageDialog(frame, 
                        "Nombre inválido. Solo se permiten letras y espacios.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    continue; // Volver a pedir datos
                }
                
                // Validar teléfono (exactamente 10 dígitos)
                if (!TELEFONO_PATTERN.matcher(telefonoStr).matches()) {
                    JOptionPane.showMessageDialog(frame, 
                        "Teléfono inválido. Debe tener exactamente 10 dígitos.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    continue; // Volver a pedir datos
                }
                
                try {
                    Cliente c = new Cliente(nombreStr, telefonoStr);
                    service.crearPedido(c);
                    break; // Datos válidos, salir del loop
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
            }
        }

        // Verificar estado
        if (service.getPedido().getEstado() == EstadoPedido.CONFIRMADO) {
            JOptionPane.showMessageDialog(frame, "Pedido ya confirmado. No puede agregar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Agregar item y refrescar panel de pedidos
        try {
            service.agregarItem(producto, qty);
            refreshOrdersPanel();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error al agregar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshOrdersPanel() {
        if (ordersPanel == null) {
            // Si ordersPanel no está inicializado, no hacer nada
            return;
        }
        
        ordersPanel.removeAll();
        JLabel title = new JLabel("Pedido (tickets)");
        title.setForeground(TEXT_WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        ordersPanel.add(title);
        ordersPanel.add(Box.createVerticalStrut(8));

        if (service.getPedido() == null || service.getPedido().getItems().isEmpty()) {
            JLabel empty = new JLabel("No hay ítems en el pedido. Agrega productos desde la izquierda.");
            empty.setForeground(TEXT_SECOND);
            empty.setBorder(new EmptyBorder(8,8,8,8));
            ordersPanel.add(empty);
        } else {
            // Obtener fecha y hora actual
            String fechaHora = LocalDateTime.now().format(DATE_FORMATTER);
            
            // Cabecera cliente con fecha/hora
            Cliente cli = service.getPedido().getCliente();
            JLabel cLabel = new JLabel(String.format("<html><b>%s</b> — %s<br/><small>Fecha: %s</small></html>", 
                cli.getNombre(), cli.getTelefono(), fechaHora));
            cLabel.setForeground(TEXT_WHITE);
            ordersPanel.add(cLabel);
            ordersPanel.add(Box.createVerticalStrut(8));

            // Cada item como pequeño ticket
            for (ItemPedido it : service.getPedido().getItems()) {
                JPanel ticket = new RoundedPanel(10, new Color(20,20,20));
                ticket.setLayout(new BorderLayout());
                ticket.setBorder(new EmptyBorder(8,8,8,8));
                ticket.setMaximumSize(new Dimension(1000, 80));

                String left = String.format("<html><b>%s</b><br/><span style='color:gray'>x%d</span></html>", it.getProducto().getNombre(), it.getCantidad());
                JLabel l = new JLabel(left);
                l.setForeground(TEXT_WHITE);
                ticket.add(l, BorderLayout.WEST);

                String precioTipo = (it.getProducto().hasVolumen() && it.getCantidad() >= it.getProducto().getUmbralVolumen()) ? 
                    "<span style='color:#4CAF50; font-size:10px;'>precio volumen</span>" : 
                    "<span style='color:#FF9800; font-size:10px;'>precio unitario</span>";
                
                String right = String.format("<html><div style='text-align:right; color:lightgray;'>%s<br/><b style='font-size:14px; color:white;'>$%,d</b></div></html>",
                        precioTipo,
                        it.getSubtotal()).replace(',', '.');
                JLabel r = new JLabel(right);
                ticket.add(r, BorderLayout.EAST);

                ordersPanel.add(ticket);
                ordersPanel.add(Box.createVerticalStrut(8));
            }

            // Totales y botones
            JPanel totals = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
            totals.setOpaque(false);
            int bruto = service.getPedido().calcularTotalBruto();
            int montoDesc = service.getPedido().calcularMontoDescuento();
            int finalTotal = service.getPedido().calcularTotalFinal();
            
            String descInfo = "";
            if (montoDesc > 0) {
                double porcentajeDesc = service.getPedido().calcularPorcentajeDescuento() * 100;
                descInfo = String.format("<html><div style='text-align:right; color:white;'>"
                        + "Subtotal: <b>$%,d</b><br/>"
                        + "Descuento (%d%%): <b style='color:#4CAF50;'>-$%,d</b><br/>"
                        + "<hr style='border-color:#444; margin:4px 0;'/>"
                        + "<b style='font-size:16px;'>Total: $%,d</b></div></html>", 
                        bruto, (int)porcentajeDesc, montoDesc, finalTotal).replace(',', '.');
            } else {
                descInfo = String.format("<html><div style='text-align:right; color:white;'>"
                        + "Subtotal: <b>$%,d</b><br/>"
                        + "Descuento: <b>$0</b><br/>"
                        + "<hr style='border-color:#444; margin:4px 0;'/>"
                        + "<b style='font-size:16px;'>Total: $%,d</b></div></html>", 
                        bruto, finalTotal).replace(',', '.');
            }
            
            JLabel tot = new JLabel(descInfo);
            tot.setForeground(TEXT_WHITE);
            totals.add(tot);

            JButton btnConfirm = new JButton("Confirmar");
            btnConfirm.setBackground(ACCENT_PURPLE);
            btnConfirm.setForeground(Color.WHITE);
            btnConfirm.addActionListener(this::onConfirmPedido);
            totals.add(btnConfirm);

            JButton btnResumen = new JButton("Resumen");
            btnResumen.setBackground(ACCENT_BLUE);
            btnResumen.setForeground(Color.WHITE);
            btnResumen.addActionListener(this::onMostrarResumen);
            totals.add(btnResumen);

            ordersPanel.add(totals);
        }

        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    private void onConfirmPedido(ActionEvent e) {
        try {
            service.confirmarPedido();
            JOptionPane.showMessageDialog(frame, "Pedido confirmado. Ya no puedes editarlo.", "OK", JOptionPane.INFORMATION_MESSAGE);
            refreshOrdersPanel();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onMostrarResumen(ActionEvent e) {
        try {
            String resumen = service.obtenerResumen();
            
            JTextArea ta = new JTextArea(resumen);
            ta.setEditable(false);
            ta.setBackground(new Color(30,30,30));
            ta.setForeground(Color.WHITE);
            ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            ta.setBorder(new EmptyBorder(8,8,8,8));
            JOptionPane.showMessageDialog(frame, new JScrollPane(ta), "Resumen del pedido", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ----------------- Helpers UI -------------------

    /**
     * Panel con fondo redondeado.
     */
    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg;

        RoundedPanel(int radius, Color bg) {
            super();
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}