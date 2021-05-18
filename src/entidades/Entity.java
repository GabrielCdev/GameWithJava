package entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import mapa.Camera;

// Classe pai - para organizar/alocar os itens/objetos do game (Ex.: objetos dentro do mapa etc)
public class Entity {
	
	public static BufferedImage chao = Game.sprite.getSprite(0, 0, 16, 16); // Bloco Terra
	public static BufferedImage chaoGrama = Game.sprite.getSprite(16, 0, 16, 16); // Bloco Terra e Grama
	public static BufferedImage empty = Game.sprite.getSprite(16, 32, 16, 16); // Bloco Vazio
	public static BufferedImage ceu = Game.ceu.getSprite(0, 0, 1471, 400);
	
	// Double para diminuir a velocidade do movimento do personagem (ponto flutuante)
	protected double x; 
	protected double y;
	protected int width;
	protected int height;
	protected BufferedImage sprite;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	// Setters <- Serão usados para setar a posição do player
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	// Getters
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	// Tick SEMPRE ANTES do Render
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}
