package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;
import com.jogamp.opengl.util.texture.Texture;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
abstract class RaceTrack {
    
    /** The width of one lane. The total width of the track is 4 * laneWidth. */
    protected final static float laneWidth = 1.22f;    
    
    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }


    
    /**
     * Draws this track, based on the control points.
     */
    abstract public void draw(GL2 gl, GLU glu, GLUT glut); 
    
    public void setMaterial(Material material, GL2 gl) {
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_AMBIENT, material.ambient, 0);
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, material.diffuse, 0);
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_SPECULAR, material.specular, 0);
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_SHININESS, material.shininess, 0);
    }
    
    /**
     * Returns the center of a lane at 0 <= t < 1.
     * Use this method to find the position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t){

        return Vector.O;

    }
    
    /**
     * Returns the tangent of a lane at 0 <= t < 1.
     * Use this method to find the orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t){
        
        return Vector.O;

    }
    
     abstract protected Vector[][] precalculate();
     
     abstract protected Vector[][] precalculateSidewallInner();
     
     abstract protected Vector[][] precalculateSidewallOuter();
    
    // Returns a point on the test track at 0 <= t < 1.
    protected abstract Vector getPoint(double t);

    // Returns a tangent on the test track at 0 <= t < 1.
    protected abstract Vector getTangent(double t);
    
    // Returns the starting position of all 4 robots
    public abstract Vector[] getStartingPositions();
    
    // find parameter t, with respect to x and y
    public abstract double findT(double x, double y);
    
    // Calculates a point on the Bezier Curve segment defined by four control points
    public static Vector getCubicBezierPnt(double t, Vector P0, Vector P1, Vector P2, Vector P3) {
        return P0.scale(Math.pow(1 - t, 3)).add(
               P1.scale(3 * t * Math.pow(1 - t, 2))).add(
               P2.scale(3 * Math.pow(t, 2) * (1 - t))).add(
               P3.scale(Math.pow(t, 3)));
    }
    
    // Calculates the tangent on a Bezier curve segment defined by four control points
    public static Vector getCubicBezierTng(double t, Vector P0, Vector P1, Vector P2, Vector P3) {
        return P1.subtract(P0).scale(Math.pow(1 - t, 2)).add(
               P2.subtract(P1).scale(2 * t * (1 - t))).add(
               P3.subtract(P2).scale(Math.pow(t, 2))).scale(3);
    }
}
