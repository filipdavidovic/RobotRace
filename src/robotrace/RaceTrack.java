package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
abstract class RaceTrack {
    
    /** The width of one lane. The total width of the track is 4 * laneWidth. */
    private final static float laneWidth = 1.22f;
    
    
    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }


    
    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        double du = 1.0/50.0;
        double u=0;
        
        gl.glFrontFace(GL_CW);
        gl.glBegin(GL2.GL_QUAD_STRIP);
        
        Vector firstPoint = this.getPoint(u);
        Vector firstTangent = this.getTangent(u);
        Vector firstNormal = new Vector(firstTangent.y(), firstTangent.x(), 0);
        firstNormal = firstNormal.normalized();
        
        gl.glVertex3f( (float) ( firstPoint.x() + ( firstNormal.x() * 2 * laneWidth ) ), (float) ( firstPoint.y() + ( firstNormal.y() * 2 * laneWidth ) ), 1.0f);
        gl.glVertex3f( (float) ( firstPoint.x() - ( firstNormal.x() * 2 * laneWidth ) ), (float) ( firstPoint.y() - ( firstNormal.y() * 2 * laneWidth ) ), 1.0f);
        
        for(int i=1;i<51;i++) {
            u = i*du;
            Vector pointTemp = this.getPoint(u);
            Vector tangentTemp = this.getTangent(u);
            Vector normalTemp = new Vector(firstTangent.y(), firstTangent.x(), 0);
            normalTemp = normalTemp.normalized();
            
            gl.glVertex3f( (float) ( pointTemp.x() + ( normalTemp.x() * 2 * laneWidth ) ), (float) ( pointTemp.y() + ( normalTemp.y() * 2 * laneWidth ) ), 1.0f);
            gl.glVertex3f( (float) ( pointTemp.x() - ( normalTemp.x() * 2 * laneWidth ) ), (float) ( pointTemp.y() - ( normalTemp.y() * 2 * laneWidth ) ), 1.0f);
        }
        
        gl.glEnd();
        
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
    
    
    
    // Returns a point on the test track at 0 <= t < 1.
    protected abstract Vector getPoint(double t);

    // Returns a tangent on the test track at 0 <= t < 1.
    protected abstract Vector getTangent(double t);
}
