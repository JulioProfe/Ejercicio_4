import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class Logica implements Observer {
	private Comunicacion com;
	private PApplet app;
	private boolean jugar;
	private int turno, oponente, turnoChi;
	private int matriz[][] = new int[3][3];
	private ArrayList<Objeto> ob = new ArrayList<>();

	public Logica(PApplet app) {
		this.app = app;
		com = new Comunicacion();
		Thread hilo = new Thread(com);
		hilo.start();
		com.addObserver(this);
		jugar = false;
		turno = com.getIdentifier();

		if (turno == 0) {
			jugar = true;
			oponente = 1;
		} else if (turno == 1) {
			oponente = 0;
		}
	}

	public void pintar() {
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				app.rect((i * 50) + 180, (j * 50) + 160, 50, 50);
			}
		}
		for (int i = 0; i < ob.size(); i++) {
			if (jugar == true) {
				ob.get(i).pintarTurno(app);
			}
		}
	}

	public void release() {
		jugar = false;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		System.out.println("RECIBIDITO UN OBJETITO");
		if (arg instanceof Objeto) {
			ob.add((Objeto) arg);
			System.out.println("Es un objeto");
			jugar = true;
		}
	}
	
	public void onClick() {
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				if (app.dist((i * 50) + 180 + 25, (j * 50) + 160 + 25, app.mouseX, app.mouseY) < 25) {
					try {
						Objeto o;
						o = new Objeto(turnoChi, com.getIdentifier(), i, j);
						ByteArrayOutputStream bs = new ByteArrayOutputStream();
						ObjectOutputStream os = new ObjectOutputStream(bs);
						os.writeObject(o);
						byte[] datos = bs.toByteArray();
						com.sendMessage(datos);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		ganar();
	}

	public void ganar() {
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				if ((matriz[0][0] == turno && matriz[0][1] == turno && matriz[0][2] == turno)
						|| (matriz[1][0] == turno && matriz[1][1] == turno && matriz[1][2] == turno)
						|| (matriz[2][0] == turno && matriz[2][1] == turno && matriz[2][2] == turno)
						|| (matriz[0][0] == turno && matriz[1][0] == turno && matriz[2][0] == turno)
						|| (matriz[0][1] == turno && matriz[1][1] == turno && matriz[2][1] == turno)
						|| (matriz[0][2] == turno && matriz[1][2] == turno && matriz[2][2] == turno)
						|| (matriz[0][0] == turno && matriz[1][1] == turno && matriz[2][2] == turno)
						|| (matriz[0][2] == turno && matriz[1][1] == turno && matriz[2][0] == turno)) {
					app.textAlign(app.CENTER);
					app.text("Ganador " + turno, app.width / 2, 400);
				} else if ((matriz[0][0] == oponente && matriz[0][1] == oponente && matriz[0][2] == oponente)
						|| (matriz[1][0] == oponente && matriz[1][1] == oponente && matriz[1][2] == oponente)
						|| (matriz[2][0] == oponente && matriz[2][1] == oponente && matriz[2][2] == oponente)
						|| (matriz[0][0] == oponente && matriz[1][0] == oponente && matriz[2][0] == oponente)
						|| (matriz[0][1] == oponente && matriz[1][1] == oponente && matriz[2][1] == oponente)
						|| (matriz[0][2] == oponente && matriz[1][2] == oponente && matriz[2][2] == oponente)
						|| (matriz[0][0] == oponente && matriz[1][1] == oponente && matriz[2][2] == oponente)
						|| (matriz[0][2] == oponente && matriz[1][1] == oponente && matriz[2][0] == oponente)) {
					app.textAlign(app.CENTER);
					app.text("Ganador " + oponente, app.width / 2, 400);
				}
			}
		}
	}
}
