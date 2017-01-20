package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.imageio.ImageIO;

/**
 * Represents the terrain, to be implemented according to the Assignments.
 */
class Terrain {

    int displayList;
    private static final Random random = new Random();
    private final Tree[] trees = new Tree[random.nextInt(10) + 1];
    BufferedImage heightmap = null;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
    
    public Terrain() {
        for(int i=0;i<trees.length;i++) {
            double coneHeight = random.nextDouble() * 2.0 + 1.0;
            double coneRadius = coneHeight / 3.0;
            double cylHeight = random.nextDouble() * 0.5 + 0.4;
            double cylRadius = cylHeight / 3.5;
            
            double x, y, z;
            
            do {
                x = random.nextInt(40) - 20.0;
                y = random.nextInt(40) - 20.0;
            } while( Math.abs(x) <= 11 || Math.abs(y) <= 15 || height(x, y) <= 0.5);
            
            z = height(x, y);
            
            trees[i] = new Tree(coneHeight, coneRadius, cylHeight, cylRadius, x, y, z);
        }
        
        try {
            heightmap = ImageIO.read(new File("/home/ficabj5/data/School/2nd Quartile/Computer Graphics/Homework Assingments/Assignment 2 - Robot Race/RobotRace/src/robotrace/res/heightmap.png"));
        } catch(IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        // if(displayList == 0) { // if display list is not generated, generate one, and store the draw procedure in it
            // displayList = gl.glGenLists(1);
            // gl.glNewList(displayList, gl.GL_COMPILE_AND_EXECUTE);
            
            // draw the terrain
            Vector normal;
            float delta = 0.3f;

            // setMaterial(Material.WHITE, gl);
            
            float height;
            
            ShaderPrograms.terrainShader.setUniform(gl, "useTex", 1);
            
            Textures.terrain.enable(gl);
            Textures.terrain.bind(gl);
            
            for (float x = -20.0f; x<20.0f; x+=delta) {
                gl.glBegin(gl.GL_TRIANGLE_STRIP);
                for (float y = -20.0f; y<=20.0f; y+=delta) {
                    height = height(x, y);
                    /*if(height <= 0.0f) {
                        gl.glColor3f(0.2f, 0.6f, 1.0f);
                    } else if(height <= 0.5f) {
                        gl.glColor3f(0.95f, 0.87f, 0.13f);
                    } else {
                        gl.glColor3f(0.0f, 0.8f, 0.0f);
                    }*/
                    normal = getNormal(x,y);
                    gl.glNormal3f((float)normal.x(),(float)normal.y(),(float)normal.z());
                    gl.glTexCoord2f(height/2.0f + 1.0f/2.0f, 1.0f);
                    gl.glVertex3f(x, y, height);

                    height = height(x+delta, y);
                    /*if(height <= 0.0f) {
                        gl.glColor3f(0.2f, 0.6f, 1.0f);
                    } else if(height <= 0.5f) {
                        gl.glColor3f(0.95f, 0.87f, 0.13f);
                    } else {
                        gl.glColor3f(0.0f, 0.8f, 0.0f);
                    }*/
                    normal = getNormal(x+delta, y);
                    gl.glNormal3f((float) normal.x(), (float)normal.y(),(float)normal.z());
                    gl.glTexCoord2f(height/2.0f + 1.0f/2.0f, 1.0f);
                    gl.glVertex3f(x+delta,y,height);

                }
                gl.glEnd();
            }    
            
            Textures.terrain.disable(gl);
            
            ShaderPrograms.terrainShader.setUniform(gl,"useTex", 0);
            
            // draw water
            gl.glColor4d(0.5,0.5,0.7,0.5);
            gl.glNormal3f(0,0,1f);
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex3f(-20,-20,0);
            gl.glVertex3f(-20,20,0);
            gl.glVertex3f(20,20,0);
            gl.glVertex3f(20,-20,0);
            gl.glEnd();

            
            // draw the trees
            for(int i=0; i<trees.length; i++) {
                trees[i].draw(gl, glut);
            }
            

            // close the display list
            // gl.glEndList();
        // } else { // if display list is initialized, call it
           // gl.glCallList(displayList);
        // }
    }
 
    public static float height(double x, double y) {
        /*
        int a = (int) ((5 * x) + 100);
        int b = (int) ((5 * y) + 100);
        float ret = heightmap.getRGB(a, b);
        ret += MAX_PIXEL_COLOR/2.0f;
        ret /= MAX_PIXEL_COLOR/2.0f;
        return ret;
        */
        return ((float) ( 0.6 * Math.cos(0.3 * x + 0.2 * y) + 0.4 * Math.cos(x - 0.5 * y) ));
    }
    
    private Vector getNormal(float x, float y) {
        Vector xTangent = new Vector(1.0, 0.0, -0.3*0.6 * Math.sin(0.3 * x + 0.2 * y) - 0.4 * Math.sin(x - 0.5 * y));
        Vector yTangent = new Vector(0.0, 1.0, -0.2*0.6* Math.sin(0.3 * x + 0.2 * y) + 0.2 * Math.sin(x - 0.5 * y));
        
        Vector normalRet = xTangent.cross(yTangent);
        return normalRet.normalized();
    }
    
    private void drawTree(GL2 gl, GLUT glut) {
        
    }
    
    public void setMaterial(Material material, GL2 gl) {
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_AMBIENT, material.ambient, 0);
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_DIFFUSE, material.diffuse, 0);
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_SPECULAR, material.specular, 0);
        gl.glMaterialfv(gl.GL_FRONT_AND_BACK, gl.GL_SHININESS, material.shininess, 0);
    }
}