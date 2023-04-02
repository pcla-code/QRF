package edu.upenn.qrf;

import java.util.ArrayList;

//import com.rabbitmq.client.Consumer;

import edu.upenn.qrf.comm.QRFCommBase;

/**
 * @author Martin van Velsen <vvelsen@knossys.com>
 */
public class QRFLinkData
{	
  public static String EXCHANGE_NAME = "QRF";
  
  public static QRFMessageGenerator generator=null;
  public static QRFMessageParser parser=null;
  public static QRFCommBase commManager = null;
  public static QRFMessageClient currentListener = null;
  
  public static String session="-1";
  public static ArrayList<String> responses=null;
  
  public static String rabbitmqHost="localhost";
  // Don't use 'guest' if you're not testing on localhost
  public static String rabbitmqUsername="guest";
  // Don't use 'guest' if you're not testing on localhost
  public static String rabbitmqPassword="guest";
  
  public static String sessionCache="";

  public static String [] names = {
  "Domingo",
  "Vanessa",
  "Margarita",
  "Jeniffer",
  "Augustus",
  "Patrina",
  "Shavonda",
  "Tierra",
  "Len",
  "Edith",
  "Laura",
  "Riley",
  "Vallie",
  "Tilda",
  "Charlie",
  "Guy",
  "Maggie",
  "Florentina",
  "Marchelle",
  "Christin",
  "Deangelo",
  "Garnet",
  "Catalina",
  "Divina",
  "Antonia",
  "Kemberly",
  "Danika",
  "Connie",
  "Emmy",
  "Karine",
  "Katlyn",
  "Janelle",
  "Gregorio",
  "Stormy",
  "Clemmie",
  "Milan",
  "Lachelle",
  "Porsche",
  "Cecil",
  "Vance",
  "Lissa",
  "Mafalda",
  "Nancee",
  "Consuela",
  "Lizeth",
  "Jarod",
  "Rodrick",
  "Lakiesha",
  "Monika",
  "Viva"
  };
}
