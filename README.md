Tienda Fitness "WichoFit"
WichoFit es una tienda especializada en productos fitness que permite a los clientes realizar pedidos de ropa deportiva y suplementos nutricionales. El sistema registra los datos del cliente y permite crear pedidos con productos del catálogo, aplicando precios por volumen y descuentos según reglas de negocio. La aplicación cuenta con una interfaz gráfica moderna que facilita la navegación y gestión de pedidos.

Primer paso: Extraer requerimientos funcionales
RF1. Registrar cliente - Nombre y teléfono (10 dígitos, validado con regex)
RF2. Crear pedido en estado EN_CREACION asociado a un cliente
RF3. Agregar ítems al pedido (producto del catálogo y cantidad)
RF4. Aplicar precio por volumen cuando se alcanza el umbral mínimo:

Productos con volumen: Polera básica, Sudadera, Gorra deportiva, Pantalón deportivo, Bandas de resistencia, Botella de agua

Productos sin volumen: Creatina, Pre-entreno, Proteína Whey, Glutamina, BCAAs, Vitaminas
RF5. Calcular total del pedido:

Subtotal por ítem (precio aplicado × cantidad)

Total bruto = suma de subtotales

Aplicar descuento según reglas:
a. Si hay al menos 1 suplemento (producto sin volumen) Y 4+ prendas (productos con volumen) → 10%
b. Si no aplica (a) y total bruto > $300.000 → 7%
c. Si no aplica (a) ni (b) y total bruto > $150.000 → 5%
d. En cualquier otro caso → 0%
RF6. Confirmar pedido → cambia estado a CONFIRMADO; luego no se puede editar
RF7. Validaciones:

Cantidades > 0

Nombre solo letras y espacios

Teléfono exactamente 10 dígitos

No editar pedidos confirmados
RF8. Generar resumen detallado con:

Información del cliente

Detalle de productos (producto, cantidad, precio unitario aplicado, subtotal)

Total bruto

Descuento (porcentaje y monto)

Total final

RNF1. Usabilidad

Tiempo de respuesta: La interfaz debe responder en menos de 100ms para acciones del usuario

Curva de aprendizaje: Un usuario nuevo debe poder crear un pedido en menos de 2 minutos

Accesibilidad: Los botones deben tener tamaño mínimo de 44x44 píxeles para facilitar clic

Feedback visual: Todas las acciones deben proporcionar feedback inmediato (cambios de color, mensajes)

RNF2. Rendimiento

Carga inicial: La aplicación debe cargarse completamente en menos de 3 segundos

Cálculos: Los cálculos financieros deben completarse en menos de 50ms

Memoria: Uso máximo de memoria RAM: 256MB

Imágenes: Las imágenes deben cargarse de forma asíncrona para no bloquear la interfaz

RNF3. Fiabilidad

Disponibilidad: La aplicación debe funcionar offline una vez cargada (excepto imágenes externas)

Tolerancia a fallos: Errores en carga de imágenes no deben afectar la funcionalidad principal

Consistencia: Los cálculos deben ser determinísticos e idénticos en cada ejecución

Recuperación: Los datos de cliente deben persistir durante la sesión completa

RNF4. Mantenibilidad

Modularidad: Separación clara entre dominio, servicio y presentación

Documentación: Código comentado para métodos complejos (especialmente cálculos de descuento)

Extensibilidad: Diseño que permita añadir nuevos productos sin modificar lógica existente

Pruebas: Estructura que facilite pruebas unitarias (aunque no implementadas en esta entrega)

RNF5. Portabilidad

Sistema operativo: Debe ejecutarse en Windows, macOS y Linux con Java 8+

Resolución: Adaptarse a resoluciones desde 1024x768 hasta 1920x1080

Dependencias: Solo dependencias estándar de Java (no librerías externas)

RNF6. Seguridad

Validación inputs: Prevenir inyección de código en campos de texto

Datos sensibles: No almacenar información de pago ni datos personales persistentes

Consistencia estados: Garantizar que pedidos confirmados no puedan ser modificados

RNF7. Estándares de Diseño

Consistencia visual: Uso de paleta de colores coherente en toda la aplicación

Tipografía: Fuentes legibles con soporte para caracteres especiales y emojis

Espaciado: Uso consistente de márgenes y padding según principios de diseño

Jerarquía visual: Elementos importantes destacados apropiadamente

RNF8. Experiencia de Usuario

Flujo intuitivo: Progresión lógica: registro → selección → revisión → confirmación

Reducción de errores: Validación en tiempo real con sugerencias para corrección

Control usuario: Posibilidad de cancelar operaciones en cualquier momento

Transparencia: Mostrar claramente precios, descuentos y totales en todo momento

Segundo paso: Reglas del negocio
Catálogo de productos:
Polera básica: $40.000 normal / $35.000 volumen (≥5 unidades)

Sudadera: $80.000 normal / $75.000 volumen (≥3 unidades)

Creatina 300g: $120.000 (sin volumen)

Pre-entreno 200g: $90.000 (sin volumen)

Gorra deportiva: $25.000 normal / $22.000 volumen (≥4 unidades)

Pantalón deportivo: $65.000 normal / $58.000 volumen (≥2 unidades)

Bandas de resistencia: $35.000 normal / $30.000 volumen (≥6 unidades)

Botella de agua inteligente: $45.000 normal / $40.000 volumen (≥3 unidades)

Proteína Whey 500g: $95.000 normal / $85.000 volumen (≥2 unidades)

Glutamina 200g: $75.000 normal / $68.000 volumen (≥3 unidades)

BCAAs 300g: $85.000 normal / $78.000 volumen (≥2 unidades)

Vitaminas complejo B: $55.000 normal / $50.000 volumen (≥4 unidades)

Reglas de volumen:
Productos marcados con "tieneVolumen: true" aplican precio especial al alcanzar umbral

Productos marcados con "tieneVolumen: false" mantienen precio único

Reglas de descuento:
10% si: (al menos 1 suplemento) Y (4+ prendas deportivas)

7% si: total bruto > $300.000 y no aplica regla 1

5% si: total bruto > $150.000 y no aplica reglas 1 o 2

0% si no aplica ninguna regla anterior

Estados del pedido:
EN_CREACION: Puede agregar/editar ítems

CONFIRMADO: Bloqueado para edición, solo lectura

Tercer paso: Criterios de aceptación
CA1. Precio por volumen - Dado 5 poleras básicas, cuando calculo subtotal, entonces uso $35.000 c/u → $175.000

CA2. Sin volumen - Dado 2 creatinas, cuando calculo subtotal, entonces uso $120.000 c/u → $240.000

CA3. Descuento 10% - Dado 1 proteína whey (suplemento) + 4 poleras (prendas), cuando calculo, entonces 10% descuento

CA4. Descuento 7% - Dado total bruto $350.000 sin combinación suplemento+prendas, cuando calculo, entonces 7% descuento

CA5. Descuento 5% - Dado total bruto $200.000 sin condiciones anteriores, cuando calculo, entonces 5% descuento

CA6. Bloqueo edición - Dado pedido CONFIRMADO, cuando intento agregar ítem, entonces se rechaza

CA7. Validación datos - Dado teléfono "12345", cuando registro cliente, entonces se rechaza (necesita 10 dígitos)

CA8. Resumen completo - Dado pedido válido, cuando pido resumen, entonces muestra: cliente, ítems con precio aplicado, subtotales, total bruto, descuento aplicado, total final

Cuarto paso: Estructura de clases (Diseño modular)
Paquete domain:
Cliente - Entidad con nombre y teléfono

Producto - Enum con 12 productos, precios normal/volumen, umbral y flag de volumen

ItemPedido - Producto + cantidad, calcula precio unitario aplicado y subtotal

Pedido - Composición de ítems, estado, cálculos de total, descuento y generación de resumen

EstadoPedido - Enum (EN_CREACION, CONFIRMADO)

Paquete service:
WichoFitService - Coordina operaciones: crear pedido, agregar ítems, confirmar, obtener resumen

Paquete app:
MainModern - Clase principal con interfaz gráfica Swing

ImagenUtil - Utilidad para carga de imágenes desde URL o archivos

WrapLayout - Layout personalizado para distribución automática de tarjetas

Quinto paso: Flujo de la interfaz gráfica
Pantalla de inicio - Bienvenida y características de la tienda

Registro de cliente - Al agregar primer producto, solicita nombre y teléfono

Catálogo de productos - Tarjetas con imagen, nombre, precio normal/volumen y botón "Agregar"

Panel de pedido - Muestra:

Información del cliente

Tickets con productos agregados (cantidad, precio aplicado, subtotal)

Totales (bruto, descuento, final)

Botones "Resumen" y "Confirmar"

Confirmación - Bloquea edición y muestra mensaje de éxito

Resumen detallado - Ventana emergente con formato estructurado

Características de la interfaz
Diseño visual:
Tema oscuro con gradientes y acentos azul/púrpura

Fuente compatible con emojis (Segoe UI Emoji con fallback SansSerif)

Tarjetas con efectos hover para mejor experiencia de usuario

Bordes redondeados en todos los componentes

Imágenes de productos cargadas desde URLs con placeholders

Navegación:
Sidebar fijo con: Inicio, Productos, Nuevo pedido, Historial

Contenido principal con CardLayout para cambio entre vistas

Panel dividido en productos (izquierda) y pedido (derecha)

Validaciones y feedback:
Validación en tiempo real de nombre y teléfono

Mensajes de error claros y contextuales

Prevención de acciones inválidas (pedido confirmado, cantidades ≤0)

Estructura del proyecto
text
/wicho-fit/
├── domain/
│   ├── Cliente.java
│   ├── Producto.java
│   ├── ItemPedido.java
│   ├── Pedido.java
│   └── EstadoPedido.java
├── service/
│   └── WichoFitService.java
├── app/
│   ├── MainModern.java
│   ├── ImagenUtil.java
│   └── WrapLayout.java
└── (opcional) assets/
    └── imágenes locales
Tecnologías utilizadas
Java 8+ - Lenguaje base

Swing - Para interfaz gráfica

Gradientes y efectos visuales - Graphics2D para diseño moderno

Manejo de imágenes - ImageIO para carga desde URLs

Validaciones - Expresiones regulares para datos de cliente
