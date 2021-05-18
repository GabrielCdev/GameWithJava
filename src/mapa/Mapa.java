package mapa;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entidades.Entity;
import entidades.Solido;
import main.Game;

public class Mapa {
	
	public static int WIDTH; // Para ter um acesso ao tamanho largura do vetor
	public static int HEIGHT; // Para ter um acesso ao tamanho altura do vetor
	public Tile[] tiles;
	
	public Mapa(String path) {
		try {
			BufferedImage level = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[level.getWidth() * level.getHeight()]; // Vetor de um tamanho => qtd de blocos do game
			tiles = new Tile[level.getWidth() * level.getHeight()];
			WIDTH = level.getWidth();
			HEIGHT = level.getHeight();
			level.getRGB(0, 0, level.getWidth(), level.getHeight(), pixels, 0, level.getWidth()); // Cores dentro do game
			
			// Definindo as cores para cada pixel da imagem (com ajuda do Aseprite para os hexadecimais)
			for(int x = 0; x < level.getWidth(); x++) { // Tudo que estiver na HORIZONTAL
				for(int y = 0; y < level.getHeight(); y++) { // Tudo que estiver na VERTICAL
					// Quando cruzarem o mesmo ponto (se coincidirem)...
					int pixelAtual = pixels[x + (y*level.getWidth())]; // Acessa o tamanho do vetor para verificar/adicionar/renderizar os elementos
					tiles[x + (y*WIDTH)] = new Empty(x*16, y*16, Entity.empty); // Background transparente
					
					// Verificações (com ajuda do Aseprite)
					if(pixelAtual == 0xFF7bff00) { // Verificação para o PLAYER
						Game.player.setX(x*16); // Pixel atual * 16 = posição correta do player
						Game.player.setY(y*16); // Pixel atual * 16 = posição correta do player
					}else if(pixelAtual == 0xFF8f563b) { // Verificação para o SOLO (terra)
						Solido solido = new Solido(x*16, y*16, 16, 16, Entity.chao);
						Game.entidades.add(solido);
					}else if(pixelAtual == 0xFF4b692f) { // Verificações para o SOLO (grama)
						Solido solido = new Solido(x*16, y*16, 16, 16, Entity.chaoGrama);
						Game.entidades.add(solido);
					}else if(pixelAtual == 0xFFffffff) { // Bloco vazio
						tiles[x + (y*WIDTH)] = new Empty(x*16, y*16, Entity.empty); // *16 -> A imagem tem 1 pixel só, mas o tile tem que caber em tudo (=16 pixels)
					}
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		int xi = Camera.x/16; // xi = X inicial - Verificar o pixel da imagem original sem o tile
		int yi = Camera.y/16; // yi = Y inicial - Verificar o pixel da imagem original sem o tile
		int xf = xi + (Game.WIDTH/16); // xf = X final - Verificar até onde dá pra renderizar
		int yf = yi + (Game.HEIGHT/16); // yf = Y final - Verificar até onde dá pra renderizar

		for(int x = xi; x <= xf; x++) {
			for(int y = yi; y <= yf; y++) {
				// Verificação para o jogo continuar (sem "colidir" com a parte preta)
				if(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
					continue;
				}
				
				// Acessar o tamanho do vetor e renderizar
				Tile tile = tiles[x + (y*WIDTH)];
				tile.render(g);
			}
		}
	}
}
