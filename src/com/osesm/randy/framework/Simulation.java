package com.osesm.randy.framework;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.osesm.randy.framework.gl.ShaderCompiler;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;

public abstract class Simulation extends Activity implements Renderer {

	private static final String TAG = "RandyLab";
	
	enum SimulationState {
		Initialized, Running, Paused, Finished, Idle
	}

	private Scene scene;
	private FileIO fileIO;
	private GLSurfaceView glView;

	Object stateChanged = new Object();
	SimulationState state = SimulationState.Initialized;

	long startTime = System.nanoTime();
	private ShaderCompiler shaderCompiler;
	
	public void debug(String message) {
		Log.d(TAG, message);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		glView = new GLSurfaceView(this);
		glView.setEGLContextClientVersion(2);
		glView.setRenderer(this);
		
		setContentView(glView);

		fileIO = new FileIO(getAssets());
		shaderCompiler = new ShaderCompiler(getAssets());
	}

	@Override
	protected void onPause() {
		synchronized (stateChanged) {
			if (isFinishing())
				state = SimulationState.Finished;
			else
				state = SimulationState.Paused;
			while (true) {
				try {
					stateChanged.wait();
					break;
				} catch (InterruptedException e) {
				}
			}
		}

		glView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		glView.onResume();
	}

	@Override
	public void onDrawFrame(GL10 unUsed) {
		SimulationState state = null;
		synchronized (stateChanged) {
			state = this.state;
		}
		if (state == SimulationState.Running) {
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			scene.update(deltaTime);
			scene.present(deltaTime);
		}
		if (state == SimulationState.Paused) {
			scene.pause();
			synchronized (stateChanged) {
				this.state = SimulationState.Idle;
				stateChanged.notifyAll();
			}
		}
		if (state == SimulationState.Finished) {
			scene.pause();
			scene.dispose();
			synchronized (stateChanged) {
				this.state = SimulationState.Idle;
				stateChanged.notifyAll();
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		scene.changed(width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 unUsed, EGLConfig config) {
		synchronized (stateChanged) {
			if (state == SimulationState.Initialized)
				scene = getStartScene();
			state = SimulationState.Running;
			scene.resume();
			startTime = System.nanoTime();
		}
	}

	public FileIO getFileIO() {
		return fileIO;
	}
	
	public ShaderCompiler getShaderCompiler() {
		return shaderCompiler;
	}

	public void setScene(Scene scene) {
		if (scene == null) {
			throw new IllegalArgumentException("Scene must not be null");
		}

		this.scene.pause();
		this.scene.dispose();
		scene.resume();
		scene.update(0);
		this.scene = scene;

	}

	public Scene getCurrentScene() {
		return scene;
	}

	public abstract Scene getStartScene();
}
