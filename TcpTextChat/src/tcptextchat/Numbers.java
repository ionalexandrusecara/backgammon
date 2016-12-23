
package tcptextchat;

public class Numbers {
    final static int chatPort   =  4242; // the port number to be used
  final static int soTimeout  =    10; // ms to wait for socket read
  final static int readRetry  =    10; // # re-try of handshake
  final static int sleepTime  =   200; // ms to sleep - 200 is fine
  final static int bufferSize =   128; // # chars in line 
}
