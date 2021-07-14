package cn.mpy.shiro.exception;

/**
 * 异常
 * @author mpy
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }

    public UnauthorizedException() {
        super();
    }
}
