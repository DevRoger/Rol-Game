package Game;

import javax.swing.*;
import java.util.ArrayList;

public class Priest extends Player {

    public Priest(String name, int vidas, int velocidad, ArrayList<Icon> IconArrayList) {
        this.name = name;
        this.vidas = vidas;
        this.velocidad = velocidad;
        this.IconArrayList = IconArrayList;
    }
}