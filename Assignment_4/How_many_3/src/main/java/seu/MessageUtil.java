package seu;

public class MessageUtil {

    public static short getMessage(int code, int parameter) {
        short message = 0;
        return setParameter(setCode(message, code), parameter) ;
    }

    public static short getMessage(int code) {
        short message = 0;
        return setParameter(setCode(message, code), 0) ;
    }

    public static short setParameter(short message, int parameter) {
        message &= 0x0C00;
        message |= parameter;
        return message;
    }

    public static short getParameter(short message) {
        message &= 0x03FF;
        return message;
    }

    public static short setCode(short message, int code) {
        message &= 0x03FF;
        message |= (code << 10);
        return message;
    }

    public static short getCode(short message) {
        message &= 0x0C00;
        message >>= 10;
        return message;
    }
}
