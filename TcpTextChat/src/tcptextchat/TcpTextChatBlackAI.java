
package tcptextchat;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;


public class TcpTextChatBlackAI {

    static String _quit = "quit"; // user command
    static String _start = "start";
    static String _prompt = ">";
    static String _busy = "::busy::";

    static String _me;          // identity to be used for chat
    static String _otherPerson; // the other person

    static ServerSocket _listen;      // to receive from network
    static BufferedReader _keyboard;    // to receive from keyboard

    static ChatState _state;
    static Socket _call;        // for chat

    InputStream _in;  // network input stream
    OutputStream _out; // network output stream 

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

    public static void main(String[] argv)
            throws InterruptedException, IOException {
        if (argv.length != 0) {
            report("usage: TcpTextChat");
            System.exit(0);
        }
        startup();

        TcpTextChatBlackAI chat = null; // a chat session
        boolean quit = false;

        //tryyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
        while (!quit) {

            String lineKeyboard = null;

            // input from user?
            lineKeyboard = readKeyboard();

            try {
                switch (_state) {

                    case idle:
                        // wait for either:
                        // - user typing the name for a remote host
                        // - an incoming connection

                        // assume user has given the name of a remote host
                        if (checkStart(lineKeyboard)) {
                            quit = true;
                            //shutdown();
                            //System.exit(0);
                        } else {
                            chat = makeCall(lineKeyboard);
                        }

                        if (chat == null) {
                            chat = checkIncomingCall();
                        }

                        if (chat != null) {
                            _state = ChatState.chatting;
                        }
                        break;

                    case chatting:
                        // check for other chat requests from network
                        checkIncomingCall();

                        // anything to print?
                        String lineNetwork = chat.recv();
                        chat.print(lineNetwork);

                        // anything to send?
                        chat.send(lineKeyboard);

                        if (checkStart(lineKeyboard) || checkStart(lineNetwork)) {
                            //chat.endCall();
                            //chat = null;
                            //_state = ChatState.idle;
                            quit = true;
                        }
                        break;
                } // switch

            } // try
            catch (java.io.IOException e) {
                String eName = e.getClass().getName();
                if (eName != "java.net.SocketTimeoutException") {
                    error("main() problem: " + eName);
                    throw e;
                }
            }

            // avoid CPU overhead of continuous looping here
            Thread.sleep(Numbers.sleepTime);

        }
        //end of tryyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy

        TcpTextChatBlackAI Game = new TcpTextChatBlackAI();
        Game.showJFrameDemo();
        while (Game.getNoOfWhites() > 0 || Game.getNoOfBlacks() > 0) {

            String lineKeyboard = null;
            String lineNetwork = null;

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
            
            //System.out.println(Game.dice1 + " " + Game.dice2+ " " + Game.scoreW+ " " + Game.scoreB);
            //System.out.println("NOOOOO" + Game.currentPlayer);
            lineNetwork = null;
            while (lineNetwork == null) {
                lineNetwork = chat.recv();
            }
            //System.out.println("111" + lineNetwork);
            //chat.print(lineNetwork);
            //System.out.println("lineNetworkkk " + lineNetwork);
            Game.dice1 = Integer.parseInt(lineNetwork.substring(0, 1));
            Game.dice2 = Integer.parseInt(lineNetwork.substring(2));
            Game.showDicesScores(Game.dice1, Game.dice2, Game.scoreW, Game.scoreB);
            //System.out.println("@@@ " + Game.dice1 + " @@@ " + Game.dice2);
            lineNetwork = null;
            if (Game.dice1 == Game.dice2) {
                Game.dice1Again = true;
                Game.dice2Again = true;
            } else {
                Game.dice1Again = false;
                Game.dice2Again = false;
            }
            while (Game.dice1 > 0 || Game.dice2 > 0) {

                // input from user?
                lineKeyboard = readKeyboard();
                //System.out.println(_state);
                try {
                    switch (_state) {

                        case idle:
                        // wait for either:
                            // - user typing the name for a remote host
                            // - an incoming connection

                            // assume user has given the name of a remote host
                            if (checkQuit(lineKeyboard)) {
                                quit = true;
                                shutdown();
                                System.exit(0);
                            } else {
                                chat = makeCall(lineKeyboard);
                            }

                            if (chat == null) {
                                chat = checkIncomingCall();
                            }

                            if (chat != null) {
                                _state = ChatState.chatting;
                            }
                            break;

                        case chatting:
                            // check for other chat requests from network
                            checkIncomingCall();

                            // anything to print?
                            lineNetwork = chat.recv();
                            chat.print(lineNetwork);
                            //System.out.println("HERE " + lineNetwork);

                            // anything to send?
                            chat.send(lineKeyboard);
                            //System.out.println("HERE as WELL " + lineKeyboard);

                            if (checkQuit(lineKeyboard) || checkQuit(lineNetwork)) {
                                chat.endCall();
                                chat = null;
                                _state = ChatState.idle;
                            }
                            break;
                    } // switch

                } // try
                catch (java.io.IOException e) {
                    String eName = e.getClass().getName();
                    if (eName != "java.net.SocketTimeoutException") {
                        error("main() problem: " + eName);
                        throw e;
                    }
                }

                // avoid CPU overhead of continuous looping here
                Thread.sleep(Numbers.sleepTime);

                if (Game.currentPlayer % 2 == 1) {
                    JOptionPane.showMessageDialog(null, "White turn!");
                } else {
                    JOptionPane.showMessageDialog(null, "Black turn!");
                }
                lineKeyboard=null;
                lineNetwork=null;
                if (Game.currentPlayer % 2 == 0) {
                    while (lineKeyboard == null) {
                        System.out.println(NetworkingAi.move());
                        lineKeyboard = NetworkingAi.move();
                        chat.send(lineKeyboard);
                        Game.okBegin = false; 
                        
                        /*while (lineKeyboard == null) {
                            lineKeyboard = readKeyboard();
                        }
                        
                        //System.out.println("@@@" + lineKeyboard);
                        if (Game.checkLineKeyboard(lineKeyboard) == false) {
                            lineKeyboard = null;
                        }*/
                    }
                    

                } else {
                    while (lineNetwork == null) {
                        while (lineNetwork == null) {
                            lineNetwork = chat.recv();
                        }
                        chat.print(lineNetwork);
                        if (Game.checkLineNetwork(lineNetwork) == false) {
                            lineNetwork = null;
                        }
                    }
                    while (Game.okBegin) {
                        Game.chooseStart(Game.startingTriangle);
                    }

                }
                JOptionPane.showMessageDialog(null, "Starting piece selected!");
                if (Game.currentPlayer % 2 == 1 && Game.checkIfRetriveW() && Game.pieceColors[Game.dice1].equals("w") && Game.startingTriangle == Game.dice1) {
                    if (Game.noPieces[Game.startingTriangle - 1] == 1) {
                        Game.pieceColors[Game.startingTriangle - 1] = "n";
                    }
                    Game.noPieces[Game.startingTriangle - 1]--;

                    if (Game.dice1 == Game.dice2) {
                        Game.diceAgain++;
                        lineKeyboard = null;
                        lineNetwork = null;
                        if (Game.diceAgain == 3) {
                            Game.dice1Again = false;
                        }
                        if (Game.diceAgain == 4) {
                            Game.dice2Again = false;
                        }
                    }
                    

                    if (Game.dice1Again == false && Game.dice2Again == false && Game.diceAgain == 0) {
                        Game.dice1 = -1;
                        lineKeyboard = null;
                        lineNetwork = null;
                    } else if (Game.dice1Again == false && Game.dice2Again == false) {
                        Game.dice1 = -1;
                        Game.dice2 = -1;
                        lineKeyboard = null;
                        lineNetwork = null;
                    }
                    Game.displayOnButtons();
                } else if (Game.currentPlayer % 2 == 1 && Game.checkIfRetriveW() && Game.pieceColors[Game.dice2].equals("w") && Game.startingTriangle == Game.dice2) {
                    if (Game.noPieces[Game.startingTriangle - 1] == 1) {
                        Game.pieceColors[Game.startingTriangle - 1] = "n";
                    }
                    Game.noPieces[Game.startingTriangle - 1]--;
                    Game.dice2 = -1;
                    lineKeyboard = null;
                    lineNetwork = null;
                    Game.displayOnButtons();
                } else if (Game.currentPlayer % 2 == 0 && Game.checkIfRetriveB() && Game.pieceColors[Game.dice1].equals("b") && (25 - Game.startingTriangle == Game.dice1)) {
                    if (Game.noPieces[Game.startingTriangle - 1] == 1) {
                        Game.pieceColors[Game.startingTriangle - 1] = "n";
                    }
                    Game.noPieces[Game.startingTriangle - 1]--;
                    if (Game.dice1 == Game.dice2) {
                        Game.diceAgain++;
                        lineKeyboard = null;
                        lineNetwork = null;
                        if (Game.diceAgain == 3) {
                            Game.dice1Again = false;
                        }
                        if (Game.diceAgain == 4) {
                            Game.dice2Again = false;
                        }
                    }
                    
                    
                    
                    if (Game.dice1Again == false && Game.dice2Again == false && Game.diceAgain == 0) {
                        Game.dice1 = -1;
                        lineKeyboard = null;
                        lineNetwork = null;
                    } else if (Game.dice1Again == false && Game.dice2Again == false) {
                        Game.dice1 = -1;
                        Game.dice2 = -1;
                        lineKeyboard = null;
                        lineNetwork = null;
                    }
                    Game.displayOnButtons();
                } else if (Game.currentPlayer % 2 == 0 && Game.checkIfRetriveB() && Game.pieceColors[Game.dice2].equals("b") && (25 - Game.startingTriangle == Game.dice2)) {
                    if (Game.noPieces[Game.startingTriangle - 1] == 1) {
                        Game.pieceColors[Game.startingTriangle - 1] = "n";
                    }
                    Game.noPieces[Game.startingTriangle - 1]--;
                    Game.dice2 = -1;
                    lineKeyboard = null;
                    lineNetwork = null;
                    Game.displayOnButtons();
                } else {
                    
                    if(Game.currentPlayer%2 == 0){
                        Game.okStart = false;
                        Game.okBegin = true;
                    }

                    while (Game.okStart) {
                        Game.chooseEnd(Game.endingTriangle);
                    }

                    JOptionPane.showMessageDialog(null, "Destination chosen!");
                    if (Game.currentPlayer % 2 == 1) {
                        if (Game.dice1 > 0 && Game.checkMoveW1(Game.startingTriangle, Game.endingTriangle, Game.dice1)) {
                            if (Game.pieceColors[Game.endingTriangle - 1].equals("b")) {
                                Game.moveBlackToStart();
                                Game.noPieces[Game.endingTriangle - 1]--;
                            }
                            
                            Game.noPieces[Game.endingTriangle - 1]++;
                            Game.noPieces[Game.startingTriangle - 1]--;
                            if (Game.dice1 == Game.dice2) {
                                Game.diceAgain++;
                                lineKeyboard = null;
                                lineNetwork = null;
                                if (Game.diceAgain == 3) {
                                    Game.dice1Again = false;
                                }
                                if (Game.diceAgain == 4) {
                                    Game.dice2Again = false;
                                }
                            }
                            
                           

                            if (Game.dice1Again == false && Game.dice2Again == false && Game.diceAgain == 0) {
                                Game.dice1 = -1;
                                lineKeyboard = null;
                                lineNetwork = null;
                            } else if (Game.dice1Again == false && Game.dice2Again == false) {
                                Game.dice1 = -1;
                                Game.dice2 = -1;
                                lineKeyboard = null;
                                lineNetwork = null;
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
                            
                            Game.noPieces[Game.endingTriangle - 1]++;
                            Game.noPieces[Game.startingTriangle - 1]--;
                            Game.dice2 = -1;
                            lineKeyboard = null;
                            lineNetwork = null;
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
                            
                            Game.noPieces[Game.endingTriangle - 1]++;
                            Game.noPieces[Game.startingTriangle - 1]--;
                            if (Game.dice1 == Game.dice2) {
                                Game.diceAgain++;
                                lineKeyboard = null;
                                lineNetwork = null;
                                if (Game.diceAgain == 3) {
                                    Game.dice1Again = false;
                                }
                                if (Game.diceAgain == 4) {
                                    Game.dice2Again = false;
                                }
                            }
                            
                            

                            if (Game.dice1Again == false && Game.dice2Again == false && Game.diceAgain == 0) {
                                Game.dice1 = -1;
                                lineKeyboard = null;
                                lineNetwork = null;
                            } else if (Game.dice1Again == false && Game.dice2Again == false) {
                                Game.dice1 = -1;
                                Game.dice2 = -1;
                                lineKeyboard = null;
                                lineNetwork = null;
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
                            
                            Game.noPieces[Game.endingTriangle - 1]++;
                            Game.noPieces[Game.startingTriangle - 1]--;
                            Game.dice2 = -1;
                            lineKeyboard = null;
                            lineNetwork = null;
                            Game.pieceColors[Game.endingTriangle - 1] = "b";
                            Game.displayOnButtons();
                            if (Game.noPieces[Game.startingTriangle - 1] == 0) {
                                Game.pieceColors[Game.startingTriangle - 1] = "n";
                            }
                        }
                    }
                }
                Game.printNoPieces();
                Game.printPieceColors();
            }
            Game.currentPlayer++;
        } // while
        if (Game.getNoOfWhites() == 0) {
            JOptionPane.showMessageDialog(null, "White won!");
        } else {
            JOptionPane.showMessageDialog(null, "Black won!");
        }

        // while
        System.exit(0);
    }

    TcpTextChatBlackAI() {
        prepareGUI();
    }

    TcpTextChatBlackAI(Socket call, String otherPerson)
            throws SocketException {
        _call = call;
        _otherPerson = otherPerson;

        try {
            _in = call.getInputStream();
            _out = call.getOutputStream();
        } catch (java.io.IOException e) {
            error("TcpTextChat(): io problem " + e.getClass().getName());
            System.exit(-1);
        }

        report("new call");
        report("local: "
                + _call.getLocalAddress().getHostName() + " "
                + _call.getLocalAddress().getHostAddress()
                + " port: " + _call.getLocalPort());
        report("remote: "
                + _call.getInetAddress().getHostName() + " "
                + _call.getInetAddress().getHostAddress()
                + " port: " + _call.getPort());
        report("chatting to: " + _otherPerson);
    }

    static boolean checkQuit(String s) {
        return s != null ? s.compareTo(_quit) == 0 : false;
    }
    
    static boolean checkStart(String s) {
        return s != null ? s.compareTo(_start) == 0 : false;
    }

    static boolean checkBusy(String s) {
        return s != null ? s.compareTo(_busy) == 0 : false;
    }

    static void error(String s) {
        System.err.println("-!- " + s);
    }

    static void report(String s) {
        System.out.println("-*- " + s);
    }

    static void chatting(String s) {
        System.out.println("--> " + s);
    }

    static void startup() {
        // create listening socket
        try {
            _listen = new ServerSocket(Numbers.chatPort);
            _listen.setSoTimeout(Numbers.soTimeout);
        } catch (java.io.IOException e) {
            error("server failed! " + e.getClass().getName());
            System.exit(-1);
        }
        // local input
        _keyboard = new BufferedReader(new InputStreamReader(System.in),
                Numbers.bufferSize); // restrict line length

        _me = System.getProperty("user.name");
        report("greetings " + _me);
        report("started server");
        InetAddress h;
        String s = "(unknown)";
        try {
            h = InetAddress.getLocalHost();
            s = h.getCanonicalHostName();
            report("host: " + h.getByName(s));
        } catch (java.net.UnknownHostException e) {
            s = "(unknown)";
            error("startup(): cannot get hostname!");
        }
        report("port: " + _listen.getLocalPort());
        report("ready ...");

        _state = ChatState.idle;
    }

    static void shutdown() {
        try {
            if (_call != null) {
                _call.close();
            }
            _listen.close();
            report("shutdown ... bye ...");
        } catch (IOException e) {
            error("io problem() " + e.getClass().getName());
            System.exit(-1);
        }
    }

    static TcpTextChatBlackAI checkIncomingCall()
            throws IOException {
        TcpTextChatBlackAI chat = null;
        Socket connection = null;

        try {
            // wait for a connection request
            connection = _listen.accept();
            connection.setSoTimeout(Numbers.soTimeout);
            connection.setTcpNoDelay(true);
            if (!handshake(connection)) {
                return null;
            }

            switch (_state) {

                case idle:
                    chat = new TcpTextChatBlackAI(connection, _otherPerson);
                    _state = ChatState.chatting;
                    break; // idle

                // already chatting so send "busy" signal
                case chatting:
                    // handshake() does the following:
                    // - sends a "busy" signal
                    // closes the connection
                    break;

            } // switch
        } // try // try // try // try
        catch (java.net.SocketTimeoutException e) {
            // ignore
        } catch (java.net.UnknownHostException e) {
            report("checkIncomingCall(): cannot get hostname of remote host");
        } catch (java.io.IOException e) {
            error("checkIncomingCall() problem: " + e.getClass().getName());
            throw e;
        }

        return chat;
    }

    static TcpTextChatBlackAI makeCall(String host) {
        TcpTextChatBlackAI chat = null;
        Socket call = null;
        String callee = null;

        if (host != null) {

            try {
                call = new Socket(host, Numbers.chatPort);
                call.setSoTimeout(Numbers.soTimeout);
                call.setTcpNoDelay(true);
                if (!handshake(call)) {
                    return null;
                }
                chat = new TcpTextChatBlackAI(call, _otherPerson);
            } // try // try // try // try
            catch (java.net.UnknownHostException e) {
                error("makeCall(): unknown host " + host);
                return null;
            } catch (java.io.IOException e) {
                error("makeCall(): i/o problem " + e.getClass().getName());
                return null;
            }

        } // if

        if (chat != null) {
            report("new chat!");
        }

        return chat;
    }

    static String readNetwork(Socket connection)
            throws IOException {
        return connection == null ? null : recvLine(connection.getInputStream());
    }

    static void writeNetwork(Socket connection, String line)
            throws IOException {
        sendLine(connection.getOutputStream(), line);
    }

    static String readKeyboard() {
        String line = null;

        try {
            if (_keyboard.ready()) {
                line = _keyboard.readLine();
            }
        } catch (java.io.IOException e) {
            error("readKeyboard(): problem reading! " + e.getClass().getName());
            System.exit(-1);
        }

        return line;
    }

    static void sendLine(OutputStream out, String line) {
        if (out == null || line == null || line.length() < 0) {
            return;
        }

        try {
            int l = line.length();
            if (l > Numbers.bufferSize) {
                report("line too long (" + l + ") truncated to " + Numbers.bufferSize);
                l = Numbers.bufferSize;
            }
            out.write(line.getBytes(), 0, l);
        } catch (java.io.IOException e) {
            error("sendLine() problem " + e.getClass().getName());
        }
    }

    static String recvLine(InputStream in)
            throws IOException {
        if (in == null) {
            return null;
        }

        String line = null;

        try {
            byte buffer[] = new byte[Numbers.bufferSize];
            int l = in.read(buffer);
            if (l > 0) {
                line = new String(buffer, 0, l);
            }
        } catch (java.net.SocketTimeoutException e) {
            // ignore
        } catch (java.io.IOException e) {
            String eName = e.getClass().getName();
            if (eName != "java.io.InterruptedIOException") {
                error("recvLine() problem " + eName);
                throw e;
            }
        }

        return line;
    }

    static boolean handshake(Socket connection) {
        boolean receivedName = false;

        try {
            // already chatting ...
            if (_state == ChatState.chatting) {
                writeNetwork(connection, _busy);
                connection.close();
                return false;
            }

            writeNetwork(connection, _me);

            for (int i = 0; (i < Numbers.readRetry) && !receivedName; ++i) {
                String line = readNetwork(connection);
                if (line != null) {
                    if (checkBusy(line)) {
                        report("other end is busy - try again later.");
                        break;
                    } else {
                        _otherPerson = line;
                        receivedName = true;
                    }
                } else {
                    Thread.sleep(Numbers.sleepTime);
                }
            }

        } // try
        catch (java.lang.InterruptedException e) {
            // ignore - just means that we are still waiting
        } catch (java.io.IOException e) {
            error("handshake(): io exception '" + e.getClass().getName());
        }

        return receivedName;
    }

    void print(String line) {
        if (line != null) {
            chatting(_otherPerson + " " + _prompt + " " + line);
        }
    }

    void send(String line) {
        sendLine(_out, line);
    }

    String recv()
            throws IOException {
        return recvLine(_in);
    }

    void endCall() {
        try {
            if (_state == ChatState.chatting) {
                report("closing call with " + _otherPerson);
                _call.close();
                _call = null;
                _state = ChatState.idle;
            }
        } catch (java.io.IOException e) {
            error("endCall(): io problem " + e.getClass().getName());
            System.exit(-1);
        }
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

    public void showJFrameDemo() throws IOException {
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

        piecesButton16 = new JLabel("3 b", SwingConstants.CENTER);
        piecesButton16.setBounds(900, 200, 90, 50);

        piecesButton17 = new JLabel("0 n", SwingConstants.CENTER);
        piecesButton17.setBounds(800, 200, 90, 50);

        piecesButton18 = new JLabel("5 b", SwingConstants.CENTER);
        piecesButton18.setBounds(700, 200, 90, 50);

        piecesButton19 = new JLabel("0 n", SwingConstants.CENTER);
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

    public void chooseStart(int i) {
        okStart = false;
        //System.out.println(currentPlayer + " @ " + pieceColors[i - 1] + " @ " + noPieces[i - 1] + " @i " + i);
        if (currentPlayer % 2 == 1 && pieceColors[i - 1].equals("w") && noPieces[i - 1] > 0 && (i > 0) && (i < 25)) {
            startingTriangle=i;
            okStart = true;
        } else if (currentPlayer % 2 == 0 && pieceColors[i - 1].equals("b") && noPieces[i - 1] > 0 && (i > 0) && (i < 25)) {
            startingTriangle=i;
            okStart = true;
        } else {
            //System.out.println("HERE111111111111111111111111111111111");
            JOptionPane.showMessageDialog(null, "Move not allowed!");
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

    public void chooseEnd(int i) {
        //System.out.println(currentPlayer + " @ " + checkDestinationW(i - 1) + " @ " + " @i " + i);
        if (currentPlayer % 2 == 1 && checkDestinationW(i - 1) && (i > 0) && (i < 25)) {
            okBegin = true;
        } else if (currentPlayer % 2 == 0 && checkDestinationB(i - 1) && (i > 0) && (i < 25)) {
            okBegin = true;
        } else {
            //System.out.println("HERE222222222222222222222222222222222222");
            JOptionPane.showMessageDialog(null, "Move not allowed!");
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
            if ((i != endingTriangle - 1) && (pieceColors[i].equals("w") || pieceColors[i].equals("n"))) {
                noPieces[i]++;
                pieceColors[i] = "w";
                break;
            }
        }
    }

    public void showDicesScores(int x, int y, int sw, int sb) {
        middleDices.setText("");
        middleDices = new JLabel(x + " " + y, SwingConstants.CENTER);
        middleDices.setBounds(600, 200, 100, 200);
        mainFrame.add(middleDices);

        middleScoreW.setText("");
        middleScoreW = new JLabel("W: " + sw, SwingConstants.CENTER);
        middleScoreW.setBounds(600, 0, 100, 200);
        mainFrame.add(middleScoreW);

        middleScoreB.setText("");
        middleScoreB = new JLabel("B: " + sb, SwingConstants.CENTER);
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
        scoreW = scoreW + (15 - getNoOfWhites()) * 200;
        for (int i = 0; i < 6; i++) {
            if (pieceColors[i].equals("w")) {
                scoreW = scoreW + (6 - i) * 10 * noPieces[i];
            }
        }
    }

    public void scoreBlackRetrieve() {
        scoreB = scoreB + (15 - getNoOfWhites()) * 200;
        for (int i = 0; i < 6; i++) {
            if (pieceColors[i].equals("w")) {
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
        for (int i = 0; i < 6; i++) {
            if (pieceColors[i].equals("b")) {
                piecesIn = piecesIn + noPieces[i];
            }
        }

        for (int i = 6; i < 24; i++) {
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

    public boolean checkLineKeyboard(String str) {
        if (str.indexOf("(") != 0) {
            JOptionPane.showMessageDialog(null, "Format: (xx,yy)");
            return false;
        }
        str = str.substring(1);
        String sTriangle = str.substring(0, 2);
        int start;
        try {
            start = Integer.parseInt(sTriangle);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "You have to write a number not a string!");
            return false;
        }
        startingTriangle = start;
        //System.out.println(startingTriangle);

        str = str.substring(2);

        if (str.indexOf(",") != 0) {
            JOptionPane.showMessageDialog(null, "Format: (xx,yy)");
            return false;
        }

        str = str.substring(1);

        String eTriangle = str.substring(0, 2);
        int end;
        try {
            end = Integer.parseInt(eTriangle);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "You have to write a number not a string!");
            return false;
        }
        endingTriangle = end;
        //System.out.println("----" + endingTriangle);
        str = str.substring(2);
        if (str.indexOf(")") != 0) {
            return false;
        }

        return true;
    }

    public boolean checkLineNetwork(String str) {
        str = str.substring(str.indexOf("("));
        if (str.indexOf("(") != 0) {
            JOptionPane.showMessageDialog(null, "Format: (xx,yy)");
            return false;
        }
        str = str.substring(1);
        String sTriangle = str.substring(0, 2);
        int start;
        try {
            start = Integer.parseInt(sTriangle);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "You have to write a number not a string!");
            return false;
        }
        startingTriangle = start;

        str = str.substring(2);

        if (str.indexOf(",") != 0) {
            JOptionPane.showMessageDialog(null, "Format: (xx,yy)");
            return false;
        }

        str = str.substring(1);

        String eTriangle = str.substring(0, 2);
        int end;
        try {
            end = Integer.parseInt(eTriangle);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "You have to write a number not a string!");
            return false;
        }
        endingTriangle = end;
        str = str.substring(2);
        if (str.indexOf(")") != 0) {
            return false;
        }

        return true;
    }

}

