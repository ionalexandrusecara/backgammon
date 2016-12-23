
package backgammon;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;


public class BackGammon {
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

    public static void setBoard(int [] x){

        board = x;
    }

    public static void setWhitesTurn(Boolean x){

        whitesTurn = x;
    }

    public static void setWhiteBornOff(int x){

        whiteBornOff = x;
    }

    public static void setRedBornOff(int x){

        redBornOff = x;
    }

    public static void setStartPosition(int x){

       startPosition = x;
    }

    public static void setEndPosition(int x){

        endPosition = x;
    }

    public static void setCaptured(Boolean x){

       captured = x;
    }

    public static void setRedBar(int x){

        redBar = x;
    }

    public static void setWhiteBar(int x){

        whiteBar = x;
    }

    public static void setDice1(int x){

        dice1 = x;
    }

    public static void setDice2(int x){

        dice2 = x;
    }

    public static void setRollsLeftForDouble(int x){

        rollsLeftForDouble = x;
    }

    public static int getRollsLeftForDouble(){return rollsLeftForDouble;}

    public static int [] getBoard(){
        return board;
    }

    public static Boolean getWhitesTurn(){
        return whitesTurn;
    }

    public static int getRedBornOff(){
        return redBornOff;
    }

    public static int getWhiteBornOff(){
        return whiteBornOff;
    }

    public static int getStartPosition(){
        return startPosition;
    }

    public static int getEndPosition(){
        return endPosition;
    }

    public static int getWhiteBar(){
        return whiteBar;
    }

    public static int getRedBar(){
        return redBar;
    }

    public static int getDice1(){
        return dice1;
    }

    public static int getDice2(){
        return dice2;
    }
    
    public static void guiBothAI(){
        
        translate();
               
        //System.out.println("translated");
        
        System.out.println("Dice1: " + dice1);
        System.out.println("Dice2: " + dice2);
        
        if(whitesTurn){
            
            System.out.println("White's Turn");
            GUIAISimple.move();
            
        }
        else{
            
            System.out.println("Black's turn");
       
            GUIAIComplex.move(); 
        }
             
    }
    
    public static int guiBothAIStart(){
        
        guiBothAI();
        startPosition++;
        return startPosition;
    }
    
    public static int guiBothAIEnd(){
        
        endPosition++;
        return endPosition;
    }
    
    public static int guiSimpleAIStart(){
        
        translate();
        
        System.out.println("Dice1: " + dice1);
        System.out.println("Dice2: " + dice2);
        
        GUIAISimple.move();
        
        startPosition++;
        return startPosition;
    }
    
    public static int guiSimpleAIEnd(){
        
        endPosition++;
        return endPosition;
    }
    
    public static int guiComplexAIStart(){
        
        translate();
        
        System.out.println("Dice1: " + dice1);
        System.out.println("Dice2: " + dice2);
        
        GUIAIComplex.move();
        
        startPosition++;
        return startPosition;
    }
    
    public static int guiComplexAIEnd(){
        
        endPosition++;
        
        return endPosition;
    }
    
    public static void translate(){
        
        startPosition = GUI.startingTriangle;
        endPosition = GUI.endingTriangle;
        
        dice1 = GUI.dice1;
        dice2 = GUI.dice2;
        
        if(dice1 == -1){
            
            dice1 = 50;
        }
        
        if(dice2 == -1){
            
            dice2 = 50;
        }
        
        if(GUI.currentPlayer%2==0){
            
            whitesTurn = false;    
            
        }
        else{
            
            whitesTurn = true;
        }
        
        String[] pieceColours = GUI.pieceColors;
        int[] noPieces = GUI.noPieces;
        
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
        
        whiteBornOff = TOTAL_COUNTERS-whiteTotal;
        redBornOff = TOTAL_COUNTERS - redTotal;
        
        whiteBar = 0;
        redBar = 0;
    }


    public static void main(String[] args) throws IOException {

        board = new int[24];
        board[0] = 2;
        board[11] = 5;
        board[16] = 3;
        board [18] = 5;

        board[5] = -5;
        board[7] = -3;
        board[12] = -5;
        board [23] = -2;

        int choice = -1;
        
        int guiChoice = -1;
        
        while(guiChoice!=1&&guiChoice!=2){
            
            System.out.println("Please Enter 1 or 2");
            System.out.println("1: Use GUI");
            System.out.println("2: Don't Use GUI");
            guiChoice = EasyIn.getInt();
            
            if(guiChoice == 1){
                gui = true;
            }
            
            if(guiChoice == 2){
                
                gui = false;
            }    
        }

        while (choice!=0&&choice!=1&&choice !=2){

            System.out.println("Please Enter 0 or 1 or 2");
            System.out.println("0: AI play each other");
            System.out.println("1: Single Player");
            System.out.println("2: 2 Players");
            choice = EasyIn.getInt();
        }
        
        int AI = 0;

        while (choice==1&&AI!=1&&AI!=2){

            System.out.println("Please Enter 1 or 2");
            System.out.println("1: Play against Simple AI");
            System.out.println("2: Play against Complex AI");
            AI = EasyIn.getInt();
        }
        
        if(gui == true){
            
            GUI.gui(choice,AI);
        }
        
        else{
            
            if (AI == 2){
                whitesTurn = false;
            }
            
            while (whiteBornOff != TOTAL_COUNTERS && redBornOff != TOTAL_COUNTERS){
                

            rollsLeftForDouble = 0;
            roll();

            if (choice == 0){

                if (whitesTurn){

                    AIComplex.makeMove();
                }
                else {

                    AISimple.makeMove();
                }
            }

            if(choice==1){

                if (AI == 1){

                    if(whitesTurn){

                        changeBoard();
                    }
                    else {
                        AISimple.makeMove();
                    }
                }
                else {

                    if(whitesTurn){

                        AIComplex.makeMove();
                    }
                    else {
                        changeBoard();
                    }
                }

            }
            if(choice==2) {
                changeBoard();
            }

            if (whitesTurn){
                whitesTurn = false;
            }
            else {
                whitesTurn = true;
            }
        }
            if (whitesTurn) {

            System.out.println("Red Wins!");
        }
        else{
            System.out.println("White Wins!");
        }

       
        }        
    }
    
    public static void changeBoard(){

        if (!CanMove.moveCanBeMade()){
            info();

            System.out.println("Impossible to Move!");
        }

        while(CanMove.moveCanBeMade()){

            info();

            if(onBar()){
                moveOffBar();
            }
            else{
                change();
            }
        }


    }

    public static void change(){

        if(countersInBearOffPosition()){
            bearOffMove();
        }
        else{
            move();
        }
    }

    public static void bearOffMove(){

        Boolean legal = false;

        while (!legal){

            bearOffStartPosition();

            if(!counterCanMove()){

                if(whitesTurn) {

                    whiteBornOff++;
                    board[startPosition]--;
                    legal = true;
                }

                else {
                    redBornOff++;
                    board[startPosition]++;
                    legal = true;
                }

                if(rollsLeftForDouble>0){
                    rollsLeftForDouble--;

                    if(rollsLeftForDouble == 0){
                        dice2 = 50;
                        dice1 = 50;
                    }
                }
                else{
                    if(whitesTurn){
                        if((24-startPosition)==dice1){
                            dice1 = 50;
                        }
                        else{
                            dice2 = 50;
                        }

                    }
                    else {

                        if((1+startPosition)==dice1){
                            dice1 = 50;
                        }
                        else{
                            dice2 = 50;
                        }

                    }
                }
            }
            else{
                if(!counterCanBearOff()){

                    bearOffEndPosition();

                    if(whitesTurn){

                        int possible1 = startPosition+dice1;
                        int possible2 = startPosition+dice2;

                        if(endPosition!=possible1&&endPosition!=possible2){

                            System.out.println("Illegal Move!");
                            legal = false;
                        }
                        else {
                            board[startPosition]--;

                            if(board[endPosition]==-1){
                                redBar++;
                                board[endPosition] = 1;
                            }
                            else {
                                board[endPosition]++;
                            }
                            legal = true;
                        }

                    }

                    else{

                        int possible1 = startPosition-dice1;
                        int possible2 = startPosition-dice2;

                        if(endPosition!=possible1&&endPosition!=possible2){

                            System.out.println("Illegal Move!");
                            legal = false;
                        }
                        else {

                            board[startPosition]++;

                            if(board[endPosition]==1){
                                whiteBar++;
                                board[endPosition] = -1;
                            }
                            else {
                                board[endPosition]--;
                            }
                            legal = true;
                        }


                    }

                    if(legal){

                        int a = endPosition - startPosition;
                        int b = endPosition-startPosition;

                        int difference1 = Math.abs(a);
                        int difference2 = Math.abs(b);

                        if(rollsLeftForDouble>0){
                            rollsLeftForDouble--;

                            if(rollsLeftForDouble == 0){
                                dice2 = 50;
                                dice1 = 50;
                            }
                        }
                        else{
                            if(dice1==difference1){
                                dice1 = 50;
                            }
                            else {
                                dice2 = 50;
                            }
                        }

                    }

                }
                else {

                    System.out.println("Move or Bear Off");
                    String choice = EasyIn.getString();

                    while(!choice.equals("Move")&&!choice.equals("Bear Off")){

                        System.out.println("Please enter Move or Bear Off");
                        choice = EasyIn.getString();
                    }

                    if(choice.equals("Bear Off")){

                        if(whitesTurn){

                            whiteBornOff++;
                            board[startPosition]--;
                            legal = true;
                        }
                        else{

                            redBornOff++;
                            board[startPosition]++;
                            legal = true;
                        }

                        if(rollsLeftForDouble>0){
                            rollsLeftForDouble--;

                            if(rollsLeftForDouble == 0){
                                dice2 = 50;
                                dice1 = 50;
                            }
                        }
                        else{
                            if(whitesTurn){
                                if((24-startPosition)==dice1){
                                    dice1 = 50;
                                }
                                else{
                                    dice2 = 50;
                                }

                            }
                            else {

                                if((1+startPosition)==dice1){
                                    dice1 = 50;
                                }
                                else{
                                    dice2 = 50;
                                }

                            }
                        }
                    }

                    if(choice.equals("Move")){

                        bearOffEndPosition();

                        if(whitesTurn){

                            int possible1 = startPosition+dice1;
                            int possible2 = startPosition+dice2;

                            if(endPosition!=possible1&&endPosition!=possible2){

                                System.out.println("Illegal Move!");
                                legal = false;
                            }
                            else {
                                board[startPosition]--;

                                if(board[endPosition]==-1){
                                    redBar++;
                                    board[endPosition] = 1;
                                }
                                else {
                                    board[endPosition]++;
                                }
                                legal = true;
                            }

                        }

                        if(!whitesTurn){

                            int possible1 = startPosition-dice1;
                            int possible2 = startPosition-dice2;

                            if(endPosition!=possible1&&endPosition!=possible2){

                                System.out.println("Illegal Move!");
                                legal = false;
                            }
                            else {

                                board[startPosition]++;

                                if(board[endPosition]==1){
                                    whiteBar++;
                                    board[endPosition] = -1;
                                }
                                else {
                                    board[endPosition]--;
                                }
                                legal = true;
                            }


                        }

                        if(legal){

                            int a = endPosition - startPosition;
                            int b = endPosition-startPosition;

                            int difference1 = Math.abs(a);
                            int difference2 = Math.abs(b);

                            if(rollsLeftForDouble>0){
                                rollsLeftForDouble--;

                                if(rollsLeftForDouble == 0){
                                    dice2 = 50;
                                    dice1 = 50;
                                }
                            }
                            else{
                                if(dice1==difference1){
                                    dice1 = 50;
                                }
                                else {
                                    dice2 = 50;
                                }
                            }

                        }

                    }

                }

            }
        }
    }

    public static void bearOffEndPosition(){

        endPosition = -1;

        while(0>endPosition||endPosition>23){

            System.out.println("Enter End Position");
            endPosition = EasyIn.getInt();
            endPosition--;

        }

        if(whitesTurn){

            while(board[endPosition]<-1){

                System.out.println("Invalid End Position");

                endPosition = -1;

                while(0>endPosition||endPosition>23){

                    System.out.println("Enter End Position");
                    endPosition = EasyIn.getInt();
                    endPosition--;

                }
            }

        }

        if(!whitesTurn){

            while(board[endPosition]>1){

                System.out.println("Invalid End Position");

                endPosition = -1;

                while(0>endPosition||endPosition>23){

                    System.out.println("Enter End Position");
                    endPosition = EasyIn.getInt();
                    endPosition--;

                }
            }

        }

    }
    public static void bearOffStartPosition(){

        startPosition = -1;

        while(0>startPosition||startPosition>23){

            System.out.println("Enter Start Position");
            startPosition = EasyIn.getInt();
            startPosition--;

        }

        if(whitesTurn){

            while (board[startPosition]<1){

                System.out.println("Invalid Start Position");

                startPosition = -1;

                while(0>startPosition||startPosition>23){

                    System.out.println("Enter Start Position");
                    startPosition = EasyIn.getInt();
                    startPosition--;

                }
            }

            while ((!counterCanMove())&&(!counterCanBearOff())){

                System.out.println("That Counter Cannot Move");

                startPosition = -1;

                while(0>startPosition||startPosition>23){

                    System.out.println("Enter Start Position");
                    startPosition = EasyIn.getInt();
                    startPosition--;

                }

                while (board[startPosition]<1){

                    System.out.println("Invalid Start Position");

                    startPosition = -1;

                    while(0>startPosition||startPosition>23){

                        System.out.println("Enter Start Position");
                        startPosition = EasyIn.getInt();
                        startPosition--;

                    }
                }


            }
        }

        if(!whitesTurn) {

            while (board[startPosition] > -1) {

                System.out.println("Invalid Start Position");

                startPosition = -1;

                while (0 > startPosition || startPosition > 23) {

                    System.out.println("Enter Start Position");
                    startPosition = EasyIn.getInt();
                    startPosition--;

                }
            }

            while ((!counterCanMove()) && (!counterCanBearOff())) {

                System.out.println("That Counter Cannot Move");

                startPosition = -1;

                while (0 > startPosition || startPosition > 23) {

                    System.out.println("Enter Start Position");
                    startPosition = EasyIn.getInt();
                    startPosition--;

                }

                while (board[startPosition] > -1) {

                    System.out.println("Invalid Start Position");

                    startPosition = -1;

                    while (0 > startPosition || startPosition > 23) {

                        System.out.println("Enter Start Position");
                        startPosition = EasyIn.getInt();
                        startPosition--;

                    }
                }
            }
        }

    }
    
    public static Boolean counterCanBearOffGUI(){
        
        translate();
        
        if (startPosition>23||startPosition<0){

            return false;
        }

        if(whitesTurn){

            int possible1 = startPosition- dice1;
            int possible2 = startPosition - dice2;

            if(possible1==-1||possible2==-1){
                return true;
            }
        }
        else{

            int possible1 = startPosition-dice1;
            int possible2 = startPosition - dice2;

            if(possible1==24||possible2==24){
                return true;
            }
        }
        return false;
        
        
    }

    public static Boolean counterCanBearOff(){

        if (startPosition>23||startPosition<0){

            return false;
        }

        if(whitesTurn){

            int possible1 = startPosition+ dice1;
            int possible2 = startPosition + dice2;

            if(possible1==24||possible2==24){
                return true;
            }
        }
        else{

            int possible1 = startPosition-dice1;
            int possible2 = startPosition - dice2;

            if(possible1==-1||possible2==-1){
                return true;
            }
        }
        return false;
    }

    public static Boolean counterCanMove(){

        if(whitesTurn) {

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

    public static void move(){

        Boolean legal = false;

        while (!legal){

            getStartPositionFromUser();
            getEndPositionFromUser();

            legal = isMoveLegal();

        }

        makeMove();

    }

    public static Boolean isMoveLegal(){

        if(whitesTurn){

            int possible1 = startPosition+dice1;
            int possible2 = startPosition+dice2;

            if(possible1!=endPosition&&possible2!=endPosition){

                System.out.println("Invalid Move!");
                return false;
            }

            if(board[endPosition] < -1){

                System.out.println("Invalid Move!");
                return false;
            }

            if (board[endPosition] == -1){
                captured = true;
            }
        }

        if(!whitesTurn){

            int possible1 = startPosition-dice1;
            int possible2 = startPosition-dice2;

            if(possible1!=endPosition&&possible2!=endPosition){

                System.out.println("Invalid Move!");
                return false;
            }

            if(board[endPosition] > 1){

                System.out.println("Invalid Move!");
                return false;
            }

            if (board[endPosition] == 1){
                captured = true;
            }
        }

        return true;
    }

    public static void getEndPositionFromUser(){

        endPosition = -1;

        while(0>endPosition||endPosition>23){

            System.out.println("Enter End Position");
            endPosition = EasyIn.getInt();
            endPosition--;

        }

    }

    public static void getStartPositionFromUser(){

        startPosition = -1;

        while(0>startPosition||startPosition>23){

            System.out.println("Enter Start Position");
            startPosition = EasyIn.getInt();
            startPosition--;

        }

        if(whitesTurn){

            while (board[startPosition]<1){

                System.out.println("Invalid Start Position");

                startPosition = -1;

                while(0>startPosition||startPosition>23){

                    System.out.println("Enter Start Position");
                    startPosition = EasyIn.getInt();
                    startPosition--;

                }
            }
        }

        if(!whitesTurn){

            while (board[startPosition]>-1){

                System.out.println("Invalid Start Position");

                startPosition = -1;

                while(0>startPosition||startPosition>23){

                    System.out.println("Enter Start Position");
                    startPosition = EasyIn.getInt();
                    startPosition--;

                }
            }
        }

    }

    public static void moveOffBar(){

        System.out.println("Please Enter End Position: ");
        endPosition = EasyIn.getInt();
        endPosition--;

        if (whitesTurn){

            while (board[endPosition]<-1){
                System.out.println("Please Enter Valid Position: ");
                endPosition = EasyIn.getInt();
                endPosition--;
            }

            while(endPosition+1!=dice1&&endPosition+1!=dice2){
                System.out.println("Invalid Move");
                System.out.println("Please Enter Valid Position: ");
                endPosition = EasyIn.getInt();
                endPosition--;

                while (board[endPosition]<-1){
                    System.out.println("Please Enter Valid Position: ");
                    endPosition = EasyIn.getInt();
                    endPosition--;
                }
            }

            if(board[endPosition] == -1){
                whiteBar--;
                redBar++;
                board[endPosition] = 1;
            }
            else{
                whiteBar--;
                board[endPosition]++;

            }
        }
        else{

            while (board[endPosition]>1){
                System.out.println("Please Enter Valid Position: ");
                endPosition = EasyIn.getInt();
                endPosition--;
            }

            while(endPosition!=(24-dice1)&&endPosition!=(24-dice2)){

                System.out.println("Invalid Move: ");
                System.out.println("Please Enter Valid Position: ");
                endPosition = EasyIn.getInt();
                endPosition--;

                while (board[endPosition]>1){
                    System.out.println("Please Enter Valid Position: ");
                    endPosition = EasyIn.getInt();
                    endPosition--;
                }

            }

            if(board[endPosition] == 1){
                whiteBar++;
                redBar--;
                board[endPosition] = -1;
            }
            else{
                board[endPosition]--;
                redBar--;
            }

        }

        if(rollsLeftForDouble>0){

            rollsLeftForDouble--;

            if(rollsLeftForDouble == 0){
                dice2 = 50;
                dice1 = 50;
            }
        }
        else {

            if(whitesTurn){
                if(endPosition+1==dice1){
                    dice1 = 50;
                }
                else {
                    dice2 =50;
                }
            }
            else{
                if(24-endPosition==dice1){
                    dice1 = 50;
                }
                else {
                    dice2 =50;
                }

            }
        }
    }

    public static Boolean onBar(){

        if (whitesTurn){

            if(whiteBar>0){
                return true;
            }
        }

        if (!whitesTurn){

            if(redBar>0){
                return true;
            }
        }

        return false;
    }

    public static void makeMove(){

        if(captured){

            if (whitesTurn){

                board[startPosition]--;
                board[endPosition] = 1;

                redBar++;
            }

            if(!whitesTurn){

                board[startPosition]++;
                board[endPosition] = -1;

                whiteBar++;
            }
        }
        else{
            if (whitesTurn){

                board[startPosition]--;
                board[endPosition]++;

            }

            if(!whitesTurn){

                board[startPosition]++;
                board[endPosition]--;
            }
        }

        captured = false;

        int a = endPosition - startPosition;
        int b = endPosition-startPosition;

        int difference1 = Math.abs(a);
        int difference2 = Math.abs(b);

        if(rollsLeftForDouble>0){

            rollsLeftForDouble--;

            if(rollsLeftForDouble == 0){
                dice2 = 50;
                dice1 = 50;
            }
        }
        else{
            if(dice1==difference1){
                dice1 = 50;
            }
            else {
                dice2 = 50;
            }
        }
    }

    public static void roll(){

        List<String> die = new ArrayList<>();

        die.add("1");
        die.add("2");
        die.add("3");
        die.add("4");
        die.add("5");
        die.add("6");

        Collections.shuffle(die);
        dice1 = Integer.parseInt(die.get(0));

        Collections.shuffle(die);
        dice2 = Integer.parseInt(die.get(0));

        if (dice1 == dice2){

            rollsLeftForDouble = 4;
        }
    }
    
    public static Boolean countersInBearOffPositionGUI(){
        
        translate();
        
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

    public static Boolean countersInBearOffPosition(){

        if (whitesTurn){

            int whiteCounters = 0;
            int index = 18;

            while (index<24){

                if(board[index]>0){

                    whiteCounters += board[index];
                }
                index++;
            }

            if (whiteCounters == (TOTAL_COUNTERS- whiteBornOff)){

                return true;
            }

            else {
                return false;
            }
        }

        else {

            int redCounters = 0;
            int index =0;

            while (index<6){

                if(board[index]<0){

                    redCounters += Math.abs(board[index]);

                }
                index++;
            }

            if (redCounters == (TOTAL_COUNTERS- redBornOff)){
                return true;
            }

            else {
                return false;
            }

        }
    }

    public static void info(){

        printBoard();

        if (whitesTurn){

            System.out.println("White's Turn");

            if(whiteBar>0){

                System.out.println("Must Move off bar");
            }

            if(countersInBearOffPosition()){

                System.out.println("White's Counters are in the bear off position");
            }
        }

        else {
            System.out.println("Red's Turn");

            if(redBar>0){

                System.out.println("Must Move off bar");
            }

            if(countersInBearOffPosition()){

                System.out.println("Red's Counters are in the bear off position");
            }
        }

        System.out.println("Counters Born Off by White: " + whiteBornOff);
        System.out.println("Counters Born Off by Red: " + redBornOff);

        if (rollsLeftForDouble>0){

            System.out.println("Doubles!");
            System.out.println("Value of Dice: " + dice1);
            System.out.println("Rolls Left: " + rollsLeftForDouble);
        }
        else {

            if (dice1!=50 && dice2 !=50){

                System.out.println("Value of First Die: " + dice1);
                System.out.println("Value of Second Die: " + dice2);
            }

            if (dice1 == 50){

                System.out.println("Value of Remaining Die: " + dice2);
            }

            if (dice2 == 50){

                System.out.println("Value of Remaining Die: " + dice1);
            }
        }

    }
    
    public static void printBoardGUI(){
        
        int maxHeight = 5;
        int index = 0;

        while (index<24){

            if(Math.abs(board[index])>maxHeight){

                maxHeight = Math.abs(board[index]);
            }
            index++;
        }

        if(maxHeight<redBar){
            maxHeight = redBar;
        }
        if(maxHeight<whiteBar){
            maxHeight = whiteBar;
        }

        System.out.println("24 23 22 21 20 19    18 17 16 15 14 13 ");

        int a =0;

        while(a<maxHeight){
            int x = 23;
            while(x>11){
                if(x==17){
                    
                    System.out.print("| |");
                  
                }
                if (board[x]>0){
                    if (board[x]-a>0){
                        System.out.print("[B]");
                    }
                    else{

                        System.out.print("[ ]");
                    }
                }
                if (board[x]<0){
                    if (board[x]+a<0){
                        System.out.print("[W]");
                    }
                    else{

                        System.out.print("[ ]");
                    }
                }
                if (board[x] == 0){
                    System.out.print("[ ]");
                }
                x--;
            }
            System.out.println();
            a++;
        }

        System.out.println("                  | |");
        System.out.println("                  | |");
        int redTemp = redBar;
        redBar --;
        a=maxHeight;
        a--;
        while (a>=0){
            int x = 0;
            while(x<12){
                if(x==6){
                    
                    System.out.print("| |");
                    
                }
                if(board[x]>0){
                    if (board[x]>a){
                        System.out.print("[B]");
                    }
                    else{
                        System.out.print("[ ]");
                    }
                }
                if(board[x]<0){
                    if (Math.abs(board[x])>a){
                        System.out.print("[W]");
                    }
                    else{
                        System.out.print("[ ]");
                    }
                }
                if(board[x]==0){
                    System.out.print("[ ]");
                }
                x++;
            }
            System.out.println();
            a--;
        }
        redBar = redTemp;
        System.out.println("01 02 03 04 05 06    07 08 09 10 11 12 ");


    }
        

    public static void printBoard(){

        int maxHeight = 5;
        int index = 0;

        while (index<24){

            if(Math.abs(board[index])>maxHeight){

                maxHeight = Math.abs(board[index]);
            }
            index++;
        }

        if(maxHeight<redBar){
            maxHeight = redBar;
        }
        if(maxHeight<whiteBar){
            maxHeight = whiteBar;
        }

        System.out.println("12 11 10 09 08 07    06 05 04 03 02 01 ");

        int a =0;

        while(a<maxHeight){
            int x = 11;
            while(x>=0){
                if(x==5){
                    if(whiteBar>a){
                        System.out.print("|W|");
                    }
                    else{
                        System.out.print("| |");
                    }
                }
                if (board[x]>0){
                    if (board[x]-a>0){
                        System.out.print("[W]");
                    }
                    else{

                        System.out.print("[ ]");
                    }
                }
                if (board[x]<0){
                    if (board[x]+a<0){
                        System.out.print("[R]");
                    }
                    else{

                        System.out.print("[ ]");
                    }
                }
                if (board[x] == 0){
                    System.out.print("[ ]");
                }
                x--;
            }
            System.out.println();
            a++;
        }

        System.out.println("                  | |");
        System.out.println("                  | |");
        int redTemp = redBar;
        redBar --;
        a=maxHeight;
        a--;
        while (a>=0){
            int x = 12;
            while(x<24){
                if(x==18){
                    if(redBar==a){
                        System.out.print("|R|");
                        redBar--;
                    }
                    else{
                        System.out.print("| |");
                    }
                }
                if(board[x]>0){
                    if (board[x]>a){
                        System.out.print("[W]");
                    }
                    else{
                        System.out.print("[ ]");
                    }
                }
                if(board[x]<0){
                    if (Math.abs(board[x])>a){
                        System.out.print("[R]");
                    }
                    else{
                        System.out.print("[ ]");
                    }
                }
                if(board[x]==0){
                    System.out.print("[ ]");
                }
                x++;
            }
            System.out.println();
            a--;
        }
        redBar = redTemp;
        System.out.println("13 14 15 16 17 18    19 20 21 22 23 24 ");


    }
}
