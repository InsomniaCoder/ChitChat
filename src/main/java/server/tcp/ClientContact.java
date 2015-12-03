package server.tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by PorPaul on 17/11/2558.
 */
public class ClientContact {

    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    public ClientContact(Socket socket, InputStream inputStream, OutputStream outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
