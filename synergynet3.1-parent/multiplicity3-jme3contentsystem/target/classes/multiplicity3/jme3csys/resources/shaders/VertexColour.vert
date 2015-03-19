uniform mat4 g_WorldViewProjectionMatrix;
attribute vec3 inPosition;
attribute vec4 inColor;
varying vec4 vertColor;

void main(){
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
    vertColor = inColor;
}
