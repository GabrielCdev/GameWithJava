package mapa;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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
					tiles[x + (y*WIDTH)] = new Empty(x*16, y*16, Tile.empty); // Background transparente
					
					// Verifica��es (com ajuda do Aseprite)
					if(pixelAtual == 0xFF7bff00) { // Verifica��o para o PLAYER
						tiles[x + (y*WIDTH)] = new Empty(x*16, y*16, Tile.empty); // No player n�o vai ter nada de background
					}else if(pixelAtual == 0xFF8f563b) { // Verifica��o para o SOLO (terra)
						tiles[x + (y*WIDTH)] = new Solo(x*16, y*16, Tile.chao);
					}else if(pixelAtual == 0xFF4b692f) { // Verifica��es para o SOLO (grama)
						tiles[x + (y*WIDTH)] = new Solo(x*16, y*16, Tile.grama);
					}else if(pixelAtual == 0xFFffffff) { // Bloco vazio
						tiles[x + (y*WIDTH)] = new Empty(x*16, y*16, Tile.empty); // *16 -> A imagem tem 1 pixel s�, mas o tile tem que caber em tudo (=16 pixels)
					}
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				// Acessar o tamanho do vetor e renderizar
				Tile tile = tiles[x + (y*WIDTH)];
				tile.render(g);
			}
		}
	}
}
