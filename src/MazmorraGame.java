import Game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Object;
import java.util.ArrayList;
import java.util.Random;


public class MazmorraGame extends JFrame {
    private JPanel panelPrincipal;
    private JPanel panelMenu;
    private JPanel panelBotonesMenu;
    private JPanel panelJuego;
    private JPanel panelSuperiorBorde;
    private JPanel panelSuperior;
    private JButton startButton;
    private JButton pauseButton;
    private JLabel jLabelPlayer;
    private JLabel jLabelFondoPantalla; // Background menú
    private JLabel jLabelTimer;
    private JLabel jLabelClassName;
    private JLabel jLabelEnemy;
    private JLabel jLabelVida;
    private JLabel jLabelOro;
    private JLabel jLabelVidaTexto;
    private JLabel jLabelOroTexto;
    private JLabel jLabelExitDoor;
    private JLabel jLabelGameOver;
    private JLabel jLabelWin;
    private Player player;
    private ArrayList<Enemy> enemyArrayList = new ArrayList<>();
    private ArrayList<Icon> gifs_Player = new ArrayList<>(); // Gifs player
    private ArrayList<Icon> gifs_Enemy = new ArrayList<>(); // Gifs enemy
    private ArrayList<JLabel> jLabelsArrayList_BloquesMazmorra = new ArrayList<>(); // Array JLabel mazmorra
    private ArrayList<JLabel> jLabelsEnemies = new ArrayList<>(); // Array JLabel Enemies
    private ArrayList<JLabel> jLabelObjetos = new ArrayList<>(); // Array JLabel Objetos
    private Timer timer;
    private Timer enemyMoveTimer;
    private int seconds = 0;

    // METODO PRINCIPAL
    public static void main(String[] args) {
        JFrame frame = new JFrame("Mazmorra Game");
        frame.setContentPane(new MazmorraGame().panelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Toolkit pantalla = Toolkit.getDefaultToolkit();
        Image icono = pantalla.getImage("src/Resources/logo.png");
        frame.setIconImage(icono);

        frame.setLocationRelativeTo(null); // Centrar pantalla
        frame.setLayout(null);
        frame.addWindowListener(new FrameWindowsListener(frame));
    }

    // CONSTRUCTOR DEL JUEGO
    public MazmorraGame() {
        // Panel Principal
        createPanelPrincipal();

        // Panel Menú Principal
        panelMenu.setPreferredSize(new Dimension(1280, 800));// Tamaño panel menú
        panelMenu.setSize(1280, 800);
        panelMenu.setLayout(null);
        jLabelFondoPantalla = showBackgroundMenu("background1.gif");
        panelMenu.add(jLabelFondoPantalla);// Añadir fondo de pantalla

        panelMenu.repaint();
        panelPrincipal.add(panelMenu);


        // Botones Menú Principal
        panelBotonesMenu = new JPanel();
        panelBotonesMenu.setOpaque(false); // Eliminar fondo
        panelBotonesMenu.setSize(panelMenu.getWidth(), 50); // Tamaño panel botones
        panelBotonesMenu.setLocation(0, 700); // Posicion botones

        panelMenu.add(panelBotonesMenu);
        panelMenu.setComponentZOrder(panelBotonesMenu, 0);

        startGame();
        exitGame();
    }

    private void createPanelPrincipal() {
        panelPrincipal = new JPanel();
        panelPrincipal.setPreferredSize(new Dimension(1280, 800));// Tamaño panel menú
        panelPrincipal.setSize(1280, 800);
        panelPrincipal.setLayout(null);
    }

    private void exitGame() {
        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(150, 50));

        // Custom border
        exitButton.setBorder(BorderFactory.createRaisedBevelBorder());

        // Set button colors
        exitButton.setBackground(new Color(0x000000)); // Red
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusable(false);

        exitButton.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                System.exit(0); // Terminate the program if user confirms
            }
        });
        panelBotonesMenu.add(exitButton);
    }

    private void startGame() {
        // Button start main menu
        startButton = new JButton("Start Game");
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.setBorder(BorderFactory.createRaisedBevelBorder());
        startButton.setBackground(new Color(0x000000)); // Red
        startButton.setForeground(Color.WHITE);
        startButton.setFocusable(false);

        panelBotonesMenu.add(startButton);


        // Escojes nombre y escojes clase
        startButton.addActionListener(e -> {
            boolean salir = false;
            String name;
            panelBotonesMenu.setVisible(false);
            panelMenu.remove(jLabelFondoPantalla);
            jLabelFondoPantalla = showBackgroundMenu("background.gif");
            panelMenu.add(jLabelFondoPantalla);
            do {
                name = JOptionPane.showInputDialog(this, "Enter your name:", "Create player", JOptionPane.QUESTION_MESSAGE);

                if (name == null) {
                    panelBotonesMenu.setVisible(true);
                    panelMenu.remove(jLabelFondoPantalla);
                    jLabelFondoPantalla = showBackgroundMenu("background1.gif");
                    panelMenu.add(jLabelFondoPantalla);
                    salir = true;
                } else if (!name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Welcome, " + name + "!", "Create player", JOptionPane.INFORMATION_MESSAGE);
                    int classSelection = showClassButton(); // Muestra las opciones de clases
                    // Creamos el Label del personaje
                    jLabelPlayer = new JLabel();
                    jLabelPlayer.setSize(64, 64);
                    createPlayer(name, classSelection);
                    salir = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a name to start the game.", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            } while (!salir);


            // Empieza el juego
            if (name != null) {
                generateMap();
            }
        });
    }

    private void generateMap() {
        int panelHeight = 50; // Altura del panel superior
        int blockX = panelPrincipal.getWidth() / 32;
        int blockY = (panelPrincipal.getHeight() - panelHeight) / 32;

        Icon iconTile04 = getIcon("src\\Resources\\dungeon\\tile004.png");
        Icon iconTile01 = getIcon("src\\Resources\\dungeon\\tile001.png");

        // Crear el panel superior (borde)
        panelSuperiorBorde = new JPanel();
        panelSuperiorBorde.setSize(panelPrincipal.getWidth(), panelHeight);
        panelSuperiorBorde.setLayout(null);
        panelSuperiorBorde.setVisible(true);
        panelSuperiorBorde.setBackground(new Color(239, 184, 16)); // Color de fondo (borde)

        panelMenu.add(panelSuperiorBorde);
        panelMenu.setComponentZOrder(panelSuperiorBorde, 1);

        // Crear el panel superior
        panelSuperior = new JPanel();
        panelSuperior.setSize(panelPrincipal.getWidth() - 6, panelHeight - 6);
        panelSuperior.setLayout(null);
        panelSuperior.setVisible(true);
        panelSuperior.setBackground(new Color(11, 49, 66)); // Color de fondo
        panelSuperior.setLocation(3, 3);

        panelMenu.add(panelSuperior);
        panelMenu.setComponentZOrder(panelSuperior, 1);

        // Generar Timer
        jLabelTimer = new JLabel("00:00");
        jLabelTimer.setSize(62, 30); // Ajustar tamaño del label
        jLabelTimer.setLocation(panelSuperior.getWidth() / 2 - jLabelTimer.getWidth() / 2, 8); // Ajustar la posición del label
        jLabelTimer.setForeground(new Color(239, 184, 16)); // Ajustar color del texto
        jLabelTimer.setFont(new Font("Arial", Font.BOLD, 24)); // Cambiar la fuente y tamaño
        jLabelTimer.setOpaque(true);
        jLabelTimer.setBackground(new Color(6, 27, 37)); // Fondo

        panelSuperior.add(jLabelTimer);
        jLabelTimer.setVisible(true);

        // Iniciar Timer
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                jLabelTimer.setText(formatTime(seconds));
            }
        });

        Timer timerColission = new Timer(10, new PlayerTimerActionListener(player)); // Comprueba colisiones
        timerColission.start();
        timer.start();


        // Mostrar nombre y clase del jugador
        jLabelClassName = new JLabel(player.getClass().getSimpleName() + ": " + player.getName());
        jLabelClassName.setSize(400, 30);
        jLabelClassName.setLocation(20, 8);
        jLabelClassName.setFont(new Font("Arial", Font.BOLD, 16)); // Cambiar la fuente y tamaño
        jLabelClassName.setForeground(Color.WHITE);
        jLabelClassName.setOpaque(false);

        panelSuperior.add(jLabelClassName);
        jLabelClassName.setVisible(true);

        // Mostrar vida y oro
        jLabelVida = new JLabel();
        jLabelVida.setSize(25, 25);
        ImageIcon imageIconVida = new ImageIcon("src/Resources/heart.png");
        Icon iconVida = new ImageIcon(
                imageIconVida.getImage().getScaledInstance(jLabelVida.getWidth(), jLabelVida.getHeight(), Image.SCALE_DEFAULT));
        jLabelVida.setIcon(iconVida);
        jLabelVida.setLocation(350, 10);

        panelSuperior.add(jLabelVida);
        jLabelVida.setVisible(true);

        jLabelVidaTexto = new JLabel(String.valueOf(player.getVidas()));
        jLabelVidaTexto.setSize(30, 25); // Ajustar tamaño del label
        jLabelVidaTexto.setLocation(382, 10); // Ajustar la posición del label
        jLabelVidaTexto.setFont(new Font("Arial", Font.BOLD, 16));
        jLabelVidaTexto.setForeground(Color.WHITE); // Ajustar color del texto
        jLabelVidaTexto.setOpaque(false);

        panelSuperior.add(jLabelVidaTexto);
        jLabelVidaTexto.setVisible(true);


        jLabelOro = new JLabel();
        jLabelOro.setSize(40, 40);
        ImageIcon imageIconOro = new ImageIcon("src/Resources/ore.png");
        Icon iconOro = new ImageIcon(
                imageIconOro.getImage().getScaledInstance(jLabelOro.getWidth(), jLabelOro.getHeight(), Image.SCALE_DEFAULT));
        jLabelOro.setIcon(iconOro);
        jLabelOro.setLocation(420, 3);

        panelSuperior.add(jLabelOro);
        jLabelOro.setVisible(true);

        jLabelOroTexto = new JLabel("0");
        jLabelOroTexto.setSize(30, 25); // Ajustar tamaño del label
        jLabelOroTexto.setLocation(456, 10); // Ajustar la posición del label
        jLabelOroTexto.setFont(new Font("Arial", Font.BOLD, 16));
        jLabelOroTexto.setForeground(Color.WHITE); // Ajustar color del texto
        jLabelOroTexto.setOpaque(false);

        panelSuperior.add(jLabelOroTexto);
        jLabelOroTexto.setVisible(true);


        // Redibujado del panel superior
        panelSuperior.revalidate();
        panelSuperior.repaint();

        // Boton de pausa
        pauseButton = new JButton("Pausa");
        pauseButton.setSize(new Dimension(100, 30));
        pauseButton.setBorder(BorderFactory.createLineBorder(new Color(239, 184, 16), 3));
        pauseButton.setBackground(new Color(17, 81, 110));
        pauseButton.setForeground(Color.BLACK);
        pauseButton.setFocusable(false);
        pauseButton.addActionListener(e -> {
            if (pauseButton.getText().equals("Pausa")) {
                timer.stop();
                enemyMoveTimer.stop();
                pauseButton.setText("Reanudar");
            } else {
                timer.start();
                enemyMoveTimer.start();
                pauseButton.setText("Pausa");
            }
        });

        pauseButton.setLocation(1150, 7);
        panelSuperior.add(pauseButton);


        // Crear panel de juego
        panelJuego = new JPanel();
        panelJuego.setSize(panelPrincipal.getWidth(), panelPrincipal.getHeight() - panelHeight);
        panelJuego.setLocation(0, panelHeight);
        panelJuego.setLayout(null);
        panelJuego.setVisible(true);
        panelJuego.setBackground(new Color(255, 0, 0));

        panelMenu.add(panelJuego);
        panelMenu.setComponentZOrder(panelJuego, 1);

        // Paredes mapa
        for (int i = 0; i < blockX; i++) {
            JLabel jLabel_Tile04 = new JLabel();
            jLabel_Tile04.setIcon(iconTile04);
            jLabel_Tile04.setLocation(32 * i, 0);
            jLabel_Tile04.setSize(32, 32);
            panelJuego.add(jLabel_Tile04);
            jLabelsArrayList_BloquesMazmorra.add(jLabel_Tile04);
        }
        for (int i = 0; i < blockX; i++) {
            JLabel jLabel_Tile04 = new JLabel();
            jLabel_Tile04.setIcon(iconTile04);
            jLabel_Tile04.setLocation(32 * i, panelJuego.getHeight() - 32);
            jLabel_Tile04.setSize(32, 32);
            panelJuego.add(jLabel_Tile04);
            jLabelsArrayList_BloquesMazmorra.add(jLabel_Tile04);
        }
        for (int i = 0; i < blockY; i++) {
            // Crear abertura en la pared izquierda cerca del borde superior
            if (i >= 2 && i <= 3) {
                // Rellenar con bloques de suelo
                JLabel jLabel_Tile01 = new JLabel();
                jLabel_Tile01.setIcon(iconTile01);
                jLabel_Tile01.setLocation(0, 32 * i);
                jLabel_Tile01.setSize(32, 32);
                panelJuego.add(jLabel_Tile01);
                continue;
            }
            JLabel jLabel_Tile04 = new JLabel();
            jLabel_Tile04.setIcon(iconTile04);
            jLabel_Tile04.setLocation(0, 32 * i);
            jLabel_Tile04.setSize(32, 32);
            panelJuego.add(jLabel_Tile04);
            jLabelsArrayList_BloquesMazmorra.add(jLabel_Tile04);
        }
        for (int i = 0; i < blockY; i++) {
            // Crear abertura en la pared derecha cerca del borde inferior
            if (i >= blockY - 4 && i <= blockY - 3) {
                // Rellenar con bloques de suelo
                JLabel jLabel_Tile01 = new JLabel();
                jLabel_Tile01.setIcon(iconTile01);
                jLabel_Tile01.setLocation(panelJuego.getWidth() - 32, 32 * i);
                jLabel_Tile01.setSize(32, 32);
                panelJuego.add(jLabel_Tile01);
                continue;
            }
            JLabel jLabel_Tile04 = new JLabel();
            jLabel_Tile04.setIcon(iconTile04);
            jLabel_Tile04.setLocation(panelJuego.getWidth() - 32, 32 * i);
            jLabel_Tile04.setSize(32, 32);
            panelJuego.add(jLabel_Tile04);
            jLabelsArrayList_BloquesMazmorra.add(jLabel_Tile04);
        }


        // Suelo mapa
        for (int x = 1; x < blockX - 1; x++) {
            for (int y = 1; y < blockY; y++) {
                JLabel jLabel_Tile01 = new JLabel();
                jLabel_Tile01.setIcon(iconTile01);
                jLabel_Tile01.setLocation(32 * x, 32 * y);
                jLabel_Tile01.setSize(32, 32);
                panelJuego.add(jLabel_Tile01);
            }
        }


        // Creación de personaje
        jLabelPlayer.setLocation(20, 70);
        panelJuego.add(jLabelPlayer, 1);
        panelJuego.addKeyListener(new MovePlayerKeyAdapter(gifs_Player));
        panelJuego.setFocusable(true);
        panelJuego.requestFocusInWindow();


        // Creación de enemigos
        Random rd = new Random();
        for (int i = 0; i < 6; i++) {
            jLabelEnemy = new JLabel();
            jLabelEnemy.setSize(64, 64);
            gifs_Enemy = loadIconsEnemy();
            Enemy enemy = new Enemy("Enemy " + i, gifs_Enemy);
            int enemyX = rd.nextInt(32, panelJuego.getWidth() - 96);
            int enemyY = rd.nextInt(20, panelJuego.getHeight() - 96);
            jLabelEnemy.setLocation(enemyX, enemyY);
            panelJuego.add(jLabelEnemy, 0);
            enemyArrayList.add(enemy);
            jLabelsEnemies.add(jLabelEnemy);
        }

        // Movimiento enemigos
        moveEnemiesRandomly();

        // Generación de objetos en mapa
        jLabelObjetos = generarObjetos();
    }

    private ArrayList<JLabel> generarObjetos() {
        Random rd = new Random();
        jLabelObjetos = new ArrayList<>();
        String[] nombreObjeto = new String[4];
        nombreObjeto[0] = "dollar";
        nombreObjeto[1] = "mitra";
        nombreObjeto[2] = "potion";
        nombreObjeto[3] = "sword";

        for (int i = 0; i < nombreObjeto.length; i++) {
            ImageIcon imageIconObjeto = new ImageIcon("src/Resources/dungeon/" + nombreObjeto[i] + ".png");
            Icon iconObjeto = new ImageIcon(
                    imageIconObjeto.getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT)
            );
            JLabel jLabelObjeto = new JLabel();
            jLabelObjeto.setIcon(iconObjeto);
            jLabelObjeto.setSize(32, 32);
            jLabelObjeto.setName(nombreObjeto[i]);
            jLabelObjeto.setLocation(rd.nextInt(panelJuego.getWidth() - (32 * 2)),
                    rd.nextInt(panelJuego.getHeight() - (32 * 2)));

            panelJuego.add(jLabelObjeto, 0);
            jLabelObjeto.setVisible(true);

            jLabelObjetos.add(jLabelObjeto);
        }
        return jLabelObjetos;
    }

    private void moveEnemiesRandomly() {
        enemyMoveTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();
                for (JLabel enemyLabel : jLabelsEnemies) {
                    int direction = random.nextInt(4); // 0: arriba, 1: abajo, 2: izquierda, 3: derecha
                    int deltaX = 0;
                    int deltaY = 0;

                    switch (direction) {
                        case 0: // Arriba
                            deltaY = -20;
                            enemyLabel.setIcon(gifs_Enemy.get(0));
                            break;
                        case 1: // Abajo
                            deltaY = 20;
                            enemyLabel.setIcon(gifs_Enemy.get(1));
                            break;
                        case 2: // Izquierda
                            deltaX = -20;
                            enemyLabel.setIcon(gifs_Enemy.get(3));
                            break;
                        case 3: // Derecha
                            deltaX = 20;
                            enemyLabel.setIcon(gifs_Enemy.get(2));
                            break;
                    }

                    int newX = enemyLabel.getX() + deltaX;
                    int newY = enemyLabel.getY() + deltaY;

                    // Verifica que el nuevo movimiento esté dentro de los límites del panel de juego
                    if (newX >= 10 && newX <= panelJuego.getWidth() - enemyLabel.getWidth() - 10
                            && newY >= 10 && newY <= panelJuego.getHeight() - enemyLabel.getHeight() - 32) {
                        enemyLabel.setLocation(newX, newY);
                    }
                }
            }
        });
        enemyMoveTimer.start();
    }

    private static Icon getIcon(String filename) {
        ImageIcon imageTile = new ImageIcon(filename);
        return new ImageIcon(
                imageTile.getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
    }

    private void createPlayer(String name, int classSelection) {
        switch (classSelection) {
            case 0:
                gifs_Player = loadIcons("wizard");
                player = new Mage(name, 3, 7, gifs_Player);
                break;
            case 1:
                gifs_Player = loadIcons("warrior");
                player = new Warrior(name,5, 3, gifs_Player);
                break;
            case 2:
                gifs_Player = loadIcons("priest");
                player = new Priest(name, 4, 5, gifs_Player);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + classSelection);
        }
        JOptionPane.showMessageDialog(this, "You have chosen " + player.getClass().getSimpleName() + "!", "Create player", JOptionPane.INFORMATION_MESSAGE);
    }

    private ArrayList<Icon> loadIcons(String clase) {
        ArrayList<Icon> arrayAux = new ArrayList<>();
        // Ruta del directorio de imágenes
        String directoryPath = "src/Resources/" + clase + "/";

        // Cargar las imágenes correspondientes
        ImageIcon imageIconUp = new ImageIcon(directoryPath + clase + "_up.gif");
        Icon iconUp = new ImageIcon(
                imageIconUp.getImage().getScaledInstance(jLabelPlayer.getWidth(), jLabelPlayer.getHeight(), Image.SCALE_DEFAULT));

        ImageIcon imageIconDown = new ImageIcon(directoryPath + clase + "_down.gif");
        Icon iconDown = new ImageIcon(
                imageIconDown.getImage().getScaledInstance(jLabelPlayer.getWidth(), jLabelPlayer.getHeight(), Image.SCALE_DEFAULT));

        ImageIcon imageIconRight = new ImageIcon(directoryPath + clase + "_right.gif");
        Icon iconRight = new ImageIcon(
                imageIconRight.getImage().getScaledInstance(jLabelPlayer.getWidth(), jLabelPlayer.getHeight(), Image.SCALE_DEFAULT));

        ImageIcon imageIconLeft = new ImageIcon(directoryPath + clase + "_left.gif");
        Icon iconLeft = new ImageIcon(
                imageIconLeft.getImage().getScaledInstance(jLabelPlayer.getWidth(), jLabelPlayer.getHeight(), Image.SCALE_DEFAULT));

        // Por defecto
        jLabelPlayer.setIcon(iconRight);

        // Agregar las imágenes a la lista de iconos
        arrayAux.add(iconUp);
        arrayAux.add(iconDown);
        arrayAux.add(iconRight);
        arrayAux.add(iconLeft);

        return arrayAux;
    }

    private ArrayList<Icon> loadIconsEnemy() {
        ArrayList<Icon> arrayAux = new ArrayList<>();
        // Ruta del directorio de imágenes
        String directoryPath = "src/Resources/skeleton/";

        // Cargar las imágenes correspondientes
        ImageIcon imageIconUp = new ImageIcon(directoryPath + "skeleton_up.gif");
        Icon iconUp = new ImageIcon(
                imageIconUp.getImage().getScaledInstance(jLabelEnemy.getWidth(), jLabelEnemy.getHeight(), Image.SCALE_DEFAULT));

        ImageIcon imageIconDown = new ImageIcon(directoryPath + "skeleton_down.gif");
        Icon iconDown = new ImageIcon(
                imageIconDown.getImage().getScaledInstance(jLabelEnemy.getWidth(), jLabelEnemy.getHeight(), Image.SCALE_DEFAULT));

        ImageIcon imageIconRight = new ImageIcon(directoryPath + "skeleton_right.gif");
        Icon iconRight = new ImageIcon(
                imageIconRight.getImage().getScaledInstance(jLabelEnemy.getWidth(), jLabelEnemy.getHeight(), Image.SCALE_DEFAULT));

        ImageIcon imageIconLeft = new ImageIcon(directoryPath + "skeleton_left.gif");
        Icon iconLeft = new ImageIcon(
                imageIconLeft.getImage().getScaledInstance(jLabelEnemy.getWidth(), jLabelEnemy.getHeight(), Image.SCALE_DEFAULT));

        // Por defecto
        jLabelEnemy.setIcon(iconRight);

        // Agregar las imágenes a la lista de iconos
        arrayAux.add(iconUp);
        arrayAux.add(iconDown);
        arrayAux.add(iconRight);
        arrayAux.add(iconLeft);

        return arrayAux;
    }

    private int showClassButton() {
        return JOptionPane.showOptionDialog(
                null, // Main component
                "Choose class", // Message
                "Create player", // Title
                JOptionPane.YES_NO_CANCEL_OPTION, // Selection type
                JOptionPane.QUESTION_MESSAGE, // Message type
                null, // Icon
                new Object[]{"Mage", "Warrior", "Priest"}, // Options
                "Option 1" // Default option
        );
    }

    private JLabel showBackgroundMenu(String file) {
        // Gestionar fondo de pantalla menú principal
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setSize(panelMenu.getWidth(), panelMenu.getHeight());
        backgroundLabel.setLocation(0, 0);
        ImageIcon imageIcon = new ImageIcon("src/resources/" + file);
        backgroundLabel.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(1280, 800, Image.SCALE_DEFAULT)));
        return backgroundLabel;
    }

    private static class FrameWindowsListener extends WindowAdapter {
        JFrame frame;

        public FrameWindowsListener(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
            int confirmado = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirmado == JOptionPane.YES_OPTION) {
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            } else {
                frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            }
        }
    }

    private class MovePlayerKeyAdapter extends KeyAdapter {
        ArrayList<Icon> gifsPlayer;

        public MovePlayerKeyAdapter(ArrayList<Icon> gifsPlayer) {
            this.gifsPlayer = gifsPlayer;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            // Verificar si el juego está pausado
            if (pauseButton.getText().equals("Pausa")) {
                int x = jLabelPlayer.getX();
                int y = jLabelPlayer.getY();


                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        x += 20;
                        jLabelPlayer.setIcon(gifs_Player.get(2));
                        break;
                    case KeyEvent.VK_LEFT:
                        x -= 20;
                        jLabelPlayer.setIcon(gifs_Player.get(3));
                        break;
                    case KeyEvent.VK_UP:
                        y -= 20;
                        jLabelPlayer.setIcon(gifs_Player.get(0));
                        break;
                    case KeyEvent.VK_DOWN:
                        y += 20;
                        jLabelPlayer.setIcon(gifs_Player.get(1));
                        break;
                }

                if (x >= 10 && x <= panelJuego.getWidth() - jLabelPlayer.getWidth()) {
                    jLabelPlayer.setLocation(x, jLabelPlayer.getY());
                }
                if (y >= 10 && y <= panelJuego.getHeight() - jLabelPlayer.getHeight() - 10) {
                    jLabelPlayer.setLocation(jLabelPlayer.getX(), y);
                }
            }
        }
    }
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    // No se que hace esto
    public static boolean checkCollision(JLabel jLabelPersonaje, JLabel jLabelAleatorio, int reductionAmount) {
        Rectangle bounds1 = getReducedBounds(jLabelPersonaje.getBounds(), reductionAmount);
        Rectangle bounds2 = getReducedBounds(jLabelAleatorio.getBounds(), reductionAmount);
        return bounds1.intersects(bounds2);
    }

    private static Rectangle getReducedBounds(Rectangle originalBounds, int reductionAmount) {
        int newWidth = originalBounds.width - 2 * reductionAmount;
        int newHeight = originalBounds.height - 2 * reductionAmount;
        int newX = originalBounds.x + reductionAmount;
        int newY = originalBounds.y + reductionAmount;

        // Comprobar que las nuevas dimensiones no son negativas
        if (newWidth < 0) newWidth = 0;
        if (newHeight < 0) newHeight = 0;

        return new Rectangle(newX, newY, newWidth, newHeight);
    }

    public class PlayerTimerActionListener implements ActionListener {
        Player player;
        Random rd = new Random();
        boolean objectUsed = false;


        public PlayerTimerActionListener(Player player) {
            this.player = player;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Check objects collision
            // Gold
            if (checkCollision(jLabelPlayer, jLabelObjetos.get(0), 0)) {
                if (player.getOro() < 50) {
                    int oro = player.getOro() + 10;
                    player.setOro(oro);
                    jLabelOroTexto.setText(String.valueOf(player.getOro()));
                    jLabelObjetos.get(0).setLocation
                            (rd.nextInt(panelJuego.getWidth() - (32 * 2)),
                                    rd.nextInt(panelJuego.getHeight() - (32 * 2)));
                } else {
                    jLabelObjetos.get(0).setLocation
                            (rd.nextInt(panelJuego.getWidth() - (32 * 2)),
                                    rd.nextInt(panelJuego.getHeight() - (32 * 2)));
                }
            }

            // Max gold
            if (player.getOro() >= 50) {
                jLabelExitDoor = new JLabel();
                jLabelExitDoor.setOpaque(true);
                jLabelExitDoor.setBackground(new Color(116, 198, 157, 60));
                jLabelExitDoor.setSize(64, 64);
                jLabelExitDoor.setLocation(panelJuego.getWidth() - 64, panelJuego.getHeight() - 143);

                panelJuego.add(jLabelExitDoor, 1);
                jLabelExitDoor.setVisible(true);

                // Exit door
                if (checkCollision(jLabelPlayer, jLabelExitDoor, 10)){
                    timer.stop();
                    enemyMoveTimer.stop();
                    panelJuego.setFocusable(false);

                    jLabelWin = new JLabel();
                    jLabelWin.setSize(540, 360);
                    ImageIcon imageIconW = new ImageIcon("src/Resources/win.png");
                    Icon iconW = new ImageIcon(
                            imageIconW.getImage().getScaledInstance(jLabelWin.getWidth(), jLabelWin.getHeight(), Image.SCALE_DEFAULT));
                    jLabelWin.setIcon(iconW);
                    jLabelWin.setLocation((panelJuego.getWidth()/2 - jLabelWin.getWidth()/2),180);

                    panelJuego.add(jLabelWin, 0);
                    jLabelWin.setVisible(true);
                }
            }

            // Mitra
            if (checkCollision(jLabelPlayer, jLabelObjetos.get(1), 0)) {
                panelJuego.remove(jLabelObjetos.get(1));
                panelSuperior.add(jLabelObjetos.get(1), 1);
                jLabelObjetos.get(1).setLocation(800, 8);
                player.getObjetosArrayList().add(new ObjectGame("mitra"));
            }
            // Potion
            if (checkCollision(jLabelPlayer, jLabelObjetos.get(2), 0)) {
                panelJuego.remove(jLabelObjetos.get(2));
                panelSuperior.add(jLabelObjetos.get(2), 1);
                jLabelObjetos.get(2).setLocation(850, 8);
                player.getObjetosArrayList().add(new ObjectGame("potion"));
            }
            // Sword
            if (checkCollision(jLabelPlayer, jLabelObjetos.get(3), 0)) {
                panelJuego.remove(jLabelObjetos.get(3));
                panelSuperior.add(jLabelObjetos.get(3), 1);
                jLabelObjetos.get(3).setLocation(900, 8);
                player.getObjetosArrayList().add(new ObjectGame("sword"));

                if (player instanceof Warrior) {
                    if (!objectUsed) {
                        for (ObjectGame element : player.getObjetosArrayList()) {
                            if (element.getType().equals("sword")) {
                                panelJuego.remove(jLabelsEnemies.get(0));
                                objectUsed = true;
                            }
                        }
                    }
                }
            }



            // Check enemies collision
            if (checkCollision(jLabelPlayer, jLabelsEnemies.get(0), 15) ||
                    checkCollision(jLabelPlayer, jLabelsEnemies.get(1), 15) ||
                    checkCollision(jLabelPlayer, jLabelsEnemies.get(2), 15) ||
                    checkCollision(jLabelPlayer, jLabelsEnemies.get(3), 15) ||
                    checkCollision(jLabelPlayer, jLabelsEnemies.get(4), 15) ||
                    checkCollision(jLabelPlayer, jLabelsEnemies.get(5), 15)) {

                jLabelPlayer.setLocation(20, 70);

                if (player instanceof Mage) {
                    if (!objectUsed) {
                        for (ObjectGame element : player.getObjetosArrayList()) {
                            if (element.getType().equals("potion")) {
                                jLabelPlayer.setLocation(20, 70);
                                player.setVidas(player.getVidas() + 1);
                                jLabelVidaTexto.setText(String.valueOf(player.getVidas()));
                                objectUsed = true;
                            }
                        }
                        if (!objectUsed) {
                            player.setVidas(player.getVidas() - 1);
                            jLabelVidaTexto.setText(String.valueOf(player.getVidas()));
                        }
                    } else {
                        player.setVidas(player.getVidas() - 1);
                        jLabelVidaTexto.setText(String.valueOf(player.getVidas()));
                    }


                } else if (player instanceof Priest) {
                    if (!objectUsed) {
                        for (ObjectGame element : player.getObjetosArrayList()) {
                            if (element.getType().equals("mitra")) {
                                jLabelPlayer.setLocation(20, 70);
                                objectUsed = true;
                            }
                        }
                        if (!objectUsed) {
                            player.setVidas(player.getVidas() - 1);
                            jLabelVidaTexto.setText(String.valueOf(player.getVidas()));
                        }
                    } else {
                        player.setVidas(player.getVidas() - 1);
                        jLabelVidaTexto.setText(String.valueOf(player.getVidas()));
                    }
                } else {
                    player.setVidas(player.getVidas() - 1);
                    jLabelVidaTexto.setText(String.valueOf(player.getVidas()));
                }
            }

            if (player.getVidas() == 0) {
                timer.stop();
                enemyMoveTimer.stop();
                panelJuego.setFocusable(false);

                jLabelGameOver = new JLabel();
                jLabelGameOver.setSize(528, 528);
                ImageIcon imageIconGO = new ImageIcon("src/Resources/game_over.png");
                Icon iconGO = new ImageIcon(
                        imageIconGO.getImage().getScaledInstance(jLabelGameOver.getWidth(), jLabelGameOver.getHeight(), Image.SCALE_DEFAULT));
                jLabelGameOver.setIcon(iconGO);
                jLabelGameOver.setLocation((panelJuego.getWidth()/2 - jLabelGameOver.getWidth()/2),100);

                panelJuego.add(jLabelGameOver, 0);
                jLabelGameOver.setVisible(true);
            }
        }
    }
}
