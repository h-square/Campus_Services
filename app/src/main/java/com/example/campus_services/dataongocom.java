package com.example.campus_services;

public class dataongocom
{
    private String Complain_type;
    private String Complain;
    private String Location;
    private String Image_URL;
    private String User_id;
    private String user_name;
    private String Complain_id;
    private String Worker_name;
    private String Worker_number;
    private String Status_by_student;

    public String getStatus_by_student() {
        return Status_by_student;
    }

    public void setStatus_by_student(String status_by_student) {
        Status_by_student = status_by_student;
    }

    public String getComplain_id() {
        return Complain_id;
    }

    public void setComplain_id(String complain_id) {
        Complain_id = complain_id;
    }

    public String getComplain_type() {
        return Complain_type;
    }

    public void setComplain_type(String complain_type) {
        Complain_type = complain_type;
    }

    public String getComplain() {
        return Complain;
    }

    public void setComplain(String complain) {
        Complain = complain;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getWorker_name() {
        return Worker_name;
    }

    public void setWorker_name(String worker_name) {
        Worker_name = worker_name;
    }

    public String getWorker_number() {
        return Worker_number;
    }

    public void setWorker_number(String worker_number) {
        Worker_number = worker_number;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public dataongocom() {
    }
}
