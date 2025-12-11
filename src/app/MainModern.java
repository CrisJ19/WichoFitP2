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
    // --- CONSTANTES PARA LA INTERFAZ ---
    // Colores del tema oscuro moderno con gradiente
    private static final Color BG_TOP = new Color(20, 20, 30);
    private static final Color BG_BOTTOM = new Color(10, 10, 15);
    private static final Color SIDEBAR = new Color(26, 26, 36);
    private static final Color ACCENT_BLUE = new Color(0x00, 0x7A, 0xFF);
    private static final Color ACCENT_PURPLE = new Color(0x8A, 0x2B, 0xE2);
    private static final Color CARD = new Color(35, 35, 45);
    private static final Color CARD_HOVER = new Color(45, 45, 55);
    private static final Color TEXT_SECOND = new Color(200, 200, 200);
    private static final Color TEXT_WHITE = new Color(255, 255, 255);
    private static final Color TEXT_GOLD = new Color(255, 215, 0);
    
    // Fuente que soporta emojis - versión corregida
    private static final String FONT_NAME = "Segoe UI Emoji";
    private static final String FALLBACK_FONT = "SansSerif";
    
    // --- VALIDACIONES CON EXPRESIONES REGULARES ---
    private static final Pattern NOMBRE_PATTERN = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$");
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("^\\d{10}$");
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // --- MAPA DE IMÁGENES POR PRODUCTO ---
    private static final Map<Producto, String> PRODUCT_IMAGE_URLS = new HashMap<>();
    static {
        PRODUCT_IMAGE_URLS.put(Producto.POLERA_BASICA, "https://contents.mediadecathlon.com/p2772905/k$5d024cac783cb49e91608c2f2d429fbe/creatina-healthy-sports-100-dosis.jpg");
        PRODUCT_IMAGE_URLS.put(Producto.SUDADERA, "https://underrated.es/cdn/shop/files/SudaderagymPRIMEMODEenUnderrated.esIII.webp?v=1745766197");
        PRODUCT_IMAGE_URLS.put(Producto.CREATINA_300G, "https://nutritienda.co/1009-medium_default/creatina-monohidratada-micronizada-x300-gramos-de-nutrex.jpg");
        PRODUCT_IMAGE_URLS.put(Producto.PRE_ENTRENO_200G, "https://dragonpharmalabs.com/cdn/shop/files/600x600-venom-3_300x300.png?v=1745857750");
        PRODUCT_IMAGE_URLS.put(Producto.GORRA_DEPORTIVA, "https://contents.mediadecathlon.com/p2797328/k$df1bd9a8b83c195d462b21fa76b66a3e/gorra-deportiva-artengo-tc-500-t58-azul-blanco.jpg");
        PRODUCT_IMAGE_URLS.put(Producto.PANTALON_DEPORTIVO, "https://d22fxaf9t8d39k.cloudfront.net/e2e1bdbb5882815dd5152d3e19c5ddb62ad022cc9ac3d1b251afe9b0531f245861391.png");
        PRODUCT_IMAGE_URLS.put(Producto.BANDAS_RESISTENCIA, "https://efisicas.com/wp-content/webpc-passthru.php?src=https://efisicas.com/wp-content/uploads/bandas-de-resistencia-2-2.jpg&nocache=1");
        PRODUCT_IMAGE_URLS.put(Producto.BOTELLA_AGUA, "https://img.asmedia.epimg.net/resizer/v2/ELSVCLXVWBAWJCAZKW4NYDDUFU.png?auth=7005e13038d373cff21b074171cf4e19a11b8cbfa96ff68241e87c0b1b250702&width=1472&height=1104&focal=630%2C299");
        PRODUCT_IMAGE_URLS.put(Producto.PROTEINA_WHEY, "https://cloudinary.images-iherb.com/image/upload/f_auto,q_auto:eco/images/nrx/nrx02905/y/33.jpg");
        PRODUCT_IMAGE_URLS.put(Producto.GLUTAMINA_200G, "https://www.sorianatural.com/storage/img/F0000013374_mg_glutamina.jpg");
        PRODUCT_IMAGE_URLS.put(Producto.BCAAS_300G, "https://www.bike-discount.de/media/image/07/30/52/BCAA-LM_800x800@2x.jpg");
        PRODUCT_IMAGE_URLS.put(Producto.VITAMINAS_COMPLEJO_B, "https://farmaciacoyoacan.com/cdn/shop/files/7503008344730_1.jpg?v=1723702575");
    }

    private final WichoFitService service = new WichoFitService();
    private JFrame frame;
    private JPanel productsPanel;
    private JPanel ordersPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private boolean productosPanelCreado = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainModern().createAndShowGUI());
    }

    // MÉTODO getFont CORREGIDO - sin NullPointerException
    private Font getFont(int style, int size) {
        // 1. Intentar con la fuente preferida que soporta emojis
        Font font = new Font(FONT_NAME, style, size);
        
        // 2. Verificar SI la fuente se creó correctamente Y puede mostrar caracteres básicos
        if (font != null && font.canDisplay('A')) {
            return font;
        }
        
        // 3. Si falla, usar una fuente genérica segura que SÍ existe
        return new Font(FALLBACK_FONT, style, size);
    }

    private void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        frame = new JFrame("WichoFit — Tienda Fitness");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 800);
        frame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerSize(3);
        split.setDividerLocation(250);
        split.setBorder(null);
        split.setBackground(BG_TOP);

        split.setLeftComponent(createSidebar());
        split.setRightComponent(createMainContent());

        mainPanel.add(split, BorderLayout.CENTER);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel left = new JPanel();
        left.setBackground(SIDEBAR);
        left.setLayout(new BorderLayout());
        left.setBorder(new EmptyBorder(20, 15, 20, 15));

        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        
        JLabel logoText = new JLabel("WICHO FIT");
        logoText.setFont(getFont(Font.BOLD, 28));
        logoText.setForeground(TEXT_GOLD);
        logoText.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitle = new JLabel("TIENDA FITNESS");
        subtitle.setFont(getFont(Font.PLAIN, 12));
        subtitle.setForeground(TEXT_SECOND);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        logoPanel.add(logoText, BorderLayout.CENTER);
        logoPanel.add(subtitle, BorderLayout.SOUTH);
        
        left.add(logoPanel, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(40, 0, 0, 0));

        JButton btnInicio = makeSidebarButton("🏠 Inicio");
        btnInicio.addActionListener(e -> mostrarInicio());
        
        JButton btnProductos = makeSidebarButton("🛍️ Productos");
        btnProductos.addActionListener(e -> mostrarProductos());
        
        JButton btnNuevoPedido = makeSidebarButton("➕ Nuevo Pedido");
        btnNuevoPedido.addActionListener(e -> {
            if (service.getPedido() == null) {
                JOptionPane.showMessageDialog(frame, 
                    "Primero navega a 'Productos' y agrega artículos a tu pedido.", 
                    "Nuevo pedido", JOptionPane.INFORMATION_MESSAGE);
                mostrarProductos();
            } else {
                mostrarProductos();
            }
        });
        
        JButton btnPedidosRealizados = makeSidebarButton("📦 Historial");
        btnPedidosRealizados.addActionListener(e -> mostrarPedidosRealizados());

        menu.add(btnInicio);
        menu.add(Box.createVerticalStrut(12));
        menu.add(btnProductos);
        menu.add(Box.createVerticalStrut(12));
        menu.add(btnNuevoPedido);
        menu.add(Box.createVerticalStrut(12));
        menu.add(btnPedidosRealizados);
        menu.add(Box.createVerticalGlue());

        left.add(menu, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel footerText = new JLabel("v2.0 • © 2024");
        footerText.setFont(getFont(Font.PLAIN, 11));
        footerText.setForeground(new Color(120, 120, 140));
        footerText.setHorizontalAlignment(SwingConstants.CENTER);
        
        footer.add(footerText, BorderLayout.CENTER);
        left.add(footer, BorderLayout.SOUTH);

        return left;
    }

    private JButton makeSidebarButton(String text) {
        JButton b = new JButton(text);
        b.setFont(getFont(Font.PLAIN, 14));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setFocusPainted(false);
        b.setBackground(SIDEBAR);
        b.setForeground(TEXT_SECOND);
        b.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(new Color(40, 40, 50));
                b.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(SIDEBAR);
                b.setForeground(TEXT_SECOND);
            }
        });
        
        return b;
    }

    private JPanel createMainContent() {
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setOpaque(false);
        
        mainContentPanel.add(createInicioPanel(), "inicio");
        mainContentPanel.add(createPedidosRealizadosPanel(), "pedidos");
        mainContentPanel.add(new JPanel(), "productos");
        
        cardLayout.show(mainContentPanel, "inicio");
        
        return mainContentPanel;
    }

    private JPanel createInicioPanel() {
        JPanel inicioPanel = new JPanel(new GridBagLayout());
        inicioPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(50, 50, 50, 50));
        content.setMaximumSize(new Dimension(800, 600));

        JLabel titulo = new JLabel("💪 ¡Bienvenido a WichoFit!");
        titulo.setFont(getFont(Font.BOLD, 32));
        titulo.setForeground(TEXT_WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titulo);
        content.add(Box.createVerticalStrut(30));
        
        String[] features = {
            "🏋️‍♂️ Productos de calidad fitness",
            "👕 Ropa deportiva premium", 
            "💊 Suplementos certificados",
            "🚚 Envíos rápidos",
            "💯 Satisfacción garantizada"
        };
        
        for (String feature : features) {
            JPanel featureCard = new RoundedPanel(15, new Color(40, 40, 50, 180));
            featureCard.setLayout(new BorderLayout());
            featureCard.setBorder(new EmptyBorder(15, 20, 15, 20));
            featureCard.setMaximumSize(new Dimension(400, 60));
            
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(getFont(Font.PLAIN, 16));
            featureLabel.setForeground(TEXT_WHITE);
            featureCard.add(featureLabel, BorderLayout.CENTER);
            
            content.add(featureCard);
            content.add(Box.createVerticalStrut(10));
        }
        
        content.add(Box.createVerticalStrut(40));
        
        // BOTÓN CORREGIDO: texto negro
        JButton empezarBtn = new JButton("🛍️ Comenzar a Comprar");
        empezarBtn.setFont(getFont(Font.BOLD, 16));
        empezarBtn.setBackground(ACCENT_PURPLE);
        empezarBtn.setForeground(Color.BLACK);  // Texto en negro
        empezarBtn.setFocusPainted(false);
        empezarBtn.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        empezarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        empezarBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        empezarBtn.addActionListener(e -> mostrarProductos());
        
        content.add(empezarBtn);
        
        inicioPanel.add(content);
        return inicioPanel;
    }

    private JPanel createProductosPanel() {
        if (productosPanelCreado) {
            return null;
        }
        
        productosPanelCreado = true;
        
        JPanel productosPanelContainer = new JPanel(new BorderLayout());
        productosPanelContainer.setOpaque(false);
        
        productsPanel = new JPanel();
        productsPanel.setOpaque(false);
        productsPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 20, 20));
        productsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        for (Producto p : Producto.values()) {
            String url = PRODUCT_IMAGE_URLS.getOrDefault(p, "");
            productsPanel.add(makeProductCard(p, url));
        }

        JScrollPane productsScroll = new JScrollPane(productsPanel);
        productsScroll.setBorder(null);
        productsScroll.setOpaque(false);
        productsScroll.getViewport().setOpaque(false);
        productsScroll.getVerticalScrollBar().setUnitIncrement(16);
        productsScroll.getVerticalScrollBar().setBackground(CARD);
        productsScroll.getVerticalScrollBar().setForeground(ACCENT_BLUE);

        ordersPanel = new JPanel();
        ordersPanel.setOpaque(false);
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel ordersTitlePanel = new RoundedPanel(10, new Color(40, 40, 50, 180));
        ordersTitlePanel.setLayout(new BorderLayout());
        ordersTitlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        ordersTitlePanel.setMaximumSize(new Dimension(380, 60));
        
        JLabel ordersTitle = new JLabel("📋 Tu Pedido");
        ordersTitle.setFont(getFont(Font.BOLD, 18));
        ordersTitle.setForeground(TEXT_WHITE);
        ordersTitlePanel.add(ordersTitle, BorderLayout.CENTER);
        
        ordersPanel.add(ordersTitlePanel);
        ordersPanel.add(Box.createVerticalStrut(15));

        JScrollPane ordersScroll = new JScrollPane(ordersPanel);
        ordersScroll.setPreferredSize(new Dimension(400, 700));
        ordersScroll.setBorder(null);
        ordersScroll.setOpaque(false);
        ordersScroll.getViewport().setOpaque(false);
        ordersScroll.getVerticalScrollBar().setUnitIncrement(16);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, productsScroll, ordersScroll);
        rightSplit.setDividerLocation(800);
        rightSplit.setDividerSize(3);
        rightSplit.setBorder(null);
        rightSplit.setBackground(BG_TOP);

        productosPanelContainer.add(rightSplit, BorderLayout.CENTER);
        
        return productosPanelContainer;
    }

    private JPanel createPedidosRealizadosPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(50, 50, 50, 50));
        content.setMaximumSize(new Dimension(600, 400));

        JLabel titulo = new JLabel("📦 Historial de Pedidos");
        titulo.setFont(getFont(Font.BOLD, 28));
        titulo.setForeground(TEXT_WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titulo);
        content.add(Box.createVerticalStrut(30));

        JLabel info = new JLabel("<html><div style='text-align: center; color: #cccccc; line-height: 1.6;'>"
                + "Próximamente podrás ver tu historial completo de pedidos.<br><br>"
                + "⏳ En desarrollo: seguimiento en tiempo real<br>"
                + "📄 Facturas digitales<br>"
                + "⭐ Sistema de valoraciones</div></html>");
        info.setFont(getFont(Font.PLAIN, 14));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(info);
        
        panel.add(content);
        return panel;
    }

    private JPanel makeProductCard(Producto producto, String imageUrl) {
        JPanel card = new RoundedPanel(15, CARD);
        card.setPreferredSize(new Dimension(220, 320));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(CARD_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(CARD);
            }
        });

        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setOpaque(false);
        imageContainer.setPreferredSize(new Dimension(190, 160));
        
        JLabel img = new JLabel();
        img.setHorizontalAlignment(SwingConstants.CENTER);
        
        try {
            ImageIcon originalIcon = new ImageIcon(new java.net.URL(imageUrl));
            Image scaledImage = originalIcon.getImage().getScaledInstance(190, 160, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            img.setText("🏋️‍♂️");
            img.setFont(getFont(Font.PLAIN, 48));
            img.setForeground(TEXT_SECOND);
        }
        
        imageContainer.add(img, BorderLayout.CENTER);
        card.add(imageContainer, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JLabel name = new JLabel(producto.getNombre());
        name.setFont(getFont(Font.BOLD, 14));
        name.setForeground(TEXT_WHITE);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.add(name);
        info.add(Box.createVerticalStrut(8));
        
        String precioTexto;
        if (producto.hasVolumen()) {
            precioTexto = String.format("$%,d  •  $%,d vol", producto.getPrecioNormal(), producto.getPrecioVolumen()).replace(',', '.');
        } else {
            precioTexto = String.format("$%,d", producto.getPrecioNormal()).replace(',', '.');
        }
        
        JLabel price = new JLabel(precioTexto);
        price.setFont(getFont(Font.PLAIN, 13));
        price.setForeground(TEXT_SECOND);
        price.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.add(price);
        
        if (producto.hasVolumen() && producto.getUmbralVolumen() > 0) {
            JLabel volInfo = new JLabel("📦 " + producto.getUmbralVolumen() + "+ unidades");
            volInfo.setFont(getFont(Font.PLAIN, 11));
            volInfo.setForeground(new Color(100, 200, 255));
            volInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
            info.add(Box.createVerticalStrut(4));
            info.add(volInfo);
        }
        
        card.add(info, BorderLayout.CENTER);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        foot.setOpaque(false);

        // BOTÓN CORREGIDO: texto negro
        JButton add = new JButton("➕ Agregar");
        add.setFont(getFont(Font.BOLD, 12));
        add.setBackground(ACCENT_BLUE);
        add.setForeground(Color.BLACK);  // Texto en negro
        add.setFocusPainted(false);
        add.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add.addActionListener(e -> onAgregarProducto(producto));
        
        add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                add.setBackground(new Color(0x00, 0x9A, 0xFF));
                add.setForeground(Color.BLACK);  // Mantener negro en hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                add.setBackground(ACCENT_BLUE);
                add.setForeground(Color.BLACK);  // Mantener negro al salir
            }
        });

        foot.add(add);
        card.add(foot, BorderLayout.SOUTH);

        return card;
    }

    private void onAgregarProducto(Producto producto) {
        mostrarProductos();
        
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setOpaque(false);
        
        JLabel label = new JLabel("<html><b style='color:white;'>Cantidad para:</b><br>" + producto.getNombre() + "</html>");
        label.setFont(getFont(Font.PLAIN, 13));
        inputPanel.add(label, BorderLayout.NORTH);
        
        JTextField quantityField = new JTextField("1", 10);
        quantityField.setFont(getFont(Font.PLAIN, 14));
        quantityField.setHorizontalAlignment(JTextField.CENTER);
        quantityField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 120), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        inputPanel.add(quantityField, BorderLayout.CENTER);
        
        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Agregar Producto", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result != JOptionPane.OK_OPTION) return;
        
        int qty;
        try {
            qty = Integer.parseInt(quantityField.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "⚠️ Ingrese una cantidad válida (mayor que 0).", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (service.getPedido() == null) {
            pedirDatosCliente();
            if (service.getPedido() == null) return;
        }

        if (service.getPedido().getEstado() == EstadoPedido.CONFIRMADO) {
            JOptionPane.showMessageDialog(frame, "❌ Pedido ya confirmado. No se pueden agregar más productos.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            service.agregarItem(producto, qty);
            refreshOrdersPanel();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "❌ Error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pedirDatosCliente() {
        while (true) {
            JPanel input = new JPanel(new GridBagLayout());
            input.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            gbc.gridx = 0; gbc.gridy = 0;
            input.add(new JLabel("👤 Nombre completo:"), gbc);
            
            gbc.gridx = 1; gbc.gridy = 0;
            gbc.gridwidth = 2;
            JTextField nombre = new JTextField(20);
            nombre.setFont(getFont(Font.PLAIN, 13));
            input.add(nombre, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            gbc.gridwidth = 1;
            input.add(new JLabel("📱 Teléfono (10 dígitos):"), gbc);
            
            gbc.gridx = 1; gbc.gridy = 1;
            gbc.gridwidth = 2;
            JTextField telefono = new JTextField(20);
            telefono.setFont(getFont(Font.PLAIN, 13));
            input.add(telefono, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            gbc.gridwidth = 3;
            JLabel hint = new JLabel("<html><small style='color:gray'>* Solo letras y espacios para el nombre</small></html>");
            hint.setFont(getFont(Font.PLAIN, 11));
            input.add(hint, gbc);

            int res = JOptionPane.showConfirmDialog(frame, input, "👥 Datos del Cliente", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return;
            
            String nombreStr = nombre.getText().trim();
            String telefonoStr = telefono.getText().trim();
            
            if (!NOMBRE_PATTERN.matcher(nombreStr).matches()) {
                JOptionPane.showMessageDialog(frame, 
                    "❌ Nombre inválido. Solo se permiten letras y espacios.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            
            if (!TELEFONO_PATTERN.matcher(telefonoStr).matches()) {
                JOptionPane.showMessageDialog(frame, 
                    "❌ Teléfono inválido. Debe tener exactamente 10 dígitos.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            
            try {
                Cliente c = new Cliente(nombreStr, telefonoStr);
                service.crearPedido(c);
                break;
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, "❌ Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
        }
    }

    private void refreshOrdersPanel() {
        if (ordersPanel == null) return;
        
        ordersPanel.removeAll();
        
        JPanel ordersTitlePanel = new RoundedPanel(10, new Color(40, 40, 50, 180));
        ordersTitlePanel.setLayout(new BorderLayout());
        ordersTitlePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        ordersTitlePanel.setMaximumSize(new Dimension(380, 60));
        
        JLabel ordersTitle = new JLabel("📋 Tu Pedido");
        ordersTitle.setFont(getFont(Font.BOLD, 18));
        ordersTitle.setForeground(TEXT_WHITE);
        ordersTitlePanel.add(ordersTitle, BorderLayout.CENTER);
        
        ordersPanel.add(ordersTitlePanel);
        ordersPanel.add(Box.createVerticalStrut(15));

        if (service.getPedido() == null || service.getPedido().getItems().isEmpty()) {
            JPanel emptyPanel = new RoundedPanel(15, new Color(40, 40, 50, 100));
            emptyPanel.setLayout(new BorderLayout());
            emptyPanel.setBorder(new EmptyBorder(30, 20, 30, 20));
            emptyPanel.setMaximumSize(new Dimension(380, 120));
            
            JLabel emptyLabel = new JLabel("<html><div style='text-align:center; color:#aaaaaa;'>"
                    + "🛒 Carrito vacío<br><br>"
                    + "<small>Agrega productos desde el catálogo</small></div></html>");
            emptyLabel.setFont(getFont(Font.PLAIN, 14));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            
            ordersPanel.add(emptyPanel);
        } else {
            String fechaHora = LocalDateTime.now().format(DATE_FORMATTER);
            
            JPanel clientPanel = new RoundedPanel(10, new Color(50, 50, 60, 180));
            clientPanel.setLayout(new BorderLayout());
            clientPanel.setBorder(new EmptyBorder(12, 15, 12, 15));
            clientPanel.setMaximumSize(new Dimension(380, 70));
            
            Cliente cli = service.getPedido().getCliente();
            JLabel cLabel = new JLabel(String.format("<html><b style='color:white;'>👤 %s</b><br>"
                    + "<small style='color:#cccccc;'>📱 %s</small><br>"
                    + "<small style='color:#999999;'>📅 %s</small></html>", 
                cli.getNombre(), cli.getTelefono(), fechaHora));
            cLabel.setFont(getFont(Font.PLAIN, 12));
            clientPanel.add(cLabel, BorderLayout.CENTER);
            
            ordersPanel.add(clientPanel);
            ordersPanel.add(Box.createVerticalStrut(10));

            for (ItemPedido it : service.getPedido().getItems()) {
                JPanel ticket = new RoundedPanel(8, new Color(45, 45, 55, 180));
                ticket.setLayout(new BorderLayout());
                ticket.setBorder(new EmptyBorder(12, 15, 12, 15));
                ticket.setMaximumSize(new Dimension(380, 80));

                String left = String.format("<html><b style='color:white;'>%s</b><br>"
                        + "<span style='color:#cccccc; font-size:11px;'>Cantidad: %d</span></html>", 
                    it.getProducto().getNombre(), it.getCantidad());
                JLabel l = new JLabel(left);
                l.setFont(getFont(Font.PLAIN, 12));
                ticket.add(l, BorderLayout.WEST);

                String precioTipo = (it.getProducto().hasVolumen() && it.getCantidad() >= it.getProducto().getUmbralVolumen()) ? 
                    "<span style='color:#4CAF50; font-size:10px;'>precio volumen</span>" : 
                    "<span style='color:#FF9800; font-size:10px;'>precio regular</span>";
                
                String right = String.format("<html><div style='text-align:right;'>"
                        + "<span style='color:#cccccc; font-size:10px;'>%s</span><br>"
                        + "<b style='font-size:14px; color:white;'>$%,d</b></div></html>",
                    precioTipo, it.getSubtotal()).replace(',', '.');
                JLabel r = new JLabel(right);
                r.setFont(getFont(Font.PLAIN, 12));
                ticket.add(r, BorderLayout.EAST);

                ordersPanel.add(ticket);
                ordersPanel.add(Box.createVerticalStrut(8));
            }

            JPanel totalsPanel = new RoundedPanel(10, new Color(40, 40, 50, 180));
            totalsPanel.setLayout(new BorderLayout());
            totalsPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
            totalsPanel.setMaximumSize(new Dimension(380, 150));

            int bruto = service.getPedido().calcularTotalBruto();
            int montoDesc = service.getPedido().calcularMontoDescuento();
            int finalTotal = service.getPedido().calcularTotalFinal();
            
            String descInfo;
            if (montoDesc > 0) {
                double porcentajeDesc = service.getPedido().calcularPorcentajeDescuento() * 100;
                descInfo = String.format("<html><div style='color:#cccccc; line-height:1.6;'>"
                        + "<div style='display:flex; justify-content:space-between;'>"
                        + "<span>Subtotal:</span>"
                        + "<span><b>$%,d</b></span></div>"
                        + "<div style='display:flex; justify-content:space-between; color:#4CAF50;'>"
                        + "<span>Descuento (%d%%):</span>"
                        + "<span><b>-$%,d</b></span></div>"
                        + "<hr style='border-color:#555; margin:10px 0;'>"
                        + "<div style='display:flex; justify-content:space-between; font-size:16px;'>"
                        + "<span style='color:white;'>TOTAL:</span>"
                        + "<span style='color:#4CAF50; font-weight:bold;'>$%,d</span></div>"
                        + "</div></html>", 
                        bruto, (int)porcentajeDesc, montoDesc, finalTotal).replace(',', '.');
            } else {
                descInfo = String.format("<html><div style='color:#cccccc; line-height:1.6;'>"
                        + "<div style='display:flex; justify-content:space-between;'>"
                        + "<span>Subtotal:</span>"
                        + "<span><b>$%,d</b></span></div>"
                        + "<div style='display:flex; justify-content:space-between;'>"
                        + "<span>Descuento:</span>"
                        + "<span><b>$0</b></span></div>"
                        + "<hr style='border-color:#555; margin:10px 0;'>"
                        + "<div style='display:flex; justify-content:space-between; font-size:16px;'>"
                        + "<span style='color:white;'>TOTAL:</span>"
                        + "<span style='color:white; font-weight:bold;'>$%,d</span></div>"
                        + "</div></html>", 
                        bruto, finalTotal).replace(',', '.');
            }
            
            JLabel tot = new JLabel(descInfo);
            tot.setFont(getFont(Font.PLAIN, 12));
            totalsPanel.add(tot, BorderLayout.CENTER);
            
            ordersPanel.add(totalsPanel);
            ordersPanel.add(Box.createVerticalStrut(15));

            JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            buttonsPanel.setOpaque(false);
            buttonsPanel.setMaximumSize(new Dimension(380, 45));

            // BOTONES CORREGIDOS: texto negro
            JButton btnResumen = new JButton("📄 Resumen");
            btnResumen.setFont(getFont(Font.BOLD, 12));
            btnResumen.setBackground(ACCENT_BLUE);
            btnResumen.setForeground(Color.BLACK);  // Texto en negro
            btnResumen.setFocusPainted(false);
            btnResumen.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            btnResumen.addActionListener(this::onMostrarResumen);
            
            JButton btnConfirm = new JButton("✅ Confirmar");
            btnConfirm.setFont(getFont(Font.BOLD, 12));
            btnConfirm.setBackground(ACCENT_PURPLE);
            btnConfirm.setForeground(Color.BLACK);  // Texto en negro
            btnConfirm.setFocusPainted(false);
            btnConfirm.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
            btnConfirm.addActionListener(this::onConfirmPedido);

            buttonsPanel.add(btnResumen);
            buttonsPanel.add(btnConfirm);
            
            ordersPanel.add(buttonsPanel);
        }

        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    private void onConfirmPedido(ActionEvent e) {
        try {
            service.confirmarPedido();
            JOptionPane.showMessageDialog(frame, 
                "✅ ¡Pedido confirmado con éxito!\n\nTu orden ha sido procesada y está siendo preparada.", 
                "Confirmación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            refreshOrdersPanel();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "❌ Error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onMostrarResumen(ActionEvent e) {
        try {
            String resumen = service.obtenerResumen();
            
            JTextArea ta = new JTextArea(resumen);
            ta.setEditable(false);
            ta.setBackground(new Color(30, 30, 40));
            ta.setForeground(Color.WHITE);
            ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
            ta.setBorder(new EmptyBorder(15, 15, 15, 15));
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
            
            JScrollPane scroll = new JScrollPane(ta);
            scroll.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(frame, scroll, "📋 Resumen Detallado del Pedido", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "❌ Error: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarInicio() {
        if (cardLayout != null && mainContentPanel != null) {
            cardLayout.show(mainContentPanel, "inicio");
        }
    }
    
    private void mostrarProductos() {
        if (cardLayout != null && mainContentPanel != null) {
            if (!productosPanelCreado) {
                JPanel productosPanel = createProductosPanel();
                if (productosPanel != null) {
                    mainContentPanel.add(productosPanel, "productos");
                }
            }
            
            cardLayout.show(mainContentPanel, "productos");
            
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

    // --- CLASE INTERNA: PANEL CON BORDES REDONDEADOS ---
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