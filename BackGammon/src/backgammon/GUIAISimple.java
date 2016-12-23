
package backgammon;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class GUIAISimple {
    
    //White!!! decreasing
    
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
    private static final int TOTAL_COUNTERS = 15;
    private static Boolean gui;
    
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
    
    public static void move(){
        
        getFields();
        
        if(BackGammon.countersInBearOffPositionGUI()){
            bearOff();
        }
                
        else {
            
            randomMove();
                
        }
        
        setFields();
           
    }
    
    public static void bearOff(){
        
        if(CanMove.aCounterCanBearOffGUI()){

            startPosition = dice1-1;
            endPosition = -1;

            if(startPosition<6){

                if(board[startPosition]<0){

                  
                }
                else {

                    startPosition = dice2-1;
                    endPosition = -1;
                }
            }
            else{

                startPosition = dice2-1;
                endPosition = -1;
    
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

        while(!counterCanMove()){

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

        }
        else {

            endPosition = startPosition-dice2;

            

        }

    
    }
    
    public static Boolean counterCanMove(){

        if(!whitesTurn) {

            int possible1 = startPosition + dice1;
            int possible2 = startPosition + dice2;

            if (possible1 < 24) {

                if (board[possible1] > -2) {

                    return true;
                }
                else {

                    if (possible2 < 24) {

                        if (board[possible2] > -2) {

                            return true;
                        }
                    }


                }
            }
            else {

                if (possible2 < 24) {

                    if (board[possible2] > -2) {

                        return true;
                    }
                }
            }

        }
        else {

            int possible1 = startPosition-dice1;
            int possible2 = startPosition-dice2;

            if (possible1 > -1) {

                if (board[possible1] < 2) {

                    return true;
                }
                else {

                    if (possible2 > -1) {

                        if (board[possible2] < 2) {

                            return true;
                        }
                    }
                }
            }
            else {

                if (possible2 > -1) {

                    if (board[possible2] < 2) {

                        return true;
                    }
                }
            }
        }
        return false;
    }
      
}
