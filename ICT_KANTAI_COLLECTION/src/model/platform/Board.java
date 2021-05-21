package model.platform;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import model.system.GameConfig;
import model.unit.warship.Ship;

public class Board extends Parent {
	// private Cell[][] battleField = new Cell[numOfRows][numOfColumns];
	// init a list of ship
	private List<Ship> shipArmy = new ArrayList<>();

	// create a horizontal box containing columns of cell
	private HBox recordBox = new HBox();

	// create box to bound rulers & battlefield
	// add mouse handlers to each cell inside the battlefield
	public Board(EventHandler<MouseEvent> mouseEnteredHandler, EventHandler<MouseEvent> mouseExitedHandler,
			EventHandler<MouseEvent> mouseClickedHandler, List<Ship> shipArmy) {
		this.shipArmy = shipArmy;
		// create a horizontal box to bound column ruler and (row ruler & battlefield)
		HBox hBoundBox = new HBox();

		// create ruler A -> J
		VBox rulerCol = new VBox();
		rulerCol.getChildren().add(new Cell());
		Text txt;
		for (int j = 0; j < GameConfig.NUM_OF_ROWS; j++) {
			txt = new Text(Character.toString((char) (j + 65)));
			StackPane stack = new StackPane();
			stack.getChildren().addAll(new Cell(), txt);
			rulerCol.getChildren().addAll(stack);
		}
		hBoundBox.getChildren().add(rulerCol);

		// create a vertical box to bound row ruler and battlefield
		VBox vBoundBox = new VBox();

		// create ruler 1 -> 10
		HBox rulerRow = new HBox();
		for (int i = 0; i < GameConfig.NUM_OF_COLS; i++) {
			txt = new Text(Integer.toString(i + 1));
			StackPane stack = new StackPane();
			stack.getChildren().addAll(new Cell(), txt);
			rulerRow.getChildren().addAll(stack);
		}
		vBoundBox.getChildren().add(rulerRow);

		// pass event handler to each cell in the board
		for (int i = 0; i < GameConfig.NUM_OF_COLS; i++) {
			// create new columns of cell to add to recordBox -> form a board
			VBox colBox = new VBox();
			for (int j = 0; j < GameConfig.NUM_OF_ROWS; j++) {
				// init cell with coordinate and set event handler for it
				Cell c = new Cell(this, i, j);
				c.setMouseEvtHandler(mouseEnteredHandler, mouseExitedHandler, mouseClickedHandler);
				// add cell to column
				colBox.getChildren().add(c);
			}
			// add column to recordBox
			recordBox.getChildren().add(colBox);
		}
		// add recordBox to the vertical box
		vBoundBox.getChildren().add(recordBox);
		// add vertical box to horizontal box
		hBoundBox.getChildren().add(vBoundBox);
		// add horizontal box to the fxml
		getChildren().add(hBoundBox);
		// getChildren().add(recordBox);
	}

	// get Cell by Coordinate
	public Cell getCellByCoordinate(int coorX, int coorY) {
		// get cell from the column that extracted from the rowBox
		return (Cell) ((VBox) recordBox.getChildren().get(coorX)).getChildren().get(coorY);
	}

	public int getNumOfShipLeft() {
		return shipArmy.size();
	}

	public void hideAllCellOfBoard() {
		for (Ship ship : shipArmy) {
			ship.stealthMode();
		}
	}

	// check if a cell is valid by the bound of board
	public static boolean isValidCell(int cXPos, int cYPos) {
		return cXPos >= 0 && cXPos < GameConfig.NUM_OF_ROWS && cYPos >= 0 && cYPos < GameConfig.NUM_OF_COLS;
	}

	// get list of surrounding cells of a cell
	public List<Cell> getAdjacentCellList(int cXPos, int cYPos) {
		List<Cell> adjacentList = new ArrayList<Cell>();
		List<Pair<Integer, Integer>> adjacentCoordinate = new ArrayList<>();
		// North
		adjacentCoordinate.add(new Pair<Integer, Integer>(cXPos, cYPos - 1));
		// East
		adjacentCoordinate.add(new Pair<Integer, Integer>(cXPos + 1, cYPos));
		// South
		adjacentCoordinate.add(new Pair<Integer, Integer>(cXPos, cYPos + 1));
		// West
		adjacentCoordinate.add(new Pair<Integer, Integer>(cXPos - 1, cYPos));
		// North East
		adjacentCoordinate.add(new Pair<Integer, Integer>(cXPos + 1, cYPos - 1));
		// South East
		adjacentCoordinate.add(new Pair<Integer, Integer>(cXPos + 1, cYPos + 1));
		// South West
		adjacentCoordinate.add(new Pair<Integer, Integer>(cXPos - 1, cYPos + 1));
		// North West
		adjacentCoordinate.add(new Pair<Integer, Integer>(cXPos - 1, cYPos - 1));
		for (Pair<Integer, Integer> pairI : adjacentCoordinate) {
			int xCoor = pairI.getKey();
			int yCoor = pairI.getValue();
			// if cell is valid, add to the surrounding list
			if (isValidCell(xCoor, yCoor)) {
				adjacentList.add(getCellByCoordinate(xCoor, yCoor));
			}
		}
		return adjacentList;
	}

	// remove sunk ship from list of ship	
	public void removeShipFromArmy(Ship removedShip) {
		shipArmy.remove(removedShip);
	}

}