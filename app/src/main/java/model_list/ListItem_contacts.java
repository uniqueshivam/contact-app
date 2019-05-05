package model_list;

import android.net.Uri;

public class ListItem_contacts {


    private String email,name,mobile;
    String ID;
   Uri img_uri;


    public ListItem_contacts(String name,String Id, Uri img_uri) {

        this.name = name;
       this.img_uri = img_uri;
       this.ID =Id;

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Uri getImg_uri() {
        return img_uri;
    }

    public void setImg_uri(Uri img_uri) {
        this.img_uri = img_uri;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
