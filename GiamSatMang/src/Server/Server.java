/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.awt.Color;
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

public class Server {

    ServerSocket ss = null;
    public static ArrayList<Socket> listClient = null;
    public static ArrayList<JButton> listsButton = null;

    public Server(ArrayList<JButton> list) throws IOException {
        Server.listsButton = list;
        ServerSocket ss = new ServerSocket(2000);
        listClient = new ArrayList<Socket>();
        InputStream in = null;
        OutputStream out = null;

        while (true) {
            Socket s = null;

            try {
                s = ss.accept();
                listClient.add(s);
                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams 
                try {
                    in = s.getInputStream();
                } catch (IOException ex) {
                    System.out.println("Can't get socket input stream. ");
                }

                try {
                    String host = s.getInetAddress().toString();
                    host = host.replace(".", "_");
                    out = new FileOutputStream("C:\\Users\\Administrator\\laptrinhmang\\output\\" + host + "ReceivedTmp.txt");
                } catch (FileNotFoundException ex) {
                    System.out.println("File not found. ");
                }

                System.out.println("Assigning new thread for this client");
                Thread t = new ClientHandler(s, in, out);

                // Invoking the start() method 
                t.start();

            } catch (Exception e) {
                s.close();
            }
        }
    }
}

// ClientHandler class 
class ClientHandler extends Thread {

    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final InputStream is;
    final OutputStream os;
    final Socket s;

    // Constructor 
    public ClientHandler(Socket s, InputStream is, OutputStream os) {
        this.s = s;
        this.is = is;
        this.os = os;
    }

    @Override
    public void run() {
        if (getClient(this.s) != -1) {
            Server.listsButton.get(getClient(this.s)).setBackground(Color.GREEN);
        } else {
            System.out.println("Client kêt nôi không xacs định");
        }

        while (true) {

            try {
                receiveFile(this.s, is, os);
                appendFile();
                Thread.sleep(100000);

            } catch (IOException e) {
                System.out.println("sssssssssss");
                e.printStackTrace();
                Server.listsButton.get(getClient(this.s)).setBackground(Color.red);
                break;
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            // closing resources 
            this.is.close();
            this.os.close();

        } catch (IOException e) {
        }
    }

    public int getClient(Socket socket) {
        String host = s.getInetAddress().toString();
        int ret;
        switch (host) {
            case "/127.0.0.1": {
                ret = 0;
                break;
            }
            case "/192.168.58.19": {
                ret = 1;
                break;
            }
            default: {
                ret = -1;
                break;
            }
        }
        return ret;

    }

    public void receiveFile(Socket s, InputStream in, OutputStream out) {

        try {
            byte[] bytes = new byte[16 * 1024];

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
//        out.close();
//        in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void appendFile() throws FileNotFoundException, IOException {
        String host = s.getInetAddress().toString();
        host = host.replace(".", "_");
        String input = "C:\\Users\\Administrator\\laptrinhmang\\output\\" + host + "ReceivedTmp.txt";
        FileInputStream fis = new FileInputStream(new File(input));
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line != null && !line.isEmpty()) {
                FileWriter fw = new FileWriter(new File("C:\\Users\\Administrator\\laptrinhmang\\output\\" + host + "Log.txt"), true);
                try (BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.newLine();
                    bw.write(line);
                    line = "";
                }

            }
        }
    }

}
