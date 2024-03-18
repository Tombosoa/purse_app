package com.example.PurseApp.Entity;

import java.util.Objects;
import java.util.UUID;

public class TransferHistory {
    private int id;
    private UUID idTransactionDebited;
    private UUID idTransactionCredited;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getIdTransactionDebited() {
        return idTransactionDebited;
    }

    public void setIdTransactionDebited(UUID idTransactionDebited) {
        this.idTransactionDebited = idTransactionDebited;
    }

    public UUID getIdTransactionCredited() {
        return idTransactionCredited;
    }

    public void setIdTransactionCredited(UUID idTransactionCredited) {
        this.idTransactionCredited = idTransactionCredited;
    }

    public TransferHistory(int id, UUID idTransactionDebited, UUID idTransactionCredited) {
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
        return getId() == that.getId() && Objects.equals(getIdTransactionDebited(), that.getIdTransactionDebited()) && Objects.equals(getIdTransactionCredited(), that.getIdTransactionCredited());
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
