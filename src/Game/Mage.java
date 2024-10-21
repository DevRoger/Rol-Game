package Game;

import javax.swing.*;
import java.util.ArrayList;

public class Mage extends Player {

    public Mage(String name, int vidas, int velocidad, ArrayList<Icon> IconArrayList) {
        this.name = name;
        this.vidas = vidas;
        this.velocidad = velocidad;
        this.IconArrayList = IconArrayList;
    }
}
