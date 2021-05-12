package entidades;

import java.awt.image.BufferedImage;

public class Player extends Entity {

	public boolean right, left, down, up; // Keyboard arrows
	public double speed = 1.5;	// Velocidade de movimentação
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

	// Tick SEMPRE ANTES do Render
	public void tick() {
		if(right)
			x += speed;
		
		if(left)
			x -= speed;
		
		if(down)
			y += speed;
		
		if(up)
			y -= speed;

		// Teste de movimentação
		// x += 2; // Dir
		// x -= 2; // Esq
		// y += 2; // Baixo
		// y -= 2; // Cima
	}
	
	public void render() {
		
	}
}
