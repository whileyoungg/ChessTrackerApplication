package io.Main;

import io.dataretreive.Dataretreiver;

public class Main {
    public static void main(String[] args) {
        Dataretreiver receiver = new Dataretreiver();
        System.out.println(receiver.getPlayer("ocevn").toString());
    }
}
