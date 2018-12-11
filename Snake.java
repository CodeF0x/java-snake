package com.codef0x.snake;
import java.util.ArrayList;

public class Snake {
    ArrayList<SnakePart> coordinates;
    int dx = 10;
    int dy = 0;

    public Snake(ArrayList<SnakePart> coords) {
        this.coordinates = coords;
    }

    public void move() {
        SnakePart head = new SnakePart(this.coordinates.get(0).x + this.dx, this.coordinates.get(0).y + this.dy);

        this.coordinates.add(0, head);
        this.coordinates.remove(this.coordinates.size() - 1);
    }

    public void grow() {
        SnakePart newPart = new SnakePart(0, 0);
        newPart.x = this.coordinates.get(this.coordinates.size() - 1).x - 10;
        newPart.y = this.coordinates.get(this.coordinates.size() - 1).y;

        this.coordinates.add(newPart);
    }
}
