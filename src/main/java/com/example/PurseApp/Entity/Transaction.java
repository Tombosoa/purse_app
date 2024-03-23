package com.example.PurseApp.Entity;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    private int id;
    private String type;
    private String description;
    private LocalDate registrationDate;
    private LocalDate effectiveDate;
    private double amount;
    private boolean status;
    private String reference;
    private int idCategory;
    private String idAccount;
    private String label;
    private String situation;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public Transaction(int id, String type, String description, LocalDate registrationDate, LocalDate effectiveDate, double amount, boolean status, String reference, int idCategory, String idAccount) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.registrationDate = registrationDate;
        this.effectiveDate = effectiveDate;
        this.amount = amount;
        this.status = status;
        this.reference = reference;
        this.idCategory = idCategory;
        this.idAccount = idAccount;
    }

    public Transaction() {
    }

    public Transaction(int id, String type, String description, LocalDate registrationDate, LocalDate effectiveDate, double amount, boolean status, String reference, int idCategory, String idAccount, String label, String situation) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.registrationDate = registrationDate;
        this.effectiveDate = effectiveDate;
        this.amount = amount;
        this.status = status;
        this.reference = reference;
        this.idCategory = idCategory;
        this.idAccount = idAccount;
        this.label = label;
        this.situation = situation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return getId() == that.getId() && Double.compare(getAmount(), that.getAmount()) == 0 && isStatus() == that.isStatus() && getIdCategory() == that.getIdCategory() && Objects.equals(getType(), that.getType()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getRegistrationDate(), that.getRegistrationDate()) && Objects.equals(getEffectiveDate(), that.getEffectiveDate()) && Objects.equals(getReference(), that.getReference()) && Objects.equals(getIdAccount(), that.getIdAccount()) && Objects.equals(getLabel(), that.getLabel()) && Objects.equals(getSituation(), that.getSituation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getDescription(), getRegistrationDate(), getEffectiveDate(), getAmount(), isStatus(), getReference(), getIdCategory(), getIdAccount(), getLabel(), getSituation());
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", registrationDate=" + registrationDate +
                ", effectiveDate=" + effectiveDate +
                ", amount=" + amount +
                ", status=" + status +
                ", reference='" + reference + '\'' +
                ", idCategory=" + idCategory +
                ", idAccount='" + idAccount + '\'' +
                ", label='" + label + '\'' +
                ", situation='" + situation + '\'' +
                '}';
    }
}
