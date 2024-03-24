package com.example.PurseApp.Entity;

import java.time.LocalDate;
import java.util.Objects;

public class AccountStatement {
    private LocalDate effectiveDate;
    private String reference;
    private String motif;
    private double CreditMGA;
    private double DebitMGA;
    private double balance;

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public double getCreditMGA() {
        return CreditMGA;
    }

    public void setCreditMGA(double creditMGA) {
        CreditMGA = creditMGA;
    }

    public double getDebitMGA() {
        return DebitMGA;
    }

    public void setDebitMGA(double debitMGA) {
        DebitMGA = debitMGA;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountStatement(LocalDate effectiveDate, String reference, String motif, double creditMGA, double debitMGA, double balance) {
        this.effectiveDate = effectiveDate;
        this.reference = reference;
        this.motif = motif;
        CreditMGA = creditMGA;
        DebitMGA = debitMGA;
        this.balance = balance;
    }

    public AccountStatement() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountStatement that)) return false;
        return Double.compare(getCreditMGA(), that.getCreditMGA()) == 0 && Double.compare(getDebitMGA(), that.getDebitMGA()) == 0 && Double.compare(getBalance(), that.getBalance()) == 0 && Objects.equals(getEffectiveDate(), that.getEffectiveDate()) && Objects.equals(getReference(), that.getReference()) && Objects.equals(getMotif(), that.getMotif());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEffectiveDate(), getReference(), getMotif(), getCreditMGA(), getDebitMGA(), getBalance());
    }

    @Override
    public String toString() {
        return "AccountStatement{" +
                "effectiveDate=" + effectiveDate +
                ", reference='" + reference + '\'' +
                ", motif='" + motif + '\'' +
                ", CreditMGA=" + CreditMGA +
                ", DebitMGA=" + DebitMGA +
                ", balance=" + balance +
                '}';
    }
}
