uniform mat4 uMVPMatrix;
attribute vec4 aPosition;

varying vec4 DestinationColor;

void main() {
	DestinationColor = vec4(0, 0, 0, 1);
	gl_Position = uMVPMatrix * aPosition;
}
