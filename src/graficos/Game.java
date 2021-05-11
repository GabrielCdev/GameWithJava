package graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
// Canvas = janela do Game

	private static final long serialVersionUID = 1L; // Nada muito importante... só pra tirar a marcação em Game
	
	public static JFrame jFrame; // Janela
	private Thread thread;
	private boolean isRunning = true;
	
	private static int WIDTH = 160; // Largura
	private static int HEIGHT = 120; // Altura
	private static int SCALE = 3; // Servirá para multiplicar a largura e altura pelo valor da escala
	
	private BufferedImage fundo; // Fundo do jogo
	
	public Game() {
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE)); // Tamanho para o JFrame
		initFrame(); // Organizar partes do código e separá-las (mais literal)
		fundo = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); // Inicializando...
	}
	
	public void initFrame() {
		jFrame = new JFrame("JOGO"); // Inicializando o objeto JFrame
		jFrame.add(this); // Adicionando no Game
		jFrame.setResizable(false); // Não poder alterar o tamanho da tela
		jFrame.pack(); // Conteúdo "acima" (na frente) da janela
		jFrame.setLocationRelativeTo(null); // Definir a posição da janela no centro (null) da tela do computador
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Fechar o Game
		jFrame.setVisible(true); // Deixar visível
	}
	
	public static void main(String[] args) {
		Game game = new Game(); // Inicializando objeto Game
		game.start(); // Iniciar o game
	}
	
	// Método de Sincronização
	public synchronized void start() {
		thread = new Thread(this); // Inicializando a Thread nesta classe > this
		isRunning = true; // True por causa do loop
		thread.start(); // Ativar o loop
	}
	
	// Método de Sincronização
	public synchronized void stop() {
		
	}
	
	// Tick antes do Render
	public void tick() {
		
	}
	
	public void render() {
		BufferStrategy buffer = this.getBufferStrategy();
		
		if(buffer == null) {
			this.createBufferStrategy(3);
			
			return;
		}
		
		Graphics g = fundo.getGraphics();
		g.setColor(new Color(20, 20, 20));
		g.fillRect(0, 0, WIDTH, HEIGHT);
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
	}

}
