import java.io.Serializable;

import processing.core.PApplet;

public class Objeto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int posX, posY, id;
	
	public Objeto(int turno, int id, int x, int y) {
		this.id = id;
		this.posX = x;
		this.posY = y;
	}
	
	public void pintarTurno(PApplet app){
		if (id == 0) {
			app.fill(0, 255, 0);
		} else if (id == 1) {
			app.fill(255, 0, 0);
		}
		app.ellipse((posX*50) + 180+25, (posY*50) + 160+25, 40, 40);
	}
	
}
