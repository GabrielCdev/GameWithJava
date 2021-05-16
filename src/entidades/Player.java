package entidades;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import mapa.Camera;

public class Player extends Entity {
	
	// Keyboard arrows
	public boolean right;
	public boolean left;
	public boolean down;
	public boolean up;
	public double speed = 1.5;	// Velocidade de movimenta��o

	public int direita = 1;
	public int esquerda = 0;
	public int direcaoAtual = direita; // O objeto sempre ser� inicializado para a direita
	
	public int movimentacao = 0; // 0 por conta do idle (parado)
	public int frames = 0; // Contador
	public int maxFrames = 5; // frames = maxFrames -> Index
	public int index = 0;
	public int maxIndex = 3; // 0 a 3 (ser�o usadas 4 anima��es)
	
	public BufferedImage[] playerRight;
	public BufferedImage[] playerLeft;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		playerRight = new BufferedImage[4]; // 4 = 4 Sprites
		playerLeft = new BufferedImage[4];
		
		// Esses valores s�o pegos no Aseprite no 1� pixel do quadrante das respectivas anima��es
		for(int i = 0; i < 4; i++) { // Ir� percorrer as anima��es e alocar dentro do vetor
			playerRight[i] = Game.sprite.getSprite(32 + (i*16), 0, 16, 16); // Os valores correspondem a 1� anima��o (para a direita). +*16 para passar as anima��es
		}
		
		for(int i = 0; i < 4; i++) { // Ir� percorrer as anima��es e alocar dentro do vetor
			playerLeft[i] = Game.sprite.getSprite(80 - (i*16), 16, 16, 16); // Os valores correspondem a �ltima anima��o (para a esquerda). -*16 para passar as anima��es
		}
	}

	// Tick SEMPRE ANTES do Render
	public void tick() {
		// Adicionando anima��o para movimenta��es
		movimentacao = 0;
		
		if(right) {
			x += speed;
			movimentacao = 1;
			direcaoAtual = direita;
		}
			
		if(left) {
			x -= speed;
			movimentacao = 1;
			direcaoAtual = esquerda;
		}
		
		if(down) {
			y += speed;
			movimentacao = 1;
		}
		
		if(up) {
			y -= speed;
			movimentacao = 1;
		}
		
		// Teste de movimenta��o
		// x += 2; // Dir
		// x -= 2; // Esq
		// y += 2; // Baixo
		// y -= 2; // Cima

		// Toda vez que movimentar o frame � incrementado
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
		
		Camera.x = this.getX() - (Game.WIDTH/2); // Posi��o atual do player - largura do jogo/2 -> C�mera do jogo fica centralizada no player
		Camera.y = this.getY() - (Game.HEIGHT/2); // Posi��o atual do player - altura do jogo/2 -> C�mera do jogo fica centralizada no player
	}
	
	public void render(Graphics g) {
		// Movimenta��o para a direita
		if(direcaoAtual == direita && movimentacao == 1) {
			g.drawImage(playerRight[index], this.getX() - Camera.x, this.getY() - Camera.y, null);			
		}
		
		// Idle
		if(direcaoAtual == direita && movimentacao == 0) {
			g.drawImage(playerRight[0], this.getX() - Camera.x, this.getY() - Camera.y, null);			
		}
		
		// Movimenta��o para esquerda
		if(direcaoAtual == esquerda && movimentacao == 1) {
			g.drawImage(playerLeft[index], this.getX() - Camera.x, this.getY() - Camera.y, null);			
		}
		
		// Idle
		if(direcaoAtual == esquerda && movimentacao == 0) {
			g.drawImage(playerLeft[0], this.getX() - Camera.x, this.getY() - Camera.y, null);			
		}
	}
}
