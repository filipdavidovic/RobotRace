// terrain fragment shader

uniform sampler2D tex;
uniform bool useTex;

void main()
{
    if(useTex) {
      gl_FragColor = texture2D(tex, gl_TexCoord[0].st);
    } else {
      gl_FragColor = gl_Color;
    }
}
