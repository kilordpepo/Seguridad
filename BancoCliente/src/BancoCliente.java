
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class BancoCliente {

    public static void main(String[] args) {
        int i = 1;
        try {
            String certificateName = "CA";
            String path = "C:/Seguridad/" + certificateName + ".jks";
            String passphrase = "arepas95";
            System.setProperty("javax.net.ssl.keyStore", path);
            System.setProperty("javax.net.ssl.keyStorePassword",passphrase);
            //Creaet a SSLServersocket
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket = (SSLServerSocket) factory.createServerSocket(8189);
            System.out.println("Servidor Iniciado..");
            while(true){
            new ThreadedEchoHandler((SSLSocket) sslserversocket.accept(), i).run();
            i++;
            }
        } catch (IOException e) {
            System.out.print(e);
        }
    }

}
