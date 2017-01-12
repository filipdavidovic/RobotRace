
package robotrace;

import java.lang.Math.*;

/**
 * Implementation of RaceTrack, creating a track from a parametric formula
 */
public class ParametricTrack extends RaceTrack {
    
    @Override
    protected Vector getPoint(double t) {
        float xCoord = 10 * ( (float) Math.cos(2*Math.PI*t) );
        float yCoord = 14 * ( (float) Math.sin(2*Math.PI*t) );
        return new Vector(xCoord, yCoord, 1);

    }

    @Override
    protected Vector getTangent(double t) {
        float xCoord = -20 * ( (float) Math.PI) * ( (float) Math.sin(2*Math.PI*t));
        float yCoord = 28 * ( (float) Math.PI) * ( (float) Math.cos(2*Math.PI*t));
        return new Vector(xCoord, yCoord, 0);

    }
    
}
