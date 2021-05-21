package entidades;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import mapa.Camera;

public class Player extends Entity {
	
	// Keyboard arrows
	public boolean right;
	public boolean left;
	public boolean down;
	public boolean up;
	public double speed = 1.5;	// Velocidade de movimenta��o do Player
	public static double life = 100; // Vida do player
	public static double maxLife = 100; // Vida m�xima poss�vel

	public int direita = 1;
	public int esquerda = 0;
	public int direcaoAtual = direita; // O objeto sempre ser� inicializado para a direita
	
	public int movimentacao = 0; // 0 por conta do idle (parado)
	public int frames = 0; // Contador
	public int maxFrames = 5; // frames = maxFrames -> Index
	public int index = 0;
	public int maxIndex = 3; // 0 a 3 (ser�o usadas 4 anima��es)
	
	public int maskx = 0;
	public int masky = 0;
	public int maskw = 16;
	public int maskh = 16;
	
	public BufferedImage[] playerRight;
	public BufferedImage[] playerLeft;
	
	public boolean jump = false; // Pulo do player
	public boolean isJump = false; // Est� pulando? Colis�o...
	public int jumpHeight = 36; // Altura do pulo
	public int jumpFrames = 0;
	public Inimigo ini;
	public Cenoura vida;
	
	public int posX;
	public int posY;
	
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

	@SuppressWarnings("unused") // Eliminando Warning do FOR
	// Tick SEMPRE ANTES do Render
	public void tick() {
		// Adicionando anima��o para movimenta��es
		movimentacao = 0;
		
		
		
		// Colis�o
		// Se n�o estiver colidindo com o ch�o... (y+1 = 1 px acima no eixo Y para n�o ficar abaixo do solo).
		// Se n�o estiver pulando <- caindo
		if(!colisao((int)x, (int)(y+1)) && isJump == false) {
			y += 2;
			
			// Destruir os inimigos
			// Se estiver "caindo" em cima do inimigo... get(Y) - 8 => Para posicionar-se em cima do inimigo, por�m abaixo do limite do ret�ngulo (= 16) de colis�o (m�scara de colis�o)
			for(int i = 0; i < Game.inimigo.size(); i++) {
				Inimigo e = Game.inimigo.get(i); // Aloca o inimigo na vari�vel
				
				if(e instanceof Inimigo) { // Verifica se o inimigo � realmente um inimigo
					// getY()-8 � para o Player cair EM CIMA do inimigo, visto que o ret�ngulo (m�scara de colis�o) tem 16px
					if(damage(this.getX(), this.getY()-8)) { // Verifica se est� caindo por cima do inimigo
						isJump = true; // Quicar ap�s "matar" o inimigo
						((Inimigo) e).life--;
						
						if(((Inimigo) e).life == 0) {
							Game.inimigo.remove(e); // Remover o inimigo
							break;
						}
					}
				}
				break;
			}
		}
		
		// Adicionando anima��o para movimenta��es com colis�o
		if(right && !colisao((int)(x+speed), this.getY())) { // Se n�o tiver colidindo para a direita, continue andando
			x += speed;
			movimentacao = 1;
			direcaoAtual = direita;
		}
			
		if(left && !colisao((int)(x-speed), this.getY())) { // Se n�o tiver colidindo para a esquerda, continue andando
			x -= speed;
			movimentacao = 1;
			direcaoAtual = esquerda;
		}

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
		
		if(jump) {
			if(colisao(this.getX(), this.getY() + 1)) {
				isJump = true;
			}
		}
		
		if(isJump) {
			if(!colisao(this.getX(), this.getY() - 2)) {
				y -= 2;
				jumpFrames += 2;
				
				if(jumpFrames == jumpHeight) {
					isJump = false;
					jump = false;
					jumpFrames = 0;
				}
			}else {
				isJump = false;
				jump = false;
				jumpFrames = 0;
			}
		}
		
		// Teste de movimenta��o
		/*
		if(down) {
			y += speed;
			movimentacao = 1;
		}
		
		if(up) {
			y -= speed;
			movimentacao = 1;
		} 
		*/
		
		// x += 2; // Dir
		// x -= 2; // Esq
		// y += 2; // Baixo
		// y -= 2; // Cima
		
		if(damage((int)(x+speed), this.getY())) {
			life -= 0.45;
		}
		
		// Recuperar vida
		if(vida(this.getX(), this.getY()) && life < 100) { // Pega as coordenadas do player
			life += 10;
			
			// N�o permitir que a vida m�xima no game seja maior que 100
			if(life > 100) {
				life = 100;
			}
			
			Game.cenoura.remove(vida);
		}
		
		// Recuperar vida
		if(checkpoint(this.getX(), this.getY())) { // Verifica quando o player encosta no Checkpoint
			posX = this.getX(); // Atribui a posi��o X do player
			posY = this.getY(); // Atribui a posi��o Y do player
		}
		
		if(life <= 0) { // Se a vida for menor ou igual a 0
			// Seta a posi��o do personagem na posi��o do checkpoint
			setX(posX);
			setY(posY);
			life = 100; // Volta com a vida 100%
		}
		
		// Limitar a tela de jogo baseado no "mapa"
		Camera.x = Camera.Clamp(this.getX() - (Game.WIDTH/2), 0, mapa.Mapa.WIDTH*16 - Game.WIDTH); // 1� item = Posi��o atual do player - largura do jogo/2 -> C�mera do jogo fica centralizada no player, 2� min = 0 (s� renderizar valores >= 0)
		Camera.y = Camera.Clamp(this.getY() - (Game.HEIGHT/2), 0, mapa.Mapa.HEIGHT*16 - Game.HEIGHT); // 1� item = Posi��o atual do player - largura do jogo/2 -> C�mera do jogo fica centralizada no player, 2� min = 0 (s� renderizar valores >= 0)
	}
	
	// M�todo de Colis�o
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
	
	// Colis�o com o checkpoint
	public boolean checkpoint(int nextx, int nexty) { // nextx e nexty = pegar a posi��o X e Y do personagem
		Rectangle player = new Rectangle(nextx + maskx, nexty + masky, maskw, maskh); // Criar um ret�ngulo pro player
		
		for(int i = 0; i < Game.entidades.size(); i++) {
			Entity entidade = Game.entidades.get(i);
			
			if(entidade instanceof Check) { // Verifica se � o Checkpoint e cria uma �rea retangular nele
				Rectangle solido = new Rectangle(entidade.getX() + maskx, entidade.getY() + masky, maskw, maskh);
				
				if(player.intersects(solido)) { // Verifica se o player est� encostando no Checkpoint
					return true;
				}
			}
		}
		return false;
	}
	
	// Colis�o com o inimigo
	public boolean damage(int nextx, int nexty) { // nextx e nexty = pegar a posi��o X e Y do personagem
		Rectangle player = new Rectangle(nextx + maskx, nexty + masky, maskw, maskh); // Criar um ret�ngulo pro player
		
		for(int i = 0; i < Game.inimigo.size(); i++) {
			Inimigo entidade = Game.inimigo.get(i);
			
			if(entidade instanceof Inimigo) { // Verifica se � um inimigo e cria uma �rea retangular nele
				Rectangle inimigo = new Rectangle(entidade.getX() + maskx, entidade.getY() + masky, maskw, maskh);
				
				if(player.intersects(inimigo)) { // Verifica se o player est� encostando num inimigo
					ini = entidade; // Pegar a entidade atual e alocar no ini
					return true;
				}
			}
		}
		return false;
	}
	
	// Colis�o com a cenoura para regenerar vida
	public boolean vida(int nextx, int nexty) { // nextx e nexty = pegar a posi��o X e Y da cenoura
		Rectangle player = new Rectangle(nextx + maskx, nexty + masky, maskw, maskh); // Criar um ret�ngulo pro player
		
		for(int i = 0; i < Game.cenoura.size(); i++) {
			Cenoura cenoura = Game.cenoura.get(i);
			
			if(cenoura instanceof Cenoura) { // Verifica se � uma cenoura e cria uma �rea retangular nela
				Rectangle solido = new Rectangle(cenoura.getX() + maskx, cenoura.getY() + masky, maskw, maskh);
				
				if(player.intersects(solido)) { // Verifica se o player est� encostando na cenoura
					vida = cenoura;
					return true;
				}
			}
		}
		return false;
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
