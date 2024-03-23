package com.example.PurseApp.Entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class ApplyInterest {
    private int id;
    private UUID idAccount;
    private int idInterest;
    private LocalDate startDate;
    private double firstDue;
    private double actualDue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(UUID idAccount) {
        this.idAccount = idAccount;
    }

    public int getIdInterest() {
        return idInterest;
    }

    public void setIdInterest(int idInterest) {
        this.idInterest = idInterest;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public double getFirstDue() {
        return firstDue;
    }

    public void setFirstDue(double firstDue) {
        this.firstDue = firstDue;
    }

    public double getActualDue() {
        return actualDue;
    }

    public void setActualDue(double actualDue) {
        this.actualDue = actualDue;
    }

    public ApplyInterest(int id, UUID idAccount, int idInterest, LocalDate startDate, double firstDue, double actualDue) {
        this.id = id;
        this.idAccount = idAccount;
        this.idInterest = idInterest;
        this.startDate = startDate;
        this.firstDue = firstDue;
        this.actualDue = actualDue;
    }

    public ApplyInterest() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplyInterest that)) return false;
        return getId() == that.getId() && getIdInterest() == that.getIdInterest() && Double.compare(getFirstDue(), that.getFirstDue()) == 0 && Double.compare(getActualDue(), that.getActualDue()) == 0 && Objects.equals(getIdAccount(), that.getIdAccount()) && Objects.equals(getStartDate(), that.getStartDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIdAccount(), getIdInterest(), getStartDate(), getFirstDue(), getActualDue());
    }

    @Override
    public String toString() {
        return "ApplyInterest{" +
                "id=" + id +
                ", idAccount=" + idAccount +
                ", idInterest=" + idInterest +
                ", startDate=" + startDate +
                ", firstDue=" + firstDue +
                ", actualDue=" + actualDue +
                '}';
    }
}
