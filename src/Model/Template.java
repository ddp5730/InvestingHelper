package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

/**
 * The template account for the program.
 */
public class Template {

    private Map<String, TemplatePosition> positions;

    /**
     * Create a new template from file.
     * @param filename
     */
    Template(String filename) {

        positions = new HashMap<>();

        try {
            Scanner sc = new Scanner(new File(filename));
            while (sc.hasNext()) {
                String input = sc.nextLine();
                String[] tokens = input.split(" ");
                TemplatePosition pos = new TemplatePosition(tokens[0], Double.parseDouble(tokens[1])/100);
                positions.put(pos.getSymbol(), pos);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Return positions.
     * @return
     */
    public Map<String, TemplatePosition> getPositions() {
        return positions;
    }
}
