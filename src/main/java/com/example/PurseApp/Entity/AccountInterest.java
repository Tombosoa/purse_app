package com.example.PurseApp.Entity;

import java.util.Objects;
import java.util.UUID;

public class AccountInterest {
    private UUID idAccount;
    private double actualDue;
    private double firstDue;
    private double counts;

    public UUID getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(UUID idAccount) {
        this.idAccount = idAccount;
    }

    public double getActualDue() {
        return actualDue;
    }

    public void setActualDue(double actualDue) {
        this.actualDue = actualDue;
    }

    public double getFirstDue() {
        return firstDue;
    }

    public void setFirstDue(double firstDue) {
        this.firstDue = firstDue;
    }

    public double getCounts() {
        return counts;
    }

    public void setCounts(double counts) {
        this.counts = counts;
    }

    public AccountInterest(UUID idAccount, double actualDue, double firstDue, double counts) {
        this.idAccount = idAccount;
        this.actualDue = actualDue;
        this.firstDue = firstDue;
        this.counts = counts;
    }

    public AccountInterest() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountInterest that)) return false;
        return Double.compare(getActualDue(), that.getActualDue()) == 0 && Double.compare(getFirstDue(), that.getFirstDue()) == 0 && Double.compare(getCounts(), that.getCounts()) == 0 && Objects.equals(getIdAccount(), that.getIdAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdAccount(), getActualDue(), getFirstDue(), getCounts());
    }

    @Override
    public String toString() {
        return "AccountInterest{" +
                "idAccount=" + idAccount +
                ", actualDue=" + actualDue +
                ", firstDue=" + firstDue +
                ", counts=" + counts +
                '}';
    }
}
