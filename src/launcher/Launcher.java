package launcher;

import io.FileManager;
import view.DecklistWindow;

public class Launcher {

	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DecklistWindow dlw = new DecklistWindow();
                dlw.setCardList(FileManager.getCardsData());
            }
        });

	}

}
