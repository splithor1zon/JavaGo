package sk.hor1zon.javago.game;

public class GameStep {

	Stone placedStone;
	boolean isOffer;
	int[] offer;
	boolean accept;
	
	GameStep(Stone placedStone) {
		this.placedStone = placedStone;
		this.isOffer = false;
		this.offer = null;
		this.accept = false;
	}
	
	GameStep(boolean accept, int[] offer) {
		this.placedStone = null;
		this.isOffer = true;
		this.offer = offer;
		this.accept = accept;
	}

}
