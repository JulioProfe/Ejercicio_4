import java.io.Serializable;

public class Seria implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String contenido;
	
	public Seria(String contenido){
		this.contenido = contenido;
	}

	public String getContent() {
		return contenido;
	}

	public void setContent(String contenido) {
		this.contenido = contenido;
	}
}
