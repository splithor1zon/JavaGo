package sk.hor1zon.javago.game;

public enum ViewAction {
	PLACE(), PLACEKO(), PLACEBATCH(), ALERT(), UPDATE_TIME(), UPDATE_PRISONERS(), SWITCH_PLAYER(), NEGOTIATE(), FINISH();
	
	private Object content;
	
	ViewAction() {
		this.content = null;
	}
	
	public Object getContent() {
		return content;
	}
	
	public void setContent(Object content) {
		this.content = content;
	}
}
