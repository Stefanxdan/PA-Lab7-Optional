package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class Player implements Runnable {
    String name;
    int score;

    Board board;
    int order;
    Game game;
    TimeKeeper timeKeeper;
    Set<Token> tokensSet = new TreeSet<>();
    int lapLength = 0;

    public Player(String name) {
        this.name = name;
    }

    public void Set(int order, Game game, TimeKeeper timeKeeper) {
        this.board = game.board;
        this.order = order;
        this.game = game;
        this.timeKeeper = timeKeeper;
        tokensSet.clear();
        lapLength = 0;
    }

    protected abstract int GetIndex() throws IOException;

    @Override
    public void run() {
        synchronized (this) {
            while (!game.IsEnd() && timeKeeper.isAlive()) { /// cat timp nu s a terminat jocul si nu a expirat timpul:
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (game.turn != order) { // astept cat timp nu e timpul meu
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (game.IsEnd())
                        return;
                }
                ///dupe ce am asteptat verific daca nu cumva s a terminat jocul intre timp si pun o piesa
                // GetIndex difera de la tipul de player: random, smart...
                if (!game.IsEnd()) {
                    try {
                        this.tokensSet.add(board.Extract(this.GetIndex()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lapLength = game.LLAP(this.tokensSet);
                    //System.out.println(this.name + this.tokensSet);
                    game.turn = (order + 1) % game.nrPlayers;
                } else // daca s a terminat jocul ma opresc
                    return;
            }
        }
    }

    @Override
    public String toString() {
        return "Player:" + name + '\'' +
                ", score=" + score +
                ", tokensSet=" + tokensSet;
    }
}
