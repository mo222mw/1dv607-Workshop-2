package model;

import java.util.ArrayList;

public class Member
{
    private int id;
    private String name;
    private String persNr;

    private ArrayList<Boat> boats = new ArrayList<>();

    public Member(int id, String name, String personNr)
    {
        this.id = id;
        this.name = name;
        this.persNr = personNr;
    }

    public Boat registerBoat(Boat.BoatType type, int length)
    {
        Boat boat = new Boat(boats.size(), id, type, length);
        boats.add(boat);

        return boat;
    }

    //Get methods:
    public int getId() {return id;}
    public String getName()
    {
        return name;
    }
    public String getPersNr()
    {
        return persNr;
    }
    public Boat getBoat(int boatId) {return boats.get(boatId);}
    public ArrayList<Boat> getBoats() {return boats;}
}
