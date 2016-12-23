package backgammon;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GUI {// need to be able to play single AI

    JFrame mainFrame;
    static String[] pieceColors = {"b", "n", "n", "n", "n", "w", "n", "w", "n", "n", "n", "b",
        "w", "n", "n", "n", "b", "n", "b", "n", "n", "n", "n", "w"};
    static int[] noPieces = {2, 0, 0, 0, 0, 5, 0, 3, 0, 0, 0, 5, 5, 0, 0, 0, 3,
        0, 5, 0, 0, 0, 0, 2};

    static int currentPlayer = 1;
    static int startingTriangle;
    static int endingTriangle;
    int pressedButtons;

    int scoreW;
    int scoreB;

    public GUI() {
        prepareGUI();
    }

    public static void gui(int choice, int AI) throws IOException {
        GUI Game = new GUI();

        Game.showJFrameDemo();
        while (Game.getNoOfWhites() > 0 && Game.getNoOfBlacks() > 0) {

            Game.scoreW = 0;
            Game.scoreB = 0;
            if (Game.checkIfRetriveW()) {
                Game.scoreWhiteRetrieve();
            } else {
                Game.scoreWhite();
            }
            if (Game.checkIfRetriveB()) {
                Game.scoreBlackRetrieve();
            } else {
                Game.scoreBlack();
            }
            Game.dice1 = (int) (Math.random() * 6 + 1);
            Game.dice2 = (int) (Math.random() * 6 + 1);
            Game.diceAgain = 0;
            if (Game.dice1 == Game.dice2) {
                Game.dice1Again = true;
                Game.dice2Again = true;
            } else {
                Game.dice1Again = false;
                Game.dice2Again = false;
            }
            Game.showDicesScores(Game.dice1, Game.dice2, Game.scoreW, Game.scoreB);

            if (CanMove.moveCanBeMadeGUI()) {

                while ((Game.dice1 > 0 || Game.dice2 > 0) && CanMove.moveCanBeMadeGUI()) {

                    BackGammon.translate();
                    BackGammon.printBoardGUI();

                    if (choice == 0) {

                        System.out.println("AI Play Each other");

                        Game.startingTriangle = BackGammon.guiBothAIStart();
                        System.out.println("Starting Triangle: " + Game.startingTriangle);
                    }

                    if (choice == 1) {

                        if (AI == 1) {

                            
                            if (Game.currentPlayer % 2 == 1) {

                                Game.startingTriangle = BackGammon.guiSimpleAIStart();
                                
                                System.out.println("Starting triangle: " + Game.startingTriangle);

                            } else {

                                while (Game.okBegin) {

                                    Game.chooseStart();
                                    //should stay in this while loop until button is pressed
                                    
                                    //Game.okBegin = true;    
                                }
                                
                                System.out.println("Out Start!!");
                            }

                        } 

                        else {

                            if (Game.currentPlayer % 2 == 1) {

                                while (Game.okBegin) {

                                    Game.chooseStart();
                                }
                                
                                System.out.println("Out Start!!");
                                //Game.okBegin =true;
                                
                            } else {

                                Game.startingTriangle = BackGammon.guiComplexAIStart();
                                
                                System.out.println("Starting triangle: " + Game.startingTriangle);
                            }
                        }

                    }

                    if (choice == 2) {

                        while (Game.okBegin) {

                            Game.chooseStart();
                        }
                        
                        System.out.println("Out Start!!");
                        
                        //Game.okBegin = true;

                    }
                    
                    if(choice == 2||(choice==1&&((AI==1&&Game.currentPlayer % 2 == 0)||(AI==2&&Game.currentPlayer%2==1)))){
                        
                        //JOptionPane.showMessageDialog(null, "Starting piece selected!");
                              
                    }
                    
                    //System.out.println("!!" + Game.checkIfRetriveB());
                    if (Game.dice1 > 0 && Game.currentPlayer % 2 == 1 && Game.checkIfRetriveW() && Game.pieceColors[Game.dice1 - 1].equals("w") && Game.startingTriangle == Game.dice1) {
                        if (Game.noPieces[Game.startingTriangle - 1] == 1) {
                            Game.pieceColors[Game.startingTriangle - 1] = "n";
                        }
                        Game.noPieces[Game.startingTriangle - 1]--;

                        if (Game.dice1 == Game.dice2) {
                            Game.diceAgain++;
                            if (Game.diceAgain == 3) {
                                Game.dice1Again = false;
                            }
                            if (Game.diceAgain == 4) {
                                Game.dice2Again = false;
                            }
                        }

                        if (Game.dice1Again == false && Game.dice2Again == false && Game.diceAgain == 0) {
                            Game.dice1 = -1;
                        } else if (Game.dice1Again == false && Game.dice2Again == false) {
                            Game.dice1 = -1;
                            Game.dice2 = -1;
                        }
                        Game.displayOnButtons();
                    } else if (Game.dice2 > 0 && Game.currentPlayer % 2 == 1 && Game.checkIfRetriveW() && Game.pieceColors[Game.dice2 - 1].equals("w") && Game.startingTriangle == Game.dice2) {
                        if (Game.noPieces[Game.startingTriangle - 1] == 1) {
                            Game.pieceColors[Game.startingTriangle - 1] = "n";
                        }
                        Game.noPieces[Game.startingTriangle - 1]--;
                        Game.dice2 = -1;
                        Game.displayOnButtons();
                    } else if (Game.dice1 > 0 && Game.currentPlayer % 2 == 0 && Game.checkIfRetriveB() && Game.pieceColors[24 - Game.dice1].equals("b") && (25 - Game.startingTriangle == Game.dice1)) {
                        //System.out.println("HERE");
                        if (Game.noPieces[Game.startingTriangle - 1] == 1) {
                            Game.pieceColors[Game.startingTriangle - 1] = "n";
                        }
                        Game.noPieces[Game.startingTriangle - 1]--;
                        if (Game.dice1 == Game.dice2) {
                            Game.diceAgain++;
                            if (Game.diceAgain == 3) {
                                Game.dice1Again = false;
                            }
                            if (Game.diceAgain == 4) {
                                Game.dice2Again = false;
                            }
                        }

                        if (Game.dice1Again == false && Game.dice2Again == false && Game.diceAgain == 0) {
                            Game.dice1 = -1;
                        } else if (Game.dice1Again == false && Game.dice2Again == false) {
                            Game.dice1 = -1;
                            Game.dice2 = -1;
                        }
                        Game.displayOnButtons();
                    } else if (Game.dice2 > 0 && Game.currentPlayer % 2 == 0 && Game.checkIfRetriveB() && Game.pieceColors[24 - Game.dice2].equals("b") && (25 - Game.startingTriangle == Game.dice2)) {
                        if (Game.noPieces[Game.startingTriangle - 1] == 1) {
                            Game.pieceColors[Game.startingTriangle - 1] = "n";
                        }
                        Game.noPieces[Game.startingTriangle - 1]--;
                        Game.dice2 = -1;
                        Game.displayOnButtons();
                    } else {

                        if (choice == 0) {

                            Game.endingTriangle = BackGammon.guiBothAIEnd();
                            System.out.println("End Triangle: " + Game.endingTriangle);
                        }

                        if (choice == 1) {

                            if (AI == 1) {
                                
                                
                                if (Game.currentPlayer % 2 == 1) {

                                    Game.endingTriangle = BackGammon.guiSimpleAIEnd();
                                    System.out.println("Ending triangle: " + Game.endingTriangle);

                                } else {
                                    
                                    //okBegin Would be false at this point as needs to get out of previous while loop

                                    while (Game.okStart) {

                                        Game.chooseEnd();
                                    }
                                    
                                    System.out.println("Out End!!");
                                }
                                

                            } 
                            else{

                                if (Game.currentPlayer % 2 == 1) {

                                    while (Game.okStart) {

                                        Game.chooseEnd();
                                    }
                                    
                                    System.out.println("Out End!!");
                                } else {

                                    Game.endingTriangle = BackGammon.guiComplexAIEnd();
                                    System.out.println("Ending triangle: " + Game.endingTriangle);
                                }
                            }

                        }

                        if (choice == 2) {

                            while (Game.okStart) {

                                Game.chooseEnd();
                            }
                            
                            System.out.println("Out End!!");

                        }

                        
                        if (Game.currentPlayer % 2 == 1) {
                            if (Game.dice1 > 0 && Game.checkMoveW1(Game.startingTriangle, Game.endingTriangle, Game.dice1)) {
                                System.out.println(Game.endingTriangle);
                                if (Game.pieceColors[Game.endingTriangle - 1].equals("b")) {
                                    Game.moveBlackToStart();
                                    Game.noPieces[Game.endingTriangle - 1]--;
                                }
                                /*System.out.println("Move done White Dice 1!");
                                 System.out.println("start1: " + Game.startingTriangle);
                                 System.out.println("end1: " + Game.endingTriangle);*/
                                Game.noPieces[Game.endingTriangle - 1]++;
                                Game.noPieces[Game.startingTriangle - 1]--;
                                if (Game.dice1 == Game.dice2) {
                                    Game.diceAgain++;
                                    if (Game.diceAgain == 3) {
                                        Game.dice1Again = false;
                                    }
                                    if (Game.diceAgain == 4) {
                                        Game.dice2Again = false;
                                    }
                                }

                                if (Game.dice1Again == false && Game.dice2Again == false && Game.diceAgain == 0) {
                                    Game.dice1 = -1;
                                } else if (Game.dice1Again == false && Game.dice2Again == false) {
                                    Game.dice1 = -1;
                                    Game.dice2 = -1;
                                }
                                Game.pieceColors[Game.endingTriangle - 1] = "w";
                                Game.displayOnButtons();
                                if (Game.noPieces[Game.startingTriangle - 1] == 0) {
                                    Game.pieceColors[Game.startingTriangle - 1] = "n";
                                }
                            } else if (Game.dice2 > 0 && Game.checkMoveW2(Game.startingTriangle, Game.endingTriangle, Game.dice2)) {
                                if (Game.pieceColors[Game.endingTriangle - 1].equals("b")) {
                                    Game.moveBlackToStart();
                                    Game.noPieces[Game.endingTriangle - 1]--;
                                }
                                /*System.out.println("Move done White Dice 2!");
                                 System.out.println("start2: " + Game.startingTriangle);
                                 System.out.println("end2: " + Game.endingTriangle);*/
                                Game.noPieces[Game.endingTriangle - 1]++;
                                Game.noPieces[Game.startingTriangle - 1]--;
                                Game.dice2 = -1;
                                Game.pieceColors[Game.endingTriangle - 1] = "w";
                                Game.displayOnButtons();
                                if (Game.noPieces[Game.startingTriangle - 1] == 0) {
                                    Game.pieceColors[Game.startingTriangle - 1] = "n";
                                }
                            }
                        }

                        if (Game.currentPlayer % 2 == 0) {
                            if (Game.dice1 > 0 && Game.checkMoveB1(Game.startingTriangle, Game.endingTriangle, Game.dice1)) {
                                if (Game.pieceColors[Game.endingTriangle - 1].equals("w")) {
                                    Game.moveWhiteToStart();
                                    Game.noPieces[Game.endingTriangle - 1]--;
                                }
                                //System.out.println("Move done Black Dice 1!");
                                Game.noPieces[Game.endingTriangle - 1]++;
                                Game.noPieces[Game.startingTriangle - 1]--;
                                if (Game.dice1 == Game.dice2) {
                                    Game.diceAgain++;
                                    if (Game.diceAgain == 3) {
                                        Game.dice1Again = false;
                                    }
                                    if (Game.diceAgain == 4) {
                                        Game.dice2Again = false;
                                    }
                                }

                                if (Game.dice1Again == false && Game.dice2Again == false && Game.diceAgain == 0) {
                                    Game.dice1 = -1;
                                } else if (Game.dice1Again == false && Game.dice2Again == false) {
                                    Game.dice1 = -1;
                                    Game.dice2 = -1;
                                }
                                Game.pieceColors[Game.endingTriangle - 1] = "b";
                                Game.displayOnButtons();
                                if (Game.noPieces[Game.startingTriangle - 1] == 0) {
                                    Game.pieceColors[Game.startingTriangle - 1] = "n";
                                }
                            } else if (Game.dice2 > 0 && Game.checkMoveB2(Game.startingTriangle, Game.endingTriangle, Game.dice2)) {
                                if (Game.pieceColors[Game.endingTriangle - 1].equals("w")) {
                                    Game.moveWhiteToStart();
                                    Game.noPieces[Game.endingTriangle - 1]--;
                                }
                                //System.out.println("Move done Black Dice 2!");
                                Game.noPieces[Game.endingTriangle - 1]++;
                                Game.noPieces[Game.startingTriangle - 1]--;
                                Game.dice2 = -1;
                                Game.pieceColors[Game.endingTriangle - 1] = "b";
                                Game.displayOnButtons();
                                if (Game.noPieces[Game.startingTriangle - 1] == 0) {
                                    Game.pieceColors[Game.startingTriangle - 1] = "n";
                                }
                            }
                        }
                        Game.printNoPieces();
                        Game.printPieceColors();
                    }

                }
            } else {
                System.out.println("No Posible Moves");
                BackGammon.printBoardGUI();
            }

            Game.currentPlayer++;

        }
        if (Game.getNoOfWhites() == 0) {
            JOptionPane.showMessageDialog(null, "White won!");
        } else {
            JOptionPane.showMessageDialog(null, "Black won!");
        }
        System.exit(0);

    }

    private void prepareGUI() {
        mainFrame = new JFrame("Backgammon");
        mainFrame.setSize(1315, 595);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }
    boolean okBegin = true;
    boolean okStart;

    static int dice1;
    static int dice2;
    int diceAgain;
    boolean dice1Again;
    boolean dice2Again;

    JButton button1;
    JButton button2;
    JButton button3;
    JButton button4;
    JButton button5;
    JButton button6;

    JButton button7;
    JButton button8;
    JButton button9;
    JButton button10;
    JButton button11;
    JButton button12;

    //JButton buttonMiddle;
    JLabel middleDices;
    JLabel middleScoreW;
    JLabel middleScoreB;

    JButton button13;
    JButton button14;
    JButton button15;
    JButton button16;
    JButton button17;
    JButton button18;

    JButton button19;
    JButton button20;
    JButton button21;
    JButton button22;
    JButton button23;
    JButton button24;

    JLabel piecesButton1;
    JLabel piecesButton2;
    JLabel piecesButton3;
    JLabel piecesButton4;
    JLabel piecesButton5;
    JLabel piecesButton6;

    JLabel piecesButton7;
    JLabel piecesButton8;
    JLabel piecesButton9;
    JLabel piecesButton10;
    JLabel piecesButton11;
    JLabel piecesButton12;

    JLabel piecesButton13;
    JLabel piecesButton14;
    JLabel piecesButton15;
    JLabel piecesButton16;
    JLabel piecesButton17;
    JLabel piecesButton18;

    JLabel piecesButton19;
    JLabel piecesButton20;
    JLabel piecesButton21;
    JLabel piecesButton22;
    JLabel piecesButton23;
    JLabel piecesButton24;

    private void showJFrameDemo() throws IOException {
        //System.out.println(getClass().getResource("triangle.png"));

        middleDices = new JLabel("Dices", SwingConstants.CENTER);
        middleDices.setBounds(600, 0, 100, 200);

        middleScoreW = new JLabel("Score White: ", SwingConstants.CENTER);
        middleScoreW.setBounds(600, 200, 100, 200);

        middleScoreB = new JLabel("Score Black: ", SwingConstants.CENTER);
        middleScoreB.setBounds(600, 400, 100, 200);

        piecesButton1 = new JLabel("2 b", SwingConstants.CENTER);
        piecesButton1.setBounds(0, 310, 90, 50);

        piecesButton2 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton2.setBounds(100, 310, 90, 50);

        piecesButton3 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton3.setBounds(200, 310, 90, 50);

        piecesButton4 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton4.setBounds(300, 310, 90, 50);

        piecesButton5 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton5.setBounds(400, 310, 90, 50);

        piecesButton6 = new JLabel("5 w", SwingConstants.CENTER);
        piecesButton6.setBounds(500, 310, 90, 50);

        piecesButton7 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton7.setBounds(700, 310, 90, 50);

        piecesButton8 = new JLabel("3 w", SwingConstants.CENTER);
        piecesButton8.setBounds(800, 310, 90, 50);

        piecesButton9 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton9.setBounds(900, 310, 90, 50);

        piecesButton10 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton10.setBounds(1000, 310, 90, 50);

        piecesButton11 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton11.setBounds(1100, 310, 90, 50);

        piecesButton12 = new JLabel("5 b", SwingConstants.CENTER);
        piecesButton12.setBounds(1200, 310, 90, 50);

        piecesButton13 = new JLabel("5 w", SwingConstants.CENTER);
        piecesButton13.setBounds(1200, 200, 90, 50);

        piecesButton14 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton14.setBounds(1100, 200, 90, 50);

        piecesButton15 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton15.setBounds(1000, 200, 90, 50);

        piecesButton16 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton16.setBounds(900, 200, 90, 50);

        piecesButton17 = new JLabel("3 b", SwingConstants.CENTER);
        piecesButton17.setBounds(800, 200, 90, 50);

        piecesButton18 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton18.setBounds(700, 200, 90, 50);

        piecesButton19 = new JLabel("5 b", SwingConstants.CENTER);
        piecesButton19.setBounds(500, 200, 90, 50);

        piecesButton20 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton20.setBounds(400, 200, 90, 50);

        piecesButton21 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton21.setBounds(300, 200, 90, 50);

        piecesButton22 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton22.setBounds(200, 200, 90, 50);

        piecesButton23 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton23.setBounds(100, 200, 90, 50);

        piecesButton24 = new JLabel("2 w", SwingConstants.CENTER);
        piecesButton24.setBounds(0, 200, 90, 50);

        mainFrame.setLayout(null);
        BufferedImage redTriangle, blackTriangle, redTriangleR, blackTriangleR;
        redTriangle = ImageIO.read(getClass().getResource("blackTriangle.png"));
        blackTriangle = ImageIO.read(getClass().getResource("redTriangle.png"));

        redTriangleR = ImageIO.read(getClass().getResource("blackTriangleR.png"));
        blackTriangleR = ImageIO.read(getClass().getResource("redTriangleR.png"));

        button1 = new JButton(new ImageIcon(redTriangle));
        button1.setBorder(BorderFactory.createEmptyBorder());
        button1.setContentAreaFilled(false);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button1");
            }
        });

        button2 = new JButton(new ImageIcon(blackTriangle));
        button2.setBorder(BorderFactory.createEmptyBorder());
        button2.setContentAreaFilled(false);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button2");
            }
        });

        button3 = new JButton(new ImageIcon(redTriangle));
        button3.setBorder(BorderFactory.createEmptyBorder());
        button3.setContentAreaFilled(false);
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button3");
            }
        });

        button4 = new JButton(new ImageIcon(blackTriangle));
        button4.setBorder(BorderFactory.createEmptyBorder());
        button4.setContentAreaFilled(false);
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button4");
            }
        });

        button5 = new JButton(new ImageIcon(redTriangle));
        button5.setBorder(BorderFactory.createEmptyBorder());
        button5.setContentAreaFilled(false);
        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button5");
            }
        });

        button6 = new JButton(new ImageIcon(blackTriangle));
        button6.setBorder(BorderFactory.createEmptyBorder());
        button6.setContentAreaFilled(false);
        button6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button6");
            }
        });

        button7 = new JButton(new ImageIcon(redTriangle));
        button7.setBorder(BorderFactory.createEmptyBorder());
        button7.setContentAreaFilled(false);
        button7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button7");
            }
        });

        button8 = new JButton(new ImageIcon(blackTriangle));
        button8.setBorder(BorderFactory.createEmptyBorder());
        button8.setContentAreaFilled(false);
        button8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button8");
            }
        });

        button9 = new JButton(new ImageIcon(redTriangle));
        button9.setBorder(BorderFactory.createEmptyBorder());
        button9.setContentAreaFilled(false);
        button9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button9");
            }
        });

        button10 = new JButton(new ImageIcon(blackTriangle));
        button10.setBorder(BorderFactory.createEmptyBorder());
        button10.setContentAreaFilled(false);
        button10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button10");
            }
        });

        button11 = new JButton(new ImageIcon(redTriangle));
        button11.setBorder(BorderFactory.createEmptyBorder());
        button11.setContentAreaFilled(false);
        button11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button11");
            }
        });
        button12 = new JButton(new ImageIcon(blackTriangle));
        button12.setBorder(BorderFactory.createEmptyBorder());
        button12.setContentAreaFilled(false);
        button12.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button12");
            }
        });

        button13 = new JButton(new ImageIcon(blackTriangleR));
        button13.setBorder(BorderFactory.createEmptyBorder());
        button13.setContentAreaFilled(false);
        button13.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button13");
            }
        });

        button14 = new JButton(new ImageIcon(redTriangleR));
        button14.setBorder(BorderFactory.createEmptyBorder());
        button14.setContentAreaFilled(false);
        button14.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button14");
            }
        });

        button15 = new JButton(new ImageIcon(blackTriangleR));
        button15.setBorder(BorderFactory.createEmptyBorder());
        button15.setContentAreaFilled(false);
        button15.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button15");
            }
        });

        button16 = new JButton(new ImageIcon(redTriangleR));
        button16.setBorder(BorderFactory.createEmptyBorder());
        button16.setContentAreaFilled(false);
        button16.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button16");
            }
        });

        button17 = new JButton(new ImageIcon(blackTriangleR));
        button17.setBorder(BorderFactory.createEmptyBorder());
        button17.setContentAreaFilled(false);
        button17.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button17");
            }
        });

        button18 = new JButton(new ImageIcon(redTriangleR));
        button18.setBorder(BorderFactory.createEmptyBorder());
        button18.setContentAreaFilled(false);
        button18.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button18");
            }
        });

        button19 = new JButton(new ImageIcon(blackTriangleR));
        button19.setBorder(BorderFactory.createEmptyBorder());
        button19.setContentAreaFilled(false);
        button19.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button19");
            }
        });

        button20 = new JButton(new ImageIcon(redTriangleR));
        button20.setBorder(BorderFactory.createEmptyBorder());
        button20.setContentAreaFilled(false);
        button20.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button20");
            }
        });

        button21 = new JButton(new ImageIcon(blackTriangleR));
        button21.setBorder(BorderFactory.createEmptyBorder());
        button21.setContentAreaFilled(false);
        button21.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button21");
            }
        });

        button22 = new JButton(new ImageIcon(redTriangleR));
        button22.setBorder(BorderFactory.createEmptyBorder());
        button22.setContentAreaFilled(false);
        button22.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button22");
            }
        });

        button23 = new JButton(new ImageIcon(blackTriangleR));
        button23.setBorder(BorderFactory.createEmptyBorder());
        button23.setContentAreaFilled(false);
        button23.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button23");
            }
        });

        button24 = new JButton(new ImageIcon(redTriangleR));
        button24.setBorder(BorderFactory.createEmptyBorder());
        button24.setContentAreaFilled(false);
        button24.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("button24");
            }
        });

        button1.setBounds(0, 360, 100, 200);
        button2.setBounds(100, 360, 100, 200);
        button3.setBounds(200, 360, 100, 200);
        button4.setBounds(300, 360, 100, 200);
        button5.setBounds(400, 360, 100, 200);
        button6.setBounds(500, 360, 100, 200);
        button7.setBounds(700, 360, 100, 200);
        button8.setBounds(800, 360, 100, 200);
        button9.setBounds(900, 360, 100, 200);
        button10.setBounds(1000, 360, 100, 200);
        button11.setBounds(1100, 360, 100, 200);
        button12.setBounds(1200, 360, 100, 200);

        //buttonMiddle.setBounds(600, 0, 100, 600);
        button13.setBounds(1200, -5, 100, 200);
        button14.setBounds(1100, -5, 100, 200);
        button15.setBounds(1000, -5, 100, 200);
        button16.setBounds(900, -5, 100, 200);
        button17.setBounds(800, -5, 100, 200);
        button18.setBounds(700, -5, 100, 200);
        button19.setBounds(500, -5, 100, 200);
        button20.setBounds(400, -5, 100, 200);
        button21.setBounds(300, -5, 100, 200);
        button22.setBounds(200, -5, 100, 200);
        button23.setBounds(100, -5, 100, 200);
        button24.setBounds(0, -5, 100, 200);

        mainFrame.add(button1);
        mainFrame.add(button2);
        mainFrame.add(button3);
        mainFrame.add(button4);
        mainFrame.add(button5);
        mainFrame.add(button6);
        mainFrame.add(button7);
        mainFrame.add(button8);
        mainFrame.add(button9);
        mainFrame.add(button10);
        mainFrame.add(button11);
        mainFrame.add(button12);

        mainFrame.add(middleDices);
        mainFrame.add(middleScoreW);
        mainFrame.add(middleScoreB);

        mainFrame.add(button13);
        mainFrame.add(button14);
        mainFrame.add(button15);
        mainFrame.add(button16);
        mainFrame.add(button17);
        mainFrame.add(button18);
        mainFrame.add(button19);
        mainFrame.add(button20);
        mainFrame.add(button21);
        mainFrame.add(button22);
        mainFrame.add(button23);
        mainFrame.add(button24);

        mainFrame.add(piecesButton1);
        mainFrame.add(piecesButton2);
        mainFrame.add(piecesButton3);
        mainFrame.add(piecesButton4);
        mainFrame.add(piecesButton5);
        mainFrame.add(piecesButton6);

        mainFrame.add(piecesButton7);
        mainFrame.add(piecesButton8);
        mainFrame.add(piecesButton9);
        mainFrame.add(piecesButton10);
        mainFrame.add(piecesButton11);
        mainFrame.add(piecesButton12);

        mainFrame.add(piecesButton13);
        mainFrame.add(piecesButton14);
        mainFrame.add(piecesButton15);
        mainFrame.add(piecesButton16);
        mainFrame.add(piecesButton17);
        mainFrame.add(piecesButton18);

        mainFrame.add(piecesButton19);
        mainFrame.add(piecesButton20);
        mainFrame.add(piecesButton21);
        mainFrame.add(piecesButton22);
        mainFrame.add(piecesButton23);
        mainFrame.add(piecesButton24);
        mainFrame.setVisible(true);

    }

    public void chooseStart() {
        okStart = false;
        if (button1.getModel().isPressed()) {
            if (currentPlayer % 2 == 1 && pieceColors[0].equals("w") && noPieces[0] > 0) {
                startingTriangle = 1;
                okStart = true;
                button1.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[0].equals("b") && noPieces[0] > 0) {
                startingTriangle = 1;
                okStart = true;
                button1.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button2.getModel().isPressed()) {
            if (currentPlayer % 2 == 1 && pieceColors[1].equals("w") && noPieces[1] > 0) {
                startingTriangle = 12;
                pressedButtons++;
                okStart = true;
                button2.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[1].equals("b") && noPieces[1] > 0) {
                startingTriangle = 2;
                pressedButtons++;
                okStart = true;
                button2.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button3.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[2].equals("w") && noPieces[2] > 0) {
                startingTriangle = 3;
                okStart = true;
                button3.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[2].equals("b") && noPieces[2] > 0) {
                startingTriangle = 3;
                okStart = true;
                button3.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button4.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[3].equals("w") && noPieces[3] > 0) {
                startingTriangle = 4;
                okStart = true;
                button4.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[3].equals("b") && noPieces[3] > 0) {
                startingTriangle = 4;
                okStart = true;
                button4.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button5.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[4].equals("w") && noPieces[4] > 0) {
                startingTriangle = 5;
                okStart = true;
                button5.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[4].equals("b") && noPieces[4] > 0) {
                startingTriangle = 5;
                okStart = true;
                button5.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button6.getModel().isPressed()) {
            if (currentPlayer % 2 == 1 && pieceColors[5].equals("w") && noPieces[5] > 0) {
                startingTriangle = 6;
                okStart = true;
                button6.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[5].equals("b") && noPieces[5] > 0) {
                startingTriangle = 6;
                okStart = true;
                button6.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button7.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[6].equals("w") && noPieces[6] > 0) {
                startingTriangle = 7;
                okStart = true;
                button7.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[6].equals("b") && noPieces[6] > 0) {
                startingTriangle = 7;
                okStart = true;
                button7.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button8.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[7].equals("w") && noPieces[7] > 0) {
                startingTriangle = 8;
                okStart = true;
                button8.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[7].equals("b") && noPieces[7] > 0) {
                startingTriangle = 8;
                okStart = true;
                button8.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button9.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[8].equals("w") && noPieces[8] > 0) {
                startingTriangle = 9;
                okStart = true;
                button9.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[8].equals("b") && noPieces[8] > 0) {
                startingTriangle = 9;
                okStart = true;
                button9.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button10.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[9].equals("w") && noPieces[9] > 0) {
                startingTriangle = 10;
                okStart = true;
                button10.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[9].equals("b") && noPieces[9] > 0) {
                startingTriangle = 10;
                okStart = true;
                button10.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button11.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[10].equals("w") && noPieces[10] > 0) {
                startingTriangle = 11;
                okStart = true;
                button11.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[10].equals("b") && noPieces[10] > 0) {
                startingTriangle = 11;
                okStart = true;
                button11.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button12.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[11].equals("w") && noPieces[11] > 0) {
                startingTriangle = 12;
                okStart = true;
                button12.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[11].equals("b") && noPieces[11] > 0) {
                startingTriangle = 12;
                okStart = true;
                button12.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button13.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[12].equals("w") && noPieces[12] > 0) {
                startingTriangle = 13;
                okStart = true;
                button13.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[12].equals("b") && noPieces[12] > 0) {
                startingTriangle = 13;
                okStart = true;
                button13.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button14.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[13].equals("w") && noPieces[13] > 0) {
                startingTriangle = 14;
                okStart = true;
                button14.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[13].equals("b") && noPieces[13] > 0) {
                startingTriangle = 14;
                okStart = true;
                button14.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button15.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[14].equals("w") && noPieces[14] > 0) {
                startingTriangle = 15;
                okStart = true;
                button15.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[14].equals("b") && noPieces[14] > 0) {
                startingTriangle = 15;
                okStart = true;
                button15.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button16.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[15].equals("w") && noPieces[15] > 0) {
                startingTriangle = 16;
                okStart = true;
                button16.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[15].equals("b") && noPieces[15] > 0) {
                startingTriangle = 16;
                okStart = true;
                button16.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button17.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[16].equals("w") && noPieces[16] > 0) {
                startingTriangle = 17;
                okStart = true;
                button17.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[16].equals("b") && noPieces[16] > 0) {
                startingTriangle = 17;
                okStart = true;
                button17.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button18.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[17].equals("w") && noPieces[17] > 0) {
                startingTriangle = 18;
                okStart = true;
                button18.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[17].equals("b") && noPieces[17] > 0) {
                startingTriangle = 18;
                okStart = true;
                button18.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button19.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[18].equals("w") && noPieces[18] > 0) {
                startingTriangle = 19;
                okStart = true;
                button19.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[18].equals("b") && noPieces[18] > 0) {
                startingTriangle = 19;
                okStart = true;
                button19.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button20.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[19].equals("w") && noPieces[19] > 0) {
                startingTriangle = 20;
                okStart = true;
                button20.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[19].equals("b") && noPieces[19] > 0) {
                startingTriangle = 20;
                okStart = true;
                button20.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button21.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[20].equals("w") && noPieces[20] > 0) {
                startingTriangle = 21;
                okStart = true;
                button21.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[20].equals("b") && noPieces[20] > 0) {
                startingTriangle = 21;
                okStart = true;
                button21.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button22.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[21].equals("w") && noPieces[21] > 0) {
                startingTriangle = 22;
                okStart = true;
                button22.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[21].equals("b") && noPieces[21] > 0) {
                startingTriangle = 22;
                okStart = true;
                button22.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button23.getModel().isPressed()) {

            if (currentPlayer % 2 == 1 && pieceColors[22].equals("w") && noPieces[22] > 0) {
                startingTriangle = 23;
                okStart = true;
                button23.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[22].equals("b") && noPieces[22] > 0) {
                startingTriangle = 23;
                okStart = true;
                button23.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button24.getModel().isPressed()) {
            if (currentPlayer % 2 == 1 && pieceColors[23].equals("w") && noPieces[23] > 0) {
                startingTriangle = 24;
                okStart = true;
                button24.getModel().setPressed(false);
            } else if (currentPlayer % 2 == 0 && pieceColors[23].equals("b") && noPieces[23] > 0) {
                startingTriangle = 24;
                okStart = true;
                button24.getModel().setPressed(false);
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }
        okBegin = (!okStart);
        button1.getModel().setPressed(false);
        button2.getModel().setPressed(false);
        button3.getModel().setPressed(false);
        button4.getModel().setPressed(false);
        button5.getModel().setPressed(false);
        button6.getModel().setPressed(false);

        button7.getModel().setPressed(false);
        button8.getModel().setPressed(false);
        button9.getModel().setPressed(false);
        button10.getModel().setPressed(false);
        button11.getModel().setPressed(false);
        button12.getModel().setPressed(false);

        button13.getModel().setPressed(false);
        button14.getModel().setPressed(false);
        button15.getModel().setPressed(false);
        button16.getModel().setPressed(false);
        button17.getModel().setPressed(false);
        button18.getModel().setPressed(false);

        button19.getModel().setPressed(false);
        button20.getModel().setPressed(false);
        button21.getModel().setPressed(false);
        button22.getModel().setPressed(false);
        button23.getModel().setPressed(false);
        button24.getModel().setPressed(false);
    }

    public void chooseEnd() {
        if (button1.getModel().isPressed()) {

            if (startingTriangle != 1 && currentPlayer % 2 == 1 && checkDestinationW(0)) {
                endingTriangle = 1;
                okBegin = true;
            } else if (startingTriangle != 1 && currentPlayer % 2 == 0 && checkDestinationB(0)) {
                endingTriangle = 1;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button2.getModel().isPressed()) {

            if (startingTriangle != 2 && currentPlayer % 2 == 1 && checkDestinationW(1)) {
                endingTriangle = 2;
                okBegin = true;
            } else if (startingTriangle != 2 && currentPlayer % 2 == 0 && checkDestinationB(1)) {
                endingTriangle = 2;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button3.getModel().isPressed()) {

            if (startingTriangle != 3 && currentPlayer % 2 == 1 && checkDestinationW(2)) {
                endingTriangle = 3;
                okBegin = true;
            } else if (startingTriangle != 3 && currentPlayer % 2 == 0 && checkDestinationB(2)) {
                endingTriangle = 3;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button4.getModel().isPressed()) {

            if (startingTriangle != 4 && currentPlayer % 2 == 1 && checkDestinationW(3)) {
                endingTriangle = 4;
                okBegin = true;
            } else if (startingTriangle != 4 && currentPlayer % 2 == 0 && checkDestinationB(3)) {
                endingTriangle = 4;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button5.getModel().isPressed()) {

            if (startingTriangle != 5 && currentPlayer % 2 == 1 && checkDestinationW(4)) {
                endingTriangle = 5;
                okBegin = true;
            } else if (startingTriangle != 5 && currentPlayer % 2 == 0 && checkDestinationB(4)) {
                endingTriangle = 5;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button6.getModel().isPressed()) {
            if (startingTriangle != 6 && currentPlayer % 2 == 1 && checkDestinationW(5)) {
                endingTriangle = 6;
                okBegin = true;
            } else if (startingTriangle != 6 && currentPlayer % 2 == 0 && checkDestinationB(5)) {
                endingTriangle = 6;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button7.getModel().isPressed()) {
            if (startingTriangle != 7 && currentPlayer % 2 == 1 && checkDestinationW(6)) {
                endingTriangle = 7;
                okBegin = true;
            } else if (startingTriangle != 7 && currentPlayer % 2 == 0 && checkDestinationB(6)) {
                endingTriangle = 7;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button8.getModel().isPressed()) {

            if (startingTriangle != 8 && currentPlayer % 2 == 1 && checkDestinationW(7)) {
                endingTriangle = 8;
                okBegin = true;
            } else if (startingTriangle != 8 && currentPlayer % 2 == 0 && checkDestinationB(7)) {
                endingTriangle = 8;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button9.getModel().isPressed()) {

            if (startingTriangle != 9 && currentPlayer % 2 == 1 && checkDestinationW(8)) {
                endingTriangle = 9;
                okBegin = true;
            } else if (startingTriangle != 9 && currentPlayer % 2 == 0 && checkDestinationB(8)) {
                endingTriangle = 9;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button10.getModel().isPressed()) {

            if (startingTriangle != 10 && currentPlayer % 2 == 1 && checkDestinationW(9)) {
                endingTriangle = 10;
                okBegin = true;
            } else if (startingTriangle != 10 && currentPlayer % 2 == 0 && checkDestinationB(9)) {
                endingTriangle = 10;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button11.getModel().isPressed()) {

            if (startingTriangle != 11 && currentPlayer % 2 == 1 && checkDestinationW(10)) {
                endingTriangle = 11;
                okBegin = true;
            } else if (startingTriangle != 11 && currentPlayer % 2 == 0 && checkDestinationB(10)) {
                endingTriangle = 11;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }

        }

        if (button12.getModel().isPressed()) {

            if (startingTriangle != 12 && currentPlayer % 2 == 1 && checkDestinationW(11)) {
                endingTriangle = 12;
                okBegin = true;
            } else if (startingTriangle != 12 && currentPlayer % 2 == 0 && checkDestinationB(11)) {
                endingTriangle = 12;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button13.getModel().isPressed()) {

            if (startingTriangle != 13 && currentPlayer % 2 == 1 && checkDestinationW(12)) {
                endingTriangle = 13;
                okBegin = true;
            } else if (startingTriangle != 13 && currentPlayer % 2 == 0 && checkDestinationB(12)) {
                endingTriangle = 13;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button14.getModel().isPressed()) {

            if (startingTriangle != 14 && currentPlayer % 2 == 1 && checkDestinationW(13)) {
                endingTriangle = 14;
                okBegin = true;
            } else if (startingTriangle != 14 && currentPlayer % 2 == 0 && checkDestinationB(13)) {
                endingTriangle = 14;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button15.getModel().isPressed()) {

            if (startingTriangle != 15 && currentPlayer % 2 == 1 && checkDestinationW(14)) {
                endingTriangle = 15;
                okBegin = true;
            } else if (startingTriangle != 15 && currentPlayer % 2 == 0 && checkDestinationB(14)) {
                endingTriangle = 15;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button16.getModel().isPressed()) {

            if (startingTriangle != 16 && currentPlayer % 2 == 1 && checkDestinationW(15)) {
                endingTriangle = 16;
                okBegin = true;
            } else if (startingTriangle != 16 && currentPlayer % 2 == 0 && checkDestinationB(15)) {
                endingTriangle = 16;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button17.getModel().isPressed()) {

            if (startingTriangle != 17 && currentPlayer % 2 == 1 && checkDestinationW(16)) {
                endingTriangle = 17;
                okBegin = true;
            } else if (startingTriangle != 17 && currentPlayer % 2 == 0 && checkDestinationB(16)) {
                endingTriangle = 17;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button18.getModel().isPressed()) {

            if (startingTriangle != 18 && currentPlayer % 2 == 1 && checkDestinationW(17)) {
                endingTriangle = 18;
                okBegin = true;
            } else if (startingTriangle != 18 && currentPlayer % 2 == 0 && checkDestinationB(17)) {
                endingTriangle = 18;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button19.getModel().isPressed()) {

            if (startingTriangle != 19 && currentPlayer % 2 == 1 && checkDestinationW(18)) {
                endingTriangle = 19;
                okBegin = true;
            } else if (startingTriangle != 19 && currentPlayer % 2 == 0 && checkDestinationB(18)) {
                endingTriangle = 19;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button20.getModel().isPressed()) {

            if (startingTriangle != 20 && currentPlayer % 2 == 1 && checkDestinationW(19)) {
                endingTriangle = 20;
                okBegin = true;
            } else if (startingTriangle != 20 && currentPlayer % 2 == 0 && checkDestinationB(19)) {
                endingTriangle = 20;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button21.getModel().isPressed()) {

            if (startingTriangle != 21 && currentPlayer % 2 == 1 && checkDestinationW(20)) {
                endingTriangle = 21;
                okBegin = true;
            } else if (startingTriangle != 21 && currentPlayer % 2 == 0 && checkDestinationB(20)) {
                endingTriangle = 21;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button22.getModel().isPressed()) {

            if (startingTriangle != 22 && currentPlayer % 2 == 1 && checkDestinationW(21)) {
                endingTriangle = 22;
                okBegin = true;
            } else if (startingTriangle != 22 && currentPlayer % 2 == 0 && checkDestinationB(21)) {
                endingTriangle = 22;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button23.getModel().isPressed()) {

            if (startingTriangle != 23 && currentPlayer % 2 == 1 && checkDestinationW(22)) {
                endingTriangle = 23;
                okBegin = true;
            } else if (startingTriangle != 23 && currentPlayer % 2 == 0 && checkDestinationB(22)) {
                endingTriangle = 23;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }

        if (button24.getModel().isPressed()) {

            if (startingTriangle != 24 && currentPlayer % 2 == 1 && checkDestinationW(23)) {
                endingTriangle = 24;
                okBegin = true;
            } else if (startingTriangle != 24 && currentPlayer % 2 == 0 && checkDestinationB(23)) {
                endingTriangle = 24;
                okBegin = true;
            } else {
                JOptionPane.showMessageDialog(null, "Move not allowed!");
            }
        }
        okStart = (!okBegin);
        button1.getModel().setPressed(false);
        button2.getModel().setPressed(false);
        button3.getModel().setPressed(false);
        button4.getModel().setPressed(false);
        button5.getModel().setPressed(false);
        button6.getModel().setPressed(false);

        button7.getModel().setPressed(false);
        button8.getModel().setPressed(false);
        button9.getModel().setPressed(false);
        button10.getModel().setPressed(false);
        button11.getModel().setPressed(false);
        button12.getModel().setPressed(false);

        button13.getModel().setPressed(false);
        button14.getModel().setPressed(false);
        button15.getModel().setPressed(false);
        button16.getModel().setPressed(false);
        button17.getModel().setPressed(false);
        button18.getModel().setPressed(false);

        button19.getModel().setPressed(false);
        button20.getModel().setPressed(false);
        button21.getModel().setPressed(false);
        button22.getModel().setPressed(false);
        button23.getModel().setPressed(false);
        button24.getModel().setPressed(false);
    }

    public int getNoOfWhites() {
        int k = 0;
        for (int i = 0; i < noPieces.length; i++) {
            if (pieceColors[i].equals("w")) {
                k = k + noPieces[i];
            }
        }
        return k;
    }

    public int getNoOfBlacks() {
        int k = 0;
        for (int i = 0; i < noPieces.length; i++) {
            if (pieceColors[i].equals("b")) {
                k = k + noPieces[i];
            }
        }
        return k;
    }

    public boolean checkDestinationW(int n) {
        if (pieceColors[n].equals("w") || pieceColors[n].equals("n") || (pieceColors[n].equals("b") && noPieces[n] <= 1)) {
            return true;
        }
        return false;
    }

    public boolean checkDestinationB(int n) {
        if (pieceColors[n].equals("b") || pieceColors[n].equals("n") || (pieceColors[n].equals("w") && noPieces[n] <= 1)) {
            return true;
        }
        return false;
    }

    public boolean checkMoveB1(int start, int end, int dice1) {
        if (end - start == dice1) {
            return true;
        }
        return false;
    }

    public boolean checkMoveB2(int start, int end, int dice2) {
        if (end - start == dice2) {
            return true;
        }
        return false;
    }

    public boolean checkMoveW1(int start, int end, int dice1) {
        if (start - end == dice1) {
            return true;
        }
        return false;
    }

    public boolean checkMoveW2(int start, int end, int dice2) {
        if (start - end == dice2) {
            return true;
        }
        return false;
    }

    public void printNoPieces() {
        for (int i = 0; i < noPieces.length; i++) {
            System.out.print(" " + noPieces[i] + " ");
        }
        System.out.println();
    }

    public void printPieceColors() {
        for (int i = 0; i < pieceColors.length; i++) {
            System.out.print(" " + pieceColors[i] + " ");
        }
        System.out.println();
    }

    public void displayOnButtons() {
        piecesButton1.setText(noPieces[0] + " " + pieceColors[0]);
        piecesButton2.setText(noPieces[1] + " " + pieceColors[1]);
        piecesButton3.setText(noPieces[2] + " " + pieceColors[2]);
        piecesButton4.setText(noPieces[3] + " " + pieceColors[3]);
        piecesButton5.setText(noPieces[4] + " " + pieceColors[4]);
        piecesButton6.setText(noPieces[5] + " " + pieceColors[5]);

        piecesButton7.setText(noPieces[6] + " " + pieceColors[6]);
        piecesButton8.setText(noPieces[7] + " " + pieceColors[7]);
        piecesButton9.setText(noPieces[8] + " " + pieceColors[8]);
        piecesButton10.setText(noPieces[9] + " " + pieceColors[9]);
        piecesButton11.setText(noPieces[10] + " " + pieceColors[10]);
        piecesButton12.setText(noPieces[11] + " " + pieceColors[11]);

        piecesButton13.setText(noPieces[12] + " " + pieceColors[12]);
        piecesButton14.setText(noPieces[13] + " " + pieceColors[13]);
        piecesButton15.setText(noPieces[14] + " " + pieceColors[14]);
        piecesButton16.setText(noPieces[15] + " " + pieceColors[15]);
        piecesButton17.setText(noPieces[16] + " " + pieceColors[16]);
        piecesButton18.setText(noPieces[17] + " " + pieceColors[17]);

        piecesButton19.setText(noPieces[18] + " " + pieceColors[18]);
        piecesButton20.setText(noPieces[19] + " " + pieceColors[19]);
        piecesButton21.setText(noPieces[20] + " " + pieceColors[20]);
        piecesButton22.setText(noPieces[21] + " " + pieceColors[21]);
        piecesButton23.setText(noPieces[22] + " " + pieceColors[22]);
        piecesButton24.setText(noPieces[23] + " " + pieceColors[23]);
    }

    public void moveBlackToStart() {
        for (int i = 0; i < noPieces.length; i++) {
            if ((i != endingTriangle - 1) && (pieceColors[i].equals("b") || pieceColors[i].equals("n"))) {
                noPieces[i]++;
                pieceColors[i] = "b";
                break;
            }
        }
    }

    public void moveWhiteToStart() {
        for (int i = noPieces.length - 1; i > 0; i--) {
            if ((i != endingTriangle - 1) && pieceColors[i].equals("w") || pieceColors[i].equals("n")) {
                noPieces[i]++;
                pieceColors[i] = "w";
                break;
            }
        }
    }

    public void showDicesScores(int x, int y, int sw, int sb) {
        middleDices.setText("");
        middleDices = new JLabel(x + "     " + y, SwingConstants.CENTER);
        middleDices.setBounds(600, 200, 100, 200);
        mainFrame.add(middleDices);

        middleScoreW.setText("");
        middleScoreW = new JLabel("White: " + sw, SwingConstants.CENTER);
        middleScoreW.setBounds(600, 0, 100, 200);
        mainFrame.add(middleScoreW);

        middleScoreB.setText("");
        middleScoreB = new JLabel("Black: " + sb, SwingConstants.CENTER);
        middleScoreB.setBounds(600, 400, 100, 200);
        mainFrame.add(middleScoreB);
    }

    public void scoreWhite() {
        int j = 1;
        for (int i = pieceColors.length - 1; i > 0; i--) {
            if (pieceColors[i].equals("w")) {
                scoreW = j * 10 * noPieces[i];
                j++;
            }
        }
        //System.out.println("ScoreW " + scoreW);
    }

    public void scoreWhiteRetrieve() {
        scoreW = scoreW + (15 - getNoOfWhites()) * 300;
        for (int i = 0; i < 6; i++) {
            if (pieceColors[i].equals("w")) {
                scoreW = scoreW + (6 - i) * 10 * noPieces[i];
            }
        }
    }

    public void scoreBlackRetrieve() {
        scoreB = scoreB + (15 - getNoOfWhites()) * 300;
        for (int i = 0; i < 6; i++) {
            if (pieceColors[i].equals("b")) {
                scoreB = scoreB + (6 - i) * 10 * noPieces[i];
            }
        }
    }

    public void scoreBlack() {
        int j = 1;
        for (int i = 0; i < pieceColors.length; i++) {
            if (pieceColors[i].equals("b")) {
                scoreB = j * 10 * noPieces[i];
                j++;
            }
        }
        //System.out.println("ScoreB " + scoreB);
    }

    public boolean checkIfRetriveW() {
        int piecesIn = 0;
        int piecesOut = 0;
        for (int i = 0; i < 6; i++) {
            if (pieceColors[i].equals("w")) {
                piecesIn = piecesIn + noPieces[i];
            }
        }

        for (int i = 6; i < 24; i++) {
            if (pieceColors[i].equals("w")) {
                piecesOut = piecesOut + noPieces[i];
            }
        }
        if (piecesIn <= 15 && piecesOut == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfRetriveB() {
        int piecesIn = 0;
        int piecesOut = 0;
        for (int i = 23; i > 17; i--) {
            if (pieceColors[i].equals("b")) {
                piecesIn = piecesIn + noPieces[i];
            }
        }

        for (int i = 17; i > -1; i--) {
            if (pieceColors[i].equals("b")) {
                piecesOut = piecesOut + noPieces[i];
            }
        }
        if (piecesIn <= 15 && piecesOut == 0) {
            return true;
        } else {
            return false;
        }
    }
}
