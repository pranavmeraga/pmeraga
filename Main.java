package core;

//import tileengine.TERenderer;
//import tileengine.TETile;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;

import java.awt.*;
import java.util.Arrays;
//import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        //fill in main method here
        boolean quit = false;
        boolean colon = false;
        while (!quit) {
            drawMainMenu();
            char choice = getUserChoice();
            if (choice == 'N') {
                newWorld();
                break;
            }
            if (choice == 'L') {
                loadWorld("save.txt");
                break;
            }
            if (choice == ':') {
                colon = true;
            }
            if (choice == 'q' && colon || choice == 'Q' && colon) {
                quit = true;
                System.exit(0);
                break;
            } else {
                System.out.println("Invalid choice. Please select again.");
            }
        }
    }
    private static void drawMainMenu() {
        // Clear the canvas
        StdDraw.clear(Color.BLACK);

        // Draw the main menu
        Font font = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(0.5, 0.8, "Main Menu");
        StdDraw.text(0.5, 0.6, "N: New World");
        StdDraw.text(0.5, 0.5, "L: Load World");
        StdDraw.text(0.5, 0.4, "Q: Quit");
        StdDraw.show();
    }

    private static char getUserChoice() {
        // Wait for user input
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(100);
        }
        return Character.toUpperCase(StdDraw.nextKeyTyped());
    }

    private static void newWorld() {
        // Prompt the user to enter a seed
        StdDraw.clear(Color.BLACK);
        StdDraw.text(0.5, 0.6, "Enter the random seed (press S to confirm):");
        StdDraw.text(0.5, 0.4, "Seed entered so far: N");
        StringBuilder seedBuilder = new StringBuilder();
        while (true) {
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(100);
            }
            char key = StdDraw.nextKeyTyped();
            if (key == 'S' || key == 's') {
                break;
            }
            if (Character.isDigit(key)) {
                seedBuilder.append(key);
                StdDraw.clear(Color.BLACK);
                StdDraw.text(0.5, 0.6, "Enter the random seed (press S to confirm):");
                StdDraw.text(0.5, 0.4, "Seed entered so far: N" + seedBuilder.toString());
            }
        }
        // Generate the world using the entered seed
        String seed = seedBuilder.toString();
        World world = new World(seed);
        // Render the world

        world.renderDisplay();

    }

    public static TETile[][] loadWorld(String filename) {
        String contents = FileUtils.readFile(filename);

        String[] split = contents.split("\n");
        String widthandheight = split[0];
        String[] separatewidthheight = widthandheight.split(" ");
        int width = Integer.valueOf(separatewidthheight[0]);
        int height = Integer.valueOf(separatewidthheight[1]);
        TETile[][] board = new TETile[width][height];
        split = Arrays.copyOfRange(split, 2, split.length);
        int m = 0;
        int xcoordplayer1 = 0;
        int ycoordplayer1 = 0;
        int xcoordplayer2 = 0;
        int ycoordplayer2 = 0;
        for (int i = height - 1; i >= 0; i--) {
            int l = 0;
            String k = split[m];
            System.out.println(k);
            for (int x = 0; x < width; x++) {
                if (k.charAt(l) == '1') {
                    board[x][i] = Tileset.WALL;
                } else if (k.charAt(l) == '2') {
                    board[x][i] = Tileset.FLOOR;
                } else if (k.charAt(l) == '3') {
                    board[x][i] = Tileset.AVATAR;
                    xcoordplayer1 = x;
                    ycoordplayer1 = i;
                } else if (k.charAt(l) == '4') {
                    board[x][i] = Tileset.FLOWER;
                    xcoordplayer2 = x;
                    ycoordplayer2 = i;
                } else {
                    board[x][i] = Tileset.NOTHING;
                }
                l++;
            }
            m++;
        }

        World world = new World(board, xcoordplayer1, ycoordplayer1, xcoordplayer2, ycoordplayer2);

        world.renderDisplay();
        return board;
    }

    public static TETile[][] loadWorldnorendering(String filename, String input) {
        String contents = FileUtils.readFile(filename);

        String[] split = contents.split("\n");
        String widthandheight = split[0];
        String[] separatewidthheight = widthandheight.split(" ");
        int width = Integer.valueOf(separatewidthheight[0]);
        int height = Integer.valueOf(separatewidthheight[1]);
        TETile[][] board = new TETile[width][height];

        String seed = split[1];
        System.out.println(seed);

        String rest = input.substring(1);
        System.out.println(rest);
        split = Arrays.copyOfRange(split, 2, split.length);
        int m = 0;
        int xcoordplayer1 = 0;
        int ycoordplayer1 = 0;
        int xcoordplayer2 = 0;
        int ycoordplayer2 = 0;
        for (int i = height - 1; i >= 0; i--) {
            int l = 0;
            String k = split[m];
            System.out.println(k);
            for (int x = 0; x < width; x++) {
                if (k.charAt(l) == '1') {
                    board[x][i] = Tileset.WALL;
                } else if (k.charAt(l) == '2') {
                    board[x][i] = Tileset.FLOOR;
                } else if (k.charAt(l) == '3') {
                    board[x][i] = Tileset.AVATAR;
                    xcoordplayer1 = x;
                    ycoordplayer1 = i;
                } else if (k.charAt(l) == '4') {
                    board[x][i] = Tileset.FLOWER;
                    xcoordplayer2 = x;
                    ycoordplayer2 = i;
                } else {
                    board[x][i] = Tileset.NOTHING;
                }
                l++;
            }
            m++;
        }
        System.out.println("its working");
        World world = new World(board, xcoordplayer1, ycoordplayer1, xcoordplayer2, ycoordplayer2, rest);
        return board;
    }
}
