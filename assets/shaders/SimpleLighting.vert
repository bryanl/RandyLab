attribute vec4 aPosition; 
attribute vec3 aNormal; 
attribute vec3 aDiffuseMaterial;

uniform mat4 uMVPMatrix;
uniform mat3 uNormalMatrix;
uniform vec3 uLightPosition;
uniform vec3 uAmbientMaterial;
uniform vec3 uSpecularMaterial;
uniform float uShininess;

varying vec4 vDestinationColor;

void main() {
    vec3 N = uNormalMatrix * aNormal;
    vec3 L = normalize(uLightPosition);
    vec3 E = vec3(0, 0, 1);
    vec3 H = normalize(L + E);
    
    float df = max(0.0, dot(N, L));
    float sf = max(0.0, dot(N, H));
    sf = pow(sf, uShininess);
    
    vec3 color = uAmbientMaterial + df * aDiffuseMaterial + sf * uSpecularMaterial;
    
    vDestinationColor = vec4(color, 1);
    gl_Position = uMVPMatrix * aPosition;
}


