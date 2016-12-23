
package backgammon;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Created by eld5 on 04/03/16.
 */
public class AISimple {

    private static int [] board;
    private static Boolean whitesTurn = true;
    private static int whiteBornOff = 0;
    private static int redBornOff = 0;
    private static int startPosition;
    private static int endPosition;
    private static Boolean captured = false;
    private static int redBar;
    private static int whiteBar;
    private static int dice1;
    private static int dice2;
    private static int rollsLeftForDouble;

    public static void getFields(){

        board = BackGammon.getBoard();
        whitesTurn = BackGammon.getWhitesTurn();
        whiteBornOff = BackGammon.getWhiteBornOff();
        redBornOff = BackGammon.getRedBornOff();
        startPosition = BackGammon.getStartPosition();
        endPosition = BackGammon.getEndPosition();
        redBar = BackGammon.getRedBar();
        whiteBar = BackGammon.getWhiteBar();
        dice1 = BackGammon.getDice1();
        dice2 = BackGammon.getDice2();
        rollsLeftForDouble = BackGammon.getRollsLeftForDouble();
    }
    public static void setFields(){

        BackGammon.setBoard(board);
        BackGammon.setWhitesTurn(whitesTurn);
        BackGammon.setWhiteBornOff(whiteBornOff);
        BackGammon.setRedBornOff(redBornOff);
        BackGammon.setStartPosition(startPosition);
        BackGammon.setEndPosition(endPosition);
        BackGammon.setCaptured(captured);
        BackGammon.setRedBar(redBar);
        BackGammon.setWhiteBar(whiteBar);
        BackGammon.setDice1(dice1);
        BackGammon.setDice2(dice2);
        BackGammon.setRollsLeftForDouble(rollsLeftForDouble);
    }

    public static void makeMove(){

        getFields();

        if(!CanMove.moveCanBeMade()){

            BackGammon.info();
            System.out.println("No move possible for AI");
        }

        while(CanMove.moveCanBeMade()){

            BackGammon.info();

            if (BackGammon.onBar()){

                moveOffBar();
            }
            else {

                if(BackGammon.countersInBearOffPosition()){

                    bearOff();
                }
                else {
                    randomMove();
                }

            }

            setFields();
        }

    }

    public static void bearOff(){

        if(CanMove.aCounterCanBearOff()){

            startPosition = dice1-1;

            if(startPosition<6){

                if(board[startPosition]<0){

                    board [startPosition]++;
                    redBornOff++;

                    if(rollsLeftForDouble>0){

                        rollsLeftForDouble--;

                        if(rollsLeftForDouble==0){

                            dice1 = 50;
                            dice2 = 50;
                        }
                    }
                    else {

                        dice1 = 50;
                    }


                }
                else {

                    startPosition = dice2-1;

                    board[startPosition]++;
                    redBornOff++;

                    if(rollsLeftForDouble>0){

                        rollsLeftForDouble--;

                        if(rollsLeftForDouble==0){

                            dice1 = 50;
                            dice2 = 50;
                        }
                    }
                    else {

                        dice2 = 50;
                    }
                }
            }
            else {

                startPosition = dice2-1;

                board[startPosition]++;
                redBornOff++;

                if(rollsLeftForDouble>0){

                    rollsLeftForDouble--;

                    if(rollsLeftForDouble==0){

                        dice1 = 50;
                        dice2 = 50;
                    }
                }
                else {

                    dice2 = 50;
                }
            }

        }
        else{
            randomMove();
        }
    }

    public static void randomMove(){

        List<String> possibleStartPosition = new ArrayList<>();

        possibleStartPosition.add("0");
        possibleStartPosition.add("1");
        possibleStartPosition.add("2");
        possibleStartPosition.add("3");
        possibleStartPosition.add("4");
        possibleStartPosition.add("5");
        possibleStartPosition.add("6");
        possibleStartPosition.add("7");
        possibleStartPosition.add("8");
        possibleStartPosition.add("9");
        possibleStartPosition.add("10");
        possibleStartPosition.add("11");
        possibleStartPosition.add("12");
        possibleStartPosition.add("13");
        possibleStartPosition.add("14");
        possibleStartPosition.add("15");
        possibleStartPosition.add("16");
        possibleStartPosition.add("17");
        possibleStartPosition.add("18");
        possibleStartPosition.add("19");
        possibleStartPosition.add("20");
        possibleStartPosition.add("21");
        possibleStartPosition.add("22");
        possibleStartPosition.add("23");

        Collections.shuffle(possibleStartPosition);
        startPosition = Integer.parseInt(possibleStartPosition.get(0));

        while (board[startPosition]>-1){

            Collections.shuffle(possibleStartPosition);
            startPosition = Integer.parseInt(possibleStartPosition.get(0));
        }

        setFields();

        while(!BackGammon.counterCanMove()){

            Collections.shuffle(possibleStartPosition);
            startPosition = Integer.parseInt(possibleStartPosition.get(0));

            while (board[startPosition]>-1){

                Collections.shuffle(possibleStartPosition);
                startPosition = Integer.parseInt(possibleStartPosition.get(0));
            }

            setFields();

        }

        endPosition = startPosition - dice1;

        if (endPosition>-1 && board[endPosition]<2){

            if(board[endPosition]==1){

                whiteBar++;
                board[endPosition] = -1;
                board[startPosition]++;
            }
            else {

                board[startPosition]++;
                board[endPosition]--;
            }

            if(rollsLeftForDouble>0){

                rollsLeftForDouble--;

                if (rollsLeftForDouble == 0){
                    dice1 = 50;
                    dice2 = 50;
                }
            }
            else {
                dice1 = 50;
            }

        }
        else {

            endPosition = startPosition-dice2;

            if(board[endPosition]==1){

                whiteBar++;
                board[endPosition] = -1;
                board[startPosition]++;
            }
            else {

                board[startPosition]++;
                board[endPosition]--;
            }

            if(rollsLeftForDouble>0){

                rollsLeftForDouble--;

                if (rollsLeftForDouble == 0){
                    dice1 = 50;
                    dice2 = 50;
                }
            }
            else {
                dice2 = 50;
            }

        }

    }

    public static void moveOffBar(){

        int position1 = 24-dice1;
        int position2 = 24-dice2;

        if (position1>17&&position2>17){

            if(board[position1]<2){

                if(board[position1]==1){

                    board[position1] = -1;
                    redBar--;
                    whiteBar++;

                }
                else {
                    board[position1]--;
                    redBar--;
                }

                if (rollsLeftForDouble>0){
                    rollsLeftForDouble--;

                    if (rollsLeftForDouble==0){
                        dice1 = 50;
                        dice2 = 50;
                    }
                }
                else {

                    dice1 = 50;
                }

            }

            else {

                if(board[position2]==1){

                    board[position2] = -1;
                    redBar--;
                    whiteBar++;
                }
                else {
                    board[position2]--;
                    redBar--;
                }

                if (rollsLeftForDouble>0){
                    rollsLeftForDouble--;

                    if (rollsLeftForDouble==0){
                        dice1 = 50;
                        dice2 = 50;
                    }
                }
                else {

                    dice2 = 50;
                }


            }


        }

        else {

            if (position1>17){

                if(board[position1]==1){

                    board[position1] = -1;
                    redBar--;
                    whiteBar++;

                }
                else {
                    board[position1]--;
                    redBar--;
                }

                if (rollsLeftForDouble>0){
                    rollsLeftForDouble--;

                    if (rollsLeftForDouble==0){
                        dice1 = 50;
                        dice2 = 50;
                    }
                }
                else {

                    dice1 = 50;
                }


            }

            else {

                if(board[position2]==1){

                    board[position2] = -1;
                    redBar--;
                    whiteBar++;
                }
                else {
                    board[position2]--;
                    redBar--;
                }

                if (rollsLeftForDouble>0){
                    rollsLeftForDouble--;

                    if (rollsLeftForDouble==0){
                        dice1 = 50;
                        dice2 = 50;
                    }
                }
                else {

                    dice2 = 50;
                }


            }
        }


    }
}

