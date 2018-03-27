package seu;

import org.junit.Test;

import static org.junit.Assert.*;
import static seu.MessageUtil.*;

public class MessageUtilTest {

    private short message = getMessage(0b10, 12);

    @Test
    public void setParameterTest() {
        assertEquals(setParameter(message, 34), 2082);
    }

    @Test
    public void getParameterTest() {
        assertEquals(12, getParameter(message));
    }

    @Test
    public void setCodeTest() {
        assertEquals(setCode(message, 0b11), 3084);
    }

    @Test
    public void getCodeTest() {
        assertEquals(0b10, getCode(message));
    }
}