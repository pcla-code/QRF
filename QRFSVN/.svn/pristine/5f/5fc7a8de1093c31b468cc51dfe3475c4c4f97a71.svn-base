package edu.upenn.qrf.tools;

import java.util.Arrays;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 * @author vvelsen
 */
public class QRFDataTools {

  /**
   * 
   * @param source
   * @param chunksize
   * @return
   */
  public static byte[][] divideArray(byte[] source, int chunksize) {

    byte[][] ret = new byte[(int)Math.ceil(source.length / (double)chunksize)][chunksize];

    int start = 0;

    for(int i = 0; i < ret.length; i++) {
        ret[i] = Arrays.copyOfRange(source,start, start + chunksize);
        start += chunksize ;
    }

    return ret;
  }
  
  /**
   * 
   * @param aFile
   * @return
   * 
   * Check to see if this works on Android, alternatives:
   * https://stackoverflow.com/questions/2418485/how-do-i-convert-a-byte-array-to-base64-in-java
   */
  public static byte [] readFileToByteArray (String aFile) {

    Path path = Paths.get(aFile);
    byte[] data=null;
    
    try {
      data = Files.readAllBytes(path);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return (null);
    }
    
    return (data);
  }
}
