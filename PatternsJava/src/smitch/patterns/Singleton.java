package smitch.patterns;

/**
 * Singleton Pattern
 * For use with lazily load objects by using 'double-checked locking' to reduce
 * cost of synchronized locking.
 * 
 * BTW was going to make this a generic class but:
 * Because there is only one copy per generic class at runtime, static variables 
 * are shared among all the instances of the class, regardless of their type parameter. 
 * Consequently the type parameter cannot be used in the declaration of static 
 * variables or in static methods.
 * From: Wikipedia Generics_in_java
 * 
 * @author Steven
 *
 */
public class Singleton {

	private volatile static Singleton theInstance;

	private Singleton() 
	{
		//Something to slow down instantiation to enable thread test to fail.
		final String[] junk = new String[1];  
	}

	public static Singleton getInstance() throws InterruptedException {
		
		if (theInstance == null) //fast check for need of instantiation
		{
			synchronized (Singleton.class) { //Lock calls
				if (theInstance == null) {  //Check instantiation need again
					theInstance = new Singleton(); //Instantiate.
				}
			}

		}

		return theInstance;

		//Comment above and uncomment below to have getInstanceIn3Threads to fail.
//		
//		 if (theInstance == null) { theInstance = new Singleton(); count++; }
//		 return theInstance;
//		 

	}

}

