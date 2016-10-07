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

        System.out.println("Name: " + name);
        //Validate input:
        if(!name.matches(NAME_PATTERN))
            return null;  //Invalid name.

        System.out.println("valid name");

        if(!persNr.matches(PERSONAL_NR_PATTERN))
            return null;  //Invalid personal number.

        System.out.println("valid pers nr");


        //Format personal number:
        persNr = persNr.replace("-", "");

        //Create member:
        int id = members.size();
        Member member = new Member(id, name, persNr);

        //Add member to members:
        members.add(member);

        return member;
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
