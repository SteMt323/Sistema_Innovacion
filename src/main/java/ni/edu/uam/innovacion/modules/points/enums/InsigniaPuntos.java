package ni.edu.uam.innovacion.modules.points.enums;

import ni.edu.uam.innovacion.modules.points.dto.InsigniaResponse;

public enum InsigniaPuntos {
    SIN_INSIGNIA(0, "Sin insignia", "#64748B"),
    BRONCE(25, "Bronce", "#A16207"),
    PLATA(75, "Plata", "#64748B"),
    ORO(150, "Oro", "#CA8A04"),
    PLATINO(300, "Platino", "#0F766E");

    private final long minimo;
    private final String nombre;
    private final String color;

    InsigniaPuntos(long minimo, String nombre, String color) {
        this.minimo = minimo;
        this.nombre = nombre;
        this.color = color;
    }

    public static InsigniaResponse desdeTotal(long totalPuntos) {
        InsigniaPuntos actual = SIN_INSIGNIA;
        for (InsigniaPuntos insignia : values()) {
            if (totalPuntos >= insignia.minimo) {
                actual = insignia;
            }
        }
        return new InsigniaResponse(
            actual.name().toLowerCase(),
            actual.nombre,
            actual.nombre,
            actual.color,
            actual.minimo,
            totalPuntos
        );
    }
}
