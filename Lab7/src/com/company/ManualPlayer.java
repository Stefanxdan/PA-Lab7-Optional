package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ManualPlayer extends Player {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public ManualPlayer(String name) {
        super(name);
    }

    private int IndexOf(int value) {
        for (int i = 0; i < board.tokenList.size(); i++)
            if (board.tokenList.get(i).getNumber() == value)
                return i;
        return -1;
    }

    private boolean IsNumber(String string) {
        for (int i = 0; i < string.length(); i++)
            if (string.charAt(i) < '0' || string.charAt(i) > '9')
                return false;
        return true;
    }

    @Override
    protected int GetIndex() throws IOException {
        System.out.println(board);
        for (Player player : game.playerList)
            System.out.println(player);

        while (true) {
            System.out.println("Insert value: ");
            String data = reader.readLine();
            while (!IsNumber(data)) {
                data = reader.readLine();
            }
            if (IndexOf(Integer.parseInt(data)) != -1)
                return IndexOf(Integer.parseInt(data));

        }
    }
}
