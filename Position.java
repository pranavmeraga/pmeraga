package core;

public class Position {
    //gets the position of the box
    //saves the xy coordinate of the top left corner and the xy coordinate of the bottom right corner
    private int xcoordtopleft;
    private int ycoordtopleft;
    private int xcoordbottomright;
    private int ycoordbottomright;
    private int width;
    private int height;
    public Position(int xtopleft, int ytopleft, int xbottomright, int ybottomright, int w, int h) {
        xcoordtopleft = xtopleft;
        ycoordtopleft = ytopleft;
        xcoordbottomright = xbottomright;
        ycoordbottomright = ybottomright;
        width = w;
        height = h;
    }

    public int getXcoordtopleft() {
        return xcoordtopleft;
    }
    public int getYcoordtopleft() {
        return ycoordtopleft; }
    public int getXcoordbottomright() {
        return xcoordbottomright; }
    public int getYcoordbottomright() {
        return ycoordbottomright; }
    public int getWidth() {
        return width; }
    public int getHeight() {
        return height; }
}
