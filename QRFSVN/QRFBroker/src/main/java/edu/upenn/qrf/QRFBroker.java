package edu.upenn.qrf;

import org.jdom2.Document;
import org.jdom2.Element;

import edu.upenn.qrf.comm.QRFCommBase;

/**
 * @author vvelsen
 */
public class QRFBroker extends QRFCommBase {
    
  /**
   * 
   */
  public QRFBroker() {
    
  }
  
  /**
   * 
   * @param aMessage
   */
  public void publishXML (String aReceiver,String aMessage) {
    publish (aReceiver,aMessage);
  }  
    
  /**
   * 
   */
  public void parseXML (Document document) {
    //debug ("QRFLogReceiver:parseXML ()");
    
    Element node = document.getRootElement();
    
    Boolean processed=true;

    if (node!=null) {
      String foundSession = node.getChildText ("session");
      String foundClass = node.getChildText ("class_name");
      String foundIdentifier = node.getChildText ("device_identifier");
      String foundAction = node.getChildText ("action");

      debug ("session : " + foundSession);
      debug ("device_identifier: "  + foundIdentifier);
      
      //QRFClientProxy clientProxy=registerSession (foundSession);
      registerSession (foundSession);
      
      // At this point we should have a valid proxy for our QRF
      // Android App. We can use it to save the App's state and
      // to use it to send messages to it directly.
      
        //>------------------------------------------------------------------------
        
        if (foundAction!=null) {
          if (foundAction.equalsIgnoreCase("ping")==true ) {    
            debug ("Processing ping, replying with pong ...");
            publishXML (foundSession,messageGenerator.createActionMessage("pong"));
          }
          
          if (foundAction.equalsIgnoreCase("broadcast")==true ) {
            debug ("No need to do anything, everyone should already have received this message");
            processed=true;
          }
          
          if (foundAction.equalsIgnoreCase("register")==true ) {
            debug ("No need to do anything, the base code takes care of automatic registration");
            processed=true;
          }
        }        

        //>------------------------------------------------------------------------
        
        if (foundClass!=null) {
          //sendUsers(foundSession);
        }
        
        //>------------------------------------------------------------------------
    } else {
      debug ("Error: no msg element found!");
    }
    
    if (processed==false) {
     debug ("Error: appropriate action found in client message");
    } 
  }

  /**
   * 
   * @param argv
   * @throws Exception
   */
  public static void main(String[] argv) throws Exception { 
    System.out.println("Starting the QRF Broker ...");
    
    QRFBroker broker=new QRFBroker ();
    broker.processCommandline (argv);
    broker.init (true);
  }
}
