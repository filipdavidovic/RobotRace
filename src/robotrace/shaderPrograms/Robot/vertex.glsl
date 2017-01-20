// robot vertex shader
/*
void main()
{
    gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;      // model view transform
    gl_FrontColor = gl_Color;
    gl_TexCoord[0] = gl_MultiTexCoord0;
}
*/

varying vec3 N; // to be used in fragment shader
varying vec3 P; //postion in view space
uniform bool useTex;

void main()
{
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;      // model view transform
    gl_FrontColor = gl_Color;
    // gl_TexCoord[0] = gl_MultiTexCoord0;
    N = gl_NormalMatrix*gl_Normal;
    P = vec3(gl_ModelViewMatrix * gl_Vertex);
}
