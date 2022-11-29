package com.tutorial.crudmongoback.security.entity;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter


public class Throw {
    private int dice1;
    private int dice2;
    private boolean win;

    public Throw() {
        this.dice1 = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        this.dice2 = ThreadLocalRandom.current().nextInt(1, 6 + 1);
        if (dice1 + dice2 == 7) {
            this.win = true;
        } else {
            this.win = false;
        }
    }
}
