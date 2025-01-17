package core;

public class Avatar {

    private int xcoord;
    private int ycoord;


    public Avatar(int x, int y) {
        this.xcoord = x;
        this.ycoord = y;
    }

    public void changepos(int deltax, int deltay) {
        xcoord += deltax;
        ycoord += deltay;
    }

    public int getXcoord() {
        return xcoord;
    }

    public int getYcoord() {
        return ycoord;
    }

}
