package Model;

public class Message {
    private String date,message,photo;

    public Message() {
    }

    public Message(String date, String message, String photo) {
        this.date = date;
        this.message = message;
        this.photo = photo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
