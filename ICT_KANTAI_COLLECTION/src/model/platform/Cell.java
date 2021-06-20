package model.platform;

import java.awt.image.BufferedImage;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.system.GameConfig;
import model.unit.warship.Ship;
import model.utils.ColorCollection;
import model.utils.ImageCollection;
import model.utils.SoundCollection;

public class Cell extends Rectangle {
	// data for processing
	private int xPosition;
	private int yPosition;
	private boolean fired = false;
	private boolean sunk = false;
	private Ship curShip;
	private Board board;
	private long closestTime = System.currentTimeMillis();

	// data for presenting
	private Paint curPaint;
	private Paint curPaintBorder;
	private Paint shipImage;
	private BufferedImage explosionFrame;
	private boolean lockAnimation;
	private AnimationTimer seaAnimationTimer;
	private AnimationTimer explosionAnimationTimer;
	private int rowPicSea = 0, colPicSea = 0;
	private int rowPicExplosion = 0, colPicExplosion = 0;

	// constructor for battlefield cell
	Cell(Board board, int xPosition, int yPosition) {
		super(GameConfig.CELL_WIDTH, GameConfig.CELL_HEIGHT);
		this.board = board;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		// store original color for cell
		storeNewColor(ColorCollection.WATERBLUE.getRGBColor(), ColorCollection.WATERBORDER.getRGBColor());
		// show stored color
		showStoredPaint();
		// store explosion animation
		explosionFrame = ImageCollection.INSTANCE.waterExplosionFrames;
	}

	// constructor for ruler cell
	Cell() {
		super(GameConfig.CELL_WIDTH, GameConfig.CELL_HEIGHT);
		// store original color for cell
		storeNewColor(ColorCollection.RULER.getRGBColor(), ColorCollection.RULERBORDER.getRGBColor());
		// show stored color
		showStoredPaint();
	}

	public int getXPosition() {
		return xPosition;
	}

	public int getYPosition() {
		return yPosition;
	}

	public boolean isFired() {
		return fired;
	}

	public boolean isBlank() {
		return curShip == null;
	}

	public boolean isSunk() {
		return sunk;
	}

	public Ship getShip() {
		return curShip;
	}

	public Board getBoard() {
		return board;
	}

	public void setShip(Ship curShip, Paint shipImage) {
		this.curShip = curShip;
		curPaint = shipImage;
		this.shipImage = shipImage;
		setFill(curPaint);
		explosionFrame = ImageCollection.INSTANCE.shipExplosionFrames;
	}
	
	public void setShip(Ship curShip) {
		this.curShip = curShip;
	}

	// use when ship is sunk, surrounding cells will be terminated
	public void terminateCell() {
		if (curShip == null) {
			fired = true;
			stopSeaAnimation();
			storeNewColor(ColorCollection.GRAY.getRGBColor(), ColorCollection.WATERBORDER.getRGBColor());
			showStoredPaint();			
		}
	}
	
	public void terminateCellSimple() {
		if (curShip == null) {
			fired = true;
		}
	}


	// use when player shoot at a cell
	public boolean fireCell() {
		fired = true;
		stopSeaAnimation();
		// miss
		if (curShip == null) {
			SoundCollection.INSTANCE.playMissSFX();
			showExplosion();
			return false;
		}
		// hit ship
		SoundCollection.INSTANCE.playHitSFX();
		if (curShip.damage()) {
			// if ship is sunk
			sunk = true;
//			SoundCollection.INSTANCE.playShipExploSFX();
		} else {
			// if ship still survives
			storeNewColor(ColorCollection.RED.getRGBColor(), ColorCollection.WATERBORDER.getRGBColor());
			showStoredPaint();
		}
		return true;
	}
	
	public boolean fireCellSimple() {
		fired = true;
		// miss
		if (curShip == null) {
			return false;
		}
		// hit ship
		if (curShip.damageSimple()) {
			// if ship is sunk
			sunk = true;
		} 
		
		return true;
	}
	
	public boolean fireCellTest() {

		// miss
		if (curShip == null) {
			return false;
		}
		
		// hit ship
		return true;
	}

	//
	// helper method: presenting cell properties
	//

	// show color to fxml
	public void showColor(Color newColor) {
		if (!lockAnimation) {
			setFill(newColor);
		}
	}

	public void showColor(Color colorFill, Color colorBorder) {
		if (!lockAnimation) {
			setFill(colorFill);
			setStroke(colorBorder);
		}
	}

	public void showColorBorder(Color colorBorder) {
		if (!lockAnimation) {
			setStroke(colorBorder);
		}
	}

	// show stored paint to fxml
	public void showStoredPaint() {
		if (!lockAnimation) {
			setFill(curPaint);
			setStroke(curPaintBorder);			
		}
	}
	
	public void revealShipCell() {
		if (!lockAnimation) {
			curPaint = shipImage;
			setFill(curPaint);
		}
	}

	// store new color for cell
	public void storeNewColor(Color newColor, Color newColorBorder) {
		if (!lockAnimation) {
			curPaint = newColor;
			curPaintBorder = newColorBorder;
		}
	}

	// create sea animation and start the animation
	public void setSeaAnimation() {
		BufferedImage seaImage = ImageCollection.INSTANCE.seaFrames;
		int frameWidth = seaImage.getWidth() / 3, frameHeight = seaImage.getHeight() / 4;
		seaAnimationTimer = new AnimationTimer() {
			@Override
			public void handle(long frame) {
				if (System.currentTimeMillis() - closestTime > 500) {
					setFill(new ImagePattern(SwingFXUtils.toFXImage(seaImage.getSubimage(frameWidth * colPicSea, frameHeight * rowPicSea,
							frameWidth, frameHeight), null)));
					colPicSea++;
					rowPicSea++;
					if (colPicSea == 3)
						colPicSea = 0;
					if (rowPicSea == 4)
						rowPicSea = 0;
					closestTime = System.currentTimeMillis();
				}
			}
		};
		seaAnimationTimer.start();
	}

	// show explosion when miss / sunk the ship
	public void showExplosion() {
		lockAnimation = true;
		int divWidth, divHeight;
		// get water explosion image if player shoot the water
		// else get ship explosion image
		if (curShip == null) {
			divWidth = 3;
			divHeight = 2;
		} else {
			divWidth = 3;
			divHeight = 3;
		}
		int frameWidth = explosionFrame.getWidth() / divWidth, frameHeight = explosionFrame.getHeight() / divHeight;
		explosionAnimationTimer = new AnimationTimer() {
			@Override
			public void handle(long frame) {
				if (System.currentTimeMillis() - closestTime > 500) {
					setFill(new ImagePattern(SwingFXUtils.toFXImage(explosionFrame.getSubimage(frameWidth * colPicExplosion,
							frameHeight * rowPicExplosion, frameWidth, frameHeight), null)));
					colPicExplosion++;
					rowPicExplosion++;
					if (colPicExplosion == divHeight || rowPicExplosion == divWidth) {
						explosionAnimationTimer.stop();
						Platform.runLater(() -> {
							lockAnimation = false;
							if (curShip == null) {
								storeNewColor(ColorCollection.GRAY.getRGBColor(),
										ColorCollection.WATERBORDER.getRGBColor());
								showStoredPaint();								
							} else {
								revealShipCell();
							}
						});

					}
					closestTime = System.currentTimeMillis();
				}
			}
		};
		explosionAnimationTimer.start();
	}

	public void startSeaAnimation() {
		if (seaAnimationTimer != null) {
			seaAnimationTimer.stop();
			seaAnimationTimer.start();
		}
	}

	public void stopSeaAnimation() {
		if (seaAnimationTimer != null)
			seaAnimationTimer.stop();
		Platform.runLater(() -> {
			showStoredPaint();
		});
	}

	public void showAppropriateView() {
		if (!lockAnimation) {
			showStoredPaint();
			if (!sunk) {
				if (!fired) {
					startSeaAnimation();
					BufferedImage seaImage = ImageCollection.INSTANCE.seaFrames;
					int frameWidth = seaImage.getWidth() / 3, frameHeight = seaImage.getHeight() / 4;
					BufferedImage subSeaImage = seaImage.getSubimage(frameWidth * colPicSea, frameHeight * rowPicSea,
							frameWidth, frameHeight);
					ImagePattern seaPattern = new ImagePattern(SwingFXUtils.toFXImage(subSeaImage, null));
					setFill(seaPattern);
				} else {
					showStoredPaint();
				}
			}
		}
	}
	
	public void setFired() {
		fired = true;
	}

	//
	// helper method: handle action for cell
	//

	public void setMouseEvtHandler(EventHandler<MouseEvent> mouseEnteredHandler,
			EventHandler<MouseEvent> mouseExitedHandler, EventHandler<MouseEvent> mouseClickHandler) {
		setOnMouseEntered(mouseEnteredHandler);
		setOnMouseExited(mouseExitedHandler);
		setOnMouseClicked(mouseClickHandler);
	}

}
