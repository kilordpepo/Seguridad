
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.SSLSocket;

class ThreadedEchoHandler extends Thread {

    private SSLSocket ss;
    private int counter;

    public ThreadedEchoHandler(SSLSocket i, int c) {
        ss = i;
        counter = c;
       System.out.println("Conexion "+c+" entrante iniciada");
    }

    public void run() {
        try {
            DataOutputStream out = new DataOutputStream(ss.getOutputStream());
            DataInputStream in = new DataInputStream(ss.getInputStream());
            System.out.println("Enviando mensaje..");
            out.writeUTF("Hola! Escribir BYE para salir");
            boolean done = false;
            System.out.println("Starting Server loop..");
            while (!done) {
                System.out.println("Waiting an response..");
                String str = in.readUTF();
                System.out.println("Echo: (" + counter + "): " + str);
                if (str.trim().equals("BYE")) {
                    done = true;
                } else {
                    out.writeUTF("Mantengo la conexion, escriba BYE para salir");
                }
            }
            ss.close();
            System.out.println("Socket Closed");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
