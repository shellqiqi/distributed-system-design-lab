package seu;

public class MessageUtil {

    public short message;

    public MessageUtil(int code, int parameter) {
        message = 0;
        setCode(code);
        setParameter(parameter);
    }

    public void setParameter(int parameter) {
        message &= 0x0C00;
        message |= parameter;
    }

    public short getParameter() {
        return (short) (message & 0x03FF);
    }

    public void setCode(int code) {
        message &= 0x03FF;
        message |= code << 10;
    }

    public short getCode() {
        return (short) ((message & 0x0C00) >> 10);
    }
}
