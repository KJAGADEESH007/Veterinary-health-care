package com.example.vetcare;

public class Doctor{


    private String name,email,contact,qualification,exp,docId;

    public Doctor(){
    }

    public Doctor(String docId, String name, String email, String contact, String qualification, String exp) {
        this.docId = docId;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.qualification = qualification;
        this.exp = exp;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getQualification() {
        return qualification;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
}

