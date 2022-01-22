/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giamsatmang;

import Connect.Info;
import Connect.InfoDAO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrawData {

    private final String USER_AGENT = "Mozilla/5.0";
    static String strRespond, nameType, strHandle;
    static String[] arrData;
    public String vId;
    public String pId;
    InfoDAO usbDAO;

    public CrawData(String vId, String pId) {
        this.vId = vId;
        this.pId = pId;
        try {
            addInfoUsbDB(this.vId, this.pId);
        } catch (Exception e) {
        }
        
    }

    
    public static void main(String[] args) throws Exception {
        
    }
    private String sendGetUsb(String vID, String pID) throws Exception {

        String url = "https://www.the-sz.com/products/usbid/index.php?v=" + vID
                + "&p=" + pID + "&n=";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);

        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();

    }

    private String nameTypeUsbReuslt(String vID, String pID) {
        nameType = "";
        try {
            strRespond = sendGetUsb(vID, pID);
        } catch (Exception ex) {
            Logger.getLogger(CrawData.class.getName()).log(Level.SEVERE, null, ex);
        }
        strRespond = strRespond.substring(0, strRespond.indexOf("Some company logos are loaded from"));
        strHandle = strRespond.substring(strRespond.lastIndexOf("<table class=\"usbid_result\">"),
                strRespond.lastIndexOf("</table>"));
        strHandle = strHandle.substring(strHandle.lastIndexOf("<tr>"), strHandle.length());
        arrData = strHandle.split("</td>");
        for (String i : arrData) {
        }
        String temp = arrData[3];
        nameType = temp.substring(temp.indexOf("<td><div class=\"usbid_result_name\">"), temp.indexOf("</div>"));
        nameType = nameType.substring(nameType.lastIndexOf(">") + 1);
        System.out.println("Lamma" + nameType);
        if (nameType == null) {
            return "";
        }
        return nameType;
    }

    private boolean addInfoUsbDB(String vID, String pID) {
        usbDAO = new InfoDAO();
        String nameType = nameTypeUsbReuslt(vID, pID);
        if (!usbDAO.checkDB(vID, pID) && nameType != "") {
            usbDAO.inSertUsbInfo(new Info(vID, pID, nameType));
            return true;
        } else {
            return false;
        }

    }

}
