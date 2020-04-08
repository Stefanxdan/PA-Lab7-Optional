package com.company;

import java.util.ArrayList;
import java.util.List;

public class SmartPlayer extends Player {

    int range;
    int lapStartValue;
    int lapDif;
    int lapLocalLength;

    List<Token> myTokens;

    public SmartPlayer(String name) {
        super(name);
    }


    public void Set(int order, Game game, TimeKeeper timeKeeper) {
        this.board = game.board;
        this.order = order;
        this.game = game;
        this.timeKeeper = timeKeeper;
        range = game.n / game.k;
        tokensSet.clear();
    }

    private int IndexOf(int value) {
        for (int i = 0; i < board.tokenList.size(); i++)
            if (board.tokenList.get(i).getNumber() == value)
                return i;
        return -1;
    }

    private int LengthLAP(int dif) {
        int valueSearch = myTokens.get(0).getNumber() + dif;
        int length = 1;
        for (Token token : myTokens)
            if (token.getNumber() == valueSearch) {
                valueSearch += dif;
                length++;
            }
        return length;
    }

    protected int GetIndex() {

        int value;
        myTokens = new ArrayList<>(tokensSet);
        // primele 2 piese
        if (myTokens.size() == 0)
            return (int) (Math.random() * board.tokenList.size());
        if (myTokens.size() == 1) {
            lapStartValue = myTokens.get(0).getNumber();
            for (int i = 1; i <= range; i++) {
                value = lapStartValue + i;
                if (IndexOf(value) != -1) {
                    //System.out.println(name + ": extend second item, dif: " + i);
                    return IndexOf(value);
                }
                value = lapStartValue - i;
                if (IndexOf(value) != -1) {
                    //System.out.println(name + ": extend second item, dif: -" + i);
                    return IndexOf(value);
                }
            }
            return (int) (Math.random() * board.tokenList.size());

        }
        if (myTokens.size() == 2) {
            lapDif = myTokens.get(1).getNumber() - lapStartValue;
            lapLocalLength = 2;
        }

        // restul pieselor
        while (lapLocalLength >= 2) {
            // add backLLAP
            value = lapStartValue - lapDif;
            if (IndexOf(value) != -1) {
                //System.out.println(name + ": extend front" + myTokens);
                lapStartValue = value;
                lapLocalLength++;
                return IndexOf(value);
            }
            // add frontLLAP
            value = lapStartValue + lapDif * lapLocalLength;
            if (IndexOf(value) != -1) {
                //System.out.println(name + ": extend back" + myTokens);
                lapStartValue = value;
                lapLocalLength++;
                return IndexOf(value);
            }

            lapDif++;
            lapLocalLength = LengthLAP(lapDif);
            //System.out.println(name + ": Aleg alt LAP:" + lapStartValue + ",dif:" + lapDif + ",length:" + lapLocalLength);
        }
        //System.out.println(name + ": Aleg random");
        return (int) (Math.random() * board.tokenList.size());
    }


}