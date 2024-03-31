package com.example.PurseApp.Entity;

import java.util.Objects;
import java.util.UUID;

public class AccountInterest {
    private UUID idAccount;
    private double actual_due;
    private double counts;

    public UUID getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(UUID idAccount) {
        this.idAccount = idAccount;
    }

    public double getActual_due() {
        return actual_due;
    }

    public void setActual_due(double actual_due) {
        this.actual_due = actual_due;
    }

    public double getCounts() {
        return counts;
    }

    public void setCounts(double counts) {
        this.counts = counts;
    }

    public AccountInterest(UUID idAccount, double actual_due, double counts) {
        this.idAccount = idAccount;
        this.actual_due = actual_due;
        this.counts = counts;
    }

    public AccountInterest() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountInterest that)) return false;
        return Double.compare(getActual_due(), that.getActual_due()) == 0 && Double.compare(getCounts(), that.getCounts()) == 0 && Objects.equals(getIdAccount(), that.getIdAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdAccount(), getActual_due(), getCounts());
    }

    @Override
    public String toString() {
        return "AccountInterest{" +
                "idAccount=" + idAccount +
                ", actual_due=" + actual_due +
                ", counts=" + counts +
                '}';
    }
}
