package at.ac.tuwien.foop.userInterface.Maze;



import java.awt.Image;
import java.io.File;
import java.util.Scanner;

import javax.swing.ImageIcon;

public class Map {

	private Scanner m;
	private String Map[] = new String[500];
	private Image wall, path, cheese;
	private ImageIcon wallIcon = new ImageIcon("src\\main\\resources\\wall.png");
	private ImageIcon pathIcon = new ImageIcon("src\\main\\resources\\path.jpg");
	private ImageIcon cheeseIcon = new ImageIcon("src\\main\\resources\\cheese.jpg");

	public Map() {
		wall = wallIcon.getImage();
		path = pathIcon.getImage();
		cheese = cheeseIcon.getImage();
		openFile();
		readFile();
		closeFile();
	}

	public String getMap(int x, int y) {
		String index = Map[y].substring(x,x + 1);
		return index;
	}


	public void openFile() {
		try {
			m = new Scanner(new File("src\\main\\resources\\Map.txt"));
		} catch(Exception e) {
			System.out.println("Error loading map!");
		}
	}

	public void readFile() 	{

		while(m.hasNext())	{
			for(int i=0; i<25; i++){
				Map[i] = m.next();
			}	
		}
	}

	public void closeFile(){
		m.close();
	}

	public Image getWall() {
		return wall;
	}

	public Image getPath() {
		return path;
	}

	public Image getCheese() {
		return cheese;
	}

}
