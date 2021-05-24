package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import mapa.Mapa;
import entidades.Cenoura;
import entidades.Ceu;
import entidades.Entity;
import entidades.Inimigo;
import entidades.Player;
import graficos.Spritesheet;

public class Game extends Canvas implements Runnable, KeyListener {
// Canvas = Meio de criacao da tela e seus itens (botao de fechar, minimizar etc etc)

	private static final long serialVersionUID = 1L; // Nada muito importante... sao pra tirar a marcacao em Game
	
	public static JFrame jFrame; // Janela
	private Thread thread;
	private boolean isRunning = true;

	public static int WIDTH = 240; // Largura
	public static int HEIGHT = 160; // Altura
	private static int SCALE = 4; // Servira para multiplicar a largura e altura pelo valor da escala
	
	private BufferedImage fundo; // Fundo do jogo
	public static List<Entity> entidades;
	public static Spritesheet sprite;
	public static Mapa mapa;
	
	public static Player player;
	
	public static List<Ceu> ceuVetor;
	public static Spritesheet ceu;
	
	public static List<Cenoura> cenoura;
	public static List<Inimigo> inimigo;
	
	public UserInterface ui;
	
	public int level = 1; // 1o level
	public int levelMax = 2; // Apenas 2 levels
	
	public Game() {
		addKeyListener(this); // Adicionando um evento "escutador" de teclado
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE)); // Tamanho para o JFrame
		initFrame(); // Organizar partes do codigo e separa-las (mais literal)
		ui = new UserInterface();
		fundo = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); // Inicializando...
		entidades = new ArrayList<Entity>(); // Para rodar coisas referentes a entidades
		sprite = new Spritesheet("/spritesheet.png"); // Sprite das entidades
		cenoura = new ArrayList<Cenoura>();
		inimigo = new ArrayList<Inimigo>();
		ceuVetor = new ArrayList<Ceu>();
		ceu = new Spritesheet("/ceusprite.png");
		player = new Player(0, 0, 16, 16, sprite.getSprite(32, 0, 16, 16));
		entidades.add(player); // Adicionar o player
		mapa = new Mapa("/level1.png");
	}
	
	public void initFrame() {
		jFrame = new JFrame("JOGO"); // Inicializando o objeto JFrame
		jFrame.add(this); // Adicionando no Game
		jFrame.setResizable(false); // Nao poder alterar o tamanho da tela
		jFrame.pack(); // Conteudo "acima" (na frente) da janela
		jFrame.setLocationRelativeTo(null); // Definir a posicao da janela no centro (null) da tela do computador
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Fechar o Game
		jFrame.setVisible(true); // Deixar visivel
	}
	
	public static void main(String[] args) {
		Game game = new Game(); // Inicializando objeto Game
		game.start(); // Iniciar o game
	}
	
	// Metodo de Sincronizacao
	public synchronized void start() {
		thread = new Thread(this); // Inicializando a Thread nesta classe > this
		isRunning = true; // True por causa do loop
		thread.start(); // Ativar o loop
	}
		
	// Metodo de Sincronizacao
	public synchronized void stop() {
		isRunning = false;
		
		// Se acontecer algum problema, parar todas as threads pra evitar travamentos/lags no PC
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// Tick antes do Render
	public void tick() {
		// Para passar de level (elimina os inimigos)
		if(inimigo.size() == 0) {
			level++;
			
			// Quando chegar no ultimo level, retorna pro 1 (loop)
			if(level > levelMax) {
				level = 1;
			}
			
			String Level = "level"+level+".png";
			Mapa.nextLevel(Level);
		}
		
		for(int i = 0; i < entidades.size(); i++) {
			Entity entidade = entidades.get(i);
			entidade.tick();
		}
		
		for(int i = 0; i < ceuVetor.size(); i++) {
			Ceu entidade = ceuVetor.get(i);
			entidade.tick();
		}
		
		for(int i = 0; i < cenoura.size(); i++) {
			Cenoura entidade = cenoura.get(i);
			entidade.tick();
		}
		
		for(int i = 0; i < inimigo.size(); i++) {
			Inimigo entidade = inimigo.get(i);
			entidade.tick();
		}
	}
	
	public void render() {
		BufferStrategy buffer = this.getBufferStrategy(); // Sequencia de buffers para otimizar a renderizacao dos graficos
		
		if(buffer == null) {
			this.createBufferStrategy(3); // 3 = buffers necessarios, caso null = 3 sao criados
			
			return;
		}
		
		Graphics g = fundo.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		mapa.render(g);
		
		// Render ceu antes das entidades para carregar o ceu (background) antes
		for(int i = 0; i < ceuVetor.size(); i++) {
			Ceu entidade = ceuVetor.get(i);
			entidade.render(g);
		}
		
		// Responsavel por alocar o player, tile, solido...
		for(int i = 0; i < entidades.size(); i++) {
			Entity entidade = entidades.get(i);
			entidade.render(g);
		}
		
		for(int i = 0; i < cenoura.size(); i++) {
			Cenoura entidade = cenoura.get(i);
			entidade.render(g);
		}
		
		for(int i = 0; i < inimigo.size(); i++) {
			Inimigo entidade = inimigo.get(i);
			entidade.render(g);
		}
		
		ui.render(g);
		
		g = buffer.getDrawGraphics(); // Evitar efeito "pisca-pisca" da tela
		g.drawImage(fundo, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		buffer.show();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime(); // nanoTime() = Retorna um valor de tempo do sistema em nanossegundos
		double amountOfTicks = 60.0f; // Delimitar o tamanho do Tick
		double ms = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0; // Contador
		double timer = System.currentTimeMillis();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ms;
			lastTime = now;
			
			// System.out.println(lastTime);
			// System.out.println(timer);
			
			// Temporizador
			if(delta > 1) { // Se a conta em delta > 1 = 
				tick();
				render();
				frames++;
				delta--;
			}			
			
			// Verificar o FPS
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS:" + frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// NAO SERA UTILIZADO
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		// Tecla D ou seta para a direita
		if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = true;
			// Tecla A ou seta para a esquerda
		} else if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = true;
		}
		
		// Tecla W ou seta para cima
		if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			player.up = true;
			// Tecla S ou seta para baixo
		} else if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.down = true;
		}
		
		// Tecla espaco
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = false;
		} else if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			player.up = false;
		} else if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.down = false;
		}
	}
}
