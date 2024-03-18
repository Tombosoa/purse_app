package com.example.PurseApp.Entity;

import java.util.Objects;

public class Interest {
    private int id;
    private double count;
    private int dayGone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public int getDayGone() {
        return dayGone;
    }

    public void setDayGone(int dayGone) {
        this.dayGone = dayGone;
    }

    public Interest(int id, double count, int dayGone) {
        this.id = id;
        this.count = count;
        this.dayGone = dayGone;
    }

    public Interest() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interest interest)) return false;
        return getId() == interest.getId() && Double.compare(getCount(), interest.getCount()) == 0 && getDayGone() == interest.getDayGone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCount(), getDayGone());
    }

    @Override
    public String toString() {
        return "Interest{" +
                "id=" + id +
                ", count=" + count +
                ", dayGone=" + dayGone +
                '}';
    }
}
