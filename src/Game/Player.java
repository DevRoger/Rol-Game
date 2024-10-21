package Game;

import javax.swing.*;
import java.util.ArrayList;

public class Player {
    // Atributos
    protected String name;
    protected int vidas;
    protected int oro;
    protected int velocidad;
    protected ArrayList<ObjectGame> objetosArrayList = new ArrayList<>();
    protected ArrayList<Icon> IconArrayList = new ArrayList<>();
    protected int posicionY;
    protected int posicionX;

    // Constructors

    public Player() {
    }

    public Player(String name, int vidas, int velocidad, ArrayList<Icon> IconArrayList) {
        this.name = name;
        this.vidas = vidas;
        this.velocidad = velocidad;
        this.IconArrayList = IconArrayList;
    }

    // Getters y Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public int getOro() {
        return oro;
    }

    public void setOro(int oro) {
        this.oro = oro;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public ArrayList<ObjectGame> getObjetosArrayList() {
        return objetosArrayList;
    }

    public void setObjetosArrayList(ArrayList<ObjectGame> objetosArrayList) {
        this.objetosArrayList = objetosArrayList;
    }

    public ArrayList<Icon> getIconArrayList() {
        return IconArrayList;
    }

    public void setIconArrayList(ArrayList<Icon> iconArrayList) {
        IconArrayList = iconArrayList;
    }

    public int getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
    }

    public int getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
    }

    // Métodos

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", vidas=" + vidas +
                ", oro=" + oro +
                ", velocidad=" + velocidad +
                ", IconArrayList=" + IconArrayList +
                ", posicionY=" + posicionY +
                ", posicionX=" + posicionX +
                '}';
    }
}
