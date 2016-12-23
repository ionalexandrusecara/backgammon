
package backgammon;


public class GUIAIComplex {
    
    //Black!!! Increasing
    
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
    private static int guiBothStart;
    
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
            goodMove();
        }
        
        setFields();
        
    }
    
     public static void bearOff(){
        
        if(CanMove.aCounterCanBearOffGUI()){
            
            startPosition = 24-dice1;
            endPosition = -1;

            if(startPosition>17){

                if(board[startPosition]>0){

                    
                }

                else {

                    startPosition = 24-dice2;
                    endPosition = -1;

                }

            }
            else {

                startPosition = 24-dice2;
                endPosition = -1;

            }


        }
        else{
            goodMove();
        }
        
    }
     
    public static void goodMove(){
        
        if(counterCanBeProtected()){


        }

        else{
            if(counterCanBeCaptured()){


            }
            else{

                startPosition = 0;

                while (!movePossible()){

                    startPosition++;
                    
                    if(startPosition == 24){
                        
                        System.out.println("Error");
                        System.exit(0);
                    }
                }

            }
        }
    
    
    }
    
    public static Boolean counterCanBeProtected(){

        endPosition = 23;

        while(endPosition>0){

            if(board[endPosition]==1){

                startPosition = endPosition-dice1;

                if(startPosition>-1){

                    if(board[startPosition]>2){

                        if(rollsLeftForDouble>0){

                            rollsLeftForDouble--;

                            if(rollsLeftForDouble==0){

                                dice1=50;
                                dice2=50;
                            }
                        }

                        else {

                            dice1 = 50;
                        }

                        return true;
                    }
                }

                startPosition = endPosition-dice2;

                if(startPosition>-1){

                    if(board[startPosition]>2){

                        if(rollsLeftForDouble>0){

                            rollsLeftForDouble--;

                            if(rollsLeftForDouble==0){

                                dice1=50;
                                dice2=50;
                            }
                        }

                        else {

                            dice2 = 50;
                        }

                        return true;
                    }
                }

            }
            endPosition--;
        }
        return false;
    }

    public static Boolean movePossible(){

        if(board[startPosition]>0){

            endPosition = startPosition+dice1;

            if(endPosition<24){

                if(board[endPosition]>-2){

                    if(rollsLeftForDouble>0){

                        rollsLeftForDouble--;

                        if(rollsLeftForDouble==0){

                            dice1=50;
                            dice2=50;
                        }
                    }

                    else {

                        dice1 = 50;
                    }

                    return true;
                }
            }

            endPosition = startPosition+dice2;

            if(endPosition<24){

                if(board[endPosition]>-2){

                    if(rollsLeftForDouble>0){

                        rollsLeftForDouble--;

                        if(rollsLeftForDouble==0){

                            dice1=50;
                            dice2=50;
                        }
                    }

                    else {

                        dice2 = 50;
                    }

                    return true;

                }
            }
        }
        return false;
    }

    public static Boolean counterCanBeCaptured(){

        endPosition = 0;

        while (endPosition<24){

            if (board[endPosition]==-1){

                startPosition = endPosition - dice1;

                if(startPosition>-1){

                    if (board[startPosition]>0){

                        if (rollsLeftForDouble>0){

                            rollsLeftForDouble--;

                            if (rollsLeftForDouble == 0){

                                dice1 = 50;
                                dice2 = 50;
                            }

                        }
                        else {
                            dice1 = 50;
                        }

                        return true;
                    }

                }

                startPosition = endPosition - dice2;

                if(startPosition>-1){

                    if (board[startPosition]>0){

                        if (rollsLeftForDouble>0){

                            rollsLeftForDouble--;

                            if (rollsLeftForDouble == 0){

                                dice1 = 50;
                                dice2 = 50;
                            }

                        }
                        else {
                            dice2 = 50;
                        }

                        return true;
                    }

                }

            }

            endPosition++;
        }

        return false;
    }
        
}
