package core;

import edu.princeton.cs.algs4.StdDraw;
//import org.checkerframework.checker.units.qual.A;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
//import edu.princeton.cs.algs4.StdDraw;
import utils.FileUtils;

import java.awt.*;
//import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.min;

public class World {

    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HEIGHT = 50;

    private TETile[][] currentState;

    private final Random random;

    private int width;
    private int height;

    private final TERenderer ter = new TERenderer();

    private Avatar a;

    private Avatar b;

    private String seedforsaving;

    //keeps track of the list of rooms as they are generated
    private ArrayList<Position> listofrooms = new ArrayList<>();

    public World(String seed) {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        seedforsaving = seed;
        random = new Random(parseSeed(seedforsaving));
        ter.initialize(width, height);
        TETile[][] newWorld = new TETile[width][height];
        currentState = newWorld;
        fillwithnothing(currentState);
        createrandomnumberofrooms();
        createHallways();
        startPoint();
        while (true) {
            userKeyInputPlayer();
            renderBoard(currentState);
            renderDisplay();
        }

    }
    public World(String seed, boolean test) {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;

        seedforsaving = seed;
        random = new Random(parseSeed(seedforsaving));
        TETile[][] newWorld = new TETile[width][height];
        currentState = newWorld;
        fillwithnothing(currentState);

        createrandomnumberofrooms();
        createHallways();
        startPoint();

    }

    public World(String seed, String rest) {

        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;

        seedforsaving = seed;
        random = new Random(parseSeed(seedforsaving));
        TETile[][] newWorld = new TETile[width][height];
        currentState = newWorld;

        fillwithnothing(currentState);

        createrandomnumberofrooms();

        createHallways();

        startPoint();
        System.out.println(rest);
        //@source: https://www.geeksforgeeks.org/java-string-tochararray-example/
        for (int i = 0; i < rest.length(); i++) {
            char c = rest.charAt(i);
            if (c == 'l') {
                System.out.println("accessed l");
                currentState = Main.loadWorld("save.txt");
            }
            if (c == 'a' && canMove(returnAvatarA(), "LEFT")) {
                movement1(returnCurrentState(), returnAvatarA(), -1, 0);
            } else if (c == 'w' && canMove(returnAvatarA(), "UP")) {
                movement1(returnCurrentState(), returnAvatarA(), 0, 1);
            } else if (c == 's' && canMove(returnAvatarA(), "DOWN")) {
                movement1(returnCurrentState(), returnAvatarA(), 0, -1);
            } else if (c == 'd' && canMove(returnAvatarA(), "RIGHT")) {
                movement1(returnCurrentState(), returnAvatarA(), 1, 0);
            } else if (c == ':' && i + 1 < rest.length() && rest.charAt(i + 1) == 'q') {
                saveWorld(currentState, seedforsaving);
            } else if (c == ':' && i + 1 < rest.length() && rest.charAt(i + 1) == 'Q') {
                saveWorld(currentState, seedforsaving);
            }
        }
    }


    public World(TETile[][] tiles, int xcoordplayer1, int ycoordplayer1, int xcoordplayer2, int ycoordplayer2) {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        ter.initialize(width, height);
        currentState = tiles;
        random = null;

        spawnPiece1(currentState, xcoordplayer1, ycoordplayer1);
        spawnPiece2(currentState, xcoordplayer2, ycoordplayer2);
        while (true) {
            userKeyInputPlayer();
            renderBoard(currentState);
            renderDisplay();
        }


    }

    public World(TETile[][] tiles, int xcoordplayer1, int ycoordplayer1,
                 int xcoordplayer2, int ycoordplayer2, String rest) {
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        random = null;
        System.out.println("here is the rest: " + rest);
        currentState = tiles;
        spawnPiece1(currentState, xcoordplayer1, ycoordplayer1);
        spawnPiece2(currentState, xcoordplayer2, ycoordplayer2);
        //@source: https://www.geeksforgeeks.org/java-string-tochararray-example/
        for (int i = 0; i < rest.length(); i++) {
            char c = rest.charAt(i);
            if (c == 'l') {
                currentState = Main.loadWorld("save.txt");
            }
            if (c == 'a' && canMove(returnAvatarA(), "LEFT")) {
                movement1(returnCurrentState(), returnAvatarA(), -1, 0);
            } else if (c == 'w' && canMove(returnAvatarA(), "UP")) {
                movement1(returnCurrentState(), returnAvatarA(), 0, 1);
            } else if (c == 's' && canMove(returnAvatarA(), "DOWN")) {
                System.out.println("accessed");
                System.out.println(getString());
                movement1(returnCurrentState(), returnAvatarA(), 0, -1);
            } else if (c == 'd' && canMove(returnAvatarA(), "RIGHT")) {
                movement1(returnCurrentState(), returnAvatarA(), 1, 0);
            } else if (c == ':' && i + 1 < rest.length() && rest.charAt(i + 1) == 'q') {
                System.out.println("ready to be sved");
                saveWorld(currentState, seedforsaving);
            }
        }

    }

    public long parseSeed(String seed) {
        //takes inputted seed string with the N and the S and removes it (slicing strings in java)
        //@source: https://www.geeksforgeeks.org/substring-in-java/
        String newstring = seed.substring(1, seed.length() - 2);
        //@source: https://www.theserverside.com/blog/Coffee-Talk-Java-News-Stories-and-Opinions/String-to-long-in-Java
        //converts string to long
        return Long.parseLong(newstring);

    }

    public TETile[][] getString() {
        TETile[][] secondWorld = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int x = 0; x < height; x++) {
                secondWorld[i][x] = currentState[i][x];
            }
        }
        return secondWorld;
    }


    public void fillwithnothing(TETile[][] tiles) {
        //we need to initialize the world with nothing tiles
        //basically same as lab 9
        int h = tiles[0].length;
        int w = tiles.length;

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }

    }

    public Position makeARoom(int w, int h, int topleftx, int toplefty, TETile[][] tiles) {
        //width of room defined as number of # across and height of room defined as number of # above

        //sets width and height to whatever was passed in
        //if it goes past the default width and height (50)
        // then dimension becomes the maximum it can be without going over
        // need to subtract by 1 for some reason, don't know why
        w = min(w - 1, DEFAULT_WIDTH - topleftx - 1);
        h = min(h - 1, toplefty);

        int startxcoordtopleft = topleftx;
        int startycoordtopleft = toplefty;


        //calculates the x,y coordinate for the bottom right part of the box using width and height
        int startxcoordbottomright = startxcoordtopleft + w;
        int startycoordbottomright = startycoordtopleft - h;

        for (int i = startxcoordtopleft; i <= startxcoordbottomright; i++) {
            //creates horizontal sides
            tiles[i][startycoordtopleft] = Tileset.WALL;
            tiles[i][startycoordbottomright] = Tileset.WALL;
        }

        for (int j = startycoordtopleft; j > startycoordbottomright; j--) {
            //creates vertical sides
            tiles[startxcoordtopleft][j] = Tileset.WALL;
            tiles[startxcoordbottomright][j] = Tileset.WALL;
        }

        for (int i = startxcoordtopleft + 1; i < startxcoordbottomright; i++) {
            for (int j = startycoordtopleft - 1; j > startycoordbottomright; j--) {
                tiles[i][j] = Tileset.FLOOR;
            }
        }

        Position positionOfRoom = new Position(startxcoordtopleft, startycoordtopleft,
                startxcoordbottomright, startycoordbottomright, w + 1, h + 1);


        return positionOfRoom;

    }

    public void makeARoom(Position p, TETile[][] tiles) {

        //same as previous function but takes in Position object
        //method is needed to fix rooms that broke because of two overlapping rooms
        for (int i = p.getXcoordtopleft(); i <= p.getXcoordbottomright(); i++) {
            //creates horizontal sides
            tiles[i][p.getYcoordtopleft()] = Tileset.WALL;
            tiles[i][p.getYcoordbottomright()] = Tileset.WALL;
        }

        for (int j = p.getYcoordtopleft(); j > p.getYcoordbottomright(); j--) {
            //creates vertical sides
            tiles[p.getXcoordtopleft()][j] = Tileset.WALL;
            tiles[p.getXcoordbottomright()][j] = Tileset.WALL;
        }

        for (int i = p.getXcoordtopleft() + 1; i < p.getXcoordbottomright(); i++) {
            for (int j = p.getYcoordtopleft() - 1; j > p.getYcoordbottomright(); j--) {
                tiles[i][j] = Tileset.FLOOR;
            }
        }
    }

    public void createrandomnumberofrooms() {

        //random number of rooms based on seed
        int numofrooms = random.nextInt(13, 15);
        Position prevRoom = null;
        Position firstRoom = null;

        for (int i = 0; i < numofrooms; i++) {

            boolean overlaps = false;

            //random width and height based on seed
            int h = random.nextInt(5, 20);
            int w = random.nextInt(5, 20);

            //makes a room
            Position currRoom = makeARoom(w, h, random.nextInt(49), random.nextInt(49), currentState);

            //iterates through existing list of rooms to check for overlaps
            for (int j = 0; j < listofrooms.size(); j++) {

                //sometimes a room can overlap with more than one room
                //goes through the remaining rooms in the list and fixes the ones that broke following overlap
                if (overlappingRooms(currRoom, listofrooms.get(j)) && overlaps) {
                    makeARoom(listofrooms.get(j), currentState);
                } else if (overlappingRooms(currRoom, listofrooms.get(j))) {
                    //checks if room overlaps with another room in a list

                    //removes the room that was created
                    removeARoom(currRoom, currentState);

                    //when you remove a room, you create holes in the room it was overlapping with
                    //this fixes it
                    makeARoom(listofrooms.get(j), currentState);


                    overlaps = true;

                    //since we removed the room that was just created we need to increase numofrooms
                    //by one so that the method creates an extra room
                    numofrooms++;
                }
            }

            //if we know the room overlaps, we dont need to check if its too small
            // so the loop skips to the next iteration
            if (overlaps) {
                continue;
            }

            //because we are editing the width and height to make sure it doesnt go past default width and height,
            // the dimensions can sometimes be too small to be considered rooms
            //if its too small then we remove the room, increase numofrooms by one for the same reason as above
            if (currRoom.getWidth() < 4 || currRoom.getHeight() < 4) {
                removeARoom(currRoom, currentState);
                numofrooms++;
                //prevents this removed room from being added to the list
                continue;
            }
            listofrooms.add(currRoom);
        }
    }

    public TETile[][] returnCurrentState() {
        return currentState;
    }


    //checks if one room is overlapping with another
    //true = there are overlapping rooms
    public boolean overlappingRooms(Position posOfRoom1, Position posOfRoom2) {
        //we need to fix this for style checker
        //considers every single possibility to make sure two rooms dont overlap
        //even makes sure that two rooms aren't touching
        if (posOfRoom1.getXcoordtopleft() <= posOfRoom2.getXcoordbottomright() + 1
                && posOfRoom1.getXcoordtopleft() >= posOfRoom2.getXcoordtopleft() - 1
                && posOfRoom1.getYcoordtopleft() <= posOfRoom2.getYcoordtopleft() + 1
                && posOfRoom1.getYcoordtopleft() >= posOfRoom2.getYcoordbottomright() - 1) {
            return true;
        } else if (posOfRoom1.getXcoordbottomright() <= posOfRoom2.getXcoordbottomright() + 1
                && posOfRoom1.getXcoordbottomright() >= posOfRoom2.getXcoordtopleft() - 1
                && posOfRoom1.getYcoordbottomright() <= posOfRoom2.getYcoordtopleft() + 1
                && posOfRoom1.getYcoordbottomright() >= posOfRoom2.getYcoordbottomright() - 1) {
            return true;
        } else if (posOfRoom1.getXcoordtopleft() <= posOfRoom2.getXcoordbottomright() + 1
                && posOfRoom1.getXcoordtopleft() >= posOfRoom2.getXcoordtopleft() - 1
                && posOfRoom1.getYcoordbottomright() <= posOfRoom2.getYcoordtopleft() + 1
                && posOfRoom1.getYcoordbottomright() >= posOfRoom2.getYcoordbottomright() - 1) {
            return true;
        } else if (posOfRoom1.getXcoordbottomright() <= posOfRoom2.getXcoordbottomright() + 1
                && posOfRoom1.getXcoordbottomright() >= posOfRoom2.getXcoordtopleft() - 1
                && posOfRoom1.getYcoordtopleft() <= posOfRoom2.getYcoordtopleft() + 1
                && posOfRoom1.getYcoordtopleft() >= posOfRoom2.getYcoordbottomright() - 1) {
            return true;
        } else if (posOfRoom2.getXcoordtopleft() <= posOfRoom1.getXcoordbottomright() + 1
                && posOfRoom2.getXcoordtopleft() >= posOfRoom1.getXcoordtopleft() - 1
                && posOfRoom2.getYcoordtopleft() <= posOfRoom1.getYcoordtopleft() + 1
                && posOfRoom2.getYcoordtopleft() >= posOfRoom1.getYcoordbottomright() - 1) {
            return true;
        } else if (posOfRoom2.getXcoordbottomright() <= posOfRoom1.getXcoordbottomright() + 1
                && posOfRoom2.getXcoordbottomright() >= posOfRoom1.getXcoordtopleft() - 1
                && posOfRoom2.getYcoordbottomright() <= posOfRoom1.getYcoordtopleft() + 1
                && posOfRoom2.getYcoordbottomright() >= posOfRoom1.getYcoordbottomright() - 1) {
            return true;
        } else if (posOfRoom2.getXcoordtopleft() <= posOfRoom1.getXcoordbottomright() + 1
                && posOfRoom2.getXcoordtopleft() >= posOfRoom1.getXcoordtopleft() - 1
                && posOfRoom2.getYcoordbottomright() <= posOfRoom1.getYcoordtopleft() + 1
                && posOfRoom2.getYcoordbottomright() >= posOfRoom1.getYcoordbottomright() - 1) {
            return true;
        } else if (posOfRoom2.getXcoordbottomright() <= posOfRoom1.getXcoordbottomright() + 1
                && posOfRoom2.getXcoordbottomright() >= posOfRoom1.getXcoordtopleft() - 1
                && posOfRoom2.getYcoordtopleft() <= posOfRoom1.getYcoordtopleft() + 1
                && posOfRoom2.getYcoordtopleft() >= posOfRoom1.getYcoordbottomright() - 1) {
            return true;
        } else if (posOfRoom2.getXcoordbottomright() <= posOfRoom1.getXcoordbottomright() + 1
                && posOfRoom2.getXcoordbottomright() >= posOfRoom1.getXcoordtopleft() - 1
                && posOfRoom1.getYcoordtopleft() <= posOfRoom2.getYcoordtopleft() + 1
                && posOfRoom1.getYcoordtopleft() >= posOfRoom2.getYcoordbottomright() - 1) {
            return true;
        } else if (posOfRoom1.getXcoordbottomright() <= posOfRoom2.getXcoordbottomright() + 1
                && posOfRoom1.getXcoordbottomright() >= posOfRoom2.getXcoordtopleft() - 1
                && posOfRoom2.getYcoordtopleft() <= posOfRoom1.getYcoordtopleft() + 1
                && posOfRoom2.getYcoordtopleft() >= posOfRoom1.getYcoordbottomright() - 1) {
            return true;
        }
        return false;

    }


    public void removeARoom(Position currRoom, TETile[][] tiles) {
        //same code as makeARoom but sets tiles to nothing
        for (int i = currRoom.getXcoordtopleft(); i <= currRoom.getXcoordbottomright(); i++) {
            //creates horizontal sides
            tiles[i][currRoom.getYcoordtopleft()] = Tileset.NOTHING;
            tiles[i][currRoom.getYcoordbottomright()] = Tileset.NOTHING;
        }

        for (int j = currRoom.getYcoordtopleft(); j > currRoom.getYcoordbottomright(); j--) {
            //creates vertical sides
            tiles[currRoom.getXcoordtopleft()][j] = Tileset.NOTHING;
            tiles[currRoom.getXcoordbottomright()][j] = Tileset.NOTHING;
        }

        for (int i = currRoom.getXcoordtopleft() + 1; i < currRoom.getXcoordbottomright(); i++) {
            for (int j = currRoom.getYcoordtopleft() - 1; j > currRoom.getYcoordbottomright(); j--) {
                tiles[i][j] = Tileset.NOTHING;
            }
        }
    }

    public void createHallways() {
        Position prev = null;
        Position first = null;
        for (Position room : listofrooms) {
            if (prev != null) {
                connectToHallway(prev, room);
            } else {
                first = room;
            }
            prev = room;
        }
        if (prev != null && first != null) {
            connectToHallway(prev, first);
        }
    }

    public void connectToHallway(Position room, Position room2) {

        // Calculate the midpoint coordinates of each room
        int xMidRoom1 = room.getXcoordtopleft() + room.getWidth() / 2;
        int yMidRoom1 = room.getYcoordtopleft() - room.getHeight() / 2;
        int xMidRoom2 = room2.getXcoordtopleft() + room2.getWidth() / 2;
        int yMidRoom2 = room2.getYcoordtopleft() - room2.getHeight() / 2;

        // Initialize variables for current position and direction
        int x = xMidRoom1;
        int y = yMidRoom1;
        int x3 = x;
        int y3 = y;
        // Connect horizontally to the midpoint of the second room
        while (x != xMidRoom2) {
            currentState[x][y] = Tileset.FLOOR;
            xHelper(x, y);
            if (x < xMidRoom2) {
                x++;
            } else {
                x--;
            }
            checkEnd(x, y);
        }
        checkEnd(x, y);

        // Connect vertically to the midpoint of the second room
        while (y != yMidRoom2) {
            currentState[x][y] = Tileset.FLOOR;
            yHelper(x, y);
            if (y < yMidRoom2) {
                y++;
            } else {
                y--;
            }
            checkEnd(x, y);
        }
        checkEnd(x, y);
        if (x > 0 && y > 0 && y < currentState[0].length - 1 && x < currentState.length - 1) {
            currentState[x][y] = Tileset.FLOOR;
            checkEnd(x, y);
        } else {
            currentState[x][y] = Tileset.WALL;
        }
        checkEnd(x3, y3);
    }

    public void checkEnd(int x, int y) {
        for (int l = -1; l <= 1; l++) {
            for (int r = -1; r <= 1; r++) {
                if (currentState[x + l][y + r] == Tileset.NOTHING) {
                    currentState[x + l][y + r] = Tileset.WALL;
                }
            }
        }
    }

    public void xHelper(int x, int y) {
        if (y > 0 && currentState[x][y - 1] == Tileset.NOTHING) {
            currentState[x][y - 1] = Tileset.WALL;
        }
        if (currentState[x][0] == Tileset.FLOOR) {
            currentState[x][0] = Tileset.WALL;
        }
        if (y < currentState[0].length - 1 && currentState[x][y + 1] == Tileset.NOTHING) {
            currentState[x][y + 1] = Tileset.WALL;
        }
        if (currentState[x][currentState[0].length - 1] == Tileset.FLOOR) {
            currentState[x][currentState[0].length - 1] = Tileset.WALL;
        }
    }

    public void yHelper(int x, int y) {
        if (x > 0 && currentState[x - 1][y] == Tileset.NOTHING) {
            currentState[x - 1][y] = Tileset.WALL;
        }
        if (currentState[0][y] == Tileset.FLOOR) {
            currentState[0][y] = Tileset.WALL;
        }
        if (x < currentState.length - 1 && currentState[x + 1][y] == Tileset.NOTHING) {
            currentState[x + 1][y] = Tileset.WALL;
        }
        if (currentState[currentState.length - 1][y] == Tileset.FLOOR) {
            currentState[currentState.length - 1][y] = Tileset.WALL;
        }
    }


    //3B:

    public void renderDisplay() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (x >= 0 && y >= 0 && y < height && x < width) {
            String name = currentState[x][y].description();
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.text(2, height - 1, name);
            StdDraw.show();
        }
    }

    public Avatar spawnPiece1(TETile[][] tiles, int xcoord, int ycoord) {
        Avatar x = new Avatar(xcoord, ycoord);
        tiles[xcoord][ycoord] = Tileset.AVATAR;
        this.a = x;
        return x;

    }


    public Avatar spawnPiece2(TETile[][] tiles, int xcoord, int ycoord) {
        Avatar y = new Avatar(xcoord, ycoord);
        tiles[xcoord][ycoord] = Tileset.FLOWER;
        this.b = y;
        return y;

    }


    public void startPoint() {
        int randomroom1 = random.nextInt(listofrooms.size());
        int randomroom2 = random.nextInt(listofrooms.size());
        while (randomroom2 == randomroom1) {
            randomroom2 = random.nextInt(listofrooms.size());
        }

        Position room1 = listofrooms.get(randomroom1);
        Position room2 = listofrooms.get(randomroom2);
        spawnPiece1(currentState, room1.getXcoordtopleft() + 1, room1.getYcoordtopleft() - 1);
        spawnPiece2(currentState, room2.getXcoordtopleft() + 1, room2.getYcoordtopleft() - 1);

    }

    public void movement1(TETile[][] terr, Avatar avatar, int deltax, int deltay) {
        TETile description = Tileset.AVATAR;

        //left
        if (deltax < 0) {
            for (int i = Math.abs(deltax); i > 0; i--) {
                terr[avatar.getXcoord() - 1][avatar.getYcoord()] = description;
                terr[avatar.getXcoord()][avatar.getYcoord()] = Tileset.FLOOR;
                avatar.changepos(-1, 0);
            }
        } else if (deltax > 0) { // right
            for (int i = 0; i < deltax; i++) {
                terr[avatar.getXcoord() + 1][avatar.getYcoord()] = description;
                terr[avatar.getXcoord()][avatar.getYcoord()] = Tileset.FLOOR;
                avatar.changepos(1, 0);
            }
        }
        //down
        if (deltay < 0) {
            terr[avatar.getXcoord()][avatar.getYcoord() - 1] = description;
            terr[avatar.getXcoord()][avatar.getYcoord()] = Tileset.FLOOR;
            avatar.changepos(0, -1);

        } else if (deltay > 0) { //up
            terr[avatar.getXcoord()][avatar.getYcoord() + 1] = description;
            terr[avatar.getXcoord()][avatar.getYcoord()] = Tileset.FLOOR;
            avatar.changepos(0, 1);
        }

    }

    public void movement2(TETile[][] terr, Avatar avatar, int deltax, int deltay) {
        TETile description = Tileset.FLOWER;

        //left
        if (deltax < 0) {
            for (int i = Math.abs(deltax); i > 0; i--) {
                terr[avatar.getXcoord() - 1][avatar.getYcoord()] = description;
                terr[avatar.getXcoord()][avatar.getYcoord()] = Tileset.FLOOR;
                avatar.changepos(-1, 0);
            }
        } else if (deltax > 0) { //right
            for (int i = 0; i < deltax; i++) {
                terr[avatar.getXcoord() + 1][avatar.getYcoord()] = description;
                terr[avatar.getXcoord()][avatar.getYcoord()] = Tileset.FLOOR;
                avatar.changepos(1, 0);
            }
        }
        //down
        if (deltay < 0) {
            terr[avatar.getXcoord()][avatar.getYcoord() - 1] = description;
            terr[avatar.getXcoord()][avatar.getYcoord()] = Tileset.FLOOR;
            avatar.changepos(0, -1);

        } else if (deltay > 0) { //up
            terr[avatar.getXcoord()][avatar.getYcoord() + 1] = description;
            terr[avatar.getXcoord()][avatar.getYcoord()] = Tileset.FLOOR;
            avatar.changepos(0, 1);
        }


    }


    public boolean canMove(Avatar at, String direction) {
        if (direction.equals("LEFT")) {
            if (currentState[at.getXcoord() - 1][at.getYcoord()] == Tileset.FLOOR
                    || currentState[at.getXcoord() - 1][at.getYcoord()] == Tileset.NOTHING) {
                return true;
            }
            return false;
        } else if (direction.equals("RIGHT")) {
            if (currentState[at.getXcoord() + 1][at.getYcoord()] == Tileset.FLOOR
                    || currentState[at.getXcoord() + 1][at.getYcoord()] == Tileset.NOTHING) {
                return true;
            }
            return false;
        } else if (direction.equals("UP")) {
            if (currentState[at.getXcoord()][at.getYcoord() + 1] == Tileset.FLOOR
                    || currentState[at.getXcoord()][at.getYcoord() + 1] == Tileset.NOTHING) {
                return true;
            }
            return false;
        } else { //down
            if (currentState[at.getXcoord()][at.getYcoord() - 1] == Tileset.FLOOR
                    || currentState[at.getXcoord()][at.getYcoord() - 1] == Tileset.NOTHING) {
                return true;
            }
            return false;
        }

    }

    boolean lighton = false;
    boolean colon = false;

    public void userKeyInputPlayer() {
        while (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();
            if (key == 'a' && canMove(a, "LEFT")) {
                if (lighton) {
                    helper(temp, -1, 0);
                } else {
                    movement1(currentState, a, -1, 0);
                }
            } else if (key == 's' && canMove(a, "DOWN")) {
                if (lighton) {
                    helper(temp, 0, -1);
                } else {
                    movement1(currentState, a, 0, -1);
                }
            } else if (key == 'd' && canMove(a, "RIGHT")) {
                if (lighton) {
                    helper(temp, 1, 0);
                } else {
                    movement1(currentState, a, 1, 0);
                }
            } else if (key == 'w' && canMove(a, "UP")) {
                if (lighton) {
                    helper(temp, 0, 1);
                } else {
                    movement1(currentState, a, 0, 1);
                }
            }
            if (key == 'j' && canMove(b, "LEFT")) {
                if (lighton) {
                    currentState = temp;
                    movement2(currentState, b, -1, 0);  //move down in the temporary world
                    lightswitch(b);
                } else {
                    movement2(currentState, b, -1, 0);
                }
            } else if (key == 'k' && canMove(b, "DOWN")) {
                if (lighton) {
                    currentState = temp;
                    movement2(currentState, b, 0, -1);  //move down in the temporary world
                    lightswitch(b);
                } else {
                    movement2(currentState, b, 0, -1);
                }
            } else if (key == 'l' && canMove(b, "RIGHT")) {
                if (lighton) {
                    currentState = temp;
                    movement2(currentState, b, 1, 0);  //move right in the temporary world
                    lightswitch(b);
                } else {
                    movement2(currentState, b, 1, 0);
                }
            } else if (key == 'i' && canMove(b, "UP")) {
                if (lighton) {
                    currentState = temp;
                    movement2(currentState, b, 0, 1);  //move up in the temporary world
                    lightswitch(b);
                } else {
                    movement2(currentState, b, 0, 1);
                }
            }
            if (key == 'p' && lighton) {
                copyArray(temp, currentState);
                lighton = false;
            } else if (key == 'p') {
                lightswitch(a);
                lighton = true;
            }
            if (key == 'q' && colon) {
                saveWorld(currentState, seedforsaving);
                System.exit(0);
            } else if (key == ':') {
                colon = true;
            }
        }
    }

    public void helper(TETile[][] t, int x, int y) {
        currentState = t;
        movement1(currentState, a, x, y);
        lightswitch(a);
    }


    public void renderBoard(TETile[][] board) {
        ter.drawTiles(board);
        StdDraw.show();
    }

    TETile[][] temp = new TETile[DEFAULT_WIDTH][DEFAULT_HEIGHT];

    public void lightswitch(Avatar av) {
        temp = new TETile[DEFAULT_WIDTH][DEFAULT_HEIGHT];
        //if(!lighton){ //if the light has already turned on, we need to
        copyArray(currentState, temp);
        //saves current state to a temporary world...makes sure that the background is saved
        for (int i = 0; i < DEFAULT_WIDTH; i++) {
            for (int j = 0; j < DEFAULT_HEIGHT; j++) {
                if ((i > av.getXcoord() + 2 || i < av.getXcoord() - 2) || (j > av.getYcoord() + 2
                        || j < av.getYcoord() - 2)) {
                    currentState[i][j] = Tileset.NOTHING;
                }
            }

        }


    }

    public void copyArray(TETile[][] currentBoard, TETile[][] copyboard) {
        //@source:
        // https://stackoverflow.com/questions/2068370/efficient-system-arraycopy-on-multidimensional-arrays
        //and lab 10
        //how to copy one array to another
        for (int i = 0; i < currentBoard.length; i++) {
            System.arraycopy(currentBoard[i], 0, copyboard[i], 0, currentBoard[0].length);
        }

    }

    public static void saveWorld(TETile[][] tiles, String seed) {
        int width = tiles[0].length;
        int height = tiles.length;
        saveWorld(width, height, tiles, seed);
    }

    public static void saveWorld(int width, int height, TETile[][] currentState, String seed) {
        String contents = width + " " + height + "\n";
        contents += seed + "\n";
        for (int h = height - 1; h >= 0; h--) {
            for (int w = 0; w < width; w++) {
                if (currentState[w][h] == Tileset.WALL) {
                    contents += "1";
                } else if (currentState[w][h] == Tileset.FLOOR) {
                    contents += "2";
                } else if (currentState[w][h] == Tileset.AVATAR) {
                    contents += "3";
                } else if (currentState[w][h] == Tileset.FLOWER) {
                    contents += "4";
                } else {
                    contents += "0";
                }
            }
            contents += "\n";
        }
        System.out.println("accessed");
        FileUtils.writeFile("save.txt", contents);

    }


    public Avatar returnAvatarA() {
        return a;
    }

    public Avatar returnAvatarB() {
        return b;
    }


}


