package sk.hor1zon.javago.utils;

public class LazySingleton {
	// volatile synchronizácia vnút. cache procesora
	private static volatile LazySingleton singleton = null;

	private LazySingleton() {

	}

	// synchronizing across threads
	public static LazySingleton getSingleton() {
		if (singleton == null) {
			synchronized (LazySingleton.class) {
				if (singleton == null) {
					singleton = new LazySingleton();
				}
			}
		}
		return singleton;
	}

	public void doIt() {
		System.out.println("Hello");
	}
	
	//+ factory pattern common intf creates specified class...
}
