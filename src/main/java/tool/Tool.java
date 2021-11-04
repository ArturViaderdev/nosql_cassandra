package tool;

import enums.Urgency;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import exceptions.InputException;
import java.util.Collections;
import java.util.List;
import model.Event;
import model.Incidence;
import model.RankingTO;

/**
 * Class to ask input to user.
 *
 * @author mfontana
 */
public class Tool {

    /**
     * Request String
     *
     * @param message
     * @return String
     */
    public static String askString(String message) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        do {
            try {
                System.out.println(message);
                answer = br.readLine().trim();
                if (answer.equals("")) {
                    throw new InputException(InputException.FILL_SCORE);

                }
            } catch (IOException ex) {
                System.out.println("Error input / output.");
            } catch (InputException ex) {
                System.out.println(ex.getMessage());
            }
        } while (answer.equals(""));
        return answer;
    }

    /**
     * Request String, it can only be optionA or optionB
     *
     * @param message String
     * @param optionA String
     * @param optionB String
     * @return String (optionA or optionB)
     */
    public static String askString(String message, String optionA, String optionB) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer = "";
        do {
            answer = askString(message);
            try {
                if (!answer.equalsIgnoreCase(optionA) && !answer.equalsIgnoreCase(optionB)) {
                    throw new InputException(InputException.INCORRECT_CHOICE);
                }
            } catch (InputException ex) {
                System.out.println(ex.getMessage());
            }
        } while (!answer.equalsIgnoreCase(optionA) && !answer.equalsIgnoreCase(optionB));
        return answer;
    }

    /**
     * Request String from a ArrayList of String accepteds
     *
     * @param message String
     * @param wordsAccepted ArrayList of String
     * @return String
     */
    public static String askString(String message, ArrayList<String> wordsAccepted) {
        String answer;
        boolean wordOk;
        do {
            for (String word : wordsAccepted) {
                System.out.println(word);
            }
            answer = Tool.askString(message);
            wordOk = wordIsOk(answer, wordsAccepted);
            if (!wordOk) {
                System.out.println("Wrong answer!");
            }
        } while (!wordOk);
        return answer;
    }

    /**
     * Returns true if the word exists in wordsAccepted.
     *
     * @param word
     * @param wordsAccepted
     * @return boolean
     */
    private static boolean wordIsOk(String word, ArrayList<String> wordsAccepted) {
        for (String w : wordsAccepted) {
            if (w.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Request int
     *
     * @param message
     * @return int
     */
    public static int askInt(String message) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int num = 0;
        boolean error;
        do {
            try {
                System.out.println(message);
                num = Integer.parseInt(br.readLine());
                error = false;
            } catch (IOException ex) {
                System.out.println("Error input / output.");
                error = true;
            } catch (NumberFormatException ex) {
                System.out.println("Please, write integer number.");
                error = true;
            }
        } while (error);
        return num;
    }

    /**
     * Request int in the interval between min and max (inclusives)
     *
     * @param message
     * @param min
     * @param max
     * @return int
     */
    public static int askInt(String message, int min, int max) {
        int num;
        do {
            num = askInt(message);
            try {
                if (num < min || num > max) {
                    throw new InputException((InputException.INCORRECT_CHOICE));
                }
            } catch (InputException ex) {
                System.out.println(ex.getMessage());
            }
        } while (num < min || num > max);
        return num;
    }

    /**
     * Request double
     *
     * @param message
     * @return double
     */
    public static double askDouble(String message) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        double num = 0;
        boolean error;
        do {
            try {
                System.out.println(message);
                num = Double.parseDouble(br.readLine());
                error = false;
            } catch (IOException ex) {
                System.out.println("Error input / output.");
                error = true;
            } catch (NumberFormatException ex) {
                System.out.println("Please, write double number.");
                error = true;
            }
        } while (error);
        return num;
    }

    /**
     * Request double in the interval between min and max (inclusives)
     *
     * @param message
     * @param min
     * @param max
     * @return double
     */
    public static double askDouble(String message, double min, double max) {
        double num;
        do {
            num = askDouble(message);
            if (num < min || num > max) {
                System.out.println("Error, the number must be between " + min + " and " + max);
            }
        } while (num < min || num > max);
        return num;
    }

    /**
     * Ask string and checks its length
     *
     * @param message
     * @param length
     * @return
     */
    public static String askStringExact(String message, int length) {
        int phone;
        do {
            phone = askInt(message);
            try {
                if (String.valueOf(phone).length() != length) {
                    throw new InputException((InputException.VALUE_LENGTH));
                }
            } catch (InputException ex) {
                System.out.println(ex.getMessage());
            }
        } while (String.valueOf(phone).length() != length);
        return String.valueOf(phone);
    }

    /**
     * Asks int and checks its length and min value
     *
     * @param length
     * @param min
     * @param message
     * @return
     */
    public static int askInt(int length, int min, String message) {
        int num;
        boolean exit = false;
        do {
            num = askInt(message);
            try {
                if (num < min) {
                    throw new InputException((InputException.MINIMUM_VALUE));
                }
                if (String.valueOf(num).length() > length) {
                    throw new InputException((InputException.EXCEED_CHARACTERS));
                }
                exit = true;
            } catch (InputException ex) {
                System.out.println(ex.getMessage());
                exit = false;
            }
        } while (!exit);
        return num;
    }

    /**
     * Ask a string and checks it length
     *
     * @param message
     * @param length
     * @return
     */
    public static String askString(String message, int length) {
        String word;
        do {
            word = askString(message);
            try {
                if (word.length() > length) {
                    throw new InputException((InputException.EXCEED_CHARACTERS));
                }
            } catch (InputException ex) {
                System.out.println(ex.getMessage());
            }
        } while (word.length() > length);
        return word;
    }

    /**
     * Ask a doubles and checks its length and min characters
     *
     * @param message
     * @param length
     * @param min
     * @return
     */
    public static double askDouble(String message, int length, int min) {
        double num;
        int data;
        boolean exit = false;
        do {
            num = askDouble(message);
            num = (double) Math.round(num * 100) / 100;
            data = (int) num;
            try {
                if (num < 0) {
                    throw new InputException((InputException.NEGATIVE_VALUE));
                }
                if (num < min) {
                    throw new InputException((InputException.MINIMUM_VALUE));
                }
                if (String.valueOf(data).length() > length) {
                    throw new InputException((InputException.EXCEED_CHARACTERS));
                }
                exit = true;
            } catch (InputException ex) {
                System.out.println(ex.getMessage());
                exit = false;
            }
        } while (!exit);
        return num;
    }

    /**
     * Shows all users type available
     */
    public static void showUrgencies() {
        int x = 1;
        for (Urgency urgency : Urgency.values()) {
            System.out.println(x + ". " + urgency.name());
            x++;
        }
    }

    public static List<Event> orderEvents(List<Event> events) {
        for (int i = 0; i < events.size() - 1; i++) {
            for (int j = 0; j < events.size() - 1; j++) {
                if (events.get(j).getDateCreated().compareTo(events.get(j + 1).getDateCreated()) < 0) {
                    Collections.swap(events, j + 1, j);
                }
            }
        }
        return events;
    }

    public static List<Incidence> orderIncidences(List<Incidence> incidences) {
        for (int i = 0; i < incidences.size() - 1; i++) {
            for (int j = 0; j < incidences.size() - 1; j++) {
                if (incidences.get(j).getDateCreated().compareTo(incidences.get(j + 1).getDateCreated()) < 0) {
                    Collections.swap(incidences, j + 1, j);
                }
            }
        }
        return incidences;
    }
}
