package glapp;


import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;
import glapp.*;

/**
 * Use setMaterial(), setLight() and makeTexture() to control light and material properties.
 * <P>
 * napier at potatoland dot org
 */
public class GLApp_DemoLight extends GLApp {
    // Handles for textures
    int marbleTextureHandle = 0;
    int groundTextureHandle = 0;
    // Light position: if last value is 0, then this describes light direction.  If 1, then light position.
    float lightPosition1[]= { 0f, 0f, 0f, 1f };
    float lightPosition2[]= { 0f, -10f, 0f, 0f };
    float lightPosition3[]= { 0f, 0f, 0f, 1f };
    // Rotation of sphere
    float rotation=0f;
    // display lists
    int sphereDL=0, cubeDL=0;

    //------------------------------------------------------------------------
    // Run main loop of application.  Handle mouse and keyboard input.
    //------------------------------------------------------------------------

    public static void main(String args[]) {
    	GLApp_DemoLight demo = new GLApp_DemoLight();
        demo.window_title = "GLApp Simple Scene";
        demo.displayWidth = 800;
        demo.displayHeight = 600;
        demo.run();  // will call init(), render(), mouse event functions
    }

    public boolean initDisplayTESTTEMP() {
        // Initialize the Window
        try {
        	DM = displayMode = new DisplayMode(1440,930);
        	fullScreen = false;
            Display.create(new PixelFormat(0, depthBufferBits, 8));  // set bits per buffer: alpha, depth, stencil
            Display.setTitle(window_title);
            Display.setFullscreen(fullScreen);
            Display.setVSyncEnabled(VSyncEnabled);
            Display.setLocation(0,-30);
            msg("GLApp.initDisplay(): Created OpenGL window.");
        }
        catch (Exception exception1) {
            System.err.println("GLApp.initDisplay(): Failed to create OpenGL window: " + exception1);
            System.exit(1);
        }
        // Set viewport width/height and Aspect ratio.
        if (aspectRatio == 0f) {
            // no aspect ratio was set in GLApp.cfg: default to full screen.
            aspectRatio = (float)DM.getWidth() / (float)DM.getHeight(); // calc aspect ratio of display
        }
        // viewport may not match shape of screen.  Adjust to fit.
        viewportH = DM.getHeight();                        // viewport Height
        viewportW = (int) (DM.getHeight() * aspectRatio);  // Width
        if (viewportW > DM.getWidth()) {
            viewportW = DM.getWidth();
            viewportH = (int) (DM.getWidth() * (1 / aspectRatio));
        }
        // center viewport in screen
        viewportX = (int) ((DM.getWidth()-viewportW) / 2);
        viewportY = (int) ((DM.getHeight()-viewportH) / 2);
        return true;
    }


    /**
     * Initialize the scene.  Called by GLApp.run()
     */
    public void setup()
    {
        // Create sphere texture
        marbleTextureHandle = makeTexture("images/marble.jpg");

        // Create texture for ground plane
        groundTextureHandle = makeTexture("images/mahog_texture.jpg");

        // setup perspective
        setPerspective();

        // Create a point light (white)
        setLight( GL11.GL_LIGHT1,
        		new float[] { 1.0f, 1.0f, 1.0f, 1.0f },   // diffuse color
        		new float[] { 0.2f, 0.2f, 0.2f, 1.0f },   // ambient
        		new float[] { 1.0f, 1.0f, 1.0f, 1.0f },   // specular
        		lightPosition1 );                         // position

        // Create a directional light (dark red, to simulate reflection off wood surface)
        setLight( GL11.GL_LIGHT2,
        		new float[] { 0.95f, 0.35f, 0.15f, 1.0f },  // diffuse color
        		new float[] { 0.0f, 0.0f, 0.0f, 1.0f },     // ambient
        		new float[] { 0.03f, 0.0f, 0.0f, 1.0f },     // specular
        		lightPosition2 );   // position (pointing up)

        // Create a point light (dark blue, to simulate reflection off wood surface)
        setLight( GL11.GL_LIGHT3,
        		new float[] { 0.35f, 0.45f, 0.95f, 1.0f },  // diffuse color
        		new float[] { 0.0f, 0.0f, 0.0f, 1.0f },   // ambient
        		new float[] { 0.3f, 0.4f, 0.7f, 1.0f },   // specular
        		lightPosition3 );   // position (pointing up)

        // no overall scene lighting
        setAmbientLight(new float[] { 0.0f, 0.0f, 0.0f, 0.0f });

        sphereDL = beginDisplayList();
        	renderSphere();
        endDisplayList();
        
        cubeDL = beginDisplayList();
        	renderCube(10,40);
        endDisplayList();
        
        // enable lighting and texture rendering
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // select model view for subsequent transforms
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }

    /**
     * Set the camera position, field of view, depth.
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
    public void draw() {
    	// rotate 25 degrees per second
        rotation += 25f * getSecondsPerFrame();

        // clear depth buffer and color buffers
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // select model view for subsequent transforms
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // set the viewpoint
        GLU.gluLookAt(0f, 3f, 15f,  // where is the eye
        			0f, -1f, 0f,    // what point are we looking at
        			0f, 1f, 0f);    // which way is up

        //---------------------------------------------------------------
        // desktop
        //---------------------------------------------------------------
        
        // dark reddish material
        setMaterial( new float[] {0.8f, 0.3f, 0.2f, 1.0f}, .2f); 

        // enable texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, groundTextureHandle);

        // draw the ground plane
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(0f, -7f, 0f); // down a bit
            callDisplayList(cubeDL);
        }
        GL11.glPopMatrix();


        // orbit
        GL11.glRotatef(rotation, 0,1,0);
        GL11.glTranslatef(3,0,0);

        GL11.glRotatef(rotation/2, 0,1,0);
        GL11.glTranslatef(-2,0,0);
        
        
        //---------------------------------------------------------------
        // glowing white ball
        //---------------------------------------------------------------
        
        // for point lights set position each frame so light moves with scene
        // white light at same position as marble ball
        setLightPosition( GL11.GL_LIGHT1, lightPosition1 );
        
        // glowing white material
        setMaterial(
        		new float[] {1.0f, 1.0f, 1.0f, 1.0f},   // diffuse color
        		new float[] {1.0f, 1.0f, 1.0f, 1.0f},   // ambient
        		new float[] {0.0f, 0.0f, 0.0f, 1.0f},   // specular
        		new float[] {1.0f, 1.0f, 1.0f, 1.0f},   // emissive
        		0f);        // not shiny

        // enable texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, marbleTextureHandle);

        // draw marble sphere
        GL11.glPushMatrix();
        {
            GL11.glScalef(.5f, .5f, .5f);    // make it smaller
            callDisplayList(sphereDL);       // draw the sphere display list
        }
        GL11.glPopMatrix();

        
        // orbit
        GL11.glRotatef(rotation*1.4f, 0,1,0);
        GL11.glTranslatef(4,0,0);

        
        //---------------------------------------------------------------
        // shiny blue ball
        //---------------------------------------------------------------
        
        // shiny dark blue material
        setMaterial( new float[] {0.3f, 0.3f, 0.6f, 1.0f}, .9f); 

        // set blue light at same spot as blue sphere
        setLightPosition( GL11.GL_LIGHT3, lightPosition3 );

        // no texture (texture handle 0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        // Draw sphere
        GL11.glPushMatrix();
        {
            GL11.glScalef(1.6f, 1.6f, 1.6f);     // make it smaller
            callDisplayList(sphereDL);
        }
        GL11.glPopMatrix();
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