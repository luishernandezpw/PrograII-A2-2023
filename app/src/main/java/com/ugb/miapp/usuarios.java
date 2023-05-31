package com.ugb.miapp;

public class usuarios {
    String userName, email, urlPhoto, urlPhotoFirestore, token;

    public usuarios(){}
    public usuarios(String userName, String email, String urlPhoto, String urlPhotoFirestore, String token) {
        this.userName = userName;
        this.email = email;
        this.urlPhoto = urlPhoto;
        this.urlPhotoFirestore = urlPhotoFirestore;
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getUrlPhotoFirestore() {
        return urlPhotoFirestore;
    }

    public void setUrlPhotoFirestore(String urlPhotoFirestore) {
        this.urlPhotoFirestore = urlPhotoFirestore;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
