package sk.hor1zon.javago.utils;

public class Exmpl_Runnable implements Runnable {
	private final String id;
	
	public Exmpl_Runnable(String id) {
		this.id = id;
	}

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			System.out.println(i);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.err.println("Interrupted.");
			}
		}
	}

}
