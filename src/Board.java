import java.util.*;

public class Board {
    static int[] mesa;
    public boolean computer;
    private final int STARTING_AMOUNT = 4;
    private Jogador turno;
    
    public enum Jogador {
        Um ((mesa.length - 2) / 2, mesa.length - 1),
        Dois (mesa.length - 1, (mesa.length - 2) / 2);

        private int posicaoKalah;
        private int skipKalah;

        Jogador(int posicao, int skip) {
        	posicaoKalah = posicao;
        	skipKalah = skip;
        }
        int getKalah() {
        	return mesa[posicaoKalah];
        }
        int posicaoKalah() {
        	return posicaoKalah;
        }
        int getSkip() {
        	return skipKalah;
        }
    }
    
    public Board(int[] mesaArg){
        mesa = mesaArg;
    }
 
    public void printMesa() {
            if (!computer) {
                System.out.println("    (1) (2) (3) (4) (5) (6) ");
            }
            System.out.println("-------------------------------");
            System.out.print("|  ");
            for (int i = mesa.length - 2; i >= mesa.length / 2; i--) {
                System.out.print("| ");
                System.out.printf("%-2s",mesa[i]);
            }
            System.out.print("|  |\n|");
            System.out.printf("%-2d|-----------------------|%2d|\n", Jogador.Dois.getKalah(), Jogador.Um.getKalah());
            System.out.print("|  ");
            for (int i = 0; i < (mesa.length / 2) - 1; i++) {
                System.out.print("| ");
                System.out.printf("%-2s",mesa[i]);
            }
            System.out.println("|  |");
            System.out.println("-------------------------------");
            System.out.println("    (1) (2) (3) (4) (5) (6) ");
        }

    public boolean marcarMesa(int pos) {
		int handAmount = mesa[pos];
		mesa[pos] = 0;
		while (handAmount > 0) {
			pos = (pos + 1) % mesa.length;
			handAmount--;
			if (pos == turno.getSkip()) {
				pos = (pos + 1) % mesa.length;
			}
			mesa[pos]++;
		}
		boolean taken = false;
		if (pos != turno.posicaoKalah() && mesa[pos] == 1 && mesa[pegarOposto(pos)] != 0) {
				mesa[turno.posicaoKalah()] += mesa[pos] + mesa[pegarOposto(pos)];
				mesa[pos] = 0;
				mesa[pegarOposto(pos)] = 0;
				taken = true;
		}
		printMesa();
		if (taken) {
			System.out.println("Pieces taken!");
		} else if (!isOver() && pos == turno.posicaoKalah()) {
			System.out.println("Go again player "+turno+". You landed in the Kalah.");
			return true;
		}
		return false;
    }

    public Jogador getTurn() {
		return turno;
    }


    public int escolha() {
		Jogador computer = Jogador.Dois;
    	boolean valid = false;
		List<Integer> posicoesValidas = new ArrayList<Integer>();
		int start = (computer.getSkip() + 1) % mesa.length;
		for (int i = start; i < start + (mesa.length - 1) / 2; i++) {
			if (mesa[i] > 0) {
				posicoesValidas.add(i);
			}
		}

		// If any location lets us go again, return the first we find
		for (int location : posicoesValidas) {
			if ((location + mesa[location]) % mesa.length == computer.posicaoKalah()) {
				System.out.println("Spiffy! I get to go again!");
				return location;
			}
		}

		int maxPieces = 0;
		int pos = posicoesValidas.get(0);

		for (int location : posicoesValidas) {
			int landingPos = (location + mesa[location]) % mesa.length;
			int oppositePos = pegarOposto(landingPos);
			if (mesa[landingPos] == 0 && mesa[oppositePos] > maxPieces && oppositePos != location) {
				maxPieces = mesa[oppositePos];
				pos = location;
			}
		}

		if (maxPieces > 0) {
			System.out.println("All your pieces are belong to me!");
			return pos;
		}
		// No particular location looks advantageous. Just return a random location
		return posicoesValidas.get(new Random().nextInt(posicoesValidas.size()));
    }

	// Switches the turn from Player Um to Player Dois
    public void switchTurn() {
    	if (turno == Jogador.Um) {
    		turno = Jogador.Dois;
    	} else {
    		turno = Jogador.Um;
    	}
    }

	// Prints the winner of the game
    public void mostraVitoria(Jogador winner) {
    	if (winner == Jogador.Um) {
        	if (computer) {
        		System.out.println("Congratulations, you win!");
        	} else {
        		System.out.println("Player Um WINS!");
        	}
        } else if (winner == Jogador.Dois) {
        	if (computer) {
        		System.out.println("The computer beat you!");
				System.out.println("It says: too hard? :)");
        	} else {
	        	System.out.println("Player Dois WINS!");
        	}
        } else {
        	System.out.println("The game is a tie!");
        }
    }

	// Resets the game mesa
    public void reiniciar() {
    	for (int i = 0; i < mesa.length; i++) {
    		mesa[i] = STARTING_AMOUNT;
		}
    	for (Jogador p : Jogador.values()) {
    		mesa[p.posicaoKalah()] = 0;
    	}
    	turno = Jogador.Um;
    }

    // Returns true if the game is over (one side has no pieces)
    public boolean isOver() {
    	return sum(Jogador.Um) == 0 || sum(Jogador.Dois) == 0;
    }

    // Checks if a player has won
    // Returns null if the game isn't over or the game is a tie
    public Jogador getWinner() {
    	Jogador winner = null;
    	if (isOver()) {
    		for (Jogador p : Jogador.values()) {
        		mesa[p.posicaoKalah()] += sum(p);
        	}
			int totalOne = Jogador.Um.getKalah();
			int totalDois = Jogador.Dois.getKalah();
	    	if (totalOne > totalDois) {
	    		winner = Jogador.Um;
	    	} else if (totalOne < totalDois) {
	    		winner = Jogador.Dois;
	    	}
			for (int i = 0; i < mesa.length; i++) {
	    		if (i != Jogador.Um.posicaoKalah() && i != Jogador.Dois.posicaoKalah()) {
					mesa[i] = 0;
	    		}
			}
    	}
    	return winner;
    }

	// Returns an integer of the number of pieces on Player m's side of the mesa
    public int sum(Jogador m) {
    	int sum = 0;
    	int start = (m.getSkip() + 1) % mesa.length;
    	for (int i = start; i < start + (mesa.length - 1) / 2; i++) {
    		sum += mesa[i];
    	}
    	return sum;
    }

	// Accepts an integer position of the index of the mesa (0 based index)
    // Carries out a move for the player of the current turn
	// Returns true if the player gets to go again (landed in the kalah)


	// Returns index of the opposite position on the mesa
	private int pegarOposto(int pos) {
		return mesa.length - 2 - pos;
	}

    // Reads a value from a scanner in the console
    public int readValue() {
    	Scanner s = new Scanner(System.in);
    	int position = 2;
    	boolean valid = false;
        while (!valid) {
     		try {
         		position = s.nextInt();
         		if (position < 1 || position > 6) {
         			System.out.println("Invalid Position, input again:");
         		} else {
         			if (turno == Jogador.Um) {
         				position--;
         			} else {
         				position = mesa.length - 1 - position;
         			}
         			if (mesa[position] == 0) {
         				System.out.print("Spot is empty. Choose another spot:");
	         		} else {
	         			valid = true;
	         		}
         		}
     		}
     		catch (InputMismatchException e) {
				s.next(); // Throw away the offending input
     			System.out.println("Invalid Position, input again:");
     		}
         }
         return position;
    }
}
