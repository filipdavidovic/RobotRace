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
    private  double position;
    private Vector positionVec;
    
    /** The direction in which the robot is running. */
    private Vector direction = new Vector(0, 0, 0);
    private double angle;

    /** The material from which this robot is built. */
    private final Material material;
    
    private final double speed;  
    
    private final float laneNumber;
    
    private final float laneWidth = 1.22f;
    
    

    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material, float laneNumber, float speed) {
        this.material = material;
        this.position = 0.0f;
        this.positionVec = Vector.O;
        this.laneNumber = laneNumber;
        this.speed = speed;
    }

    public void setDirection(Vector newDirection) {
        angle = Math.acos(newDirection.dot(Vector.Y));
        this.direction = newDirection;
    }
    
    public void setPosition(Vector newPosition) {
        Vector pointOrigin = newPosition.scale(1).normalized();
        this.positionVec = new Vector(newPosition.x() + (pointOrigin.x() * laneNumber * laneWidth), newPosition.y() + (pointOrigin.y() * laneNumber * laneWidth), newPosition.z());
    }
    
    public double getPosition() {
        return position;
    }
    
    public Vector getDirection() {
        return direction;
    }
    
    public void updatePosition() {
        position += speed;
        position %= 1;
    }
     
    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, material.ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT,GL2.GL_DIFFUSE, this.material.diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, this.material.specular, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, this.material.shininess, 0);

        ShaderPrograms.robotShader.setUniform(gl, "useTex", 0);
        
        gl.glPushMatrix();
            
            gl.glTranslated(this.positionVec.x(), this.positionVec.y(), this.positionVec.z() + 1.3f);
            gl.glRotated(angle, 0, 0, 1);
            
            drawTorso(gl,glut);
            
            gl.glTranslated(-0.9,-0.455,-0.5);
            gl.glScaled(0.6,0.3,1);
                drawTorsoOverlay(gl);
            gl.glScaled(1.0/0.6, 1.0/0.3, 1);
            gl.glTranslated(0.9,0.455,0.5);
            
            gl.glTranslated(0,0,0.5);
                drawNeck(gl,glut);
            gl.glTranslated(0,0,-0.5);
            
            gl.glTranslated(0,0,0.725);
                drawHead(gl,glut);
            gl.glTranslated(0,0,-0.725);
            
            gl.glTranslated(-0.375,-1.129,0.6);
            gl.glScaled(0.25,1,0.25);
                drawFaceOverlay(gl);
            gl.glScaled(4,1,4);
            gl.glTranslated(0.375,1.129,-0.6);
            

            gl.glTranslated(0.4,0,0.4);
            gl.glRotated(Math.sin(tAnim) * 45.0, 1, 0, 0);
                drawArm(gl,glut,tAnim, 0 );
            gl.glRotated(Math.sin(tAnim) * -45.0, 1, 0, 0);
            gl.glTranslated(-0.4,0,-0.4);
            
            gl.glTranslated(-0.4,0,0.4);
            gl.glRotated(Math.sin(tAnim) * -45.0, 1, 0, 0);
                drawArm(gl,glut,tAnim, 2);
            gl.glRotated(Math.sin(tAnim) * 45.0, 1, 0, 0);
            gl.glTranslated(0.4,0,-0.4);
            
            

            gl.glTranslated(0.15,0,-0.5);
            gl.glRotated(Math.sin(tAnim) * -45.0, 1, 0, 0);
                drawLeg(gl,glut,tAnim);
            gl.glRotated(Math.sin(tAnim) * 45.0, 1, 0, 0);
            gl.glTranslated(-0.15,0,0.5);
            
            gl.glTranslated(-0.15,0,-0.5);
            gl.glRotated(Math.sin(tAnim) * 45.0, 1, 0, 0);
                drawLeg(gl,glut,tAnim);
            gl.glRotated(Math.sin(tAnim) * -45.0, 1, 0, 0);
            gl.glTranslated(0.15,0,0.5);
            
            gl.glRotated(-angle, 0, 0, 1);
            gl.glTranslated(-this.positionVec.x(), -this.positionVec.y(), -this.positionVec.z() - 1);
            
        gl.glPopMatrix();
    }
    
    private void drawTorso(GL2 gl,GLUT glut) {
        gl.glScaled(0.6,0.3,1);
        glut.glutSolidCube(1);
        gl.glScaled(10.0/6.0, 10.0/3.0, 1);
    }
    private void drawNeck(GL2 gl,GLUT glut){
        gl.glScaled(0.1,0.1,0.1);
        glut.glutSolidCylinder(1, 1, 25, 25);
        gl.glScaled(10,10,10);
    }
    
    private void drawHead(GL2 gl, GLUT glut) {
        gl.glScaled(0.25, 0.25, 0.25);
        glut.glutSolidCube(1);
        gl.glScaled(4,4,4);
    }
    
    private void drawArm(GL2 gl, GLUT glut, float tAnim, int pos){
        gl.glScaled(0.1,0.1,0.1);
        glut.glutSolidSphere(1, 25, 25);
        gl.glScaled(10,10,10);
        gl.glTranslated(0,0,-0.35);
        drawUpperArm(gl, glut, tAnim, pos);
        gl.glTranslated(0,0,0.35);
        
    }
    
    private void drawUpperArm(GL2 gl, GLUT glut, float tAnim, int pos){
        gl.glScaled(0.05,0.05,0.35);
        glut.glutSolidCylinder(1, 1, 25, 25);
        gl.glScaled(20,20,2.857);
        
        drawLowerArm(gl,glut, tAnim, pos);
    }
    
    private void drawLowerArm(GL2 gl, GLUT glut,float tAnim, int pos ){
        gl.glRotated(-60.0 + (Math.sin(tAnim+pos)*25), 1, 0, 0);
        gl.glScaled(0.05,0.05,0.35);
        glut.glutSolidCylinder(1, -1, 25, 25);
        gl.glScaled(20,20,100.0/35.0);    
        gl.glRotated(60.0 - (Math.sin((tAnim+pos))*25) , 1, 0, 0);
    }
    
    private void drawLeg(GL2 gl, GLUT glut,float tAnim){
        gl.glScaled(0.11,0.11,0.11);
        glut.glutSolidSphere(1, 25, 25);
        gl.glScaled(100.0/11.0, 100.0/11.0, 100.0/11.0);
        
        gl.glTranslated(0,0,-0.5);
        drawUpperLeg(gl,glut,tAnim);
        gl.glTranslated(0,0, 0.5);
    }
    
    private void drawUpperLeg(GL2 gl, GLUT glut,float tAnim){
        gl.glScaled(0.05,0.05, 0.4);
        glut.glutSolidCylinder(1, 1, 25, 25);
        gl.glScaled(20,20,2.5);
        
        gl.glRotated((Math.sin(tAnim*2.0) + 1) * 45.0 , 1, 0, 0);
        drawLowerLeg(gl,glut);
        gl.glRotated((Math.sin(tAnim*2.0) + 1) * -45.0 , 1, 0, 0);
    }
    
    private void drawLowerLeg(GL2 gl, GLUT glut){
        gl.glScaled(0.05,0.05, 0.4);
        glut.glutSolidCylinder(1, -1, 25, 25);
        gl.glScaled(20,20,2.5);
        
        gl.glTranslated(0,0,-0.4);
        drawFoot(gl,glut);
        gl.glTranslated(0,0,0.4);
    }
    
    private void drawFoot(GL2 gl, GLUT glut){
        gl.glScaled(1.5,2,1);
        glut.glutSolidCone(0.1,0.2,10,10);
        gl.glScaled(10.0/15.0,0.5,1);
    }
    
    //adds head texture 
    private void drawFaceOverlay(GL2 gl){
        Textures.head.bind(gl);
        ShaderPrograms.robotShader.setUniform(gl, "useTex", 1);
        gl.glBegin(GL2.GL_QUADS);
        
        gl.glNormal3f(1f,0f,0f);
        
        gl.glTexCoord2f(0f,0f);
        gl.glVertex3f(1f, 1f, 0f);
        
        gl.glTexCoord2f(0f,1f);
        gl.glVertex3f(1f, 1f, 1f);
        
        gl.glTexCoord2f(1f, 1f);
        gl.glVertex3f(2f, 1f, 1f);
        
        gl.glTexCoord2f(1f, 0f);
        gl.glVertex3f(2f, 1f, 0f);
      
        gl.glEnd();
        ShaderPrograms.robotShader.setUniform(gl, "useTex", 0);
    }
    
    //adds torso texture
    private void drawTorsoOverlay(GL2 gl){
        Textures.torso.bind(gl);
        ShaderPrograms.robotShader.setUniform(gl, "useTex", 1);
        gl.glBegin(GL2.GL_QUADS);
        
         gl.glNormal3f(1f,0f,0f);
        
        gl.glTexCoord2f(0f,1f);
        gl.glVertex3f(1f, 1f, 1f);
        
        gl.glTexCoord2f(1f,1f);
        gl.glVertex3f(2f, 1f, 1f);
        
        gl.glTexCoord2f(1f,0f);
        gl.glVertex3f(2f, 1f, 0f);
        
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex3f(1f, 1f, 0f);
        
        gl.glEnd();
        ShaderPrograms.robotShader.setUniform(gl, "useTex", 0);
    }
}
