package entidades;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;

public class Cenoura extends Entity {
	
	public int maskx = 0;
	public int masky = 0;
	public int maskw = 16;
	public int maskh = 16;

	public Cenoura(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void tick() {
		if(!colisao((int)x, (int)(y+1))) {
			y += 2;
		}
	}
	
	public boolean colisao(int nextx, int nexty) { // nextx e nexty = pegar a posi��o X e Y da cenoura
		Rectangle cenoura = new Rectangle(nextx + maskx, nexty + masky, maskw, maskh); // Criar um ret�ngulo pra cenoura
		
		for(int i = 0; i < Game.entidades.size(); i++) {
			Entity entidade = Game.entidades.get(i);
			
			if(entidade instanceof Solido) { // Verifica se � um s�lido. Se for, cria um novo ret�ngulo para ela.
				Rectangle solido = new Rectangle(entidade.getX() + maskx, entidade.getY() + masky, maskw, maskh);
				
				if(cenoura.intersects(solido)) { // Verifica se a cenoura est� encostando num s�lido
					return true;
				}
			}
		}
		return false;
	}
}
