package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Game {
    int n, k;
    int turn;
    int timeLimit;
    int nrPlayers;
    Board board;
    TimeKeeper timeKeeper;

    List<Player> playerList;
    List<Thread> threadList = new ArrayList<>();


    public Game(int n, int k, List<Player> playerList, int timeLimit) {
        this.n = n;
        this.k = k;
        this.timeLimit = timeLimit;

        board = new Board(n);
        timeKeeper = new TimeKeeper("Tk", timeLimit);
        this.playerList = playerList;
        nrPlayers = playerList.size();
        turn = 0;
        for (Player player : this.playerList) {
            player.Set(turn, this, timeKeeper);
            turn++;
        }
        turn = 0;
        System.out.println("\n------------New " + this);
    }

    // afiseaza cea mai lunga progresie aritmetica
    void DisplayLAP(Player player) {
        int n = player.tokensSet.size();
        int[][] L = new int[n][n];
        Token[] set = player.tokensSet.toArray(new Token[0]);

        int llap = 2;
        int llapi = 0, diff = 1;

        for (int i = 0; i < n; i++)
            L[i][n - 1] = 2;

        for (int j = n - 2; j >= 1; j--) {
            int i = j - 1, k = j + 1;
            while (i >= 0 && k <= n - 1) {
                if (set[i].getNumber() + set[k].getNumber() < 2 * set[j].getNumber())
                    k++;
                else if (set[i].getNumber() + set[k].getNumber() > 2 * set[j].getNumber()) {
                    L[i][j] = 2;
                    i--;

                } else {
                    L[i][j] = L[j][k] + 1;
                    if (llap < L[i][j]) {
                        llap = L[i][j];
                        diff = set[j].getNumber() - set[i].getNumber();
                        llapi = i;
                    }
                    i--;
                    k++;
                }
            }
            while (i >= 0) {
                L[i][j] = 2;
                i--;
            }
        }
        System.out.print("LLAP " + player.name + " AP(" + llap + "): ");
        int number = set[llapi].getNumber();
        for (int i = 0; i < llap; i++) {
            System.out.print(number + " ");
            number += diff;
        }
        System.out.println();
    }

    // returneaza lungimea celei mai lungi progrese aritmtice ( LLAP Length of the Longest Arithmetic Progression)
    synchronized public int LLAP(Set<Token> tokensPlayer) {
        int n = tokensPlayer.size();
        int[][] L = new int[n][n];
        Token[] set = tokensPlayer.toArray(new Token[0]);

        int llap = 2;

        for (int i = 0; i < n; i++)
            L[i][n - 1] = 2;

        for (int j = n - 2; j >= 1; j--) {
            int i = j - 1, k = j + 1;
            while (i >= 0 && k <= n - 1) {
                if (set[i].getNumber() + set[k].getNumber() < 2 * set[j].getNumber())
                    k++;
                else if (set[i].getNumber() + set[k].getNumber() > 2 * set[j].getNumber()) {
                    L[i][j] = 2;
                    i--;

                } else {
                    L[i][j] = L[j][k] + 1;
                    if (llap < L[i][j]) {
                        llap = L[i][j];
                    }
                    i--;
                    k++;
                }
            }
            while (i >= 0) {
                L[i][j] = 2;
                i--;
            }
        }
        return llap;
    }

    // verifia daca s-au terminat numerele de tabla sau daca s-a castigat jocul
    public boolean IsEnd() {
        if (board.EmptyList())
            return true;

        // verific daca a castigat doar jucatorul din tura trecuta ( deoarece apelez aceasta functie la tura fiecarui jucator)
        return playerList.get((turn + nrPlayers - 1) % nrPlayers).lapLength >= k;
    }

    // se ocupa de afisarile de la sf jocului
    private void EndGame() {
        System.out.println("##### End Game: ######");
        System.out.println("Board: " + board);

        int maxLLAP = 0;
        for (Player player : playerList) {
            System.out.println(player.name + " " + player.lapLength);
            maxLLAP = Math.max(maxLLAP, player.lapLength);
        }

        if (maxLLAP >= k)
            for (Player player : playerList) {
                if (player.lapLength >= k) {
                    System.out.print("Winner:");
                    DisplayLAP(player);
                    player.score += n;
                }
                System.out.println(player);
            }
        if (maxLLAP < k) {
            System.out.println("No Winner");
            for (Player player : playerList) {
                player.score += LLAP(player.tokensSet);
                System.out.println(player);
            }
        }
    }

    public void Simulate() throws InterruptedException {

        System.out.println(Thread.currentThread().getName() + ": simulating...");
        timeKeeper.start();

        for (Player player : playerList)
            threadList.add(new Thread(player, "Thread_" + player.name));
        for (Thread thread : threadList)
            thread.start();

        System.out.println(Thread.currentThread().getName() + ": ...wainting");
        // astept terminare joclui prin terminare threadurilor
        for (Thread thread : threadList)
            thread.join();

        if (!timeKeeper.isAlive())
            System.out.println(Thread.currentThread().getName() + ": Joc Oprit");
        timeKeeper.running = false;
        //
        EndGame();


    }

    @Override
    public String toString() {
        String fs;
        fs = "Game{ n=" + n + ", ";
        for (Player player : playerList)
            fs = fs + player.name + ", ";
        fs = String.format(fs + "Time limit=%d }", timeLimit);
        return fs;
    }
}
