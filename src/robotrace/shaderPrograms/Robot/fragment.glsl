/*uniform bool useTex;
uniform sampler2D tex;
// varying vec3 N;
// varying vec3 P;


void main()
{

   if(useTex){
         gl_FragColor = texture2D(tex,gl_TexCoord[0].st); // + shading(P,N);
    }else{
         gl_FragColor = gl_Color; // shading(P,N);
    }
}
*/

// gl_LightSourceParameters gl_LightSource[1];
// gl_MaterialParameters gl_FrontMaterial;
uniform bool useTex;
uniform sampler2D tex;
varying vec3 N;
varying vec3 P;


void main()
{
   if(useTex){
         gl_FragColor = texture2D(tex, gl_TexCoord[0].st);
    }else{
         gl_FragColor = gl_Color; // shading(P, N);
    }
}

vec4 shading(vec3 P, vec3 N) {


	vec4 ambient = vec4(0.0, 0.0, 0.0, 0.0); //ambient light contribution
	ambient += ( gl_LightSource[0].ambient);

	vec4 diffuse = vec4(0.0, 0.0, 0.0, 0.0); //diffuse light contribution
	vec3 LP = normalize( gl_LightSource[0].position.xyz - P);
	diffuse += (gl_LightSource[0].diffuse * gl_FrontMaterial.diffuse)
			* max(dot(N, LP), 0.0); //if angle is greater than 90 degrees, use 0

	vec4 specular = vec4(0.0, 0.0, 0.0, 0.0); //specular light contribution
	vec3 C = vec3(0.0, 0.0, 0.0); //camera position in view space
	vec3 CP = normalize(C-P); //vector from vertex to camera
	vec3 R = reflect(-LP, N); //reflected ray of light
	specular += (gl_LightSource[0].specular * gl_FrontMaterial.specular)
				*pow(max(dot(normalize(R), CP), 0.0), //if angle is grater than 90 degrees, use 0
				gl_FrontMaterial.shininess); //cosine of the angle, to the power of shininess of the material

	return ambient+diffuse+specular;


  // return gl_LightSource[0].ambient;

}
