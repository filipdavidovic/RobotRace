
package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import java.lang.Math.*;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static robotrace.RaceTrack.laneWidth;

/**
 * Implementation of RaceTrack, creating a track from a parametric formula
 */
public class ParametricTrack extends RaceTrack {
    
    private final static int N=64;
    
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
        return new Vector(xCoord, yCoord, 0).normalized();
    }
    
    @Override
    public Vector[] getStartingPositions() {
        Vector[] tempRet = new Vector[4];
        Vector firstPoint = this.getPoint(0);
        double i = -1.5;
        for(Vector vec : tempRet) {
            vec = new Vector( firstPoint.x() + ( i * (double) laneWidth ), 0.0, 1.0);
            i++;
        }
        return tempRet;
    }
    
    @Override
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        Vector[][] quadsTrack = this.precalculate();
        Vector[][] quadsWallInner = this.precalculateSidewallInner();
        Vector[][] quadsWallOuter = this.precalculateSidewallOuter();
        
        // Textures.track.enable(gl);
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
        
        // Textures.track.disable(gl);
        
        // Textures.brick.enable(gl);
        Textures.brick.bind(gl);
        
        gl.glBegin(gl.GL_QUADS);
        
        for(int i=0;i<quadsWallInner.length;i++) {
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f((float) quadsWallInner[i][0].x(), (float) quadsWallInner[i][0].y(), (float) quadsWallInner[i][0].z());
            
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f((float) quadsWallInner[i][1].x(), (float) quadsWallInner[i][1].y(), (float) quadsWallInner[i][1].z());
            
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f((float) quadsWallInner[i][2].x(), (float) quadsWallInner[i][2].y(), (float) quadsWallInner[i][2].z());
            
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f((float) quadsWallInner[i][3].x(), (float) quadsWallInner[i][3].y(), (float) quadsWallInner[i][3].z());
        }
        
        for(int i=0;i<quadsWallOuter.length;i++) {
            gl.glTexCoord2f(0.0f, 0.0f);
            gl.glVertex3f((float) quadsWallOuter[i][0].x(), (float) quadsWallOuter[i][0].y(), (float) quadsWallOuter[i][0].z());
            
            gl.glTexCoord2f(1.0f, 0.0f);
            gl.glVertex3f((float) quadsWallOuter[i][1].x(), (float) quadsWallOuter[i][1].y(), (float) quadsWallOuter[i][1].z());
            
            gl.glTexCoord2f(1.0f, 1.0f);
            gl.glVertex3f((float) quadsWallOuter[i][2].x(), (float) quadsWallOuter[i][2].y(), (float) quadsWallOuter[i][2].z());
            
            gl.glTexCoord2f(0.0f, 1.0f);
            gl.glVertex3f((float) quadsWallOuter[i][3].x(), (float) quadsWallOuter[i][3].y(), (float) quadsWallOuter[i][3].z());
        }
        
        // Textures.brick.disable(gl);

        gl.glEnd();
        
    }
    
    @Override
    protected Vector[][] precalculate() {
        float du = 1.0f/N;
        float u;
        float u2;
        
        Vector[][] ret = new Vector[N][4];
        double zCoord;
        for(int i=0;i<ret.length;i++) {
            u = i * du;
            u2 = (i+1) * du;
            
            Vector centerlinePoint = this.getPoint(u);
            Vector centerlineOrigin = new Vector(centerlinePoint.x(), centerlinePoint.y(), 0);
            centerlineOrigin = centerlineOrigin.normalized();
            
            Vector centerlinePoint2 = this.getPoint(u2);
            Vector centerlineOrigin2 = new Vector(centerlinePoint2.x(), centerlinePoint2.y(), 0);
            centerlineOrigin2 = centerlineOrigin2.normalized();
            
            zCoord = Math.max(Terrain.height(centerlinePoint.x() + (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() + (centerlineOrigin.y() * 2 * laneWidth)) > 0.0f ? Terrain.height(centerlinePoint.x() + (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() + (centerlineOrigin.y() * 2 * laneWidth)) + 1.0f : 0.0f, Terrain.height(centerlinePoint.x() - (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() - (centerlineOrigin.y() * 2 * laneWidth)) > 0.0f ? Terrain.height(centerlinePoint.x() - (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() - (centerlineOrigin.y() * 2 * laneWidth)) + 1.0f : 0.0f);
            ret[i][0] = new Vector(centerlinePoint.x() + (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() + (centerlineOrigin.y() * 2 * laneWidth), 1.0f);
            ret[i][1] = new Vector(centerlinePoint.x() - (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() - (centerlineOrigin.y() * 2 * laneWidth), 1.0f);
            
            zCoord = Math.max(Terrain.height(centerlinePoint2.x() - (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() - (centerlineOrigin2.y() * 2 * laneWidth)) > 0.0f ? Terrain.height(centerlinePoint2.x() - (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() - (centerlineOrigin2.y() * 2 * laneWidth)) + 1.0f : 0.0f, Terrain.height(centerlinePoint2.x() + (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() + (centerlineOrigin2.y() * 2 * laneWidth)) > 0.0f ? Terrain.height(centerlinePoint2.x() + (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() + (centerlineOrigin2.y() * 2 * laneWidth)) + 1.0f : 0.0f);
            ret[i][2] = new Vector(centerlinePoint2.x() - (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() - (centerlineOrigin2.y() * 2 * laneWidth), 1.0f);
            ret[i][3] = new Vector(centerlinePoint2.x() + (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() + (centerlineOrigin2.y() * 2 * laneWidth), 1.0f);
        }
        return ret;
    }
    
    @Override
    protected Vector[][] precalculateSidewallInner() {
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
            
            ret[i][0] = new Vector(centerlinePoint.x() - (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() - (centerlineOrigin.y() * 2 * laneWidth), 0.0f);
            ret[i][1] = new Vector(centerlinePoint.x() - (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() - (centerlineOrigin.y() * 2 * laneWidth), 1.0f);
            ret[i][2] = new Vector(centerlinePoint2.x() - (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() - (centerlineOrigin2.y() * 2 * laneWidth), 1.0f);
            ret[i][3] = new Vector(centerlinePoint2.x() - (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() - (centerlineOrigin2.y() * 2 * laneWidth), 0.0f);
        }
        return ret;
    }
    
    @Override
    protected Vector[][] precalculateSidewallOuter() {
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
            
            ret[i][0] = new Vector(centerlinePoint.x() + (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() + (centerlineOrigin.y() * 2 * laneWidth), 0.0f);
            ret[i][1] = new Vector(centerlinePoint.x() + (centerlineOrigin.x() * 2 * laneWidth), centerlinePoint.y() + (centerlineOrigin.y() * 2 * laneWidth), 1.0f);
            ret[i][2] = new Vector(centerlinePoint2.x() + (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() + (centerlineOrigin2.y() * 2 * laneWidth), 1.0f);
            ret[i][3] = new Vector(centerlinePoint2.x() + (centerlineOrigin2.x() * 2 * laneWidth), centerlinePoint2.y() + (centerlineOrigin2.y() * 2 * laneWidth), 0.0f);
        }
        return ret;
    }
    
    public double findT(double x, double y) {
        double tX = ((Math.acos(x / 10)) / (2.0 * Math.PI));
        double tY = ((Math.acos(y / 14)) / (2.0 * Math.PI));
        
        if(tX == tY) {
            return tX;
        } else {
            return 0;
        }
    }
}
