import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener{
	
	private static final long serialVersionUID = 1L;
	public boolean isRun = true;
	public static final int WIDTH = 240;
	public static final int HEIGTH = 160;
	public static final int S = 3;
	public BufferedImage layer = new BufferedImage(WIDTH,HEIGTH,BufferedImage.TYPE_INT_RGB);
	
	public int tam = 10;
	public boolean rigth,lefth,up,down;
	public Node[] nodeSnake = new Node[tam];
	public int score = 0;
	public int bolaX = 0, bolaY=0;
	public double speed = 1.0;
	
	public Game(){
		setPreferredSize(new Dimension(WIDTH*S,HEIGTH*S));
		this.addKeyListener(this);
		for(int i=0;i<nodeSnake.length;i++) {
			nodeSnake[i] = new Node(0,0);
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		new Thread(game).start(); 
	}
	public void tick() {
		for(int i=nodeSnake.length-1; i>0 ;i--) {
			nodeSnake[i].x = nodeSnake[i-1].x;
			nodeSnake[i].y = nodeSnake[i-1].y;
		}
		if(nodeSnake[0].x + tam < 0) {
			nodeSnake[0].x = WIDTH;
		}
		else if(nodeSnake[0].x >= WIDTH) {
			nodeSnake[0].x = -tam;
		}
		if(nodeSnake[0].y + tam < 0) {
			nodeSnake[0].y = HEIGTH;
		}
		else if(nodeSnake[0].y >= HEIGTH) {
			nodeSnake[0].y = -tam;
		}
		if(rigth) {
			nodeSnake[0].x+=speed;
		}
		else if (lefth) {
			nodeSnake[0].x-=speed;
		}
		else if (up) {
			nodeSnake[0].y-=speed;
		}
		else if (down) {
			nodeSnake[0].y+=speed;
		}
		if(new Rectangle(nodeSnake[0].x,nodeSnake[0].y,3,3).intersects(new Rectangle(bolaX,bolaY,3,3))) {
			bolaX = new Random().nextInt(WIDTH-3);
			bolaY = new Random().nextInt(HEIGTH-3);
			speed +=0.2;
			tam+=50;
			System.out.println("Pontos: "+score);
			//System.out.println("tamanho "+tam);
			score++;
		}
	}
	public void render() {
		
		BufferStrategy bs =  this.getBufferStrategy();
		if(bs==null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = layer.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0,WIDTH,HEIGTH);
		for(int i=0;i<nodeSnake.length;i++) {
			g.setColor(Color.green);
			g.fillRect(nodeSnake[i].x,nodeSnake[i].y,nodeSnake[i].w,nodeSnake[i].h);
		}
		g.setColor(Color.white);
		g.fillRect(bolaX,bolaY,3,3);
		g = bs.getDrawGraphics();
		g.drawImage(layer,0,0,WIDTH*S,HEIGTH*S,null);
		bs.show();
	}
	
	public void run() {
		while(isRun) {
			requestFocus();
			tick();
			render();
			try {
				Thread.sleep(1000/60);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			rigth = true;
			lefth = false;
			up = false;
			down = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			rigth = false;
			lefth = true;
			up = false;
			down = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_UP) {
			rigth = false;
			lefth = false;
			up = true;
			down = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			rigth = false;
			lefth = false;
			up = false;
			down = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
