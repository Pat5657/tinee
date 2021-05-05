
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Patryk
 */
public class ClientTest {
    
    Client client;
    
//    private final PrintStream standardOut = System.out;
//    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    
//    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//    private final PrintStream originalOut = System.out;
    
    @Before
    public void setUp() {
        this.client = new Client("testerBot", "localhost", 8888, new Locale("en", "GB"));
        
    }

//    @Before
//    public void setUpStreams() {
//      System.setOut(new PrintStream(outContent));
//    }
//
//    @After
//    public void restoreStreams() {
//      System.setOut(originalOut);
//    }

    private void symInput(String... args) throws UnsupportedEncodingException {
        String toRead = "";
        for (String arg : args) {
            toRead += arg + System.getProperty("line.separator");
        }
        ByteArrayInputStream input = new ByteArrayInputStream(toRead.getBytes("UTF-8"));
        System.setIn(input);
    }
    
    @Test
    public void readNoArgsExceptionThrownTest() throws Exception {
        this.symInput("read");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out, false, "UTF-8"));
        try {
            this.client.run();
            String data = out.toString("UTF-8");
            assertTrue(data.contains("Tag is missing"));
        } catch (RuntimeException e) {
          fail("Exception thrown");
        }
        fail("failed");
    }
    
    @Test
    public void manageNoArgsExceptionThrownTest() throws Exception {
        this.symInput("manage");
        
        try {
            this.client.run();
        } catch (RuntimeException e) {
            assertEquals("manageNoArgsExceptionThrownTest", "java.lang.ArrayIndexOutOfBoundsException: 0", e.getMessage());
        }
    }
    
    @Test
    public void pushNoLinesExceptionThrownTest() throws Exception {
        this.symInput("manage test", "push");
        
        try {
            this.client.run();
        } catch (RuntimeException e) {
            assertEquals("pushNoLinesExceptionThrownTest", "java.lang.IllegalArgumentException: Tines list should be non-empty.", e.getMessage());
        }
    }
}
