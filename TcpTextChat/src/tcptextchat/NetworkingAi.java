
package tcptextchat;


public class NetworkingAi {
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
    
    
    public static String move() {
        // TODO code application logic here
        
        board = new int[24];
        
        translate();
        
        if(countersInBearOffPositionGUI()){

            bearOff();
        }
        else {
            goodMove();
        }
        
        startPosition++; 
        endPosition++;
        
        TcpTextChatBlackAI.startingTriangle = startPosition;
        TcpTextChatBlackAI.endingTriangle = endPosition;
        
        String startTriangle = ""+startPosition;
        String endTriangle = ""+endPosition;
        
        if(startTriangle.length() <2){
            
            startTriangle = "0"+ startTriangle; 
        }
        
        if(endTriangle.length() <2){
            
            endTriangle = "0"+ endTriangle; 
        }
        
        String move = "(" + startTriangle + "," + endTriangle + ")";
        
        System.out.println(move);
        
        
        return move;
        
    }
    
    public static void bearOff(){
        
        if(aCounterCanBearOffGUI()){
            
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
    
    public static void translate(){
        
        dice1 = TcpTextChatBlackAI.dice1;
        dice2 = TcpTextChatBlackAI.dice2;
        
        if(dice1 == -1){
            
            dice1 = 50;
        }
        
        if(dice2 == -1){
            
            dice2 = 50;
        }
        
        if(TcpTextChatBlackAI.currentPlayer%2==0){
            
            whitesTurn = false;    
            
        }
        else{
            
            whitesTurn = true;
        }
        
        String[] pieceColours = TcpTextChatBlackAI.pieceColors;
        int[] noPieces = TcpTextChatBlackAI.noPieces;
       
        int index = 0;
        
        while(index<24){
            
            if(pieceColours[index].equals("w")){
               
                board[index] = 0-noPieces[index];
            }
            else{
                
                board[index] = 0+noPieces[index];
            }
              
            index++;
        }
        
        
        index = 0;
        int whiteTotal =0;
        int redTotal = 0;
        
        while(index<24){
            
            if(board[index]>0){
                
                redTotal+= board[index]; 
                
            }
            else{
                
                whiteTotal += Math.abs(board[index]);
            }
            
            index++;
        }
        
        index = 0;
        
        whiteBornOff = TOTAL_COUNTERS-whiteTotal;
        redBornOff = TOTAL_COUNTERS - redTotal;
        
        whiteBar = 0;
        redBar = 0;
    }
    
    public static Boolean countersInBearOffPositionGUI(){
        
        if(whitesTurn){
            
            int whitesCounters = 0;
            int index = 0;
            
            while(index<6){
                
                if(board[index]<0){
                    
                    whitesCounters += Math.abs(board[index]);
                }
                index++;
            }
            
            if((TOTAL_COUNTERS-whiteBornOff)==whitesCounters){
                
                return true;
            }
            
        }
        else{
            
            int blacksCounters = 0;
            int index = 23;
            
            while(index>17){
                
                if(board[index]>0){
                    
                    blacksCounters += Math.abs(board[index]);
                }
                index--;
            }
            
            if((TOTAL_COUNTERS-redBornOff)==blacksCounters){
                
                return true;
            }
            
        }
        
        return false;
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
                   

                    return true;
                }
            }

            endPosition = startPosition+dice2;

            if(endPosition<24){

                if(board[endPosition]>-2){

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
    
    public static Boolean aCounterCanBearOffGUI() {

        if (!whitesTurn) {

            int position1 = 24 - dice1;
            int position2 = 24 - dice2;

            if (position1 > 17) {

                if (board[position1] > 0) {

                    return true;
                } else {
                    if (position2 > 17) {

                        if (board[position2] > 0) {

                            return true;
                        }
                    }
                }
            } else {
                if (position2 > 17) {

                    if (board[position2] > 0) {

                        return true;
                    }
                }
            }
        } else {

            int position1 = dice1 - 1;
            int position2 = dice2 - 1;

            if (position1 < 6) {

                if (board[position1] < 0) {

                    return true;
                } else {
                    if (position2 < 6) {

                        if (board[position2] < 0) {

                            return true;
                        }
                    }
                }
            } else {
                if (position2 < 6) {

                    if (board[position2] < 0) {

                        return true;
                    }
                }
            }
        }

        return false;

    }
    
    
}