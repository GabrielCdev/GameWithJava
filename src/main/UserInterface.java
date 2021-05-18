package main;

import java.awt.Color;
import java.awt.Graphics;

import entidades.Player;

public class UserInterface {
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(19, 19, 52, 7); // Moldura preta
		
		g.setColor(Color.red);
		g.fillRect(20, 20, 50, 5); // Cor vermelha de vida perdida
		
		g.setColor(Color.green);
		g.fillRect(20, 20, (int)((Player.life/Player.maxLife)*50), 5); // Cor verde de vida (calcula e exibe onde a vida está na barrinha)
	}
}
