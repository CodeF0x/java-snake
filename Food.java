package com.codef0x.snake;

public class Food {
    int foodX;
    int foodY;

    public void spawn(int maxHeight, int maxWidth) {
        this.foodX = (int) Math.ceil(Math.random() * maxWidth / 10) * 10;
        this.foodY = (int) Math.ceil(Math.random() * maxHeight / 10) * 10;
    }
}
