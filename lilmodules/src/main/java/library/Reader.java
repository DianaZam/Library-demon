package library;

import java.io.Serializable;


public class Reader implements Serializable {
    private int card_id;
    private String passport;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String birthday;

    public  Reader(){}

    public  Reader(int card_id, String passport, String first_name, String middle_name, String last_name, String birthday){
        this.card_id=card_id;
        this.passport=passport;
        this.first_name=first_name;
        this.middle_name=middle_name;
        this.last_name=last_name;
        this.birthday=birthday;
    }

    @Override
    public String toString() {
        return "Reader: "+card_id+", "+passport+", "+first_name+", "+middle_name+", "
                +last_name+", "+birthday.toString();
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
