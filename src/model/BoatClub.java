package model;

import java.util.ArrayList;

public class BoatClub
{
    //Regular expressions:
    private static final String PERSONAL_NR_PATTERN     = "[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])-?[0-9]{4}";
    private static final String NAME_PATTERN            = "^[a-zA-Z][a-zA-Z\\s]+$";
    private static final String DIGITS_ONLY_PATTERN     = "[0-9]+";

    //Members and boats:
    private ArrayList<Member>   members = new ArrayList<>();

    public BoatClub()
    {
        //Load from registry:
        members = Registry.getMembersAndBoats();
    }

    /**
     * Creates a member, if input data is valid.
     * For the name to be valid it needs to consist of only letters
     * and the personal number can be of type: yymmddxxxx or yymmdd-xxxx.
     *
     * @param name name
     * @param persNr personal number
     * @return member ID as an integer or -1 if data is invalid
     */
    public Member addMember(String name, String persNr)
    {

        //Validate input:
        if(!name.matches(NAME_PATTERN))
            return null;  //Invalid name.

        if(!persNr.matches(PERSONAL_NR_PATTERN))
            return null;  //Invalid personal number.

        //Format personal number:
        persNr = persNr.replace("-", "");

        //Create member:
        int id = members.size();
        Member member = new Member(id, name, persNr);

        //Add member to members:
        members.add(member);

        //Update registry
        saveRegistry();

        return member;
    }

    /**
     * Edits members name if input is valid.
     *
     * @param member member to edit
     * @param name name
     * @return boolean - true if name is valid
     */
    public boolean editMemberName(Member member, String name)
    {
        //Validate input:
        if (!name.matches(NAME_PATTERN))
            return false;

        //Change name:
        member.setName(name);

        //Update registry:
        saveRegistry();
        return true;
    }

    /**
     * Edits personal number if input is valid.
     *
     * @param member member to edit
     * @param persNr personal number
     * @return - boolean - true if personal number is valid
     */
    public boolean editMemberPersNr(Member member, String persNr)
    {
        //Validate input:
        if (!persNr.matches(PERSONAL_NR_PATTERN))
            return false;

        //Change personal number:
        member.setPersNr(persNr);

        //Update registry
        saveRegistry();
        return true;
    }

    public void removeMember(Member member)
    {
        members.remove(member);
        saveRegistry();
    }

    public void removeBoat(Member member, Boat boat)
    {
        member.removeBoat(boat);
        saveRegistry();
    }

    public void removeBoat(Member member, int boatId)
    {
        member.removeBoat(boatId);
        saveRegistry();
    }

    /**
     * Search member by id, name or personal number.
     *
     * @param searchPhrase search phrase as String
     * @return Member or Null if no member found.
     */
    public Member searchMember(String searchPhrase)
    {

        if(searchPhrase.matches(NAME_PATTERN))
        {
            // Search by name:
            for(Member member : members)
                if(member.getName().equalsIgnoreCase(searchPhrase))
                    return member;
        }
        else if(searchPhrase.matches(PERSONAL_NR_PATTERN))
        {
            // Search by personal number:
            String persNr = searchPhrase.replace("-","");   //Remove hyphen
            for(Member member : members)
                if(member.getPersNr().equals(persNr))
                    return member;

        }else if(searchPhrase.matches(DIGITS_ONLY_PATTERN))
        {
            try
            {
                // Search by member id:
                int searchId = Integer.parseInt(searchPhrase);
                for (Member member: members)
                    if (member.getId() == searchId)
                        return member;

            }catch(NumberFormatException e)
            {
                return null;
            }
        }
        return null;
    }

    public ArrayList<Member> getMembers()
    {
        return members;
    }

    public void saveRegistry()
    {
        Registry.updateMemberRegister(members);
    }

}
