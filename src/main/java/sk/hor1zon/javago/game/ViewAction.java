package sk.hor1zon.javago.game;

/**
 * Enum for specifying actions executed by view.
 * @author splithor1zon
 *
 */
public enum ViewAction {
	PLACE(), PLACEKO(), PLACEBATCH(), ALERT(), UPDATE_TIME(), UPDATE_PRISONERS(), SWITCH_PLAYER(), NEGOTIATE(), FINISH();
	
	private Object content;
	
	ViewAction() {
		this.content = null;
	}
	
	/**
	 * Get contents of this enum instance.
	 * @return
	 */
	public Object getContent() {
		return content;
	}
	
	/**
	 * Set contents of this enum instance.
	 * @param content
	 */
	public void setContent(Object content) {
		this.content = content;
	}
}
