precision highp float;

attribute vec3 v_pos;
attribute vec2 v_tex;

varying vec2 v_texcoord;

uniform mat4 t_modelview_projection;

uniform vec3 v_up, v_right;

void main() {
    vec3 v_corner = v_pos + (v_tex.x * 2.0 - 1.0) * v_right + (1.0 - v_tex.y * 2.0) * v_up;
    gl_Position = t_modelview_projection * vec4(v_corner, 1.0);

    v_texcoord = v_tex;
}
