package com.example.PurseApp.Entity;


import java.sql.Date;
import java.util.Objects;

public class Client {
    private String id;
    private String firstname;
    private String lastname;
    private Date birthdate;
    private double monthlyPay;

    public Client(String id, String firstname, String lastname, Date birthdate, double monthlyPay) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.monthlyPay = monthlyPay;
    }

    public Client() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public java.sql.Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public double getMonthlyPay() {
        return monthlyPay;
    }

    public void setMonthlyPay(double monthlyPay) {
        this.monthlyPay = monthlyPay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return Double.compare(getMonthlyPay(), client.getMonthlyPay()) == 0 && Objects.equals(getId(), client.getId()) && Objects.equals(getFirstname(), client.getFirstname()) && Objects.equals(getLastname(), client.getLastname()) && Objects.equals(getBirthdate(), client.getBirthdate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstname(), getLastname(), getBirthdate(), getMonthlyPay());
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthdate=" + birthdate +
                ", monthlyPay=" + monthlyPay +
                '}';
    }
}
