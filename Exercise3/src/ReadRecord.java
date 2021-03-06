/** 
 * Started by M. Moussavi
 * March 2015
 * Completed by: STUDENT(S) NAME
 */

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadRecord {
    
    private ObjectInputStream input;
    
    /**
     *  opens an ObjectInputStream using a FileInputStream, and prints the contents to the console
     */
    private void readObjectsFromFile(String name)
    {
        MusicRecord record;
        
        try
        {
            input = new ObjectInputStream(new FileInputStream( name ) );
        }
        catch ( IOException ioException )
        {
            System.err.println( "Error opening file." );
        }
        
        /* The following loop is supposed to use readObject method of
         * ObjectInputSteam to read a MusicRecord object from a binary file that
         * contains several reords.
         * Loop should terminate when an EOFException is thrown.
         */
        
        try
        {
            while ( true )
            {

                record = (MusicRecord) input.readObject();

                System.out.println(record.getYear());
                System.out.println(record.getSongName());
                System.out.println(record.getSingerName());
                System.out.println(record.getPurchasePrice());
                System.out.println("--------------------");
           
            }   // END OF WHILE
        }
        catch(EOFException eof) {
            System.out.println("Finished reading file");
        }
        catch (IOException ioEx) {
            ioEx.printStackTrace();
            System.err.println("Error reading from file");
        }
        catch (ClassNotFoundException classEx) {
            System.err.println("No class MusicRecord found");
        }
    }           // END OF METHOD 

    /**
     * Creates a new ReadRecord object and reads from the specified file
     * @param args
     */
    public static void main(String [] args)
    {
        ReadRecord d = new ReadRecord();
        d.readObjectsFromFile("allSongs.ser");
    }
}