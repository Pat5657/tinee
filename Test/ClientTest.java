
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
        
    public ClientTest() {
        
    }
    
    @Before
    public void setUp() {
        this.client = new Client();
        this.client.set("testerBot", "localhost", 8888);        
    }
    
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

        try {
            this.client.run();
        } catch (RuntimeException e) {
            assertEquals(null, "java.lang.ArrayIndexOutOfBoundsException: 0", e.getMessage());
        }
    }
    
    @Test
    public void manageNoArgsExceptionThrownTest() throws Exception {
        this.symInput("manage");
        
        try {
            this.client.run();
        } catch (RuntimeException e) {
            assertEquals(null, "java.lang.ArrayIndexOutOfBoundsException: 0", e.getMessage());
        }
    }
    
    @Test
    public void pushNoLinesExceptionThrownTest() throws Exception {
        this.symInput("manage test", "push");
        
        try {
            this.client.run();
        } catch (RuntimeException e) {
            assertEquals(null, "java.lang.IllegalArgumentException: Tines list should be non-empty.", e.getMessage());
        }
    }
}
