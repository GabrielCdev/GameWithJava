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
	public double speed = 1.5;	// Velocidade de movimentação do Player
	public static double life = 100; // Vida do player
	public static double maxLife = 100; // Vida máxima possível

	public int direita = 1;
	public int esquerda = 0;
	public int direcaoAtual = direita; // O objeto sempre será inicializado para a direita
	
	public int movimentacao = 0; // 0 por conta do idle (parado)
	public int frames = 0; // Contador
	public int maxFrames = 5; // frames = maxFrames -> Index
	public int index = 0;
	public int maxIndex = 3; // 0 a 3 (serão usadas 4 animações)
	
	public int maskx = 0;
	public int masky = 0;
	public int maskw = 16;
	public int maskh = 16;
	
	public BufferedImage[] playerRight;
	public BufferedImage[] playerLeft;
	
	public boolean jump = false; // Pulo do player
	public boolean isJump = false; // Está pulando? Colisão...
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
		
		// Esses valores são pegos no Aseprite no 1º pixel do quadrante das respectivas animações
		for(int i = 0; i < 4; i++) { // Irá percorrer as animações e alocar dentro do vetor
			playerRight[i] = Game.sprite.getSprite(32 + (i*16), 0, 16, 16); // Os valores correspondem a 1ª animação (para a direita). +*16 para passar as animações
		}
		
		for(int i = 0; i < 4; i++) { // Irá percorrer as animações e alocar dentro do vetor
			playerLeft[i] = Game.sprite.getSprite(80 - (i*16), 16, 16, 16); // Os valores correspondem a última animação (para a esquerda). -*16 para passar as animações
		}
	}

	@SuppressWarnings("unused") // Eliminando Warning do FOR
	// Tick SEMPRE ANTES do Render
	public void tick() {
		// Adicionando animação para movimentações
		movimentacao = 0;
		
		
		
		// Colisão
		// Se não estiver colidindo com o chão... (y+1 = 1 px acima no eixo Y para não ficar abaixo do solo).
		// Se não estiver pulando <- caindo
		if(!colisao((int)x, (int)(y+1)) && isJump == false) {
			y += 2;
			
			// Destruir os inimigos
			// Se estiver "caindo" em cima do inimigo... get(Y) - 8 => Para posicionar-se em cima do inimigo, porém abaixo do limite do retângulo (= 16) de colisão (máscara de colisão)
			for(int i = 0; i < Game.inimigo.size(); i++) {
				Inimigo e = Game.inimigo.get(i); // Aloca o inimigo na variável
				
				if(e instanceof Inimigo) { // Verifica se o inimigo é realmente um inimigo
					// getY()-8 é para o Player cair EM CIMA do inimigo, visto que o retângulo (máscara de colisão) tem 16px
					if(damage(this.getX(), this.getY()-8)) { // Verifica se está caindo por cima do inimigo
						isJump = true; // Quicar após "matar" o inimigo
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
		
		// Adicionando animação para movimentações com colisão
		if(right && !colisao((int)(x+speed), this.getY())) { // Se não tiver colidindo para a direita, continue andando
			x += speed;
			movimentacao = 1;
			direcaoAtual = direita;
		}
			
		if(left && !colisao((int)(x-speed), this.getY())) { // Se não tiver colidindo para a esquerda, continue andando
			x -= speed;
			movimentacao = 1;
			direcaoAtual = esquerda;
		}

		// Toda vez que movimentar o frame é incrementado
		if(movimentacao == 1) {
			frames++;
			
			if(frames == maxFrames) { // Quando for igual ao valor máximo, o index será incrementado
				index++; // Rodará +1 index da animação... 0 -> 1 -> 2 -> 3
				frames = 0; // Começar tudo de novo para chegar a um outro index
				
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
		
		// Teste de movimentação
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
			
			// Não permitir que a vida máxima no game seja maior que 100
			if(life > 100) {
				life = 100;
			}
			
			Game.cenoura.remove(vida);
		}
		
		// Recuperar vida
		if(checkpoint(this.getX(), this.getY())) { // Verifica quando o player encosta no Checkpoint
			posX = this.getX(); // Atribui a posição X do player
			posY = this.getY(); // Atribui a posição Y do player
		}
		
		if(life <= 0) { // Se a vida for menor ou igual a 0
			// Seta a posição do personagem na posição do checkpoint
			setX(posX);
			setY(posY);
			life = 100; // Volta com a vida 100%
		}
		
		// Limitar a tela de jogo baseado no "mapa"
		Camera.x = Camera.Clamp(this.getX() - (Game.WIDTH/2), 0, mapa.Mapa.WIDTH*16 - Game.WIDTH); // 1º item = Posição atual do player - largura do jogo/2 -> Câmera do jogo fica centralizada no player, 2º min = 0 (só renderizar valores >= 0)
		Camera.y = Camera.Clamp(this.getY() - (Game.HEIGHT/2), 0, mapa.Mapa.HEIGHT*16 - Game.HEIGHT); // 1º item = Posição atual do player - largura do jogo/2 -> Câmera do jogo fica centralizada no player, 2º min = 0 (só renderizar valores >= 0)
	}
	
	// Método de Colisão
	public boolean colisao(int nextx, int nexty) { // nextx e nexty = pegar a posição X e Y do personagem
		Rectangle player = new Rectangle(nextx + maskx, nexty + masky, maskw, maskh); // Criar um retângulo pro player
		
		for(int i = 0; i < Game.entidades.size(); i++) {
			Entity entidade = Game.entidades.get(i);
			
			if(entidade instanceof Solido) { // Verifica se é um sólido. Se for, cria um novo retângulo para ela.
				Rectangle solido = new Rectangle(entidade.getX() + maskx, entidade.getY() + masky, maskw, maskh);
				
				if(player.intersects(solido)) { // Verifica se o player está encostando num sólido
					return true;
				}
			}
		}
		return false;
	}
	
	// Colisão com o checkpoint
	public boolean checkpoint(int nextx, int nexty) { // nextx e nexty = pegar a posição X e Y do personagem
		Rectangle player = new Rectangle(nextx + maskx, nexty + masky, maskw, maskh); // Criar um retângulo pro player
		
		for(int i = 0; i < Game.entidades.size(); i++) {
			Entity entidade = Game.entidades.get(i);
			
			if(entidade instanceof Check) { // Verifica se é o Checkpoint e cria uma área retangular nele
				Rectangle solido = new Rectangle(entidade.getX() + maskx, entidade.getY() + masky, maskw, maskh);
				
				if(player.intersects(solido)) { // Verifica se o player está encostando no Checkpoint
					return true;
				}
			}
		}
		return false;
	}
	
	// Colisão com o inimigo
	public boolean damage(int nextx, int nexty) { // nextx e nexty = pegar a posição X e Y do personagem
		Rectangle player = new Rectangle(nextx + maskx, nexty + masky, maskw, maskh); // Criar um retângulo pro player
		
		for(int i = 0; i < Game.inimigo.size(); i++) {
			Inimigo entidade = Game.inimigo.get(i);
			
			if(entidade instanceof Inimigo) { // Verifica se é um inimigo e cria uma área retangular nele
				Rectangle inimigo = new Rectangle(entidade.getX() + maskx, entidade.getY() + masky, maskw, maskh);
				
				if(player.intersects(inimigo)) { // Verifica se o player está encostando num inimigo
					ini = entidade; // Pegar a entidade atual e alocar no ini
					return true;
				}
			}
		}
		return false;
	}
	
	// Colisão com a cenoura para regenerar vida
	public boolean vida(int nextx, int nexty) { // nextx e nexty = pegar a posição X e Y da cenoura
		Rectangle player = new Rectangle(nextx + maskx, nexty + masky, maskw, maskh); // Criar um retângulo pro player
		
		for(int i = 0; i < Game.cenoura.size(); i++) {
			Cenoura cenoura = Game.cenoura.get(i);
			
			if(cenoura instanceof Cenoura) { // Verifica se é uma cenoura e cria uma área retangular nela
				Rectangle solido = new Rectangle(cenoura.getX() + maskx, cenoura.getY() + masky, maskw, maskh);
				
				if(player.intersects(solido)) { // Verifica se o player está encostando na cenoura
					vida = cenoura;
					return true;
				}
			}
		}
		return false;
	}
	
	public void render(Graphics g) {
		// Movimentação para a direita
		if(direcaoAtual == direita && movimentacao == 1) {
			g.drawImage(playerRight[index], this.getX() - Camera.x, this.getY() - Camera.y, null);			
		}
		
		// Idle
		if(direcaoAtual == direita && movimentacao == 0) {
			g.drawImage(playerRight[0], this.getX() - Camera.x, this.getY() - Camera.y, null);			
		}
		
		// Movimentação para esquerda
		if(direcaoAtual == esquerda && movimentacao == 1) {
			g.drawImage(playerLeft[index], this.getX() - Camera.x, this.getY() - Camera.y, null);			
		}
		
		// Idle
		if(direcaoAtual == esquerda && movimentacao == 0) {
			g.drawImage(playerLeft[0], this.getX() - Camera.x, this.getY() - Camera.y, null);			
		}
	}
}
