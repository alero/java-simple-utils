package test.org.hrodberaht.inject.testservices.annotated;

import javax.inject.Inject;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-maj-29 18:00:28
 * @version 1.0
 * @since 1.0
 */
public class Volvo implements Car{

    @Inject Tire spareTire;

    @Inject Tire frontLeft;
    @Inject Tire frontRight;
    @Inject Tire backRight;
    @Inject Tire backLeft;

    @Inject VindShield vindShield;

    @Override
    public String brand() {
        return "volvo";
    }

    public Tire getSpareTire() {
        return spareTire;
    }

    public Tire getFrontLeft() {
        return frontLeft;
    }

    public Tire getFrontRight() {
        return frontRight;
    }

    public Tire getBackRight() {
        return backRight;
    }

    public Tire getBackLeft() {
        return backLeft;
    }

    public VindShield getVindShield() {
        return vindShield;
    }
}
