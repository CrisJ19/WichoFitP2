package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Mi Aplicación");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // ======= PANEL SUPERIOR =======
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);

        JLabel logo = new JLabel();

        // ⬇️ ESTA ES LA LÍNEA QUE ME PEDISTE DONDE VA
        logo.setIcon(cargarRecurso("/assets/logo.png", 160, 80));

        topPanel.add(logo);

        panel.add(topPanel, BorderLayout.NORTH);

        // ======= PANEL CENTRAL =======
        JTextArea areaTexto = new JTextArea("Aquí va tu GUI...");
        panel.add(areaTexto, BorderLayout.CENTER);

        add(panel);
    }

    /**
     * Cargar una imagen desde /assets dentro de src
     */
    private ImageIcon cargarRecurso(String ruta, int width, int height) {
        java.net.URL imgUrl = getClass().getResource(ruta);
        if (imgUrl == null) {
            System.out.println("⚠ No se encontró la imagen: " + ruta);
            return null;
        }

        Image img = new ImageIcon(imgUrl).getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
