package Game;

public class ObjectGame {
    // Atributos
    protected String type;

    // Constructors

    public ObjectGame(String type) {
        this.type = type;
    }

    // Getters y Setters
    public String getType() {
        return type;
    }



    // Métodos
    @Override
    public String toString() {
        return "Object{" +
                "type='" + type + '\'' +
                '}';
    }
}
