package robotrace;

import static java.lang.Math.*;

/**
 * Implementation of a camera with a position and orientation. 
 */
class Camera {

    /** The position of the camera. */
    public Vector eye = new Vector(3f, 6f, 5f);

    /** The point to which the camera is looking. */
    public Vector center = Vector.O;

    /** The up vector. */
    public Vector up = Vector.Z;

    /**
     * Updates the camera viewpoint and direction based on the
     * selected camera mode.
     */
    public void update(GlobalState gs, Robot[] robots, RaceTrack track) {

        switch (gs.camMode) {
            
            // First person mode    
            case 2:
                setFirstPersonMode(gs, robots[0], track);
                break;
                
            // Helicopter mode
            case 1:
                setHelicopterMode(gs, robots, track);
                break;
            // Default mode    
            default:
                setDefaultMode(gs);
        }
    }

    /**
     * Computes eye, center, and up, based on the camera's default mode.
     */
    private void setDefaultMode(GlobalState gs) {
        float eyeX = gs.vDist * ( (float) cos(gs.theta) ) * ( (float) cos(gs.phi) ) + (float) gs.cnt.x();
        float eyeY = gs.vDist * ( (float) sin(gs.theta) ) * ( (float) cos(gs.phi) ) + (float) gs.cnt.y();
        float eyeZ = gs.vDist * ( (float) sin(gs.phi) ) + (float) gs.cnt.z();
        
        eye = new Vector(eyeX, eyeY, eyeZ);
        center = gs.cnt;       
        up = Vector.Z;
    }

    /**
     * Computes eye, center, and up, based on the first person mode.
     * The camera should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, Robot focus, RaceTrack track) {
        double position = focus.getPosition();
        Vector positionVec = track.getPoint(position);
        eye = positionVec.add(new Vector(0, 0, 2.0f));
        // eye = positionVec.add(track.getTangent(position)).add(new Vector(0, 0, 2.0f));
        center = eye.add(track.getTangent(position).scale(5.0));
        up = Vector.Z;
    }
    
    private void setHelicopterMode(GlobalState gs, Robot[] robots, RaceTrack track) {
        double x=0;
        double y=0;
        double z=0;
        
        for(Robot robot : robots) {
            double positionParametric = robot.getPosition();
            x += track.getPoint(positionParametric).x();
            y += track.getPoint(positionParametric).y();
            z += track.getPoint(positionParametric).z();
        }
        
        x = x/robots.length;
        y = y/robots.length;
        z = z/robots.length;
        
        Vector position = new Vector(x, y, z);
        
        eye = new Vector(x, y, z + 20);
        center = position;
        up = track.getTangent(track.findT(x, y));
    }
}
