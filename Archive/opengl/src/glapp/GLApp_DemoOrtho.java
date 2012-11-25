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
public class GLApp_DemoOrtho extends GLApp {
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
        GLApp_DemoOrtho demo = new GLApp_DemoOrtho();
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
        rotation += .5f;

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
            GL11.glRotatef(-90, 1, 0, 0);       // rotate around X axis
            GL11.glRotatef(rotation, 0, 0, 1);  // rotate around Y axis
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, sphereTextureHandle); // activate texture
            renderSphere();                     // draw the sphere
        }
        GL11.glPopMatrix();

        // Turn off lighting so image will draw as-is, not lit
        GL11.glDisable(GL11.GL_LIGHTING);

        // drawQuad() and drawCursor call setOrthoOn() to render in 2D mode
        // then call setOrthoOff() to return to previous state (perspective).

        // Overlay Image: draw image in 2D (ortho mode) as a texture on a quad.
        float zoom = ((float)cursorX/getWidth()) * 2f;   // mouse motion scales the image
        drawQuad(overlayTextureHandle,                      // image will be texture on a square
                (int)((getWidth()/2)-((overlayImage.w*zoom)/2)),  // center image
                (int)((getHeight()/2)-((overlayImage.h*zoom)/2)),
                (overlayImage.w*zoom),                   // scale width and height
                (overlayImage.h*zoom) );

        // rectangle
        setColor(.7f, .6f, .5f, 1f);
        setLineWidth(2);
        drawRect(10,10,550,40);
        print(15,22,"Use Ortho mode to render in 2D over the scene.");
        
        // draw the cursor as a 2D quad on top of scene
        drawCursor(cursorTextureHandle);

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