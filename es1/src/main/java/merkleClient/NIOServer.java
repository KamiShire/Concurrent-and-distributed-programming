package merkleClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static final String END_OF_SESSION = "close";

    @SuppressWarnings("unused")
    public static void main(String[] args) throws IOException {

        // Selector: multiplexor of SelectableChannel objects
        Selector selector = Selector.open(); // selector is open here

        // ServerSocketChannel: selectable channel for stream-oriented listening sockets
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        InetSocketAddress localAddr = new InetSocketAddress("localhost", 2323);

        // Binds the channel's socket to a local address and configures the socket to listen for connections
        serverSocket.bind(localAddr);

        // Adjusts this channel's blocking mode.
        serverSocket.configureBlocking(false);

        int ops = serverSocket.validOps();
        SelectionKey selectKy = serverSocket.register(selector, ops, null);

        // Infinite loop..
        // Keep server running
        while (true) {

            log("i'm a server and i'm waiting for new connection and buffer select...", "out");
            // Selects a set of keys whose corresponding channels are ready for I/O operations
            selector.select();

            // token representing the registration of a SelectableChannel with a Selector
            Set<SelectionKey> activeKeys = selector.selectedKeys();
            Iterator<SelectionKey> keys = activeKeys.iterator();



            while (keys.hasNext()) {
                log("Entered","out");
                SelectionKey myKey = keys.next();

                // Tests whether this key's channel is ready to accept a new socket connection
                if (myKey.isAcceptable()) {
                    SocketChannel clientSocket = serverSocket.accept();

                    // Adjusts this channel's blocking mode to false
                    clientSocket.configureBlocking(false);

                    // Operation-set bit for read operations
                    clientSocket.register(selector, SelectionKey.OP_READ);
                    //log("Connection Accepted: " + clientSocket.getLocalAddress() + "\n", "err");

                    // Tests whether this key's channel is ready for reading
                } else if(myKey.isReadable()) {
                    log("readable", "out");

                    ByteBuffer buffer = ByteBuffer.allocate(2048);
                    SocketChannel clientSocket = (SocketChannel) myKey.channel();
                    clientSocket.read(buffer);
                    buffer.flip();

                    String result = new String(buffer.array()).trim();

                    log("--- Message received: " + result, "out");

                    if(result.equals("close")) {
                        clientSocket.close();
                        log("Connection closed","out");
                    }
                    else{
                        clientSocket.register(selector, SelectionKey.OP_WRITE);
                    }
                }
                else if(myKey.isWritable()) {
                    log("writable","out");

                    SocketChannel clientSocket = (SocketChannel) myKey.channel();
                    ArrayList<String> test = new ArrayList<>();
                    test.add(HashUtil.md5Java("a"));
                    test.add(HashUtil.md5Java("4"));
                    test.add(HashUtil.md5Java("ff"));
                    test.add(HashUtil.md5Java("r"));
                    test.add(HashUtil.md5Java("2"));

                    log("---- hash list:","out");
                    System.out.println("number of elements: "+test.size()+test);
                    int bufferSize = (test.get(1).getBytes().length)*test.size();


                    ByteBuffer outBuffer = ByteBuffer.allocate(2048);
                    String concat = "";
                    for(String element: test) {
                        concat += element;
                    }
                    System.out.println("----Concat: "+concat);

                    outBuffer.clear();
                    outBuffer.put(concat.getBytes("UTF-8"));

                    outBuffer.flip();
                    clientSocket.write(outBuffer);

                    clientSocket.register(selector, SelectionKey.OP_READ);

                }
                keys.remove();


                //important: should delete, otherwise re-iterated the next turn again.

            }
        }
    }

    private static void log(String str, String mode) {
        switch(mode) {
            case "out": {System.out.println(str); break;}
            case "err": {System.err.println(str); break;}
            default: {}
        }
    }
}