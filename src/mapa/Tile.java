package mapa;

// Será a classe pai -> servirá para carregar os principais blocos
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class Tile {
	public static BufferedImage chao = Game.sprite.getSprite(0, 0, 16, 16); // Bloco Terra
	public static BufferedImage grama = Game.sprite.getSprite(16, 0, 16, 16); // Bloco Terra e Grama
	public static BufferedImage empty = Game.sprite.getSprite(16, 32, 16, 16); // Bloco Vazio
	
	public int x;
	public int y;
	public BufferedImage sprite;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x, y, null);
	}
}
