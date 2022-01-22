/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giamsatmang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockApp {
    
    public static ArrayList listapp = readFile();
    
    
    public static ArrayList readFile(){
        ArrayList list = new ArrayList();
        
        BufferedReader br = null;
        try {   
            br = new BufferedReader(new FileReader("C:\\Users\\Administrator\\laptrinhmang\\Appblock\\ListAppBlock.txt"));       
            String textInALine;

            while ((textInALine = br.readLine()) != null){
                System.out.println(textInALine);
                list.add(textInALine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    public static void main(String[] args) throws IOException {

        run();
        
    }
    public static void run(){
        try {
            appOnWindow();
            
            while(true){
                try {
                    sleep(10000);
                    closeProgram();
                } catch (InterruptedException ex) {
                    Logger.getLogger(BlockApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(BlockApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void closeProgram() throws IOException {
        Runtime rt = Runtime.getRuntime();
        for(int i=0;i<listapp.size();i++){
            rt.exec("taskkill /f /im " +listapp.get(i));
        }
    }

    public static void appOnWindow() throws IOException {
        Process p = Runtime.getRuntime().exec("tasklist.exe");
    }
 
}
