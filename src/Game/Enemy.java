package Game;

import javax.swing.*;
import java.util.ArrayList;

public class Enemy extends Player {
    public Enemy() {
    }

    public Enemy(String name, ArrayList<Icon> gifsEnemy) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Enemy{" +
                "name='" + name + '\'' +
                ", velocidad=" + velocidad +
                ", posicionY=" + posicionY +
                ", posicionX=" + posicionX +
                '}';
    }
}
