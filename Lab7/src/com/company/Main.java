package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        List<Player> playerList = new ArrayList<>();
        Player player1 = new RandomPlayer("player1");
        Player player2 = new RandomPlayer("player2");
        Player player3 = new RandomPlayer("player3");
        Player playerSmart1 = new SmartPlayer("pSmart1");
        Player playerSmart2 = new SmartPlayer("pSmart2");
        Player playerManual1 = new ManualPlayer("Alin");
        playerList.add(playerSmart1);
        playerList.add(player1);
        playerList.add(playerSmart2);
        playerList.add(player2);


        // Demonstratie playerSmart:
        /*

        while (true) {
            Game game = new Game(200, 10, playerList,1);
            // Game game = new Game(400, 50, playerList,1);  // demonstratie TimeKeeper
            game.Simulate();
            TimeUnit.SECONDS.sleep(1);
        }
         */
        // Demonstratie playerManual:
        playerList.add(playerManual1);
        while (true) {
            // pentru a juca manual am facut o exceptie in TimeKeeper pentru a afisa timpul ce trece pt timeLimit 600
            Game game = new Game(200, 10, playerList,600);
            game.Simulate();
            TimeUnit.SECONDS.sleep(1);
        }


    }
}
