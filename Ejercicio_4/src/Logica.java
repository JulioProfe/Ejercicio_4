import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class Logica implements Observer{
	private Comunicacion com;
	private PApplet app;
	private boolean proceso;
	private int turno;
	private int matriz[][] = new int [3][3];
	private ArrayList<Objeto> ob = new ArrayList<>();
	
	public Logica(PApplet app) {
		this.app = app;
		com = new Comunicacion();
		Thread hilo = new Thread(com);
		hilo.start();
		com.addObserver(this);
	}
	
	public void pintar(){
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				app.rect((i*50) + 180, (j*50) + 160, 50, 50);
			}
		}
		for (int i = 0; i < ob.size(); i++) {
			ob.get(i).pintarTurno(app);
		}
		
	}
	
	public void onClick(){
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				if(app.dist((i*50) + 180 + 25, (j*50) + 160 + 25, app.mouseX, app.mouseY)<25){		
					try {
						Objeto o;
						o= new Objeto(turno,com.getIdentifier(),i,j);
						ByteArrayOutputStream bs= new ByteArrayOutputStream();
						ObjectOutputStream os= new ObjectOutputStream(bs);
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
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		System.out.println("RECIBIDITO UN OBJETITO");
		if (arg instanceof Objeto) {
		ob.add((Objeto) arg);
		System.out.println("Es un objeto");
		}
	}
	

}
