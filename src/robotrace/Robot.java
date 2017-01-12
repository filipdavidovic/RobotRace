package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;

/**
* Represents a Robot, to be implemented according to the Assignments.
*/
class Robot {
    
    /** The position of the robot. */
    public Vector position = new Vector(0, 0, 0);
    
    /** The direction in which the robot is running. */
    public Vector direction = new Vector(1, 0, 0);

    /** The material from which this robot is built. */
    private final Material material;
    
    

    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material
            
    ) {
        this.material = material;

        
    }

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        gl.glPushMatrix();
            gl.glTranslated(0, 0, 1.08);
            drawTorso(gl, glut);
            gl.glTranslated(0, 0, 0.05);
            drawHead(gl, glut);
        gl.glPopMatrix();
    }
    
    private void drawTorso(GL2 gl,GLUT glut) {
        gl.glScaled(0.32, 0.32, 0.72);
        glut.glutSolidCube(1);
        gl.glScaled(3.125, 3.125, 1.39);
    }
    
    private void drawHead(GL2 gl, GLUT glut) {
        gl.glColor3f(125, 125, 125);
        glut.glutSolidCylinder(0.08, 0.1, 10, 10);
        gl.glTranslated(0, 0, 0.13);
        gl.glScaled(0.15, 0.15, 0.15);
        glut.glutSolidCube(1);
        gl.glScaled(6.67, 6.67, 6.67);
    }
}
