package view;

import java.util.Scanner;

public class MenuBuilder
{
    public static final int DEFAULT_LINE_LENGTH = 20;
    public static final int DEFAULT_MENU_SPACE = 5;

    /**
     * Creates a String menu from parameters.
     *
     * @param title String menu title
     * @param description String description text
     * @param options String varargs with options
     * @return String menu
     */
    public static String createMenu(String title, String description, String... options)
    {
        StringBuilder menu = new StringBuilder();

        menu.append(createHeader(title));

        // Description text:
        if(description!=null)
            menu.append(description).append("\n")
                    .append(getLine(DEFAULT_LINE_LENGTH)).append("\n");

        // Options:
        for(int i=0; i<options.length; i++)
            menu.append(options[i]).append("\n");

        // Text before input:
        menu.append(getLine(DEFAULT_LINE_LENGTH)).append("\n")
                .append("Choose action: ");

        return menu.toString();
    }

    /**
     * Create header
     *
     * @param title String header title
     * @return String header
     */
    public static String createHeader(String title)
    {
        StringBuilder header = new StringBuilder();

        //Header space:
        for(int i=0; i<DEFAULT_MENU_SPACE; i++)
            header.append("\n");

        //Header:
        header.append(getLine(DEFAULT_LINE_LENGTH)).append("\n")
                .append("\t").append(title).append("\n")
                .append(getLine(DEFAULT_LINE_LENGTH)).append("\n");

        return header.toString();
    }

    /**
     * Creates a line of dashes, where length is a multiple
     * of three dashes.
     *
     * @param length int length
     * @return String line
     */
    public static String getLine(int length)
    {
        //Build line:
        StringBuilder lineBuilder = new StringBuilder();
        for(int i = 0; i<length; i++)
            lineBuilder.append("---");

        return lineBuilder.toString();
    }

    public static void pauseLine()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Press any key to continue");
        scan.nextLine();
    }
}
