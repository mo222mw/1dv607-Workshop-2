package model;

public class Boat
{
    public enum BoatType {SAILBOAT, MOTORSAILER, KYAK_CANOE, OTHER};

    int id;
    int ownerId;
    BoatType type;
    int length;

    public Boat(int id, int ownerId, BoatType type, int length)
    {
        this.id = id;
        this.ownerId = ownerId;
        this.type = type;
        this.length = length;
    }

    public int getId()
    {
        return id;
    }

    public void setType(BoatType type)
    {
        this.type = type;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public BoatType getType()
    {
        return type;
    }
    public int getLength()
    {
        return length;
    }


}
