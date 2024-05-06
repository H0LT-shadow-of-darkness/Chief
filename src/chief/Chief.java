package chief;
import org.lwjgl.glfw.GLFW;

import engine.graphics.Material;
import engine.graphics.Mesh;
import engine.graphics.Renderer;
import engine.graphics.Shader;
import engine.graphics.Vertex;
import engine.io.Input;
import engine.io.ModelLoader;
import engine.io.Window;
import engine.maths.Vector2f;
import engine.maths.Vector3f;
import engine.objects.Camera;
import engine.objects.RendObject;
import engine.utils.ColorConverter;
import engine.utils.ConfigReader;
public class Chief implements Runnable {
	public Thread rend;
	public Window window;
	public Renderer renderer;
	public Shader shader;
	public Mesh mesh;
	public RendObject[] objects;
	public RendObject object; 	
	public Camera camera; 
	
	//config⬇
	public final String configPath = "config/config.json";
	
	public final int WIDTH, HEIGHT; 
	public final int n_Objects; 
	public final boolean multiObject; 
	public final String model ;
	public final int personCamera; 
	//config⬆
	
	public Chief() {
		String[] config = ConfigReader.readJson(configPath);
		this.WIDTH = Integer.parseInt(config[0]);
		this.HEIGHT = Integer.parseInt(config[1]);
		this.n_Objects = Integer.parseInt(config[2]);
		this.multiObject = Boolean.parseBoolean(config[3]);
		this.model = config[4];
		this.personCamera = Integer.parseInt(config[5]);
	}
	
	public void start() {
		rend = new Thread(this, "Renderer");
		rend.start();
	}
	
	public void init() {
		if(model.equals("cube")) {
			cubeExample();
		}else{
			mesh = ModelLoader.loadModel("resources/models/"+model+".obj", "resources/textures/"+model+".png");
		}
		
		object = new RendObject(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), mesh);
		camera =  new Camera(new Vector3f(0, 0, 1), new Vector3f(0, 0, 0));
		window = new Window(WIDTH, HEIGHT, "Renderer");
		shader = new Shader("resources/shaders/mainVertex.glsl", "resources/shaders/mainFragment.glsl");
		renderer = new Renderer(window, shader);
		window.setBackgroundColor(ColorConverter.convert(128), ColorConverter.convert(128), ColorConverter.convert(128));
		window.create();
		mesh.create();
		shader.create();
		
		if(multiObject) {
			multiObjectInit();
		}
		
	}
	
	public void run() {
		init();
		while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			update();
			render();
			if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());
			if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) window.mouseState(true);
		}
		close();
	}
	
	private void update() {
		window.update();
		chooseCamera(personCamera);
	}
	
	

	private void render() {
		multiObjectRend();
		renderer.render(object, camera);
		window.swapBuffers();
	}
	
	private void close() {
		window.destroy();
		mesh.destroy();
		shader.destroy();
	}
	
	private void cubeExample() {
		mesh = new Mesh(new Vertex[] {
				//Back face
				new Vertex(new Vector3f(-0.5f,  0.5f, -0.5f), new Vector2f(0.0f, 0.0f)),
				new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector2f(0.0f, 1.0f)),
				new Vertex(new Vector3f( 0.5f, -0.5f, -0.5f), new Vector2f(1.0f, 1.0f)),
				new Vertex(new Vector3f( 0.5f,  0.5f, -0.5f), new Vector2f(1.0f, 0.0f)),
				
				//Front face
				new Vertex(new Vector3f(-0.5f,  0.5f,  0.5f), new Vector2f(0.0f, 0.0f)),
				new Vertex(new Vector3f(-0.5f, -0.5f,  0.5f), new Vector2f(0.0f, 1.0f)),
				new Vertex(new Vector3f( 0.5f, -0.5f,  0.5f), new Vector2f(1.0f, 1.0f)),
				new Vertex(new Vector3f( 0.5f,  0.5f,  0.5f), new Vector2f(1.0f, 0.0f)),
				
				//Right face
				new Vertex(new Vector3f( 0.5f,  0.5f, -0.5f), new Vector2f(0.0f, 0.0f)),
				new Vertex(new Vector3f( 0.5f, -0.5f, -0.5f), new Vector2f(0.0f, 1.0f)),
				new Vertex(new Vector3f( 0.5f, -0.5f,  0.5f), new Vector2f(1.0f, 1.0f)),
				new Vertex(new Vector3f( 0.5f,  0.5f,  0.5f), new Vector2f(1.0f, 0.0f)),
				
				//Left face
				new Vertex(new Vector3f(-0.5f,  0.5f, -0.5f), new Vector2f(0.0f, 0.0f)),
				new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector2f(0.0f, 1.0f)),
				new Vertex(new Vector3f(-0.5f, -0.5f,  0.5f), new Vector2f(1.0f, 1.0f)),
				new Vertex(new Vector3f(-0.5f,  0.5f,  0.5f), new Vector2f(1.0f, 0.0f)),
				
				//Top face
				new Vertex(new Vector3f(-0.5f,  0.5f,  0.5f), new Vector2f(0.0f, 0.0f)),
				new Vertex(new Vector3f(-0.5f,  0.5f, -0.5f), new Vector2f(0.0f, 1.0f)),
				new Vertex(new Vector3f( 0.5f,  0.5f, -0.5f), new Vector2f(1.0f, 1.0f)),
				new Vertex(new Vector3f( 0.5f,  0.5f,  0.5f), new Vector2f(1.0f, 0.0f)),
				
				//Bottom face
				new Vertex(new Vector3f(-0.5f, -0.5f,  0.5f), new Vector2f(0.0f, 0.0f)),
				new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector2f(0.0f, 1.0f)),
				new Vertex(new Vector3f( 0.5f, -0.5f, -0.5f), new Vector2f(1.0f, 1.0f)),
				new Vertex(new Vector3f( 0.5f, -0.5f,  0.5f), new Vector2f(1.0f, 0.0f)),
				}, new int[] {
					//Back face
					0, 1, 3,	
					3, 1, 2,	
					
					//Front face
					4, 5, 7,
					7, 5, 6,
					
					//Right face
					8, 9, 11,
					11, 9, 10,
					
					//Left face
					12, 13, 15,
					15, 13, 14,
					
					//Top face
					16, 17, 19,
					19, 17, 18,
					
					//Bottom face
					20, 21, 23,
					23, 21, 22
				}, new Material("resources/textures/cube.png"));
	}
	private void multiObjectInit() {
		objects =  new RendObject[n_Objects];
		objects[0] = object;
		for (int i = 0; i < objects.length; i++) {
			objects[i] = new RendObject(new Vector3f((float) (Math.random() * 50 - 25), (float) (Math.random() * 50 - 25), (float) (Math.random() * 50 - 25)), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), mesh);
		}
	}
	private void chooseCamera(int personCamera) {
		if(personCamera == 1) {
			camera.update();	
		}
		else {
			camera.update(object);
		}
	}
	private void multiObjectRend() {
		if(multiObject) {
			for (int i = 0; i < objects.length; i++) {
				renderer.render(objects[i], camera);
			}	
		}
	}
}