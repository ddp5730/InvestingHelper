package Invest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The template account for the program.
 */
public enum Template {
    TEMPLATE("Template.txt");

    public List<PositionTemplate> positions;

    /**
     * Create a new template from file.
     * @param filename
     */
    Template(String filename) {

        positions = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File(filename));
            while (sc.hasNext()) {
                String input = sc.nextLine();
                String[] tokens = input.split(" ");
                PositionTemplate pos = new PositionTemplate(tokens[0], Double.parseDouble(tokens[1])/100);
                positions.add(pos);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns the position index of the Invest.Position with the specified symbol
     * @param symbol symbol to look for
     * @return the index of that Invest.Position, -1 if not found (crashes program)
     */
    public int indexOf(String symbol) {
        for (int i = 0; i < positions.size(); i++ ) {
            if (positions.get(i).getSymbol().equals(symbol)) {
                return i;
            }
        }
        return -1;
    }
}
