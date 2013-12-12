package mpis.domain;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class MovingObjectTest {

	@Test
	public void testID() {
		MovingObject mo = new MovingObject("test");
		assertEquals("test",mo.getID());
	}
	
	@Test
	public void testOnPointQueries() throws InterruptedException {
		MovingObject mo = new MovingObject("test");
		//Start at (0,0,0)
		mo.setPosition(new GPSPosition(0,0,0));
		//Wait for 10 milliseconds
		Thread.sleep(10);
		//Move to (100,100,100), then wait another 10 milliseconds
		mo.setPosition(new GPSPosition(100,100,100));
		Thread.sleep(10);
		
		GPSPosition thePoint = new GPSPosition(0,0,0);
		for (long i = 0; i < 20; i++) {
			if (mo.getPosition(i) != null) {
				thePoint = mo.getPosition(i);
			}
		}
		
		//Confirm that the point is at (100,100,100) at t=13.
		GPSPosition gp13 = mo.getPosition((long) 13);
		System.out.println("At t=13, position is "+gp13);
		GPSPosition whereItShouldBe = new GPSPosition(100,100,100);
		assertEquals(gp13, whereItShouldBe);
		
		//Check to see the time given for (100,100,100)
		//It is difficult to test this b/c the system can easily vary
		//by a millisecond or two.
		System.out.println("It is at (100,100,100) at time "+mo.getDate(whereItShouldBe));
		mo.printLines();
	}
	
	@Test
	public void testManyPoints() throws InterruptedException {
		System.out.println("\nTest 2");
		MovingObject mo = new MovingObject("test");
		Thread.sleep(10);
		mo.setPosition(new GPSPosition(0,0,0));
		Thread.sleep(10);
		mo.setPosition(new GPSPosition(100,0,0));
		Thread.sleep(20);
		mo.setPosition(new GPSPosition(100,100,0));
		Thread.sleep(20);
		mo.setPosition(new GPSPosition(100,100,100));
		Thread.sleep(20);
		mo.setPosition(new GPSPosition(0,0,0));
		
		GPSPosition location1 = new GPSPosition(50,0,0);
		Long t1 = mo.getDate(location1) - mo.getInitialTime();
		assertTrue(t1>=15 && t1<=19);
		assertTrue(mo.getDate(new GPSPosition(104,0,0))==null);
		assertTrue(mo.getDate(new GPSPosition(103,0,0)) - mo.getInitialTime() ==
				   mo.getDate(new GPSPosition(100,0,0)) - mo.getInitialTime());
		Long t2 = mo.getDate(new GPSPosition(100,10,0)) - mo.getInitialTime();
		assertTrue(t2>=22 && t2<27);
		Long t3 = mo.getDate(new GPSPosition(100,100,96)) - mo.getInitialTime();
		assertTrue(t3>60&&t3<=68);
		System.out.println(mo.getDate(new GPSPosition(0,0,0)) - mo.getInitialTime());
	}
	
	@Test
	public void testGPSDevice() throws InterruptedException {
		System.out.println("\nTest 3");
		MovingObject mo = new MovingObject("test");
		mo.start(10000);
		mo.stop();
		for (long i=0; i<=10000; i+=1000){
			System.out.println(mo.getPosition(i));
		}
		boolean benIsAwesome = true;
		assertTrue(benIsAwesome);
	}
	
	@Test
	public void testUncertainty() throws InterruptedException {
		System.out.println("\nTest 4");
		MovingObject mo = new MovingObject("test");
		mo.setPosition(new GPSPosition(0,0,0));
		Thread.sleep(100);
		mo.setPosition(new GPSPosition(100,0,0));
		Long t = mo.getDate(new GPSPosition(50,2,0)) - mo.getInitialTime();
		assertTrue(50<=t && t<=52);
		assertTrue(mo.getDate(new GPSPosition(105,0,0)) == null);
		Long t1 = mo.getDate(new GPSPosition(-1,-1,0)) - mo.getInitialTime();
		assertTrue(t1<=1);
	}
}
