package view;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.Boat;
import model.BoatClub;
import model.Member;

import static view.MenuBuilder.*;

public class Console
{
    private static final String QUIT_LETTER = "Q";

    private static Scanner scan = new Scanner(System.in);
    private static String input = "";

    private static BoatClub club;

    /*
     * Create a BoatClub, this were members and boats will be
     * stored and handled during runtime.
     */
    public static void main(String[] args)
    {
        club = new BoatClub(); mainMenu();
    }

    /*
     * Main menu
     * --- --- --- --- ---
     * Presents a bunch of options for the user to choose from.
     * Choosing is done by entering corresponding number.
     * To saveAndQuit the user enters the letter Q
     */
    private static void mainMenu()
    {
        String input = "";

        while(!input.equalsIgnoreCase(QUIT_LETTER))
        {

            //Create menu:
            String memberMenu = createMenu("Member menu", null,
                    "1. Add member",
                    "2. Search member",
                    "3. List members \t\t\t\t(Compact List)",
                    "4. List members and boats \t\t(Verbose List)");

            //Print menu:
            System.out.print(memberMenu);

            //Options to choose from:
            input = scan.nextLine();
            switch(input)
            {
                case "1":
                    addMember();
                    break;
                case "2":
                    searchMember();
                    break;
                case "3":
                    listMembers(true);
                    break;
                case "4":
                    listMembers(false);
                    break;
            }
        }
        saveAndQuit();
    }

    /*
     * Search member:
     * --- --- --- --- ---
     * Allows user to search for a member by:
     * Id, name or personal number.
     */
    private static void searchMember()
    {
        //Print header with information:
        System.out.print(createHeader("Search member"));
        System.out.println("Enter name, id or personal number");

        //Print search input form:
        System.out.print("Search: ");
        Member member = club.searchMember(scan.nextLine());
        System.out.println(getLine(DEFAULT_LINE_LENGTH));

        if (member != null)
        {
            accessMember(member, true);
        }else
        {
            //Search failed, present options:
            System.out.print("No member found, search again? (Y/N): ");
            input = scan.nextLine();
            if (input.equalsIgnoreCase("y"))
                searchMember();
            else if(input.equalsIgnoreCase(QUIT_LETTER))
                saveAndQuit();
            else
                mainMenu();
        }
    }

    private static void listMembers(Boolean compact)
    {
        ArrayList<Member> members = club.getMembers();

        StringBuilder description = new StringBuilder();

        if(compact)
        {
            //Compact search list:
            description.append("Id\tBoats\tName");
            for (Member member : members)
            {
                //Member information:
                description.append("\n").append(member.getId()).
                            append("\t").append(member.getBoats().size()).
                            append("\t\t").append(member.getName());

                //Line separation:
                description.append("\n").append(getLine(DEFAULT_LINE_LENGTH)).append("\n");
            }

        }else {

            //Verbose search list:
            for (Member member : members)
            {
                //Member information:
                description.append("*\tMember Id: ").append(member.getId()).
                            append("\n\tName: ").append(member.getName()).
                            append("\n\tPersonal number: ").append(member.getPersNr());

                //List boats if any:
                if (member.getBoats().size() > 0)
                {
                    //Boat information:
                    description.append("\n\t\tBoats:");
                    for (Boat boat : member.getBoats())
                        description.append("\n\t\tLength: ").append(boat.getLength()).
                                    append("\t\tType: ").append(boat.getType().toString());
                }

                //Line separation:
                description.append("\n").append(getLine(DEFAULT_LINE_LENGTH)).append("\n");
            }
        }

        description.append("Members: ").append(members.size());

        //Print menu:
        System.out.print(
                createMenu("List of members (" + (compact ? "Compact)" : "Verbose)"), description.toString(),
                "1. Access member",
                "2. Go back"));

        pauseLine();
    }

    private static String listBoats()
    {
        return null;
    }

    /*
     * Add members:
     * --- --- --- --- ---
     * Let's user register one or multiple members.
     */
    private static void addMember()
    {
        String name;
        String persNr;

        //Print header:
        System.out.print(createHeader("Register member"));

        //New member information, input form:
        System.out.print("Name (Letters only): "); 	name = scan.nextLine();
        System.out.println(getLine(DEFAULT_LINE_LENGTH));
        System.out.println("Personal number can be of type yymmddxxxx or yymmdd-xxxx");
        System.out.print("Personal number: "); 	    persNr = scan.nextLine();

        //Create member:
        Member member = club.addMember(name, persNr);
        if(member == null)
        {
            //TODO: Detect if member is already registered.
            System.out.println("Bad input, try again? (Y/N)");
            if (scan.nextLine().equalsIgnoreCase("y"))
                addMember();
        }else
        {
            System.out.print(createMenu("Register member", member.getName() + " was successfully registered.",
                    "1. Create additional member",
                    "2. Access member (" + member.getName() + ")",
                    "3. Go back"));
            while(!input.equalsIgnoreCase(QUIT_LETTER))
            {
                input = scan.nextLine();
                switch (input)
                {
                    case "1":
                        addMember();
                        break;
                    case "2":
                        accessMember(member, false);
                        break;
                    case "3":
                        club.saveRegistry();
                        mainMenu();
                        return;

                    default:
                }
            }
            saveAndQuit();
        }

    }

    private static void accessMember(Member member, boolean cameFromSearch)
    {
        String description = "Full name: " + member.getName()
                + "\nPersonal number: " + member.getPersNr()
                + "\nMember ID: " + member.getId();

        //Print menu:
        System.out.print(createMenu(member.getName(), description,
                "1. Boats..",
                "2. Edit information",
                "3. Go back"));

        while(!input.equalsIgnoreCase(QUIT_LETTER))
        {
            input = scan.nextLine();
            switch (input)
            {
                case "1":
                    boatMenu(member);
                    break;

                case "2":

                    break;

                case "3":
                    if(cameFromSearch)
                        searchMember();
                    else
                        mainMenu();

                default:
            }
        }
        saveAndQuit();
    }

    private static void addBoat(Member member)
    {
        Boat newBoat;

        //Header and information:
        System.out.print(createHeader("Register boat"));
        System.out.println("Register boat for member: " + member.getName());

        //Get boat information:
        try {
            int length;
            Boat.BoatType type;

            //Boat type:
            System.out.println("1. SailBoat, 2. Motorsailer, 3. Kayak/Canoe, 4. Other");
            System.out.print("Boat type: (Number) "); input = scan.nextLine();
            type = Boat.BoatType.values() [Integer.parseInt(input)-1];

            //Boat length:
            System.out.print("Length of boat in meters: ");
            length = scan.nextInt();

            //Attempt to create boat:
            newBoat = member.registerBoat(type, length);

        }catch (InputMismatchException|ArrayIndexOutOfBoundsException e)
        {
            newBoat = null;
        }

        if(newBoat==null)
        {
            System.out.println("Bad input, try again? (Y/N)");
            if (scan.nextLine().equalsIgnoreCase("y"))
                addBoat(member);
        }else
        {
            System.out.print(createMenu("Register boat", newBoat.getType().toString() + " was successfully registered.",
                    "1. Register additional boat/s",
                    "2. Edit boat (" + newBoat.getType().toString() + ")",
                    "3. Go back"));
            while(!input.equalsIgnoreCase(QUIT_LETTER))
            {
                input = scan.nextLine();
                switch (input)
                {
                    case "1":
                        addBoat(member);
                        break;
                    case "2":
                        editBoat(member, newBoat.getId());
                        break;
                    case "3":
                        accessMember(member, false);

                    default:
                }
            }
            saveAndQuit();
        }
    }

    private static void boatMenu(Member member)
    {
        ArrayList<Boat> memberBoats = member.getBoats();

        //Menu description: (List of boats)
        StringBuilder description = new StringBuilder();
        description.append("#. Type\t\t\t\tLength");

        for(int i=0; i<memberBoats.size(); i++)
        {
            description.append("\n").append(i + 1).append(". ");
            String boatType = memberBoats.get(i).getType().toString();

            //Tab space:
            description.append(boatType).append("\t\t");
            if(boatType.length()<9) description.append("\t");

            description.append(memberBoats.get(i).getLength());
        }

        //Print menu:
        System.out.print(createMenu(member.getName() + "'s boats", description.toString(),
                "1. Add boat",
                "2. Edit boat",
                "3. Go back"));

        input = scan.nextLine();
        while(!input.equalsIgnoreCase(QUIT_LETTER))
        {
            switch (input)
            {
                case "1":
                    addBoat(member);
                    break;
                case "2":
                    try
                    {
                        //Get boat to edit by id:
                        System.out.print("Boat nr: ");
                        int inputNumber = scan.nextInt();

                        if((inputNumber > 0) && (inputNumber < memberBoats.size()))
                            editBoat(member, inputNumber-1);
                        else
                            throw new InputMismatchException();

                    } catch (InputMismatchException e)
                    {
                        scan.nextLine(); //Ignore previous input.

                        //Try again?
                        System.out.print("Bad input, try again? (Y/N): ");
                        if (scan.nextLine().equalsIgnoreCase("y"))
                        {input="2"; continue;}
                        else
                            boatMenu(member);
                    }
                    break;

                case "3":
                    accessMember(member, false);
                    break;

                default:
                    System.err.println("Invalid input");
                    System.out.print("Choose action: ");
                    input = scan.nextLine();
            }
        }

    }

    private static void editBoat(Member member, int boatId)
    {
        Boat boat = member.getBoat(boatId);
        if(boat != null)
        {
            String description = "Boat type: " + boat.getType().toString()
                    + "\nLength: " + boat.getLength();
            System.out.print(createMenu("Edit Boat", description,
                    "1. Change type",
                    "2. Change length",
                    "3. Go back"));

            while(!input.equalsIgnoreCase(QUIT_LETTER))
            {
                try {
                    if (input.equals("1"))   //Change type
                    {
                        //Get input:
                        System.out.println(getLine(DEFAULT_LINE_LENGTH));
                        System.out.println("1. SailBoat, 2. Motorsailer, 3. Kayak/Canoe, 4. Other");
                        System.out.print("Boat type: (Number) "); input = scan.nextLine();

                        //Change type: (convert int to enum boatType)
                        boat.setType(Boat.BoatType.values()[Integer.parseInt(input) - 1]);
                    }else if(input.equals("2")) //Change length
                    {
                        //Change boat length:
                        System.out.print("Length of boat in meters: "); int length = scan.nextInt();
                        boat.setLength(length);

                        //Consume line:
                        scan.nextLine();

                    }else if(input.equals("3")) //Go back to boat menu
                        boatMenu(member);

                    //When done editing:
                    editBoat(member, boatId);
                    return;

                }catch(InputMismatchException|ArrayIndexOutOfBoundsException e)
                {
                    //Present options:
                    System.out.println("Bad input, try again? (Y/N)");
                    if (!scan.nextLine().equalsIgnoreCase("y"))
                        editBoat(member, boatId);
                    else if(input.equalsIgnoreCase(QUIT_LETTER))
                        saveAndQuit();
                }
            }
        }
    }

    private static void saveAndQuit()
    {
        club.saveRegistry();
        System.exit(0);
    }
}
