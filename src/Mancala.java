import java.util.*;

import Board.Jogador;

// Defines a Mancala kalah
public class Mancala {

    public static void main(String[] args) throws InterruptedException {
    	Board kalah = new Board(new int[14]);
    	System.out.println();
    	boolean playAgain = true;
    	while (playAgain) {
    	   	Scanner s = new Scanner(System.in);
        	System.out.print("Enter 1 to play against the computer, 2 to play against a human (1 or 2):");
        	if (s.nextLine().contains("1")) {
        		kalah.computer = true;
        	}
    		kalah.reiniciar();
        	kalah.printMesa();
			// kalah loop
	        while (!kalah.isOver()) {
	        	boolean again = true;
		        while (again) {
			        int position = 0;
		        	if (kalah.computer && kalah.getTurn() == Jogador.Dois) {
			        	System.out.println("The computer is thinking...");
				        Thread.sleep(1800);
				        position = kalah.escolha();
			        } else {
			        	System.out.print("Player " + kalah.getTurn() + ", choose which pile to take (1-6):");
			        	position = kalah.readValue();
			        }
		        	again = kalah.marcarMesa(position);
		        }
			    kalah.switchTurn();
	        }
	        System.out.println("The kalah is over! Press enter to count the pieces.");
	        s.nextLine();
	        Jogador winner = kalah.getWinner();
	        kalah.printMesa();
	        kalah.mostraVitoria(winner);
	        System.out.print("\nPlay Again? (Y/N):");
	        if (s.nextLine().equalsIgnoreCase("n")) {
	        	playAgain = false;
	        }
    	}
    }
}