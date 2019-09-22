/** NBody Simulation
  * This program is a simple Newtonian physics simulation.
  * author: Hsingyi Lin
  * date:   09/20/2019
  */

import java.util.Scanner;

public class NBody {
	
	public static double readRadius(String filename) {
		/* Start reading in the file. */
		In in = new In(filename);
		
		/* Read the first integer that represents the number of planets. */
		in.readInt();

		/* Return the value of the radius of the universe. */
		return in.readDouble();
	}
	
	public static Body[] readBodies(String filename) {
		/* Start reading in the file. */
		In in = new In(filename);
		/* Get the first integer that represents the number of planets. */
		int n = in.readInt();

		/* Initialize the array of nth Bodies. */
		Body[] planets = new Body[n];
		
		/* Read the value of the radius of the universe. */
		in.readDouble();
		
		/* Keep looking until the file is empty. */
		for (int i = 0; i < n; i++) {
			/* Each line has the x- and y-coordinates of the
			 * initial position, the x- and y-components of 
			 * the initial velocity; the mass and the name of 
			 * an image file used to display the planets. */
			double xxPos = in.readDouble();
			double yyPos = in.readDouble();
			double xxVel = in.readDouble();
			double yyVel = in.readDouble();
			double mass = in.readDouble();
			String imgFileName = in.readString();
			planets[i] = new Body(xxPos, yyPos, xxVel, 
								  yyVel, mass, imgFileName);
		}
		return planets;
	}
	
	public static void drawUniverse(Body[] planets) {
		/* Clears the drawing window. */
		StdDraw.clear();
		
		/* Draw the background picture. */
		StdDraw.picture(0, 0, "images/starfield.jpg");
		
		/* Draw the planets. */
		for (int i = 0; i < planets.length; i++) {
			planets[i].draw();
		}
		
		/* Shows the drawing to the screen, and waits 2000 milliseconds. */
		StdDraw.show();
		StdDraw.pause(1);		
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		double T = in.nextDouble();
		double dt = in.nextDouble();
		String filename = in.next();
		double radius = readRadius(filename);
		Body[] planets = readBodies(filename);
		
		/* Enables double buffering.
		 * A animation technique where all drawing takes place on the offscreen canvas.
		 * Only when you call show() does your drawing get copied from the
		 * offscreen canvas to the onscreen canvas, where it is displayed
		 * in the standard drawing window. */
		StdDraw.enableDoubleBuffering();
		
		/* Sets up the universe so it goes from
		 * -100, -100 up to 100, 100 */
		StdDraw.setScale(-radius, radius);
		
		drawUniverse(planets);
		
		for (double time = 0; time < T; time += dt) {
			/* Compute the net X- and Y-Force for each planet. */
			double[] xForces = new double[planets.length];
			double[] yForces = new double[planets.length];
			for (int i = 0; i < planets.length; i++) {
				xForces[i] = planets[i].calcNetForceExertedByX(planets);
				yForces[i] = planets[i].calcNetForceExertedByY(planets);
			}
			/* Update each planet's position and velocity. */
			for (int i = 0; i < planets.length; i++) {
				planets[i].update(dt, xForces[i], yForces[i]);
			}				
			drawUniverse(planets);
		}
		
		/* Print out the final state. */
		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i++) {
		    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		    		planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
		    		planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
		}
	}
}
// test simulation parameters:
// 157788000.0 25000.0 data/planets.txt
