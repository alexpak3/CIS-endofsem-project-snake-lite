public class food {
    private int x;
    private int y;
    private int size;
    private char type; // 'f' for regular food, 'p' for power-up, 'b' for big food

    public food(int x, int y, int size, char type) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public char getType() {
        return type;
    }
}
