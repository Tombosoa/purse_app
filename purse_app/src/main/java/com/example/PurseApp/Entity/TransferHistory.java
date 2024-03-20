package com.example.PurseApp.Entity;

import java.util.Objects;
import java.util.UUID;

public class TransferHistory {
    private int id;
    private int idTransactionDebited;
    private int idTransactionCredited;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTransactionDebited() {
        return idTransactionDebited;
    }

    public void setIdTransactionDebited(int idTransactionDebited) {
        this.idTransactionDebited = idTransactionDebited;
    }

    public int getIdTransactionCredited() {
        return idTransactionCredited;
    }

    public void setIdTransactionCredited(int idTransactionCredited) {
        this.idTransactionCredited = idTransactionCredited;
    }

    public TransferHistory(int id, int idTransactionDebited, int idTransactionCredited) {
        this.id = id;
        this.idTransactionDebited = idTransactionDebited;
        this.idTransactionCredited = idTransactionCredited;
    }

    public TransferHistory() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferHistory that)) return false;
        return getId() == that.getId() && getIdTransactionDebited() == that.getIdTransactionDebited() && getIdTransactionCredited() == that.getIdTransactionCredited();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIdTransactionDebited(), getIdTransactionCredited());
    }

    @Override
    public String toString() {
        return "TransferHistory{" +
                "id=" + id +
                ", idTransactionDebited=" + idTransactionDebited +
                ", idTransactionCredited=" + idTransactionCredited +
                '}';
    }
}
