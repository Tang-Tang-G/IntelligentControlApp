package com.example.intelligentcontrolapp.db;

import java.util.ArrayList;
import java.util.List;

public class House {

    private String name;
    private List<Area> areas;

    public House(String name) {
        this.name = name;
        this.areas = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void addArea(Area area) {
        areas.add(area);
    }
    public void removeArea(Area area) {
        areas.remove(area);
    }
}
