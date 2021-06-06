package entidades;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import mapa.Camera;

public class Inimigo extends Entity {
	
	public double speed = 0.5; // Velocidade de movimentacao do Inimigo
	
	public int movimentacao = 1; // 0 por conta do (parado)
	public int frames = 0; // Contador
	public int maxFrames = 7; // frames = maxFrames -> Index
	public int index = 0;
	public int maxIndex = 1; // 0 a 1 (serao usadas 2 animacoes)
	
	// Valores referentes a SpriteSheet
	public int maskx = 0;
	public int masky = 0;
	public int maskw = 16;
	public int maskh = 16;
	public int life = 3; // Vida do inimigo
	
	public BufferedImage[] inimigo;

	public Inimigo(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		inimigo = new BufferedImage[2]; // 2 Sprites
		
		for(int i = 0; i < 2; i++) { // Ira percorrer os frames de animacao e alocar dentro do vetor
			inimigo[i] = Game.sprite.getSprite(112 + (i*16), 0, 16, 16); // Os valores correspondem a 1a animacao (para a direita). +*16 para passar as animacoes
		}
	}
	
	public void tick() {
		// Verifica se ele nao vai colidir quando "cair" no chao
		if(!colisao((int)x, (int)(y+1))) { // Enquanto nao estiver colidindo... (y+1 = 1 px acima no eixo Y para nao ficar abaixo do solo).
			y += 2;
		}
		
		// Movimentacao do inimigo
		if(Game.player.getX() < this.getX() && !colisao((int)(x-speed), this.getY())) {
			x -= speed; // Para o inimigo ir para a esquerda
		}
		
		if(Game.player.getX() > this.getX() && !colisao((int)(x+speed), this.getY())) {
			x += speed; // Para o inimigo ir para a direita
		}
		
		// Animacao basica do inimigo
		if(movimentacao == 1) {
			frames++;
			
			if(frames == maxFrames) { // Quando for igual ao valor maximo, o index sera incrementado
				index++; // Rodara +1 index da animacao... 0 -> 1 -> 2 -> 3
				frames = 0; // Comecar tudo de novo para chegar a um outro index
				
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(inimigo[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
	// Metodo de colisao do inimigo
	public boolean colisao(int nextx, int nexty) { // nextx e nexty = pegar a posicao X e Y do inimigo
		Rectangle inimigo = new Rectangle(nextx + maskx, nexty + masky, maskw, maskh); // Criar um retangulo pro inimigo
		
		for(int i = 0; i < Game.entidades.size(); i++) {
			Entity entidade = Game.entidades.get(i);
			
			if(entidade instanceof Solido) { // Verifica se ha um solido. Se for, cria um novo retangulo para ela.
				Rectangle solido = new Rectangle(entidade.getX() + maskx, entidade.getY() + masky, maskw, maskh);
				
				if(inimigo.intersects(solido)) { // Verifica se o inimigo esta encostando num solido
					return true;
				}
			}
		}
		return false;
	}
}
