import java.util.Scanner;

/**
 * Records, prints, and updates the teeth of one family.
 * Also calculates the family's root canal indices.
 *
 * @author Dionysios Maroudas
 */
public class DentalRecord { // Start of the DentalRecord class

    // Minimum permitted family size.
    private static final int MIN_PEOPLE = 1;

    // Maximum permitted family size.
    private static final int MAX_PEOPLE = 6;

    // Number of tooth layers per person.
    private static final int NUMBER_OF_LAYERS = 2;

    // Array index of the upper layer.
    private static final int UPPER = 0;

    // Array index of the lower layer.
    private static final int LOWER = 1;

    // Maximum positions in each layer.
    private static final int MAX_TEETH = 8;

    // First tooth number shown to the user.
    private static final int FIRST_TOOTH = 1;

    // Result used when a name is absent.
    private static final int NOT_FOUND = -1;

    // Letter representing an incisor.
    private static final char INCISOR = 'I';

    // Letter representing a bicuspid.
    private static final char BICUSPID = 'B';

    // Letter representing a missing tooth.
    private static final char MISSING = 'M';

    // Letters accepted by the main menu.
    private static final String MENU_CHOICES = "PERX";

    // Letters accepted for layer selection.
    private static final String LAYER_CHOICES = "UL";

    // Factor of two in the quadratic formula denominator.
    private static final double QUADRATIC_TWO = 2.0;

    // Factor of four in the quadratic formula discriminant.
    private static final double DISCRIMINANT_FOUR = 4.0;

    /**
     * Starts the program, collects the family's records, and repeats
     * the menu until the user chooses to exit.
     *
     * @param args command-line arguments; not used by this program
     */
    public static void main(String[] args) { // Start of main

        Scanner keyboard = new Scanner(System.in);

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        int numberOfPeople = readFamilySize(keyboard);

        String[] names = new String[numberOfPeople];

        // Each person has two layers.
        // Each layer gets its actual number of tooth positions.
        char[][][] teeth = new char[numberOfPeople][NUMBER_OF_LAYERS][];

        readFamily(keyboard, names, teeth);

        String choice;

        do { // Start of the menu loop

            System.out.println();

            choice = readChoice(
                    keyboard,
                    "(P)rint, (E)xtract, (R)oot, e(X)it : ",
                    MENU_CHOICES,
                    "Invalid menu option, try again : "
            );

            switch (choice) { // Start of the menu switch

                case "P":
                    printRecords(names, teeth);
                    break;

                case "E":
                    extractTooth(keyboard, names, teeth);
                    break;

                case "R":
                    reportRoots(teeth);
                    break;

                case "X":
                    System.out.println(
                            "Exiting the Floridian Tooth Records :-)"
                    );
                    break;

                default:
                    break;

            } // End of the menu switch

        } while (!choice.equals("X")); // End of the menu loop

        keyboard.close();

    } // End of main

    /**
     * Reads the number of family members and repeats the prompt
     * until the number is within the permitted range.
     *
     * @param keyboard the Scanner used to read the user's input
     * @return a family size between MIN_PEOPLE and MAX_PEOPLE
     */
    public static int readFamilySize(Scanner keyboard) {

        System.out.print(
                "Please enter number of people in the family : "
        );

        int numberOfPeople = Integer.parseInt(
                keyboard.nextLine().trim()
        );

        while (numberOfPeople < MIN_PEOPLE
                || numberOfPeople > MAX_PEOPLE) {

            System.out.print(
                    "Invalid number of people, try again : "
            );

            numberOfPeople = Integer.parseInt(
                    keyboard.nextLine().trim()
            );

        } // End of the family-size validation loop

        return numberOfPeople;

    } // End of readFamilySize

    /**
     * Reads and stores each family member's name, upper teeth,
     * and lower teeth.
     *
     * @param keyboard the Scanner used to read the user's input
     * @param names the String array in which to store family members' names
     * @param teeth the three-dimensional char array in which to store
     *              teeth by person, layer, and tooth position
     */
    public static void readFamily(
            Scanner keyboard,
            String[] names,
            char[][][] teeth) {

        for (int person = 0; person < names.length; person++) {

            System.out.print(
                    "Please enter the name for family member "
                            + (person + 1) + " : "
            );

            names[person] = keyboard.nextLine().trim();

            while (names[person].isEmpty()) {

                System.out.print(
                        "Name cannot be empty, try again : "
                );

                names[person] = keyboard.nextLine().trim();

            } // End of the name validation loop

            teeth[person][UPPER] = readTeeth(
                    keyboard, names[person], "uppers"
            );

            teeth[person][LOWER] = readTeeth(
                    keyboard, names[person], "lowers"
            );

        } // End of the family-member input loop

    } // End of readFamily

    /**
     * Reads and validates one tooth layer, accepting uppercase
     * or lowercase I, B, and M letters.
     *
     * @param keyboard the Scanner used to read the user's input
     * @param name the family member's name to display in the prompt
     * @param layerName the layer label, either "uppers" or "lowers"
     * @return a char array containing one to MAX_TEETH valid uppercase
     *         tooth letters
     */
    public static char[] readTeeth(
            Scanner keyboard,
            String name,
            String layerName) {

        System.out.print(
                "Please enter the " + layerName + " for " + name + " : "
        );

        while (true) { // Start of the tooth-input validation loop

            String input = keyboard.nextLine().trim().toUpperCase();

            if (input.isEmpty()) {

                System.out.print(
                        "Enter at least one tooth (use M for missing) : "
                );

            } else if (input.length() > MAX_TEETH) {

                System.out.print("Too many teeth, try again : ");

            } else { // Start of checking the tooth letters

                boolean valid = true;

                for (int tooth = 0; tooth < input.length(); tooth++) {

                    char type = input.charAt(tooth);

                    if (type != INCISOR
                            && type != BICUSPID
                            && type != MISSING) {

                        valid = false;

                    } // End of the invalid-letter check

                } // End of the tooth-letter checking loop

                if (valid) {

                    return input.toCharArray();

                } // End of the valid-input check

                System.out.print(
                        "Invalid teeth types, try again : "
                );

            } // End of checking the tooth letters and input length

        } // End of the tooth-input validation loop

    } // End of readTeeth

    /**
     * Reads a single allowed letter and repeats the prompt
     * until the user enters a valid choice.
     *
     * @param keyboard the Scanner used to read the user's input
     * @param prompt the message displayed before the first input
     * @param allowed a String containing the permitted uppercase letters
     * @param errorPrompt the message displayed after an invalid choice
     * @return the valid choice as a one-letter uppercase String
     */
    public static String readChoice(
            Scanner keyboard,
            String prompt,
            String allowed,
            String errorPrompt) {

        System.out.print(prompt);

        String input = keyboard.nextLine().trim().toUpperCase();

        while (input.length() != 1 || !allowed.contains(input)) {

            System.out.print(errorPrompt);

            input = keyboard.nextLine().trim().toUpperCase();

        } // End of the choice validation loop

        return input;

    } // End of readChoice

    /**
     * Prints each family member's name and both tooth layers,
     * numbering tooth positions starting at one.
     *
     * @param names the String array containing the family members' names
     * @param teeth the three-dimensional char array containing teeth
     *              by person, layer, and tooth position
     */
    public static void printRecords(
            String[] names,
            char[][][] teeth) {

        for (int person = 0; person < names.length; person++) {

            System.out.println(names[person]);

            for (int layer = 0; layer < NUMBER_OF_LAYERS; layer++) {

                if (layer == UPPER) {

                    System.out.print("  Uppers: ");

                } else {

                    System.out.print("  Lowers: ");

                } // End of the layer-label selection

                for (int tooth = 0;
                     tooth < teeth[person][layer].length;
                     tooth++) {

                    System.out.print(
                            (tooth + 1) + ":"
                                    + teeth[person][layer][tooth] + "  "
                    );

                } // End of the tooth-printing loop

                System.out.println();

            } // End of the layer-printing loop

        } // End of the family-member printing loop

    } // End of printRecords

    /**
     * Searches for the first family member whose name matches
     * the supplied name, ignoring uppercase and lowercase differences.
     *
     * @param names the String array of family members' names to search
     * @param name the name to find in the array
     * @return the zero-based index of the first matching person,
     *         or NOT_FOUND if no name matches
     */
    public static int findPerson(String[] names, String name) {

        for (int person = 0; person < names.length; person++) {

            if (names[person].equalsIgnoreCase(name)) {

                return person;

            } // End of the matching-name check

        } // End of the name-search loop

        return NOT_FOUND;

    } // End of findPerson

    /**
     * Asks the user to select a person, layer, and existing tooth.
     * Marks the selected tooth as missing without shifting positions.
     * Returns to the menu if the selected layer has no teeth to extract.
     *
     * @param keyboard the Scanner used to read the user's selections
     * @param names the String array used to identify the family member
     * @param teeth the three-dimensional char array to update,
     *              indexed by person, layer, and tooth position
     */
    public static void extractTooth(
            Scanner keyboard,
            String[] names,
            char[][][] teeth) {

        System.out.print("Which family member : ");

        int person = findPerson(
                names, keyboard.nextLine().trim()
        );

        while (person == NOT_FOUND) {

            System.out.print(
                    "Invalid family member, try again : "
            );

            person = findPerson(
                    names, keyboard.nextLine().trim()
            );

        } // End of the family-member validation loop

        String layerChoice = readChoice(
                keyboard,
                "Which tooth layer (U)pper or (L)ower : ",
                LAYER_CHOICES,
                "Invalid layer, try again : "
        );

        int layer = UPPER;

        if (layerChoice.equals("L")) {

            layer = LOWER;

        } // End of the lower-layer selection

        // Check that this layer has at least one tooth to extract.
        boolean hasTooth = false;

        for (int tooth = 0;
             tooth < teeth[person][layer].length;
             tooth++) {

            if (teeth[person][layer][tooth] != MISSING) {

                hasTooth = true;

            } // End of the existing-tooth check

        } // End of the layer availability loop

        if (!hasTooth) {

            System.out.println(
                    "All teeth in this layer are missing."
            );

            return;

        } // End of the all-missing check

        System.out.print("Which tooth number : ");

        while (true) { // Start of the extraction validation loop

            int toothNumber = Integer.parseInt(
                    keyboard.nextLine().trim()
            );

            if (toothNumber < FIRST_TOOTH
                    || toothNumber > teeth[person][layer].length) {

                System.out.print(
                        "Invalid tooth number, try again : "
                );

            } else if (teeth[person][layer][toothNumber - 1] == MISSING) {

                System.out.print(
                        "Missing tooth, try again : "
                );

            } else {

                // Convert the user's tooth number to a zero-based index.
                teeth[person][layer][toothNumber - 1] = MISSING;

                return;

            } // End of the tooth-number and missing-tooth checks

        } // End of the extraction validation loop

    } // End of extractTooth

    /**
     * Counts the family's incisors, bicuspids, and missing teeth.
     * Uses these counts to solve I*x*x + B*x - M = 0 and prints
     * the results, including cases where the equation is not quadratic.
     *
     * @param teeth the three-dimensional char array containing the
     *              family's current teeth by person, layer, and position
     */
    public static void reportRoots(char[][][] teeth) {

        int incisors = 0;
        int bicuspids = 0;
        int missing = 0;

        for (int person = 0; person < teeth.length; person++) {

            for (int layer = 0; layer < NUMBER_OF_LAYERS; layer++) {

                for (int tooth = 0;
                     tooth < teeth[person][layer].length;
                     tooth++) {

                    switch (teeth[person][layer][tooth]) {

                        case INCISOR:
                            incisors++;
                            break;

                        case BICUSPID:
                            bicuspids++;
                            break;

                        case MISSING:
                            missing++;
                            break;

                        default:
                            break;

                    } // End of the tooth-type counting switch

                } // End of the tooth-counting loop

            } // End of the layer-counting loop

        } // End of the family-member counting loop

        if (incisors > 0) {

            // a = incisors, b = bicuspids, and c = -missing.
            double discriminant = (double) bicuspids * bicuspids
                    + DISCRIMINANT_FOUR * incisors * missing;

            double rootOne = (-bicuspids + Math.sqrt(discriminant))
                    / (QUADRATIC_TWO * incisors);

            double rootTwo = (-bicuspids - Math.sqrt(discriminant))
                    / (QUADRATIC_TWO * incisors);

            System.out.printf(
                    "One root canal at %.2f%n", rootOne
            );

            System.out.printf(
                    "Another root canal at %.2f%n", rootTwo
            );

        } else if (bicuspids > 0) {

            // Without incisors, solve B*x - M = 0.
            double root = (double) missing / bicuspids;

            System.out.printf(
                    "One root canal at %.2f (linear equation)%n", root
            );

        } else if (missing > 0) {

            System.out.println(
                    "No root canals: the equation has no solution."
            );

        } else {

            System.out.println(
                    "Every value solves the equation 0 = 0."
            );

        } // End of the equation-type selection

    } // End of reportRoots

} // End of the DentalRecord class