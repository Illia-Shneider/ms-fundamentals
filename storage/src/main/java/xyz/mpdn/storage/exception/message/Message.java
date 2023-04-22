package xyz.mpdn.storage.exception.message;


import java.util.Date;

public interface Message {
    void setTimestamp(Date date);

    void setStatusCode(int httpStatusCode);

    void setUri(String uri);

    void setException(Exception exception);
}
