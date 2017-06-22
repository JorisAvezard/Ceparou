package com.example.groov.my_ceparou;

import java.util.List;

/**
 * Created by Joris on 08/06/2017.
 */

public class Place {

    private int id_place;
    private String name_place;
    private List<Coordinate> area;
    private List<Coordinate> walls;
    private int building_id;

    public Place() {

    }

    public Place(int id_place, String name_place, List<Coordinate> area, List<Coordinate> walls, int building_id) {
        super();
        this.id_place = id_place;
        this.name_place = name_place;
        this.area = area;
        this.walls = walls;
        this.building_id = building_id;
    }

    public int getId_place() {
        return id_place;
    }

    public void setId_place(int id_place) {
        this.id_place = id_place;
    }

    public String getName_place() {
        return name_place;
    }

    public void setName_place(String name_place) {
        this.name_place = name_place;
    }

    public List<Coordinate> getArea() {
        return area;
    }

    public void setArea(List<Coordinate> area) {
        this.area = area;
    }

    public List<Coordinate> getWalls() {
        return walls;
    }

    public void setWalls(List<Coordinate> walls) {
        this.walls = walls;
    }

    public int getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(int building_id) {
        this.building_id = building_id;
    }
}