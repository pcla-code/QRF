package edu.upenn.qrf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class QRFBase 
{
  public static Logger logOutput = null;
  
	public static String TAG="QRF";
	public static int lineCounter=0;
	
	public static Boolean useInternalLogging=false;
	
	/**
	 * 
	 */
	public QRFBase () {
	  logOutput = LoggerFactory.getLogger(QRFBase.class);
	}
	
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		/*
	  if (QRFBase.useInternalLogging==true) {
		  System.out.println ("["+lineCounter + "] "+ aMessage);
		  QRFBase.lineCounter++;
	  } else {
		  logOutput.info(aMessage);
	  } 
	  */
		
	  System.out.println ("["+lineCounter + "] "+ aMessage);
	  QRFBase.lineCounter++;		
	}	
	
  /**
   * 
   */
  protected static void staticdebug (String aMessage) {
  	/*
    if (QRFBase.useInternalLogging==true) {
      System.out.println ("["+lineCounter + "] "+ aMessage);
      QRFBase.lineCounter++;
    } else {
      QRFBase.logOutput.info(aMessage);
    }*/ 
  	
    if (QRFBase.useInternalLogging==true) {
      System.out.println ("["+lineCounter + "] "+ aMessage);
      QRFBase.lineCounter++;
    } else {
      QRFBase.logOutput.info(aMessage);
    }  	
  }	
}	