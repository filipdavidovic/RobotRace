
package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Implementation of RaceTrack, creating a track from control points for a 
 * cubic Bezier curve
 */
public class BezierTrack extends RaceTrack {
    
    private Vector[] controlPoints; // control points 
    private int M; // number of segments
    private final int N = 64;
    
    BezierTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints;
        M = (controlPoints.length - 1)/3;
    }
    
    @Override
    protected Vector getPoint(double t) {
        double p = (t * M) % 1; // segment 
        
        int i = 3 * (int) (t * M); // the first point of the segment
        
        return this.getCubicBezierPnt(p, controlPoints[i], controlPoints[i + 1], controlPoints[i + 2], controlPoints[i + 3]);

    }

    @Override
    protected Vector getTangent(double t) {
        double p = t * M;
        
        int i = 3 * (int) (t * M);
        return this.getCubicBezierTng(p, controlPoints[i], controlPoints[i + 1], controlPoints[i + 2], controlPoints[i + 3]);

    }

    @Override
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        Vector[][] quadsTrack = this.precalculate();
        
        Textures.track.enable(gl);
        Textures.track.bind(gl);
        
        // setMaterial(Material.WHITE, gl);
        
        gl.glBegin(GL2.GL_QUADS);
        
        for(int i=0;i<quadsTrack.length;i++) {
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f((float) quadsTrack[i][0].x(), (float) quadsTrack[i][0].y(), (float) quadsTrack[i][0].z());
            
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f((float) quadsTrack[i][1].x(), (float) quadsTrack[i][1].y(), (float) quadsTrack[i][1].z());
            
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f((float) quadsTrack[i][2].x(), (float) quadsTrack[i][2].y(), (float) quadsTrack[i][2].z());
            
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f((float) quadsTrack[i][3].x(), (float) quadsTrack[i][3].y(), (float) quadsTrack[i][3].z());
        }
        gl.glEnd();
        
        Textures.track.disable(gl);
    }

    @Override
    protected Vector[][] precalculate() {
        float du = 1.0f/N;
        float u;
        float u2;
        
        Vector[][] ret = new Vector[N][4];
        for(int i=0;i<ret.length;i++) {
            u = i * du;
            u2 = (i+1) * du;
            
            Vector centerlinePoint = this.getPoint(u);
            Vector centerlineOrigin = new Vector(centerlinePoint.x(), centerlinePoint.y(), 0);
            centerlineOrigin = centerlineOrigin.normalized();
            
            Vector centerlinePoint2 = this.getPoint(u2);
            Vector centerlineOrigin2 = new Vector(centerlinePoint2.x(), centerlinePoint2.y(), 0);
            centerlineOrigin2 = centerlineOrigin2.normalized();
            
            ret[i][0] = new Vector(centerlinePoint.x() + (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() + (centerlineOrigin.y() * 2 * laneWidth), 1.0f);
            ret[i][1] = new Vector(centerlinePoint.x() - (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() - (centerlineOrigin.y() * 2 * laneWidth), 1.0f);
            
            ret[i][2] = new Vector(centerlinePoint2.x() - (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() - (centerlineOrigin2.y() * 2 * laneWidth), 1.0f);
            ret[i][3] = new Vector(centerlinePoint2.x() + (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() + (centerlineOrigin2.y() * 2 * laneWidth), 1.0f);
        }
        return ret;
    }

    @Override
    protected Vector[][] precalculateSidewallInner() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Vector[][] precalculateSidewallOuter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector[] getStartingPositions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public double findT(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
