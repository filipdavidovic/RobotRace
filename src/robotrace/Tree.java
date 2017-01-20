/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robotrace;

import javax.media.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Tree {
    private double coneHeight;
    private double coneRadius;
    private double cylHeight;
    private double cylRadius;
    
    private double x, y, z;
    
    Tree(double coneHeight, double coneRadius, double cylHeight, double cylRadius, double x, double y, double z) {
        this.coneHeight = coneHeight;
        this.coneRadius = coneRadius;
        this.cylHeight = cylHeight;
        this.cylRadius = cylRadius;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void draw(GL2 gl, GLUT glut) {
        gl.glPushMatrix();
        
        gl.glTranslated(this.x, this.y, this.z);
        
        gl.glColor3f(102, 51, 0);
        glut.glutSolidCylinder(this.cylRadius, this.cylHeight, 10, 10);
        gl.glTranslated(0, 0, this.cylHeight);
        
        gl.glColor3f(0, 150, 0);
        glut.glutSolidCone(this.coneRadius, this.coneHeight, 10, 10);
        gl.glTranslated(0, 0, -this.cylHeight);
        
        gl.glTranslated(-this.x, -this.y, -this.z);
        
        gl.glPopMatrix();
    }
}
