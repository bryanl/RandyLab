precision highp float;
varying vec2 v_texcoord;

uniform sampler2D n_sampler;

void main() {
	vec4 v_tex = texture2D(n_sampler, v_texcoord);
	if(v_tex.x > .5 && v_tex.y > .5 && v_tex.z > .5)
		discard;
	gl_FragColor = v_tex;
}
