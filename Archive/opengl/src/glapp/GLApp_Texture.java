package glapp;


import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;
import glapp.*;

/**
 * Demonstrates 'ortho' mode by drawing a flat 2D image over a 3D scene. Switches
 * between ortho and perspective modes to render both 3D and 2D.  Also draws a crosshair
 * image at the cursor position, another use of ortho mode.
 * <P>
 * Move mouse to scale overlay image.
 * <P>
 * GLApp initializes the LWJGL environment for OpenGL rendering,
 * ie. creates a window, sets the display mode, inits mouse and keyboard,
 * then runs a loop that calls draw().
 * <P>
 * napier at potatoland dot org
 */
public class GLApp_Texture extends GLApp {
    // Handle for texture
    int sphereTextureHandle = 0;
    // Lighting colors
    float faWhite[]      = { 1.0f, 1.0f, 1.0f, 1.0f };
    float faLightBlue[]  = { 0.8f, 0.8f, .9f, 1f };
    // Light position: if last value is 0, then this describes light direction.  If 1, then light position.
    float lightPosition[]= { -4f, 3f, 3f, 1.0f };
    // World coordinates of sphere
    float[] spherePos = {0f, 0f, 0f};
    // Rotation of sphere
    float rotation = 0f;
    // image to draw as 2D overlay over 3D scene, and texture handle for it
    GLImage overlayImage;
    int overlayTextureHandle = 0;
    // cursor is a texture drawn onto a quad
    int cursorTextureHandle = 0;

	/**
	 * Start the application.  demo.run() calls setup(), handles mouse and keyboard input,
	 * and calls draw() in a loop.
	 */
    public static void main(String args[]) {
        GLApp_Texture demo = new GLApp_Texture();
        demo.window_title = "GLApp Ortho Demo";
        demo.hideNativeCursor = true;        
        demo.displayWidth = 800;
        demo.displayHeight = 600;
        demo.run();  // will call init(), render(), mouse functions
    }

    /**
     * Initialize the scene.  Called by GLApp.run()
     */
    public void setup()
    {
        // Setup and enable perspective. Scene will be rendered in 3D.
        setPerspective();

        // Create a light (diffuse light, ambient light, position)
        setLight( GL11.GL_LIGHT1, faWhite, faWhite, faLightBlue, lightPosition );

        // enable lighting and texture rendering
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Enable alpha transparency (for overlay image)
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        sphereTextureHandle = makeTexture("images/eye.jpg");

        // Load a cursor image and make it a texture
        cursorTextureHandle = makeTexture("images/cursorCrosshair32.gif");

        // Load the image for 2D overlay and make it a texture
        overlayImage = loadImage("images/frame_ornate3.gif");
        overlayTextureHandle = makeTexture(overlayImage);
    }

    /**
     * set the camera position, field of view, depth.
     */
    public static void setPerspective()
    {
        // select projection matrix (controls perspective)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        // fovy, aspect ratio, zNear, zFar
        GLU.gluPerspective(30f, aspectRatio, 1f, 100f);
        // return to modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * Render one frame.  Called by GLApp.run().
     */
    public void render() {
        // clear depth buffer and color
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // select model view for subsequent transforms
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // set the viewpoint
        GLU.gluLookAt(0f, 0f, 15f, // where is the eye
        			0f, 0f, 0f,    // what point are we looking at
        			0f, 1f, 0f);   // which way is up

        // draw sphere at center, in perspective
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(spherePos[0], spherePos[1], spherePos[2]);
//            GL11.glRotatef(-90, 1, 0, 0);       // rotate around X axis
//            GL11.glRotatef(rotation, 0, 0, 1);  // rotate around Y axis
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, sphereTextureHandle); // activate texture
            renderPlane(2.0f, 1);
        }
        GL11.glPopMatrix();

        // Turn off lighting so image will draw as-is, not lit
        GL11.glDisable(GL11.GL_LIGHTING);

        // turn lighting back on
        GL11.glEnable(GL11.GL_LIGHTING);
    }


    public void mouseMove(int x, int y) {
    }

    public void mouseDown(int x, int y) {
    }

    public void mouseUp(int x, int y) {
    }

    public void keyDown(int keycode) {
    }

    public void keyUp(int keycode) {
    }
}