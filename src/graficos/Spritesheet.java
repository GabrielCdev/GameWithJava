package graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import mapa.Camera;

public class Spritesheet {

	public BufferedImage spritesheet;
	
	public Spritesheet(String path) { // String - por conta do arquivo (Spritesheet)
		try {  // Inicializando...
			spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height) {
		return spritesheet.getSubimage(x - Camera.x, y - Camera.y, width, height); // Retorna a imagem buscada em Spritesheet
	}
}
