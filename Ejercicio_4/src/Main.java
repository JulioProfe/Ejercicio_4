import processing.core.PApplet;

public class Main extends PApplet{
	private Logica log;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("Main");
	}
	
	@Override
	public void settings() {
		size(500, 500);
	}
	
	@Override
	public void setup() {
		log = new Logica(this);
		background(255);
	}
	
	@Override
	public void draw() {
		log.pintar();
	}
	
	@Override
	public void mouseClicked() {
		log.onClick();
	}
}
