package modele.plateau;

public enum Direction {
    HAUT(-1, 0),
    BAS(1, 0),
    GAUCHE(0, -1),
    DROITE(0, 1),
    HAUT_GAUCHE(-1, -1),
    HAUT_DROITE(-1, 1),
    BAS_GAUCHE(1, -1),
    BAS_DROITE(1, 1);

    private final int deltaX;
    private final int deltaY;

    Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }
}
