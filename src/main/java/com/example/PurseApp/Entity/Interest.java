package com.example.PurseApp.Entity;

import java.util.Objects;

public class Interest {
    private int id;
    private double counts;
    private int dayGone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCounts() {
        return counts;
    }

    public void setCounts(double counts) {
        this.counts = counts;
    }

    public int getDayGone() {
        return dayGone;
    }

    public void setDayGone(int dayGone) {
        this.dayGone = dayGone;
    }

    public Interest(int id, double counts, int dayGone) {
        this.id = id;
        this.counts = counts;
        this.dayGone = dayGone;
    }

    public Interest() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interest interest)) return false;
        return getId() == interest.getId() && Double.compare(getCounts(), interest.getCounts()) == 0 && getDayGone() == interest.getDayGone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCounts(), getDayGone());
    }


    @Override
    public String toString() {
        return "Interest{" +
                "id=" + id +
                ", counts=" + counts +
                ", dayGone=" + dayGone +
                '}';
    }
}
