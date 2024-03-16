package com.example.PurseApp.Entity;

import java.sql.Date;
import java.util.Objects;

public class Account extends Client{
    private String id;
    private double balance;
    private boolean creditAuthorization;
    private String idClient;
    private int idBank;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account(String id, String firstname, String lastname, Date birthdate, double monthlyPay, double balance, boolean creditAuthorization, String name, String idClient) {
        super(firstname, lastname, birthdate, monthlyPay);
        this.id = id;
        this.balance = balance;
        this.creditAuthorization = creditAuthorization;
        this.name = name;
        this.idClient = idClient;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isCreditAuthorization() {
        return creditAuthorization;
    }

    public void setCreditAuthorization(boolean creditAuthorization) {
        this.creditAuthorization = creditAuthorization;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public int getIdBank() {
        return idBank;
    }

    public void setIdBank(int idBank) {
        this.idBank = idBank;
    }

    public Account(String firstname, String lastname, Date birthdate, double monthlyPay, String id, double balance, boolean creditAuthorization, int idBank) {
        super(firstname, lastname, birthdate, monthlyPay);
        this.id = id;
        this.balance = balance;
        this.creditAuthorization = creditAuthorization;
        this.idBank = idBank;
    }

    public Account() {
    }

    public Account(double balance, boolean creditAuthorization, String idClient, int idBank) {
        this.balance = balance;
        this.creditAuthorization = creditAuthorization;
        this.idClient = idClient;
        this.idBank = idBank;
    }

    public Account(double balance, String idClient, int idBank) {
        this.balance = balance;
        this.idClient = idClient;
        this.idBank = idBank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        if (!super.equals(o)) return false;
        return Double.compare(getBalance(), account.getBalance()) == 0 && isCreditAuthorization() == account.isCreditAuthorization() && getIdBank() == account.getIdBank() && Objects.equals(getId(), account.getId()) && Objects.equals(getIdClient(), account.getIdClient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getBalance(), isCreditAuthorization(), getIdClient(), getIdBank());
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", balance=" + balance +
                ", creditAuthorization=" + creditAuthorization +
                ", idClient='" + idClient + '\'' +
                ", idBank=" + idBank +
                '}';
    }
}
