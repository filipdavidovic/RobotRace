package robotrace;

import java.io.IOException;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static javax.media.opengl.GL2.*;
import static robotrace.ShaderPrograms.*;

/**
 * Handles all of the RobotRace graphics functionality,
 * which should be extended per the assignment.
 * 
 * OpenGL functionality:
 * - Basic commands are called via the gl object;
 * - Utility commands are called via the glu and
 *   glut objects;
 * 
 * GlobalState:
 * The gs object contains the GlobalState as described
 * in the assignment:
 * - The camera viewpoint angles, phi and theta, are
 *   changed interactively by holding the left mouse
 *   button and dragging;
 * - The camera view width, vWidth, is changed
 *   interactively by holding the right mouse button
 *   and dragging upwards or downwards; (Not required in this assignment)
 * - The center point can be moved up and down by
 *   pressing the 'q' and 'z' keys, forwards and
 *   backwards with the 'w' and 's' keys, and
 *   left and right with the 'a' and 'd' keys;
 * - Other settings are changed via the menus
 *   at the top of the screen.
 * 
 * Textures:
 * Place your "track.jpg", "brick.jpg", "head.jpg",
 * and "torso.jpg" files in the folder textures. 
 * These will then be loaded as the texture
 * objects track, bricks, head, and torso respectively.
 * Be aware, these objects are already defined and
 * cannot be used for other purposes. The texture
 * objects can be used as follows:
 * 
 * gl.glColor3f(1f, 1f, 1f);
 * Textures.track.bind(gl);
 * gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0);
 * gl.glVertex3d(0, 0, 0);
 * gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0);
 * gl.glTexCoord2d(1, 1);
 * gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1);
 * gl.glVertex3d(0, 1, 0);
 * gl.glEnd(); 
 * 
 * Note that it is hard or impossible to texture
 * objects drawn with GLUT. Either define the
 * primitives of the object yourself (as seen
 * above) or add additional textured primitives
 * to the GLUT object.
 */
public class RobotRace extends Base {
    
    /** Array of the four robots. */
    private final Robot[] robots;
    
    /** Instance of the camera. */
    private final Camera camera;
    
    /** Instance of the race track. */
    private final RaceTrack[] raceTracks;
    
    /** Instance of the terrain. */
    private final Terrain terrain;
        
    private Vector[] startingPositions;
    /**
     * Constructs this robot race by initializing robots,
     * camera, track, and terrain.
     */
    public RobotRace() {
        
        // Create a new array of four robots
        robots = new Robot[4];
        
        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD, -1.5f, 0.004f);
        
        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER, -0.5f, 0.003f);
        
        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD, 0.5f, 0.001f);

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE, 1.5f, 0.002f);
        
        // Initialize the camera
        camera = new Camera();
        
        // Initialize the race tracks
        raceTracks = new RaceTrack[2];
        
        // Track 1
        raceTracks[0] = new ParametricTrack();
        
        // Track 2
        float g = 3.5f;
        raceTracks[1] = new BezierTrack(
            new Vector[] {
                new Vector(4, -11, 1),
                new Vector(20 / 3, -11, 1),
                new Vector(28 / 3, -11, 1),
                new Vector(12, -11, 1),
                new Vector(13, -11, 1),
                new Vector(13, -12, 1),
                new Vector(12, -12, 1),
                new Vector(8, -12, 1),
                new Vector(4, -12, 1),
                new Vector(-9, -12, 1),
                new Vector(-13, -12, 1),
                new Vector(-13, -12, 1),
                new Vector(-13, 1, 1),
                new Vector(-13, 5, 1),
                new Vector(-13, 9, 1),
                new Vector(-13, 13, 1),
                new Vector(-13, 14, 1),
                new Vector(-12, 14, 1),
                new Vector(-12, 13, 1),
                new Vector(-12, 10, 1),
                new Vector(-12, 8, 1),
                new Vector(-12, 5, 1),
                new Vector(-12, -11, 1),
                new Vector(-12, -11, 1),
                new Vector(4, -11, 1)
            }
       
        );
        
        // Initialize the terrain
        terrain = new Terrain();
    }
    
    /**
     * Called upon the start of the application.
     * Primarily used to configure OpenGL.
     */
    @Override
    public void initialize() {
		
        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                
        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);
		
        // Enable face culling for improved performance
        // gl.glCullFace(GL_BACK);
        // gl.glEnable(GL_CULL_FACE);
        
	// Normalize normals.
        gl.glEnable(GL_NORMALIZE);
        
        // Converts colors to materials when lighting is enabled.
        gl.glEnable(GL_COLOR_MATERIAL);
        // gl.glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);

        // Enable textures. 
        gl.glEnable(GL_TEXTURE_2D);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
	// Try to load four textures, add more if you like in the Textures class         
        Textures.loadTextures();
        reportError("reading textures");
        
        // Try to load and set up shader programs
        ShaderPrograms.setupShaders(gl, glu);
        reportError("shaderProgram");
    }
   
    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);
        
        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set the perspective.
        glu.gluPerspective(45, (float)gs.w / (float)gs.h, 0.1*gs.vDist, 10*gs.vDist);
        
        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
        
        gl.glEnable(GL_LIGHTING);
        // Set basic light properties of light 0
        final float[] AMBIENT = {0.1f, 0.1f, 0.1f, 1.0f};
        final float[] DIFFUSE = {1.0f, 1.0f, 1.0f, 1.0f};
        final float[] SPECULAR = {1.0f, 1.0f, 1.0f, 1.0f};
        // By positioning the light, relative to origin on the initial MODELVIEW matrix,
        // before the camera is positioned, the light will is positioned relative to the camera.
        // The positional light is set slightly to the left and slightly up (relative to the camera).
        final float[] POSITION = {-0.5f, 0.0f, 0.5f, 1.0f};

        gl.glLightfv(gl.GL_LIGHT0, gl.GL_AMBIENT, AMBIENT, 0);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_DIFFUSE, DIFFUSE, 0);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_SPECULAR, SPECULAR, 0);
        gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, POSITION, 0);
        
        // Update the view according to the camera mode and robot of interest.
        // For camera modes 1 to 4, determine which robot to focus on.
        camera.update(gs, robots, raceTracks[gs.trackNr]);
        glu.gluLookAt(camera.eye.x(),    camera.eye.y(),    camera.eye.z(),
                      camera.center.x(), camera.center.y(), camera.center.z(),
                      camera.up.x(),     camera.up.y(),     camera.up.z());
        
        
    }
    
    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
        
        gl.glUseProgram(defaultShader.getProgramID());
        reportError("program:");
        
        // Background color.
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0f);
        
        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);
        
        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);

        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);
        
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        
        // this.startingPositions = raceTracks[0].getStartingPositions();
        int i = 0;
        //for(Robot robot : robots) {
        //    robot.setPosition(startingPositions[i]);
        //    i++;
        //}
        
        // Draw the axis frame.
        if (gs.showAxes) {
            drawAxisFrame();
        }
        
        gl.glDepthMask(false);
        
        gl.glUseProgram(skyboxShader.getProgramID());
        reportError("skybox:");
        drawSkybox();
        
        gl.glDepthMask(true);
        
        // Draw the race track.
        gl.glUseProgram(trackShader.getProgramID());
        raceTracks[gs.trackNr].draw(gl, glu, glut);
        reportError("track:");
        
        // Draw the (first) robot.
        gl.glUseProgram(robotShader.getProgramID()); 
        for(Robot robot : robots) {
            robot.draw(gl, glu, glut, gs.tAnim);
            Vector newPosition = raceTracks[gs.trackNr].getPoint(robot.getPosition());
            Vector newDirection = raceTracks[gs.trackNr].getTangent(robot.getPosition());
            robot.setPosition(newPosition);
            robot.setDirection(newDirection);
            robot.updatePosition();
        }
        reportError("robot:");
        
        // Draw the terrain.
        gl.glUseProgram(terrainShader.getProgramID());
        terrain.draw(gl, glu, glut);
        reportError("terrain:");
    }
    
    public void drawSkybox() {
        final float D = 100.0f;
        
        // float eyeX = gs.vDist * ( (float) cos(gs.theta) ) * ( (float) cos(gs.phi) ) + (float) gs.cnt.x();
        // float eyeY = gs.vDist * ( (float) sin(gs.theta) ) * ( (float) cos(gs.phi) ) + (float) gs.cnt.y();
        // float eyeZ = gs.vDist * ( (float) sin(gs.phi) ) + (float) gs.cnt.z();
        
        float eyeX = 0;
        float eyeY = 0;
        float eyeZ = 0;
        
        // gl.glPushMatrix();
        
        // gl.glTranslated(eyeX, eyeY, eyeZ);
                
        Textures.skybox.enable(gl);
        Textures.skybox.bind(gl);
        
        gl.glBegin(gl.GL_QUADS);
        
        // left side
        gl.glTexCoord2f(0.0f, 1.0f/3.0f);
        gl.glVertex3f(-D + eyeX, D + eyeY, D + eyeZ);
        gl.glTexCoord2f(0.0f, 2.0f/3.0f);
        gl.glVertex3f(-D + eyeX, -D + eyeY, D + eyeZ);
        gl.glTexCoord2f(0.25f, 2.0f/3.0f);
        gl.glVertex3f(-D + eyeX, -D + eyeY, -D + eyeZ);
        gl.glTexCoord2f(0.25f, 1.0f/3.0f);
        gl.glVertex3f(-D + eyeX, D + eyeY, -D + eyeZ);
        
        // front side
        gl.glTexCoord2f(0.25f, 1.0f/3.0f);
        gl.glVertex3f(-D + eyeX, D + eyeY, -D + eyeZ);
        gl.glTexCoord2f(0.25f, 2.0f/3.0f);
        gl.glVertex3f(-D + eyeX, -D + eyeY, -D + eyeZ);
        gl.glTexCoord2f(0.5f, 2.0f/3.0f);
        gl.glVertex3f(D + eyeX, -D + eyeY, -D + eyeZ);
        gl.glTexCoord2f(0.5f, 1.0f/3.0f);
        gl.glVertex3f(D + eyeX, D + eyeY, -D + eyeZ);
        
        // right side
        gl.glTexCoord2f(0.5f, 1.0f/3.0f);
        gl.glVertex3f(D + eyeX, D + eyeY, D + eyeZ);
        gl.glTexCoord2f(0.5f, 2.0f/3.0f);
        gl.glVertex3f(D + eyeX, -D + eyeY, D + eyeZ);
        gl.glTexCoord2f(0.75f, 2.0f/3.0f);
        gl.glVertex3f(D + eyeX, -D + eyeY, -D + eyeZ);
        gl.glTexCoord2f(0.75f, 1.0f/3.0f);
        gl.glVertex3f(D + eyeX, D + eyeY, -D + eyeZ);
        
        // back side
        gl.glTexCoord2f(0.75f, 1.0f/3.0f);
        gl.glVertex3f(-D + eyeX, D + eyeY, D + eyeZ);
        gl.glTexCoord2f(0.75f, 2.0f/3.0f);
        gl.glVertex3f(-D + eyeX, -D + eyeY, D + eyeZ);
        gl.glTexCoord2f(1.0f, 2.0f/3.0f);
        gl.glVertex3f(D + eyeX, -D + eyeY, D + eyeZ);
        gl.glTexCoord2f(1.0f, 1.0f/3.0f);
        gl.glVertex3f(D + eyeX, D + eyeY, D + eyeZ);
        
        // top side
        gl.glTexCoord2f(0.25f, 0.0f);
        gl.glVertex3f(-D + eyeX, D + eyeY, D + eyeZ);
        gl.glTexCoord2f(0.5f, 0.0f);
        gl.glVertex3f(D + eyeX, D + eyeY, D + eyeZ);
        gl.glTexCoord2f(0.5f, 1.0f/3.0f);
        gl.glVertex3f(D + eyeX, D + eyeY, -D + eyeZ);
        gl.glTexCoord2f(0.25f, 1.0f/3.0f);
        gl.glVertex3f(-D + eyeX, D + eyeY, -D + eyeZ);
        
        // bottom side
        gl.glTexCoord2f(0.25f, 2.0f/3.0f);
        gl.glVertex3f(-D + eyeX, -D + eyeY, D + eyeZ);
        gl.glTexCoord2f(0.5f, 2.0f/3.0f);
        gl.glVertex3f(D + eyeX, -D + eyeY, D + eyeZ);
        gl.glTexCoord2f(0.5f, 1.0f);
        gl.glVertex3f(D + eyeX, -D + eyeY, -D + eyeZ);
        gl.glTexCoord2f(0.25f, 1.0f);
        gl.glVertex3f(-D + eyeX, -D + eyeY, -D + eyeZ);
        
        gl.glEnd();
        
        Textures.skybox.disable(gl);
        
        // gl.glTranslated(-eyeX, -eyeY, -eyeZ);
        
        // gl.glPopMatrix();
    }
    
    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue),
     * and origin (yellow).
     */
    public void drawAxisFrame() {
        gl.glPushMatrix();    
            gl.glTranslated(0, 0, 1);
            gl.glColor3f(255, 255, 0);
            glut.glutSolidSphere(0.15, 6, 6);
            gl.glColor3f(255, 0, 0);
            gl.glRotated(90, 0, 1, 0);
            drawArrow();
            gl.glColor3f(0, 255, 0);
            gl.glRotated(-90, 0, 1, 0);
            gl.glRotated(-90, 1, 0, 0);
            drawArrow();
            gl.glColor3f(0, 0, 255);
            gl.glRotated(90, 1, 0, 0);
            drawArrow();
        gl.glPopMatrix();
    }
    
    /**
     * Draws a single arrow
     */
    public void drawArrow() {  
        gl.glPushMatrix();
            glut.glutSolidCylinder(0.03, 0.8, 3, 5);
            gl.glTranslated(0, 0, 0.8);
            glut.glutSolidCone(0.1, 0.2, 5, 10);
        gl.glPopMatrix();
    }
 
    /**
     * Drawing hierarchy example.
     * 
     * This method draws an "arm" which can be animated using the sliders in the
     * RobotRace interface. The A and B sliders rotate the different joints of
     * the arm, while the C, D and E sliders set the R, G and B components of
     * the color of the arm respectively. 
     * 
     * The way that the "arm" is drawn (by calling {@link #drawSecond()}, which 
     * in turn calls {@link #drawThird()} imposes the following hierarchy:
     * 
     * {@link #drawHierarchy()} -> {@link #drawSecond()} -> {@link #drawThird()}
     */
    
    /**
     * Main program execution body, delegates to an instance of
     * the RobotRace implementation.
     */
    public static void main(String args[]) {
        RobotRace robotRace = new RobotRace();
        robotRace.run();
    }
}
