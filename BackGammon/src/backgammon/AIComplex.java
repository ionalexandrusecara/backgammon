
package backgammon;


public class AIComplex {

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
                    goodMove();
                }

            }

            setFields();
        }
    }

    public static void goodMove(){

        if(counterCanBeProtected()){

            board[startPosition]--;
            board[endPosition]++;

        }

        else{
            if(counterCanBeCaptured()){

                board[startPosition]--;
                board[endPosition] = 1;
                redBar++;

            }
            else{

                startPosition = 0;

                while (!movePossible()){

                    startPosition++;
                }

                board[startPosition]--;
                board[endPosition]++;

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

    public static void bearOff(){

        if(CanMove.aCounterCanBearOff()){

            startPosition = 24-dice1;

            if(startPosition>17){

                if(board[startPosition]>0){

                    board [startPosition]--;
                    whiteBornOff++;

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

                    startPosition = 24-dice2;

                    board[startPosition]--;
                    whiteBornOff++;

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

                startPosition = 24-dice2;

                board[startPosition]--;
                whiteBornOff++;

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
            goodMove();
        }
    }

    public static void moveOffBar(){

        int position1 = dice1-1;
        int position2 = dice2-1;

        if (position1<6&&position2<6){

            if(board[position1]>-2){

                if(board[position1]==-1){

                    board[position1] = 1;
                    whiteBar--;
                    redBar++;

                }
                else {
                    board[position1]++;
                    whiteBar--;
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

                if(board[position2]==-1){

                    board[position2] = 1;
                    redBar++;
                    whiteBar--;
                }
                else {
                    board[position2]++;
                    whiteBar--;
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

            if (position1<6){

                if(board[position1]==-1){

                    board[position1] = 1;
                    redBar++;
                    whiteBar--;

                }
                else {
                    board[position1]++;
                    whiteBar--;
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

                if(board[position2]==-1){

                    board[position2] = 1;
                    redBar++;
                    whiteBar--;
                }
                else {
                    board[position2]++;
                    whiteBar--;
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
