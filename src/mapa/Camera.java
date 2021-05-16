package mapa;

public class Camera {

	public static int x;
	public static int y;
	
	public static int Clamp(int inicio, int minimo, int maximo) {
		if(inicio < minimo) {
			inicio = minimo;
		}
		
		if(inicio > maximo) {
			inicio = maximo;
		}
		
		return inicio;
	}
}
