//package Grid;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;

import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



//ARNOLD SANDERS
//CMP 168 MINDSWEEPER IMPLEMENTATION
//VIDEO FOR ARNOLDS MINESWEEPER PROJECT

public class Grid implements ChangeListener {
	private static final long serialVersionUID = 1L;
	private Cell [][] bombGrid;
	private int [][] countGrid;
	private int numRows;
	private int numColumns;
	private int numBombs;

	public class Cell extends JButton implements ActionListener{
		private static final long serialVersionUID = 1L;
		private JButton button;
		private Boolean armed;
		public Boolean revealed;
		private int nearCount;
		private int row;
		private int col;
	
		public Cell (int row, int col) {
			button = new JButton(" ");
			button.setFont(new Font("Serif", Font.PLAIN, 30));
			addActionListener(this);
			nearCount = 0;
			armed = false;
			revealed = false;
			this.row = row;
			this.col = col;
		}
	
	
		public void setArmed () {
			this.armed = true;
		}
	
		public Boolean getArmed () {
			return this.armed;
		}
	
		public void setRevealed () {
			this.revealed = true;
			String count = String.valueOf(nearCount);
			this.setText(count);
		}
	
		public Boolean getRevealed () {
			return this.revealed;
		}
	
		public void setNearCount (int count) {
			this.nearCount = count;
		} 
	
		public int getNearCount () {
			return this.nearCount;
		} 
	
		public int getRow() {
			return row;
		}
	
		public int getCol() {
			return col;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.setRevealed();
		}
	
	}

	public Grid () {
		
		numRows = 10;
		numColumns = 10;
		numBombs = 25;
		
		createBombGrid();
		createCountGrid();
	}
	
	private void createBombGrid() {
		//how to remember that a bomb was set?
		int count = 0;
		this.bombGrid = new Cell[this.numRows][this.numColumns];
		Random rand = new Random();
		
		for (int row = 0; row < bombGrid.length; row++) {
			for (int col = 0; col < bombGrid[row].length; col++) {
				Cell cell = new Cell(row, col);
				//update Grid with information when its clicked 
				cell.addChangeListener(this);
				
				bombGrid[row][col] = cell;
				if (rand.nextInt(1) < 0.5) {
					if (count < numBombs) {
						cell.setArmed();
						count++;
					}
				}
				
			}
		}
	} 
	
	private void createCountGrid() {
		countGrid = new int[numRows][numColumns];
		
		for (int row = 0; row < bombGrid.length; row++) {
			for (int col = 0; col < bombGrid[row].length; col++ ) {
				int count = 0;
				
				for (int x = row - 1; x < row + 1; x++) {
					for (int y = col -1; y <= col + 1; y++) {
						if (x >= 0 && y >= 0 && x < row && y < col) {
							Cell cell = bombGrid[x][y];
							if (cell.getArmed()) {
								count++;
							}
						}
					}
				}
			
				if (bombGrid[row][col].getArmed()) {
					bombGrid[row][col].setNearCount(count - 1); 
					countGrid[row][col] = count - 1;
				} else {
					bombGrid[row][col].setNearCount(count);
					countGrid[row][col] = count;
				}
			}
		}
		
	}
	
	public Cell [][] getBombGrid() {
		return bombGrid;
	}
	
	public int [][] getCountGrid() {
		return countGrid;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public int getNumBombs() {
		return numBombs;
	}
	
	public Boolean isBombAtLocation(int row, int col) {
		Cell cell = bombGrid[row][col];
		return cell.getArmed();
	}
	
	public int getCountAtLocation(int row, int col) {
		return countGrid[row][col];
	}
	
	public void gameOver() {
		for (int row = 0; row < bombGrid.length; row++) {
			for (int col = 0; col < bombGrid[row].length; col++) {
				bombGrid[row][col].setRevealed();
			}
		}
	}
	

	public void revealAdjacentCells(Cell cell) {
		for (int x = cell.getRow() - 1; x < cell.getRow() + 1; x++) {
			for (int y = cell.getCol() -1; y <= cell.getCol() + 1; y++) {
				if (x >= 0 && y >= 0) {
					Cell neighbor = bombGrid[x][y];
					if (!isBombAtLocation(neighbor.getRow(), neighbor.getCol())){
						neighbor.setRevealed();
					}
//					if (neighbor.getNearCount() == 0){
////						reinitiate loop to check all other cells that are near by excluding bombs
//					}
				}
				
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Cell cell = (Cell) e.getSource();
		System.out.println("hmmmm  " + cell.getNearCount());
		if (isBombAtLocation(cell.getRow(), cell.getCol())) {
			System.out.print("hmmmm" + cell.getArmed());
			gameOver();
		}
//		if (getCountAtLocation(cell.getRow(), cell.getCol()) == 0) {
//			revealAdjacentCells(cell);
//		}
	}
	
	public static void main (String [] args) {
		   JFrame window = new JFrame();
		   window.setTitle("MineSweeper");
		   window.setSize(1000,1000);
		   
		   Grid grid = new Grid();
		   Cell [][] bombGrid = grid.getBombGrid();
		   int [][] countGrid = grid.getCountGrid();
		   
		   window.setLayout(new GridBagLayout());
		   GridBagConstraints constraints = null;
		   
		  
		   for (int row = 0; row < bombGrid.length; row++) {
			   for (int col = 0; col < bombGrid[row].length; col++) {
				   	constraints = new GridBagConstraints(); // Recreate the constraints
					constraints.gridx = col;
					constraints.gridy = row;
					window.add(bombGrid[row][col], constraints);
				}
			}
		   
		   window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   window.setVisible(true);
	}
}
