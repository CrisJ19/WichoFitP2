package app;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class ImagenUtil {

    // Cache simple para imágenes ya descargadas
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();
    
    // URLs alternativas genéricas por categoría
    private static final Map<String, String> CATEGORY_ALTERNATIVES = new HashMap<>();
    static {
        CATEGORY_ALTERNATIVES.put("ropa", "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=300&fit=crop");
        CATEGORY_ALTERNATIVES.put("suplemento", "https://images.unsplash.com/photo-1599161144150-7c6ddefc2bf9?w=400&h=300&fit=crop");
        CATEGORY_ALTERNATIVES.put("accesorio", "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop");
        CATEGORY_ALTERNATIVES.put("equipo", "https://images.unsplash.com/photo-1598974357801-cbca100e5d10?w=400&h=300&fit=crop");
        CATEGORY_ALTERNATIVES.put("default", "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400&h=300&fit=crop");
    }

    /**
     * Carga una imagen desde una URL, la redimensiona y devuelve un ImageIcon.
     * Versión mejorada con múltiples estrategias de fallback.
     */
    public static ImageIcon cargar(String urlString, int width, int height) {
        if (urlString == null || urlString.trim().isEmpty()) {
            return generarPlaceholder(width, height);
        }
        
        // Verificar cache primero
        String cacheKey = urlString + "_" + width + "x" + height;
        if (imageCache.containsKey(cacheKey)) {
            return imageCache.get(cacheKey);
        }
        
        ImageIcon result = null;
        
        // Estrategia 1: Intentar con ImageIO normal
        result = cargarConImageIO(urlString, width, height);
        
        // Estrategia 2: Si falla, intentar con conexión mejorada
        if (result == null || result.getIconWidth() <= 0) {
            result = cargarConConexionMejorada(urlString, width, height);
        }
        
        // Estrategia 3: Si aún falla, usar URL alternativa basada en categoría
        if (result == null || result.getIconWidth() <= 0) {
            String categoria = determinarCategoria(urlString);
            String urlAlternativa = CATEGORY_ALTERNATIVES.getOrDefault(categoria, CATEGORY_ALTERNATIVES.get("default"));
            result = cargarConImageIO(urlAlternativa, width, height);
        }
        
        // Estrategia 4: Si todo falla, usar placeholder
        if (result == null || result.getIconWidth() <= 0) {
            result = generarPlaceholder(width, height);
        }
        
        // Almacenar en cache
        imageCache.put(cacheKey, result);
        return result;
    }
    
    private static String determinarCategoria(String url) {
        url = url.toLowerCase();
        if (url.contains("polera") || url.contains("camiseta") || url.contains("sudadera") || 
            url.contains("gorra") || url.contains("pantalon")) {
            return "ropa";
        } else if (url.contains("creatina") || url.contains("proteina") || url.contains("whey") || 
                  url.contains("glutamina") || url.contains("bcaa") || url.contains("pre-entreno") || 
                  url.contains("vitamina")) {
            return "suplemento";
        } else if (url.contains("botella") || url.contains("gorra")) {
            return "accesorio";
        } else if (url.contains("banda") || url.contains("resistencia")) {
            return "equipo";
        }
        return "default";
    }
    
    private static ImageIcon cargarConImageIO(String urlString, int width, int height) {
        try {
            URL url = new URL(urlString);
            BufferedImage img = ImageIO.read(url);
            if (img != null) {
                Image scaled = getScaledImage(img, width, height);
                return new ImageIcon(scaled);
            }
        } catch (Exception e) {
            // Silenciar, intentaremos otra estrategia
        }
        return null;
    }
    
    private static ImageIcon cargarConConexionMejorada(String urlString, int width, int height) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            connection.setRequestProperty("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream is = connection.getInputStream();
                     ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }
                    
                    byte[] imageData = baos.toByteArray();
                    
                    // Intentar leer como BufferedImage
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(imageData)) {
                        BufferedImage img = ImageIO.read(bais);
                        if (img != null) {
                            Image scaled = getScaledImage(img, width, height);
                            return new ImageIcon(scaled);
                        }
                    }
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            // Silenciar
        }
        return null;
    }

    /**
     * Redimensiona la imagen manteniendo el aspect ratio
     */
    private static Image getScaledImage(BufferedImage img, int targetWidth, int targetHeight) {
        int originalWidth = img.getWidth();
        int originalHeight = img.getHeight();
        
        // Calcular dimensiones manteniendo aspect ratio
        double widthRatio = (double) targetWidth / originalWidth;
        double heightRatio = (double) targetHeight / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);
        
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);
        
        // Crear imagen con fondo negro para mantener tamaño consistente
        BufferedImage result = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = result.createGraphics();
        
        // Configurar calidad de renderizado
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fondo oscuro
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRect(0, 0, targetWidth, targetHeight);
        
        // Centrar la imagen escalada
        int x = (targetWidth - newWidth) / 2;
        int y = (targetHeight - newHeight) / 2;
        
        Image scaled = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        g2d.drawImage(scaled, x, y, null);
        g2d.dispose();
        
        return result;
    }

    /**
     * Carga una imagen desde un archivo local
     */
    public static ImageIcon cargarDesdeArchivo(String filePath, int width, int height) {
        try {
            if (filePath == null || filePath.isEmpty())
                return generarPlaceholder(width, height);

            File file = new File(filePath);
            if (!file.exists() || !file.canRead()) {
                return generarPlaceholder(width, height);
            }

            BufferedImage img = ImageIO.read(file);
            if (img == null) {
                return generarPlaceholder(width, height);
            }

            Image scaled = getScaledImage(img, width, height);
            return new ImageIcon(scaled);

        } catch (Exception e) {
            return generarPlaceholder(width, height);
        }
    }

    /**
     * Carga una imagen desde un recurso en el classpath
     */
    public static ImageIcon cargarDesdeRecurso(String resourcePath, int width, int height) {
        try {
            if (resourcePath == null || resourcePath.isEmpty())
                return generarPlaceholder(width, height);

            URL resourceUrl = ImagenUtil.class.getClassLoader().getResource(resourcePath);
            if (resourceUrl == null) {
                return generarPlaceholder(width, height);
            }

            BufferedImage img = ImageIO.read(resourceUrl);
            if (img == null) {
                return generarPlaceholder(width, height);
            }

            Image scaled = getScaledImage(img, width, height);
            return new ImageIcon(scaled);

        } catch (Exception e) {
            return generarPlaceholder(width, height);
        }
    }

    /**
     * Genera una imagen de placeholder atractiva
     */
    private static ImageIcon generarPlaceholder(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        
        // Configurar calidad
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Fondo con gradiente oscuro
        GradientPaint gradient = new GradientPaint(0, 0, new Color(40, 40, 40), w, h, new Color(25, 25, 25));
        g.setPaint(gradient);
        g.fillRect(0, 0, w, h);
        
        // Borde sutil
        g.setColor(new Color(60, 60, 60));
        g.setStroke(new BasicStroke(1));
        g.drawRect(1, 1, w - 2, h - 2);
        
        // Icono
        g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        String icon = "🏋️";
        FontMetrics fm = g.getFontMetrics();
        int iconWidth = fm.stringWidth(icon);
        int iconHeight = fm.getHeight();
        int iconX = (w - iconWidth) / 2;
        int iconY = (h - iconHeight) / 2 + fm.getAscent() - 10;
        g.setColor(new Color(200, 200, 200));
        g.drawString(icon, iconX, iconY);
        
        // Texto
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        String text = "Producto WichoFit";
        fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int textX = (w - textWidth) / 2;
        int textY = iconY + 20;
        g.setColor(new Color(180, 180, 180));
        g.drawString(text, textX, textY);
        
        g.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Limpiar cache (opcional, para liberar memoria si es necesario)
     */
    public static void limpiarCache() {
        imageCache.clear();
    }
}