package smitch.patternsTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
//import java.lang.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

//import smitch.patterns.*;

public class SingletonTest {
	static boolean threadsTestRun = false;
	
	@Before
	public void init()
	{
		if(!threadsTestRun) synchronization();
	}

	/* Constructor Tests */
	@Test
	public void synchronization()
	{
		//Wrapper method so that test runs once only.
		if(!threadsTestRun) getSynchronizationInstanceIn3Threads();
	}
	
	public void getSynchronizationInstanceIn3Threads() {
		int threadCount = 3;
		threadsTestRun = true;

		ThreadSingleton[] ats = new ThreadSingleton[threadCount];

		// initialize all
		for (int i = 0; i < threadCount; i++) {
			ats[i] = new ThreadSingleton(i);
		}
		
		System.out.println("All Singleton threads initialized.");

		// start all
		for (int i = 0; i < threadCount; i++) {
			ats[i].start();
		}
		
		System.out.println("All Singleton threads started.");

		// Wait until all have initialized;
		boolean waitLonger = true;
		while (waitLonger) {
			
			waitLonger = false;
			
			for (int i = 0; i < threadCount; i++) {
				if (ats[i].Instance == null)
				{
					waitLonger = true;
					
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					break;
				}
			}
		}
		
		System.out.println("All Singleton threads have an Instance object.");

		// test to see if any had concurrency issues
		for (int i = 0; i < threadCount; i++) 
		{
			for (int j = i + 1; j < threadCount; j++) 
			{
				assertEquals(ats[i].Instance, ats[j].Instance);
			}
		}

	}	

	@Test
	public void DefaultConstructorIsPrivate() throws NoSuchMethodException, SecurityException {
		@SuppressWarnings("rawtypes")
		Constructor constructor = smitch.patterns.Singleton.class.getDeclaredConstructor();
		assertTrue("Constructor is not private", Modifier.isPrivate(constructor.getModifiers()));
	}

	@Test
	public void getInstanceIsNotNull() throws InterruptedException {
		assertNotEquals(null, smitch.patterns.Singleton.getInstance());
	}

	@Test
	public void getInstanceTwiceIsSame() throws InterruptedException {
		assertEquals(smitch.patterns.Singleton.getInstance(), smitch.patterns.Singleton.getInstance());
	}

//	/**
//	 * IMPORTANT *
//	 * This test must be run first to successfully test a static Singleton class.
//	 */
//	@Test
	class ThreadSingleton extends Thread {
		private Thread t;
		private int id;
		public smitch.patterns.Singleton Instance;

		@SuppressWarnings("unused")
		private ThreadSingleton() {
		}

		public ThreadSingleton(int id) {
			this.id = id;
		}

		public void start() {
			if (t == null) {
				t = new Thread(this, "Thread-" + id);
				t.start();
			}
		}

		public void run() {
			try {
				for (int i = 10; i > 0; i--) {
					if (this.Instance == null)
					{
						this.Instance = smitch.patterns.Singleton.getInstance();
					}
					else
					{
						System.out.println("Instance created successfully: " + t.getName()); 
						return;
					}
					
					Thread.sleep(10);
				}
			} catch (InterruptedException e) {
				e.getMessage();
			}
		}
	}

}
