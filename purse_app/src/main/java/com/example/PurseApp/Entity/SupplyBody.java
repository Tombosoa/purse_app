package com.example.PurseApp.Entity;

import java.util.Objects;
import java.util.UUID;

public class SupplyBody {
    private String action;
    private double supplyAmount;
    private UUID idAccount;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public double getSupplyAmount() {
        return supplyAmount;
    }

    public void setSupplyAmount(double supplyAmount) {
        this.supplyAmount = supplyAmount;
    }

    public UUID getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(UUID idAccount) {
        this.idAccount = idAccount;
    }

    public SupplyBody(String action, double supplyAmount, UUID idAccount) {
        this.action = action;
        this.supplyAmount = supplyAmount;
        this.idAccount = idAccount;
    }

    public SupplyBody() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupplyBody that)) return false;
        return Double.compare(getSupplyAmount(), that.getSupplyAmount()) == 0 && Objects.equals(getAction(), that.getAction()) && Objects.equals(getIdAccount(), that.getIdAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAction(), getSupplyAmount(), getIdAccount());
    }

    @Override
    public String toString() {
        return "SupplyBody{" +
                "action='" + action + '\'' +
                ", supplyAmount=" + supplyAmount +
                ", idAccount=" + idAccount +
                '}';
    }
}
