package stukk.common;

/**
 * @author wenli
 * @create 2022-09-01 19:38
 * 自定义业务异常类
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
