package entidades;

import java.awt.image.BufferedImage;

public class Player extends Entity {

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	// Tick SEMPRE ANTES do Render
	public void tick() {

		// Teste de movimentação
		// x += 2; // Dir
		// x -= 2; // Esq
		// y += 2; // Baixo
		// y -= 2; // Cima
	}
	
	public void render() {
		
	}
}
