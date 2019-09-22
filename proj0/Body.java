/** NBody Simulation
  * This is the body class that stores the info of a body,
  * and that does the calculations under Newtonian physics.
  * author: Hsingyi Lin
  * date:   09/20/2019
  */

public class Body {
	public static final double G = 6.67e-11;
	
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;
	
	public Body(double xP, double yP, double xV,
            double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	
	public Body(Body b) {
		this.xxPos = b.xxPos;
		this.yyPos = b.yyPos;
		this.xxVel = b.xxVel;
		this.yyVel = b.yyVel;
		this.mass = b.mass;
		this.imgFileName = b.imgFileName;
	}
	
	/** Return the distance between the two bodies. */
	public double calcDistance(Body target) {
		return Math.sqrt(Math.pow((target.xxPos - this.xxPos), 2)
				+ Math.pow((target.yyPos - this.yyPos), 2));
	}
	
	/** Return the force exerted on this body by the given body. */
	public double calcForceExertedBy(Body target) {
		return G * this.mass * target.mass / Math.pow(calcDistance(target), 2);
	}
	
	/** Return the force exerted in the X direction by the given body. */
	public double calcForceExertedByX(Body target) {
		return calcForceExertedBy(target) 
				* (target.xxPos - this.xxPos)
				/ calcDistance(target);
	}

	/** Return the force exerted in the Y direction by the given body. */
	public double calcForceExertedByY(Body target) {
		return calcForceExertedBy(target) 
				* (target.yyPos - this.yyPos)
				/ calcDistance(target);
	}
	
	/** Return net X force exerted by all given bodies upon this Body. */
	public double calcNetForceExertedByX(Body[] targets) {
		double netForceX = 0.0;
		for (Body target : targets) {
			if (!this.equals(target)) {
				netForceX += calcForceExertedByX(target);
			}
		}
		return netForceX;
	}
	
	/** Return net Y force exerted by all given bodies upon this Body. */
	public double calcNetForceExertedByY(Body[] targets) {
		double netForceY = 0.0;
		for (Body target : targets) {
			if (!this.equals(target)) {
				netForceY += calcForceExertedByY(target);
			}
		}
		return netForceY;
	}
	
	public void update(double second, double forceX, double forceY) {
		double acceX = forceX / mass;
		double acceY = forceY / mass;
		xxVel += second * acceX;
		yyVel += second * acceY;
		xxPos += second * xxVel;
		yyPos += second * yyVel;
	}

	public void draw() {
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}
