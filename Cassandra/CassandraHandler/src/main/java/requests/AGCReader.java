package requests;

import java.io.Serializable;


public class AGCReader implements Serializable {
    private String method;
    private int card_id = 0;
    private String passport;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String birthday;

    public AGCReader(){}

    public AGCReader(int card_id, String passport, String first_name, String middle_name, String last_name, String birthday, String method){
        this.card_id=card_id;
        this.passport=passport;
        this.first_name=first_name;
        this.middle_name=middle_name;
        this.last_name=last_name;
        this.birthday=birthday;
        this.method=method;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
