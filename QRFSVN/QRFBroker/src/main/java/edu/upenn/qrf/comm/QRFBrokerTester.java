package edu.upenn.qrf.comm;

import java.util.Timer;
import java.util.TimerTask;

import org.jdom2.Document;
import org.jdom2.Element;

import edu.upenn.qrf.QRFClientProxy;
import edu.upenn.qrf.QRFLinkData;
import edu.upenn.qrf.QRFMessageGenerator;

/*
import org.apache.commons.cli.*;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
*/

/**
 * @author Martin van Velsen <vvelsen@knossys.com>
 */
public class QRFBrokerTester extends QRFCommBase {

  /** 
   * @return
   */
  public boolean getRandomBoolean() {
    return Math.random() < 0.5;  
  }
  
	/**
	 * 
	 */
	public void parseXML(Document document) {
		debug("(server) parseXML ()");

		Element node = document.getRootElement();

		Boolean processed = false;

		if (node != null) {
			String foundSession = node.getChildText("session");
			String foundIdentifier = node.getChildText("device_identifier");
			String foundAction = node.getChildText("action");
			Element fileNode=node.getChild("file");
			
			debug("session : " + foundSession);
			debug("device_identifier: " + foundIdentifier);

			QRFClientProxy clientProxy=registerSession (foundSession);
			
			// At this point we should have a valid proxy for our QRF
			// Android App. We can use it to save the App's state and
			// to use it to send messages to it directly.

			// >------------------------------------------------------------------------

			String foundAck = node.getChildText("acknowledgement_to_server");

			if (foundAck != null) {

				if (foundAck.equalsIgnoreCase("message accepted") == true) {
					debug("Processing app acknowledgement message: accepted");
					
					if (clientProxy.setState(QRFClientProxy.CLIENTSTATE_INPUT)==false) {
						sendStateTransitionError (clientProxy);
					} else {
					  // TAKE APPROPRIATE ACTION HERE
						
					}
				}

				if (foundAck.equalsIgnoreCase("message dropped") == true) {
					debug("Processing app acknowledgement message: rejected");
					
					if (clientProxy.setState(QRFClientProxy.CLIENTSTATE_INPUT)==false) {
						sendStateTransitionError (clientProxy);
					} else {
					  // TAKE APPROPRIATE ACTION HERE
						
					}
				}
				
				// SEND THE NEXT STUDENT'S INFO. RIGHT NOW IT SENDS DUMMY DATA
				if (getRandomBoolean ()==false) {
				  debug ("Sending student info straight up ...");
				  sendStudentInfo (foundSession);
				} else {
				  debug ("Sending empty queue message and waiting to send regular student info again ...");
				  
				  sendEmptyQueueMessage (foundSession);
				  
				  QRFLinkData.sessionCache=foundSession;
				  
				  Timer timer = new Timer();
				  
				  timer.schedule(new TimerTask() {
				    @Override
				    public void run() {
				      debug ("Sending student info after empty queue message");
				      sendStudentInfo (QRFLinkData.sessionCache);
				    }
				  }, 5000L);				  
				}
				
				processed = true;
			}

			// >------------------------------------------------------------------------

			String foundClass = node.getChildText("class_name");

			if (foundClass != null) {
				debug("Checking class name (" + foundClass + ")...");

				// CHECK THE VALIDITY OF THE CLASS NAME HERE
				if (true) {
					// >------------------------------------------------------------------------

					Element aYesNoMessage = QRFMessageGenerator.createYesNoMessage("yes");
					String aRawString = QRFMessageGenerator.XMLFragmentToString(aYesNoMessage);
					this.publish(foundSession, aRawString);
					
				  // >------------------------------------------------------------------------
					
					if (clientProxy.setState(QRFClientProxy.CLIENTSTATE_REGISTERED)==false) {
						sendStateTransitionError (clientProxy);
					} else {
						//publishXML(foundSession, QRFMessageGenerator.XMLFragmentToString(messageGenerator.createStudentList ()));
					}

					// >------------------------------------------------------------------------
				} else {
					foundClass = null; // This makes sure we bottom out below and not send
					                   // the user list
					Element aYesNoMessage = QRFLinkData.generator.createYesNoMessage("no");
					String aRawString = QRFMessageGenerator.XMLFragmentToString(aYesNoMessage);
					this.publish(foundSession, aRawString);
				}

				processed = true;
			}
			
			// >------------------------------------------------------------------------			
			
			if ((fileNode!=null) && (processed == false)) {
				String foundPart = fileNode.getAttributeValue("part");
				String foundOf = fileNode.getAttributeValue("of");
				String foundName = fileNode.getChildText("name");
				
				Element aFileAckMessage = QRFMessageGenerator.createFileAckMessage(foundPart,foundOf,foundName);
				String aRawString = QRFMessageGenerator.XMLFragmentToString(aFileAckMessage);
				this.publish(foundSession, aRawString);				
				
				processed = true;
			}

			// >------------------------------------------------------------------------

			if ((foundAction != null) && (processed == false)) {
				debug ("Found action: " + foundAction + ", processing ...");

        // >----------------------------------------------------------------------
        
        if (foundAction.equalsIgnoreCase("unregister") == true) {
          debug("Processing unregister message ...");                    
          processed = true;
        }				
				
			  // >----------------------------------------------------------------------
				
				if (foundAction.equalsIgnoreCase("ping") == true) {
					debug("Processing ping, replying with pong ...");
					publishXML(foundSession, messageGenerator.createActionMessage("pong"));
					
					processed = true;
				}
				
			  // >----------------------------------------------------------------------

				if (foundAction.equalsIgnoreCase("init") == true) {
					if (clientProxy.setState(QRFClientProxy.CLIENTSTATE_INITIALIZED)==false) {
						sendStateTransitionError (clientProxy);
					} else {
						// We've received an initialization message, reply with a configuration object
						String confString = QRFMessageGenerator.XMLFragmentToString(QRFMessageGenerator.createConfiguration());
						String confMessage = QRFLinkData.generator.createMessage(confString);
						this.publish(foundSession, confMessage);						
						
					  clientProxy.setReady(true);
					}  
					
					processed = true;
				}
				
		  	// >----------------------------------------------------------------------
				
				if (foundAction.equalsIgnoreCase("ready") == true) {
					if (clientProxy.setState(QRFClientProxy.CLIENTSTATE_READY)==false) {
						sendStateTransitionError (clientProxy);
					} else {
						
					  // TAKE APPROPRIATE ACTION HERE. RIGHT NOW IT SENDS DUMMY DATA 
						
						sendStudentInfo (foundSession);
						
						clientProxy.setState(QRFClientProxy.CLIENTSTATE_INPUT);
					}
					
					processed = true;
				}
				
			  // >----------------------------------------------------------------------
			}

			// >------------------------------------------------------------------------

		} else {
			debug("Error: no msg element found!");
		}

		if (processed == false) {
			debug("Error: no appropriate action found in client message");
		}
	}
	
	/**
	* 
	*/
	public void ready() {
		debug("ready ()");
	}

	/**
	 * 
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String[] argv) throws Exception {
		System.out.println("Starting QRF Test Server ...");

		QRFBrokerTester broker = new QRFBrokerTester();
    broker.processCommandline (argv);
    broker.init(true);
	}
}
