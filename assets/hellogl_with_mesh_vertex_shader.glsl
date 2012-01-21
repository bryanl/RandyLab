uniform mat4 uMVPMatrix;
attribute vec4 aPosition;
attribute vec4 SourceColor;

varying vec4 DestinationColor;

void main() {
	DestinationColor = SourceColor;
	gl_Position = uMVPMatrix * aPosition;
}
