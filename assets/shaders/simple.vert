uniform mat4 uMVPMatrix;
attribute vec4 aPosition;
attribute vec4 aSourceColor;

varying vec4 DestinationColor;

void main() {
	DestinationColor = aSourceColor;
	gl_Position = uMVPMatrix * aPosition;
}
