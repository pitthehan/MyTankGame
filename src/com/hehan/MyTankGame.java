/**
 * Tank Game
 * 1.draw tanks
 * 2.tank can move (w->up;d->right;s->down;a->left)
 * 3.shot bullets(max 5)
 * 4.after my tank hits enemytank, enemy disappears, then bombs
 * 5.after enemytank hits me, mytank bomb
 * 6.enemytank don't overlap
 *  6.1 collision written in enemytank
 * 7.different level
 *  7.1 create a start panel which is null
 *  7.2 make title blink
 * 8.exit
 * 9.exit and save
 *  9.1 use io
 *  9.2 write a record class to save data
 *  9.3 save EnNum
 *  9.4 save data, then exit, record postion of enemytank
 * 10.reload data
 * 11.insert audio files
 */

package com.hehan;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;//vector


public class MyTankGame extends JFrame implements ActionListener {
	
	MyStartPanel msp = null;
	MyPanel mp = null;
	//create menu
	JMenuBar jmb = null;
	JMenu jm1 = null;
	JMenuItem jmi1 = null;
	JMenuItem jmi2 = null;
	JMenuItem jmi3 = null;
	JMenuItem jmi4 = null;
	
	
	public static void main(String[] args) {
		MyTankGame mtg = new MyTankGame();
	}
	
	public MyTankGame() {
		
		
		//create menu
		jmb = new JMenuBar();
		jm1 = new JMenu("Game(G)");
		jm1.setMnemonic('G');
		jmi1 = new JMenuItem("Start A New Game (N)");
		jmi1.setMnemonic('N');
		jmi2 = new JMenuItem("Exit Game (E)");
		jmi2.setMnemonic('E');
		jmi3 = new JMenuItem("Save and Exit (S)");
		jmi3.setMnemonic('S');
		jmi4 = new JMenuItem("Load former game");
		jmi4.setMnemonic('L');
		
		//response to jmi1
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("save");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("load");
		
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi3);
		jm1.add(jmi4);
		jmb.add(jm1);
		
		
		msp = new MyStartPanel();
		Thread t = new Thread(msp);
		t.start();
		
		this.setJMenuBar(jmb);
		this.add(msp);
		this.setSize(600,450);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("newgame")) {
			mp = new MyPanel("newGame");
			//start mp thread
			Thread t = new Thread(mp);
			t.start();
			
			//firstly remove the old panel
			this.remove(msp);
			this.add(mp);
			this.addKeyListener(mp);
			//show, refresh JFrame
			this.setVisible(true);
		} else if(e.getActionCommand().equals("exit")) {
			//save data
			Recorder.keepRecording();
			//exit
			System.exit(0);
		} else if(e.getActionCommand().equals("save")) {
			//save number and position of enemytank
			Recorder rd = new Recorder();
			rd.setEts(mp.ets);
			rd.keepRecAndEnemyTank();
			System.exit(0);
		} else if(e.getActionCommand().equals("load")) {
			//load
			mp = new MyPanel("load");

		
			//start mp thread
			Thread t = new Thread(mp);
			t.start();
			
			//firstly remove the old panel
			this.remove(msp);
			this.add(mp);
			this.addKeyListener(mp);
			//show, refresh JFrame
			this.setVisible(true);
		}
	}
	
}

//start panel
class MyStartPanel extends JPanel implements Runnable {
	int times = 0;
	
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		
		if(times%2 == 0) {
			g.setColor(Color.red);
			Font myFront = new Font("Calibri", Font.BOLD, 25);
			g.setFont(myFront);
			g.drawString("He Han", 150,100);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			times++;
			this.repaint();
		}
	}
}

//MyPanel
class MyPanel extends JPanel implements KeyListener, Runnable {
	//define my tank
	Hero hero = null;
	
	//judge load or new
	String flag ="newGame";
	
	
	
	//define enemytank
	Vector<EnemyTank> ets = new Vector<EnemyTank>();
	Vector<Load> loads = new Vector<Load>();
	
	//define bomb
	Vector<Bomb> bombs = new Vector<Bomb>(); 
	
	//define three images
	Image image1 = null;
	Image image2 = null;
	Image image3 = null;
	
	int enSize = 5;
	//constructor
	public MyPanel(String flag) {
		//recover record
		Recorder.getRecording();
		hero = new Hero(100,100);
		
		if(flag.equals("newGame")) {
			
		
			for(int i=0; i<enSize; i++) {
				//create enemytank object
				EnemyTank et = new EnemyTank((i+1)*50,0);
				et.setColor(0);
				et.setDirect(2);
				
				//pass vector enemytank in MyPanel to enemytank
				et.setEts(ets);
				
				
				Thread t = new Thread(et);
				t.start();
				//start enemytank
				
				//add bullet
				Shot s = new Shot(et.x+10, et.y+30, 2);
				et.ss.add(s);
				Thread t2 = new Thread(s);
				t2.start();
				//add
				ets.add(et);
			}
		
		} else {
			
			loads = new Recorder().getLoads();
			for(int i=0; i<loads.size(); i++) {
				
				Load load = loads.get(i);
				//create enemytank object
				EnemyTank et = new EnemyTank(load.x,load.y);
				et.setColor(0);
				et.setDirect(load.direct);
				
				//pass vector enemytank in MyPanel to enemytank
				et.setEts(ets);
				
				
				Thread t = new Thread(et);
				t.start();
				//start enemytank
				
				//add bullet
				Shot s = new Shot(et.x+10, et.y+30, 2);
				et.ss.add(s);
				Thread t2 = new Thread(s);
				t2.start();
				//add
				ets.add(et);
			}
		}
		//three images make a bomb
			//image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb1.gif"));
			//image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb2.gif"));
			//image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb3.gif"));
		try {
			image1 = ImageIO.read(new File("bomb1.gif"));
			image2 = ImageIO.read(new File("bomb2.gif"));
			image3 = ImageIO.read(new File("bomb3.gif"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//audio
		AePlayWave apw = new AePlayWave("./tank.wav");
		apw.start();
	}
	
	public void showInfo(Graphics g) {
		this.drawTank(100, 320, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Recorder.getEnNum()+"", 103, 365);
		
		this.drawTank(140, 320, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"", 143, 365);
	
		g.setColor(Color.black);
		Font f = new Font("Arial",Font.BOLD,15);
		g.setFont(f);
		g.drawString("Your achievement:", 410, 30);
		
		this.drawTank(410, 60, g, 0, 0);
		
		g.setColor(Color.black);
		g.drawString(Recorder.getAllEnNum()+"",440, 80);
		
	}
	
	//repaint
	public void paint(Graphics g) {
		super.paint(g);
		//g.setColor(Color.black);
		g.fillRect(0, 0, 400, 300);
		
		//draw info of tankNum
		this.showInfo(g);
		
		if(hero.isLive == true) {
			this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 1);
		}
		//get all the bullets from ss, and draw
		for(int i=0;i<hero.ss.size(); i++) {
		//draw bullet
			Shot myShot = hero.ss.get(i);
			if(myShot!=null&&myShot.isLive==true) {
				g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
			}
			
			if(myShot.isLive == false) {
				hero.ss.remove(myShot);
			}
		}
		
		//draw bomb
		for(int i=0; i<bombs.size();i++) {
			Bomb b = bombs.get(i);
			if(b.life>6) {
				g.drawImage(image1, b.x, b.y, 30, 30, this);
			} else if(b.life>3) {
				g.drawImage(image2, b.x, b.y, 30, 30, this);
			} else {
				g.drawImage(image3, b.x, b.y, 30, 30, this);
			}
			
			b.lifeDown();
			
			if(b.life == 0) {
				bombs.remove(b);
			}
		}
		
		//draw enemytank
		for(int i=0; i<ets.size();i++) {
			EnemyTank et = ets.get(i);
			if(et.isLive){
				this.drawTank(et.getX(),et.getY(),g,et.getDirect(),0);
				for(int j=0; j<et.ss.size();j++) {
					Shot enemyShot = et.ss.get(j);
					if(enemyShot.isLive) {
						g.draw3DRect(enemyShot.x, enemyShot.y, 1, 1, false);
					} else {
						et.ss.remove(enemyShot);
					}
				}
			}
		}
	}
	
	//whether the enemytank is attacjed
	public boolean hitTank(Shot s, Tank et) {
		boolean b2 = false;
		//the direction of tank
		switch(et.direct) {
		case 0:
		case 2:
			if(s.x<et.x&&s.x>et.x+20&&s.y>et.y&&s.y<et.y+30) {
				//bullet dead, enemytank dead
				s.isLive = false;
				et.isLive = false;
				b2 = true;
				//create a bomb
				Bomb b = new Bomb(et.x,et.y);
				bombs.add(b);
				
			}
			break;
		case 1:
		case 3:
			if(s.x>et.x&&s.x<et.x+30&&s.y>et.y&&s.y<et.y+20) {
				//bullet dead, enemytank dead
				s.isLive = false;
				et.isLive = false;
				b2 = true;
				Bomb b = new Bomb(et.x,et.y);		
				bombs.add(b);
			}
		}
		
		return b2;
	}
	
	public void drawTank(int x, int y, Graphics g, int direct, int type) {
		//tank type
		switch(type) {
		case 0:
			g.setColor(Color.red);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		
		//direction
		switch(direct) {
		case 0:
			//1.left rect
			g.fill3DRect(x, y, 5, 30,false);
			//2.right rect
			g.fill3DRect(x+15, y, 5, 30, false);
			//3.center rect
			g.fill3DRect(x+5, y+5, 10, 20, false);
			//4.draw oval
			g.fillOval(x+5, y+10, 10, 10);
			//5.draw line
			g.drawLine(x+10, y+15, x+10, y);
			break;
		case 1:
			//1.up rect
			g.fill3DRect(x, y, 30, 5,false);
			//2.down rect
			g.fill3DRect(x, y+15, 30, 5, false);
			//3.center rect
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//4.draw oval
			g.fillOval(x+10, y+5, 10, 10);
			//5.draw line
			g.drawLine(x+15, y+10, x+30, y+10);
			break;
		case 2:
			//1.left rect
			g.fill3DRect(x, y, 5, 30,false);
			//2.right rect
			g.fill3DRect(x+15, y, 5, 30, false);
			//3.center rect
			g.fill3DRect(x+5, y+5, 10, 20, false);
			//4.draw oval
			g.fillOval(x+5, y+10, 10, 10);
			//5.draw line
			g.drawLine(x+10, y+15, x+10, y+30);
			break;
		case 3:
			//1.up rect
			g.fill3DRect(x, y, 30, 5,false);
			//2.down rect
			g.fill3DRect(x, y+15, 30, 5, false);
			//3.center rect
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//4.draw oval
			g.fillOval(x+10, y+5, 10, 10);
			//5.draw line
			g.drawLine(x+15, y+10, x, y+10);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//a->left; w->up; d->right; s->down
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() ==  KeyEvent.VK_W) {
			this.hero.setDirect(0);
			this.hero.moveUp();
		} else if (e.getKeyCode() ==  KeyEvent.VK_D) {
			this.hero.setDirect(1);
			this.hero.moveRight();
		} else if (e.getKeyCode() ==  KeyEvent.VK_S) {
			this.hero.setDirect(2);
			this.hero.moveDown();
		} else if (e.getKeyCode() ==  KeyEvent.VK_A) {
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		
		if (e.getKeyCode() ==  KeyEvent.VK_J) {
			if(this.hero.ss.size()<=4) {
				this.hero.shotEnemy();
			}
			
		}
		
		//repaint panel
		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//repaint every 100 msec
		while(true) {
			//must sleep, or the computer will be slow. 
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.hitEnemyTank();
			//write a function the judge enemytank hit hero
			this.hitMe();
			this.repaint();
			//repaint
		}
	}
	
	//judge whether hit enemytank
	public void hitEnemyTank() {
		//judge whether hit enemytank
		for(int i=0; i<hero.ss.size();i++) {
			Shot myShot = hero.ss.get(i);
			if(myShot.isLive) {
				for(int j=0;j<ets.size();j++) {
					EnemyTank et = ets.get(j);
					if(et.isLive) {
						if (this.hitTank(myShot, et)){
							Recorder.reduceEnNum();
							Recorder.addEnNum();
						}
					}
				}
			}
		}
		
	}
	
	public void hitMe() {
		//judge whether hit me
		for(int i=0; i<this.ets.size();i++) {
			
			EnemyTank et = ets.get(i);
			
			for(int j=0; j<et.ss.size();j++) {
				Shot enemyShot = et.ss.get(j);
				if(hero.isLive) {
					if(this.hitTank(enemyShot,hero)) {
						
					}
					
					
				} 
			}
		}
		
		
		
	}
	
	
}

//Tank class
