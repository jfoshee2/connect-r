import java.util.Scanner;

/**
 * Plays game of Connect R
 */
public class Main {
    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);

        System.out.println("What kind of game is this? (Enter corresponding number)");
        System.out.println("1. Human VS Human");
        System.out.println("2. Human VS AI");
        System.out.println("3. AI VS AI");

        int choice = input.nextInt();

        System.out.println("Number of columns: ");
        int n = input.nextInt();

        System.out.println("Number of rows: ");
        int m = input.nextInt();

        System.out.println("Win requirement: ");
        int r = input.nextInt();

        ConnectRGame game;

        if (choice != 1) {
            System.out.println("Depth: ");
            int depth = input.nextInt();
            game = new ConnectRGame(n, m, r, depth);
        } else {
            game = new ConnectRGame(n, m, r, 0); // Does not matter what is passed as depth if it's humans
        }

        switch (choice) {
            case 1: game.playHumanVsHuman(); break;
            case 2: game.playHumanVsAI(); break;
            case 3: game.playAIVsAI(); break;
        }

    }
}
