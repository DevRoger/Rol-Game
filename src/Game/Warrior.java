package Game;

import javax.swing.*;
import java.util.ArrayList;

public class Warrior extends Player {

    public Warrior(String name, int vidas, int velocidad, ArrayList<Icon> IconArrayList) {
        this.name = name;
        this.vidas = vidas;
        this.velocidad = velocidad;
        this.IconArrayList = IconArrayList;
    }
}

