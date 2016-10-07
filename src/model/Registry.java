package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Registry
{
    private static final String PROP_MEMBER_NAME = ".name";
    private static final String PROP_MEMBER_PERS_NR = ".personal_nr";

    private static final String PROP_BOAT_TYPE = ".boat_type";
    private static final String PROP_BOAT_LENGTH = ".boat_length";


    // Directories and files:
    private static File directory = new File("register");
    private static File memberFile = new File(directory + "/members.txt");

    // File streams:
    private static FileInputStream input;
    private static FileOutputStream output;


    public static void updateMemberRegister(ArrayList<Member> members)
    {
        try
        {
            if(!directory.exists())
                directory.mkdirs();

            //Save members:
            Properties properties = new Properties();
            for(Member member : members)
            {
                int id = member.getId();
                properties.setProperty(id + PROP_MEMBER_NAME,       member.getName());
                properties.setProperty(id + PROP_MEMBER_PERS_NR,    member.getPersNr());

                //Save the members boats:
                for(Boat boat : member.getBoats())
                {
                    int boatId = boat.getId();
                    properties.setProperty(id+"."+boatId + PROP_BOAT_TYPE,      boat.getType().toString());
                    properties.setProperty(id+"."+boatId + PROP_BOAT_LENGTH,    Integer.toString(boat.getLength()));
                }
            }

            //Store properties to file:
            output = new FileOutputStream(memberFile, false);
            properties.store(output, "Members");

        }catch (IOException e)
        {
            System.err.println("Error accessing save files");
        }
    }
    public static ArrayList<Member> getMembersAndBoats()
    {
        ArrayList<Member> members = new ArrayList<>();
        Properties properties = new Properties();
        try
        {
            input = new FileInputStream(memberFile);
            properties.load(input);

            //Load members:
            int id=0;
            while(properties.containsKey(id + PROP_MEMBER_NAME))
            {
                String name     = properties.getProperty(id + PROP_MEMBER_NAME);
                String persNr   = properties.getProperty(id + PROP_MEMBER_PERS_NR);

                Member member = new Member(id, name, persNr);
                members.add(member);

                //Load boats:
                int boatId=0;
                while(properties.containsKey(id+"."+boatId + PROP_BOAT_TYPE))
                {
                    //Read boat properties:
                    Boat.BoatType boatType = Boat.BoatType.valueOf(properties.getProperty(id+"."+boatId + PROP_BOAT_TYPE));
                    int length = Integer.parseInt(properties.getProperty(id+"."+boatId + PROP_BOAT_LENGTH));

                    //Register boat:
                    member.registerBoat(boatType, length);
                    boatId++;
                }

                id++;
            }

            input.close();

        } catch (IOException e)
        {
            //TODO: Add observer later
            System.err.println("(No previous save files found)");
        } catch (NumberFormatException e)
        {
            System.err.println("Error loading save file");
        }

        return members;
    }
    /*
    public static void updateBoatRegister(ArrayList<Boat> boats)
    {
        try
        {
            if(!directory.exists())
                directory.mkdirs();

            Properties properties = new Properties();
            for(Boat boat : boats)
            {
                int id = boat.getId();
                properties.setProperty(id + PROP_MEMBER_NAME,      member.getName());
                properties.setProperty(id + PROP_MEMBER_PERS_NR,   member.getPersNr());
            }

            //Store properties to file:
            output = new FileOutputStream(memberFile, false);
            properties.store(output, "Members");

        }catch (IOException e)
        {
            System.err.println("Error accessing save files");
        }
    }*/
    public static Boat[] getBoats()
    {

        return null;
    }
}
