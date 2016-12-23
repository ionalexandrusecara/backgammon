
package backgammon;

public class CanMove {

    private static int[] board;
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

    public static void getFields() {

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

    public static Boolean moveCanBeMade() {

        getFields();

        if (dice1 == 50 && dice2 == 50) {

            return false;
        }

        if (moveNeedsToBeMadeOffBar()) {

            if (moveCanBeMadeBar()) {
                return true;
            }

            return false;
        }

        if (BackGammon.countersInBearOffPosition()) {

            if (aCounterCanBearOff() || aCounterCanMove()) {
                return true;
            } else {

                return false;
            }
        } else {

            if (aCounterCanMove()) {
                return true;
            } else {

                return false;
            }

        }
    }

    public static Boolean moveCanBeMadeGUI() {

        BackGammon.translate();
        getFields();

        if (BackGammon.countersInBearOffPositionGUI()) {

            if (whitesTurn) {

                int possibleStart1 = dice1 - 1;
                int possibleStart2 = dice2 - 1;

                if ((possibleStart1 < 6 && board[possibleStart1] < 0) || (possibleStart2 < 6 && board[possibleStart2] < 0)) {
                    
                    System.out.println("Move Can be Made");

                    return true;

                } else {

                    int possibleStartLocation = 0;

                    while (possibleStartLocation < 24) {

                        if (board[possibleStartLocation] < 0) {

                            int possibleEnd1 = possibleStartLocation - dice1;
                            int possibleEnd2 = possibleStartLocation - dice2;

                            if ((possibleEnd1 > -1 && board[possibleEnd1] < 1) || (possibleEnd2 > -1 && board[possibleEnd2] < 1)) {

                                System.out.println("Move Can be Made");
                                
                                return true;

                            }

                        }

                        possibleStartLocation++;

                    }
                }
            } else {

                int possibleStart1 = 24 - dice1;
                int possibleStart2 = 24 - dice2;

                if ((possibleStart1 > 17 && board[possibleStart1] > 0) || (possibleStart2 > 17 && board[possibleStart2] > 0)) {

                    System.out.println("Move Can be Made");
                    
                    return true;

                } else {

                    int possibleStartLocation = 0;

                    while (possibleStartLocation < 24) {

                        if (board[possibleStartLocation] > 0) {

                            int possibleEnd1 = possibleStartLocation + dice1;
                            int possibleEnd2 = possibleStartLocation + dice2;

                            if ((possibleEnd1 < 24 && board[possibleEnd1] > -1) || (possibleEnd2 < 24 && board[possibleEnd2] > -1)) {

                                System.out.println("Move Can be Made");
                                
                                return true;

                            }

                        }

                        possibleStartLocation++;

                    }

                }
            }

        } else {

            int possibleStartLocation = 0;

            while (possibleStartLocation < 24) {

                if (whitesTurn) {

                    if (board[possibleStartLocation] < 0) {

                        int possibleEnd1 = possibleStartLocation - dice1;
                        int possibleEnd2 = possibleStartLocation - dice2;

                        if ((possibleEnd1 > -1 && board[possibleEnd1] < 1) || (possibleEnd2 > -1 && board[possibleEnd2] < 1)) {

                            System.out.println("Move Can be Made");
                            
                            return true;

                        }

                    }

                } else {

                    if (board[possibleStartLocation] > 0) {

                        int possibleEnd1 = possibleStartLocation + dice1;
                        int possibleEnd2 = possibleStartLocation + dice2;

                        if ((possibleEnd1 < 24 && board[possibleEnd1] > -1) || (possibleEnd2 < 24 && board[possibleEnd2] > -1)) {

                            System.out.println("Move Can be Made");
                            
                            return true;

                        }

                    }

                }

                possibleStartLocation++;
            }

        }
        
        System.out.println("Move Can't be Made");

        return false;
    }

    public static Boolean moveNeedsToBeMadeOffBar() {

        if (whitesTurn && whiteBar != 0) {
            return true;
        }

        if (!whitesTurn && redBar != 0) {
            return true;
        }
        return false;
    }

    public static Boolean aCounterCanMove() {

        if (whitesTurn) {

            int index = 0;

            while (index < 24) {

                if (board[index] > 0) {

                    if (dice1 != 50 && dice2 != 50) {

                        if (index + dice1 < 24) {

                            if (board[index + dice1] > -2) {

                                return true;
                            }
                        }

                        if (index + dice2 < 24) {

                            if (board[index + dice2] > -2) {

                                return true;
                            }
                        }
                    }

                    if (dice1 == 50) {

                        if (index + dice2 < 24) {

                            if (board[index + dice2] > -2) {

                                return true;
                            }
                        }
                    }

                    if (dice2 == 50) {

                        if (index + dice1 < 24) {

                            if (board[index + dice1] > -2) {

                                return true;
                            }
                        }
                    }
                }

                index++;
            }
        }

        if (!whitesTurn) {

            int index = 0;

            while (index < 24) {

                if (board[index] < 0) {

                    if (dice1 != 50 && dice2 != 50) {

                        if (index - dice1 > -1) {

                            if (board[index - dice1] < 2) {

                                return true;
                            }
                        }

                        if (index - dice2 > -1) {

                            if (board[index - dice2] < 2) {

                                return true;
                            }
                        }
                    }

                    if (dice1 == 50) {

                        if (index - dice2 > -1) {

                            if (board[index - dice2] < 2) {

                                return true;
                            }
                        }
                    }

                    if (dice2 == 50) {

                        if (index - dice1 > -1) {

                            if (board[index - dice1] < 2) {

                                return true;
                            }
                        }
                    }
                }

                index++;
            }
        }

        return false;

    }
    
    public static Boolean aCounterCanBearOffGUI() {

        getFields();

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
    

    public static Boolean aCounterCanBearOff() {

        getFields();

        if (whitesTurn) {

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

    public static Boolean moveCanBeMadeBar() {

        if (whitesTurn) {

            int position1 = dice1 - 1;
            int position2 = dice2 - 1;

            if (position1 < 6) {

                if (board[position1] > -2) {

                    return true;
                }

                if (position2 < 6) {

                    if (board[position2] > -2) {

                        return true;
                    }
                }
            } else {

                if (position2 < 6) {

                    if (board[position2] > -2) {

                        return true;
                    }
                }
            }
        } else {

            int position1 = 24 - dice1;
            int position2 = 24 - dice2;

            if (position1 > 17) {

                if (board[position1] < 2) {

                    return true;
                }

                if (position2 > 17) {

                    if (board[position2] < 2) {

                        return true;
                    }
                }
            } else {

                if (position2 > 17) {

                    if (board[position2] < 2) {

                        return true;
                    }
                }
            }

        }

        return false;
    }
}
