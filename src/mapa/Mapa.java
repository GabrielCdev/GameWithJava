package mapa;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Mapa {
	public Mapa(String path) {
		try {
			BufferedImage level = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[level.getWidth() * level.getHeight()]; // Vetor de um tamanho => qtd de blocos do game
			level.getRGB(0, 0, level.getWidth(), level.getHeight(), pixels, 0, level.getWidth()); // Cores dentro do game
			
			// Definindo as cores (com ajuda do Aseprite para os hexadecimais)
			for(int i = 0; i < pixels.length; i++) {
				if(pixels[i] == 0xFFff5e00) { // Verificar se a cor passada é a correta
					System.out.println("Cor laranja");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
