package com.hehan;
import java.util.*;
import java.io.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

class Load {
	int x;
	int y;
	int direct;
	public Load(int x, int y, int direct) {
		this.x = x;
		this.y = y;
		this.direct = direct;
	}
}

//recorder class, save settings of player
class Recorder {
	public static int getEnNum() {
		return enNum;
	}
	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}
	public static int getMyLife() {
		return myLife;
	}
	public static void setMyLife(int myLife) {
		Recorder.myLife = myLife;
	}
	
	//load
	static Vector<Load> loads = new Vector<Load>();
	
	public Vector<Load> getLoads() {
		try {
			fr = new FileReader("d:/myRecording.txt");
			br = new BufferedReader(fr);
			
			String n = "";
			//first line
			n=br.readLine();
			allEnNum = Integer.parseInt(n);
			while((n=br.readLine()) != null) {
				String []sp = n.split(" ");
				
				Load load = new Load(Integer.parseInt(sp[0]),Integer.parseInt(sp[1]),Integer.parseInt(sp[2]));
				loads.add(load);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return loads;
	}
	
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;

	
	
	
	//record enNum to file
	public static void keepRecording() {
		try {
			fw = new FileWriter("d:/myRecording.txt");
			bw = new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//read data
	private static FileReader fr = null;
	private static BufferedReader br = null;
	
	public static void getRecording() {
		try {
			fr = new FileReader("d:/myRecording.txt");
			br = new BufferedReader(fr);
			
			String n = br.readLine();
			allEnNum = Integer.parseInt(n);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	private Vector<EnemyTank> ets = new Vector<EnemyTank>();
	
	public  Vector<EnemyTank> getEts() {
		return ets;
	}
	public void setEts(Vector<EnemyTank> ets1) {
		this.ets = ets1;
		//System.out.println("ok");
	}
	//save position, number and direction of enemytank
	public void keepRecAndEnemyTank() {
		try {
			fw = new FileWriter("d:/myRecording.txt");
			bw = new BufferedWriter(fw);
			
			bw.write(allEnNum+"\r\n");
			
			//save alive enemytank position, direction
			for(int i=0;i<ets.size();i++) {
				EnemyTank et = ets.get(i);
				if(et.isLive) {
					//save
					String record = et.x+" "+et.y+" "+et.direct;
					
					//write to file
					bw.write(record+"\r\n");
					
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	//record number of enemytank
	private static int enNum = 20;
	//number of life
	private static int myLife = 3;
	//number of killed enemytank
	private static int allEnNum = 0;
	
	//reduce enemytank
	public static void reduceEnNum() {
		enNum--;
	}
	
	public static void addEnNum() {
		allEnNum++;
	}
	public static int getAllEnNum() {
		return allEnNum;
	}
	public static void setAllEnNum(int allEnNum) {
		Recorder.allEnNum = allEnNum;
	}
	
	public static void reduceMyLife() {
		myLife--;
	}
}

class Bomb {
	//the positon of bomb
	int x,y;
	int life = 9;
	boolean isLive = true;
	
	public Bomb(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//reduce life
	public void lifeDown() {
		if(life>0) {
			life--;
		} else {
			this.isLive = false;
		}
	}
}

class Shot implements Runnable {
	int x;
	int y;
	int direct;
	int speed = 2;
	
	boolean isLive = true;
	
	public Shot (int x, int y, int direct) {
		this.x = x;
		this.y = y;
		this.direct =  direct;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch(direct) {
			case 0:
				y-=speed;
				break;
			case 1:
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x-=speed;
				break;
			}
			
			//System.out.println("the position of bullet x="+x+"y="+y);
			//make the bullet dead
			//judge the bullet reach edge
			if(x<0||x>400||y<0||y>300) {
				this.isLive =  false;
				break;
			}
		}
	}
}

class Tank {
	//x axis y axis position
	int x = 0;
	int y = 0;
	//speed
	int speed = 2;
	boolean isLive = true;
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}



	//direction
	//0->up; 1->right; 2->down, 3->left
	int direct = 0;
	int color;
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
	
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}
		
}

class Hero extends Tank {
	//bullet
	//Shot s = null;
	Vector<Shot> ss = new Vector<Shot>();
	Shot s = null;
	
	public Hero(int x, int y) {
		
		super(x,y);
		
	}
	
	//Shot
	public void shotEnemy() {
		
		switch(this.direct) {
		case 0:
			//create one bullet
			s = new Shot(x+10,y,0);
			//add bullet to vector
			ss.add(s);
			break;
		case 1:
			s = new Shot(x+30,y+10,1);
			ss.add(s);
			break;
		case 2:
			s = new Shot(x+10,y+30,2);
			ss.add(s);
			break;
		case 3:
			s = new Shot(x,y+10,3);
			ss.add(s);
			break;
		}
		Thread t = new Thread(s);
		t.start();
	}
	
	//up
	public void moveUp() {
		y -= speed;
	}
	
	//right
	public void moveRight() {
		x += speed;
	}
	
	public void moveDown() {
		y += speed;
	}
	
	public void moveLeft() {
		x -= speed;
	}
}

class EnemyTank extends Tank implements Runnable {
	int times = 0;
	
	//define a vector to know enemytank in MyPanel
	Vector<EnemyTank> ets = new Vector<EnemyTank>();
	
	//define a vector, save bullet
	Vector<Shot> ss = new Vector<Shot>();
	//add bullet when the enemytank is created and one bullet is dead
	
	public EnemyTank(int x, int y) {
		super(x,y);
	}
	
	//get vector enemytank in MyPanel
	public void setEts(Vector<EnemyTank> vv) {
		this.ets = vv;
	}
	
	//collision with other enemytank
	public boolean isTouchOtherEnemy() {
		boolean b = false;
		switch(this.direct) {
		case 0:
			//get all the enemytank
			for(int i=0;i<ets.size();i++) {
				EnemyTank et = ets.get(i);
				if(et!=this) {
					if(et.direct == 0||et.direct ==2) {
						if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30) {
							return true;
						}
						
						if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y>=et.y&&this.y<=et.y+30) {
							return true;
						}
					}
					
					if(et.direct == 3 || et.direct ==1) {
						if(this.x>=et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20) {
							return true;
						}
						
						if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y>=et.y&&this.y<=et.y+20) {
							return true;
						}
					}
				}
			}
			break;
		case 1:
			for(int i=0;i<ets.size();i++) {
				EnemyTank et = ets.get(i);
				if(et!=this) {
					if(et.direct == 0||et.direct ==2) {
						if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y>=et.y&&this.y<=et.y+30) {
							return true;
						}
						
						if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y+20>=et.y&&this.y+20<=et.y+30) {
							return true;
						}
					}
					
					if(et.direct == 3 || et.direct ==1) {
						if(this.x+30>=et.x&&this.x+30<=et.x+30&&this.y>=et.y&&this.y<=et.y+20) {
							return true;
						}
						
						if(this.x+30>=et.x&&this.x+30<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20) {
							return true;
						}
					}
				}
			}
			break;
		case 2:
			for(int i=0;i<ets.size();i++) {
				EnemyTank et = ets.get(i);
				if(et!=this) {
					if(et.direct == 0||et.direct ==2) {
						if(this.x>=et.x&&this.x<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30) {
							return true;
						}
						
						if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30) {
							return true;
						}
					}
					
					if(et.direct == 3 || et.direct ==1) {
						if(this.x>=et.x&&this.x<=et.x+30&&this.y+30>=et.y&&this.y+30<=et.y+20) {
							return true;
						}
						
						if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y+30>=et.y&&this.y+30<=et.y+20) {
							return true;
						}
					}
				}
			}
			break;
		case 3:
			for(int i=0;i<ets.size();i++) {
				EnemyTank et = ets.get(i);
				if(et!=this) {
					if(et.direct == 0||et.direct ==2) {
						if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30) {
							return true;
						}
						
						if(this.x>=et.x&&this.x<=et.x+20&&this.y+20>=et.y&&this.y+20<=et.y+30) {
							return true;
						}
					}
					
					if(et.direct == 3 || et.direct ==1) {
						if(this.x>=et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20) {
							return true;
						}
						
						if(this.x>=et.x&&this.x<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20) {
							return true;
						}
					}
				}
			}
			break;
		}
		return b;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(true){
			
			switch(this.direct) {
			case 0:
				for(int i=0; i<30;i++) {
					
					if(y>0&&!this.isTouchOtherEnemy()) {
						y -= speed;
					}
					
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
				break;
			case 1:
				for(int i=0; i<30;i++) {
					if(x<400&&!this.isTouchOtherEnemy()) {
						x += speed;
					}
					
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
				break;
			case 2:
				for(int i=0; i<30;i++) {
					if(y<300&&!this.isTouchOtherEnemy()) {
						y += speed;
					}
					
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
				break;
			case 3:
				for(int i=0; i<30;i++) {
					if(x>0&&!this.isTouchOtherEnemy()) {
						x -= speed;
					}
					
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
				break;
			}	
			
			this.times++;
			if(times%2 ==0) {
				if(isLive) {
					//judge whether we need add new bullet
							if(ss.size()<5) {
								Shot s = null;
								switch(direct) {
								case 0:
									//create one bullet
									s = new Shot(x+10,y,0);
									//add bullet to vector
									ss.add(s);
									break;
								case 1:
									s = new Shot(x+30,y+10,1);
									ss.add(s);
									break;
								case 2:
									s = new Shot(x+10,y+30,2);
									ss.add(s);
									break;
								case 3:
									s = new Shot(x,y+10,3);
									ss.add(s);
									break;
								}
								
								Thread t = new Thread(s);
								t.start();
							}
				}
			}
			
			//let tank direction randomly
			this.direct = (int)(Math.random()*4);
			
			//judge enemytank is alive
			if(this.isLive == false) {
				break;
			}
			
		}
	}
}

class AePlayWave extends Thread {

	private String filename;
	public AePlayWave(String wavfile) {
		filename = wavfile;

	}

	public void run() {

		File soundFile = new File(filename);

		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}

		AudioFormat format = audioInputStream.getFormat();
		SourceDataLine auline = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		try {
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		auline.start();
		int nBytesRead = 0;
		//buffer
		byte[] abData = new byte[512];

		try {
			while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0)
					auline.write(abData, 0, nBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			auline.drain();
			auline.close();
		}

	}

	
}