package entidades;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import mapa.Camera;

public class Inimigo extends Entity {
	
	public double speed = 0.5; // Velocidade de movimenta��o do Inimigo
	
	public int movimentacao = 1; // 0 por conta do (parado)
	public int frames = 0; // Contador
	public int maxFrames = 7; // frames = maxFrames -> Index
	public int index = 0;
	public int maxIndex = 1; // 0 a 1 (ser�o usadas 2 anima��es)
	
	public int maskx = 0;
	public int masky = 0;
	public int maskw = 16;
	public int maskh = 16;
	
	public BufferedImage[] inimigo;

	public Inimigo(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		inimigo = new BufferedImage[2];
		
		for(int i = 0; i < 2; i++) { // Ir� percorrer os frames de anima��o e alocar dentro do vetor
			inimigo[i] = Game.sprite.getSprite(112 + (i*16), 0, 16, 16); // Os valores correspondem a 1� anima��o (para a direita). +*16 para passar as anima��es
		}
	}
	
	public void tick() {
		// Verifica se ele n�o vai colidir quando "cair" no ch�o
		if(!colisao((int)x, (int)(y+1))) { // Enquanto n�o estiver colidindo... (y+1 = 1 px acima no eixo Y para n�o ficar abaixo do solo).
			y += 2;
		}
		
		// Movimenta��o do inimigo
		if(Game.player.getX() < this.getX() && !colisao((int)(x-speed), this.getY())) {
			x -= speed; // Para o inimigo ir para a esquerda
		}
		
		if(Game.player.getX() > this.getX() && !colisao((int)(x+speed), this.getY())) {
			x += speed; // Para o inimigo ir para a direita
		}
		
		// Anima��o b�sica do inimigo
		if(movimentacao == 1) {
			frames++;
			
			if(frames == maxFrames) { // Quando for igual ao valor m�ximo, o index ser� incrementado
				index++; // Rodar� +1 index da anima��o... 0 -> 1 -> 2 -> 3
				frames = 0; // Come�ar tudo de novo para chegar a um outro index
				
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(inimigo[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
	// M�todo de colis�o do inimigo
	public boolean colisao(int nextx, int nexty) { // nextx e nexty = pegar a posi��o X e Y do personagem
		Rectangle player = new Rectangle(nextx + maskx, nexty + masky, maskw, maskh); // Criar um ret�ngulo pro player
		
		for(int i = 0; i < Game.entidades.size(); i++) {
			Entity entidade = Game.entidades.get(i);
			
			if(entidade instanceof Solido) { // Verifica se � um s�lido. Se for, cria um novo ret�ngulo para ela.
				Rectangle solido = new Rectangle(entidade.getX() + maskx, entidade.getY() + masky, maskw, maskh);
				
				if(player.intersects(solido)) { // Verifica se o player est� encostando num s�lido
					return true;
				}
			}
		}
		return false;
	}		
}
