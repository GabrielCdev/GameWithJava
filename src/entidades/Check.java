package entidades;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;

public class Check extends Entity {
	
	public int maskx = 0;
	public int masky = 0;
	public int maskw = 16;
	public int maskh = 16;

	public Check(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void tick() {
		// Colisao com o solo (ficar no solo)
		if(!colisao((int)x, (int)(y+1))) {
			y += 2;
		}
	}
	
	// Colisao do checkpoint com o solo (ficar no solo)
	public boolean colisao(int nextx, int nexty) { // nextx e nexty = pegar a posicao X e Y do personagem
		Rectangle player = new Rectangle(nextx + maskx, nexty + masky, maskw, maskh); // Criar um retangulo pro player
		
		for(int i = 0; i < Game.entidades.size(); i++) {
			Entity entidade = Game.entidades.get(i);
			
			if(entidade instanceof Solido) { // Verifica se eh um solido. Se for, cria um novo retangulo para ela.
				Rectangle solido = new Rectangle(entidade.getX() + maskx, entidade.getY() + masky, maskw, maskh);
				
				if(player.intersects(solido)) { // Verifica se o player esta encostando num solido
					return true;
				}
			}
		}
		return false;
	}
}
