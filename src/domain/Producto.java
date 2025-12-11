package domain;

public enum Producto {
    POLERA_BASICA("Polera básica", 40000, 35000, 5, true),
    SUDADERA("Sudadera", 80000, 75000, 3, true),
    CREATINA_300G("Creatina 300g", 120000, 0, 0, false),
    PRE_ENTRENO_200G("Pre-entreno 200g", 90000, 0, 0, false),
    // Nuevos productos
    GORRA_DEPORTIVA("Gorra deportiva", 25000, 22000, 4, true),
    PANTALON_DEPORTIVO("Pantalón deportivo", 65000, 58000, 2, true),
    BANDAS_RESISTENCIA("Bandas de resistencia", 35000, 30000, 6, true),
    BOTELLA_AGUA("Botella de agua inteligente", 45000, 40000, 3, true),
    PROTEINA_WHEY("Proteína Whey 500g", 95000, 85000, 2, false),
    GLUTAMINA_200G("Glutamina 200g", 75000, 68000, 3, false),
    BCAAS_300G("BCAAs 300g", 85000, 78000, 2, false),
    VITAMINAS_COMPLEJO_B("Vitaminas complejo B", 55000, 50000, 4, false);

    private final String nombre;
    private final int precioNormal;
    private final int precioVolumen;
    private final int umbralVolumen;
    private final boolean tieneVolumen;

    Producto(String nombre, int precioNormal, int precioVolumen, int umbralVolumen, boolean tieneVolumen) {
        this.nombre = nombre;
        this.precioNormal = precioNormal;
        this.precioVolumen = precioVolumen;
        this.umbralVolumen = umbralVolumen;
        this.tieneVolumen = tieneVolumen;
    }

    public String getNombre() { return nombre; }
    public int getPrecioNormal() { return precioNormal; }
    public int getPrecioVolumen() { return precioVolumen; }
    public int getUmbralVolumen() { return umbralVolumen; }
    public boolean hasVolumen() { return tieneVolumen; }
}