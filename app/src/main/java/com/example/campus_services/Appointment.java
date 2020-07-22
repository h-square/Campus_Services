package com.example.campus_services;

public class Appointment {
    private String Student_Id;
    private String Status;
    private String Date;
    private long Time;
    private int Slot;
    private long Arrival_Time;

    public Appointment(String student_Id, String status, String date, long time, int slot) {
        Student_Id = student_Id;
        Status = status;
        Date = date;
        Time = time;
        Slot = slot;
        Arrival_Time = -1;
    }

    public Appointment() {
    }

    public String getStudent_Id() { return Student_Id; }

    public String getStatus() { return Status; }

    public String getDate() { return Date; }

    public long getTime() { return Time; }

    public int getSlot() { return Slot; }

    public void setArrival_Time(long arrival_Time) { Arrival_Time = arrival_Time; }

    public void setStudent_Id(String student_Id) { Student_Id = student_Id; }

    public void setStatus(String status) { Status = status; }

    public void setDate(String date) { Date = date; }

    public void setTime(long time) { Time = time; }

    public void setSlot(int slot) { Slot = slot; }

    public long getArrival_Time() { return Arrival_Time; }

}
