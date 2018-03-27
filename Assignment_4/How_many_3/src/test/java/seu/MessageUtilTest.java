package seu;

import org.junit.Test;

import static org.junit.Assert.*;

public class MessageUtilTest {

    @Test
    public void setParameterTest() {
        MessageUtil m = new MessageUtil(0b10, 12);
        assertEquals(m.message, 2060);
        m.setParameter(34);
        assertEquals(m.message, 2082);
    }

    @Test
    public void getParameterTest() {
        MessageUtil m = new MessageUtil(0b10, 12);
        assertEquals(12, m.getParameter());
    }

    @Test
    public void setCodeTest() {
        MessageUtil m = new MessageUtil(0b10, 12);
        assertEquals(m.message, 2060);
        m.setCode(0b11);
        assertEquals(m.message, 3084);
    }

    @Test
    public void getCodeTest() {
        MessageUtil m = new MessageUtil(0b10, 12);
        assertEquals(0b10, m.getCode());
    }
}