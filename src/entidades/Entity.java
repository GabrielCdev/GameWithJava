package entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Entity {
// Classe pai
// Para organizar/alocar os itens/objetos do game (Ex.: objetos dentro do mapa etc)
	
	protected double x, y; // Double para diminuir a velocidade do movimento do personagem
	protected int width, height;
	protected BufferedImage sprite;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	// Setters
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
		g.drawImage(sprite, this.getX(), this.getY(), null);
	}
}
