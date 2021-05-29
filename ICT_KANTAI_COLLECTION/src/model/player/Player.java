package model.player;


import java.util.List;
import model.platform.Board;
import model.utilities.Ammo;

public class Player {
	private String name;
	private Board board;
	private List<Ammo> ammoCollection; 
	
	public String getPlayerName() {
		return name;
	}
	
	public Board getBoard() {
		return board;
	}

	public List<Ammo> getAmmoCollection() {
		return ammoCollection;
	}
	
	public Player(String name) {
		this.name = name;
	}

	public Player(String name, Board board) {
		this(name);
		this.board = board;
	}

	public Player(String name, Board board, List<Ammo> ammoCollection) {
		this(name, board);
		this.ammoCollection = ammoCollection;
	}
	
	
}
