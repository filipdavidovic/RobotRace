// vertex shader for terrain water surface

varying vec3 N;
varying vec3 P;

void main()
{
    P = vec3(gl_ModelViewMatrix * gl_Vertex); //position of vertex in view coordinates
    N = gl_NormalMatrix * gl_Normal; //position of the normal in view coordinates

    gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;      // model view transform
    gl_FrontColor = gl_Color;
    gl_TexCoord[0] = gl_MultiTexCoord0;
}
