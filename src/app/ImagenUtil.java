package app;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class ImagenUtil {

    /**
     * Carga una imagen desde una URL, la redimensiona y devuelve un ImageIcon.
     * Maneja errores devolviendo un placeholder.
     */
    public static ImageIcon cargar(String urlString, int width, int height) {
        try {
            if (urlString == null || urlString.isEmpty())
                return generarPlaceholder(width, height);

            // Intentar cargar desde URL
            BufferedImage img = ImageIO.read(new URL(urlString));
            if (img == null)
                return generarPlaceholder(width, height);

            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);

        } catch (Exception e) {
            System.out.println("Error al cargar imagen desde URL: " + e.getMessage());
            return generarPlaceholder(width, height);
        }
    }

    /**
     * Carga una imagen desde un archivo local.
     * Primero intenta desde la ruta absoluta, luego desde recursos.
     */
    public static ImageIcon cargarDesdeArchivo(String filePath, int width, int height) {
        try {
            if (filePath == null || filePath.isEmpty())
                return generarPlaceholder(width, height);

            File file = new File(filePath);
            
            // Intentar 1: Ruta directa
            if (file.exists()) {
                BufferedImage img = ImageIO.read(file);
                if (img != null) {
                    Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
            }
            
            // Intentar 2: Como recurso del classpath
            URL resourceUrl = ImagenUtil.class.getClassLoader().getResource(filePath);
            if (resourceUrl != null) {
                BufferedImage img = ImageIO.read(resourceUrl);
                if (img != null) {
                    Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
            }
            
            // Intentar 3: Buscar en src/assets/ relativo al directorio de trabajo
            String relativePath = "src/assets/" + new File(filePath).getName();
            File relativeFile = new File(relativePath);
            if (relativeFile.exists()) {
                BufferedImage img = ImageIO.read(relativeFile);
                if (img != null) {
                    Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
            }
            
            return generarPlaceholder(width, height);
            
        } catch (Exception e) {
            System.out.println("Error al cargar imagen desde archivo: " + e.getMessage());
            return generarPlaceholder(width, height);
        }
    }

    /**
     * Genera una imagen de placeholder cuando no se puede cargar la imagen real.
     * Diseño mejorado con logo de WichoFit simulado.
     */
    private static ImageIcon generarPlaceholder(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        
        // Fondo con gradiente oscuro
        GradientPaint gradient = new GradientPaint(0, 0, new Color(40, 40, 40), w, h, new Color(25, 25, 25));
        g.setPaint(gradient);
        g.fillRect(0, 0, w, h);
        
        // Borde sutil
        g.setColor(new Color(60, 60, 60));
        g.setStroke(new BasicStroke(1));
        g.drawRect(1, 1, w - 2, h - 2);
        
        // Logo simulado de WichoFit
        g.setFont(new Font("Arial", Font.BOLD, 18));
        String logoText = "WichoFit";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(logoText);
        int textHeight = fm.getHeight();
        
        // Sombra del texto
        g.setColor(new Color(0, 80, 160));
        g.drawString(logoText, (w - textWidth) / 2 + 2, (h - textHeight) / 2 + fm.getAscent() + 2);
        
        // Texto principal
        g.setColor(new Color(0, 120, 215));
        g.drawString(logoText, (w - textWidth) / 2, (h - textHeight) / 2 + fm.getAscent());
        
        // Subtítulo
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        String subText = "Tienda Fitness";
        fm = g.getFontMetrics();
        textWidth = fm.stringWidth(subText);
        g.setColor(new Color(180, 180, 180));
        g.drawString(subText, (w - textWidth) / 2, (h - textHeight) / 2 + fm.getAscent() + 20);
        
        g.dispose();
        return new ImageIcon(img);
    }
}