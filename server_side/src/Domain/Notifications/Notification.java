package Domain.Notifications;

import java.util.Observable;

public class Notification {
    private Observable sender;
    private Object content;
    private boolean isRead;

     public Notification(Observable sender, Object content, boolean isRead) {
        this.sender = sender;
        this.content = content;
        this.isRead = isRead;
    }

    public Observable getSender() {
        return sender;
    }

    public void setSender(Observable sender) {
        this.sender = sender;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
