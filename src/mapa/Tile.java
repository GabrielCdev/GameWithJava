package mapa;

// Sera a classe pai -> servira para carregar os principais blocos
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {
	
	public int x;
	public int y;
	public BufferedImage sprite;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
