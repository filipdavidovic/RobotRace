// fragment shader for terrain water surface
varying vec3 N; //normal of the vertex in view coordinates
varying vec3 P; //position of vertex in view coordinates

uniform gl_LightSourceParameters gl_LightSource[1]; // light sources
uniform gl_MaterialParameters gl_FrontMaterial; // material parameters
void main()
{
  	vec4 ambient = vec4(0.0, 0.0, 0.0, 0.0); //ambient light contribution
		ambient += (gl_LightSource[0].ambient * gl_FrontMaterial.ambient);


  	vec4 diffuse = vec4(0.0, 0.0, 0.0, 0.0); //diffuse ligth contribution
  	vec3 Ls; //array of positions of all light sources
  	Ls = normalize(gl_LightSource[0].position.xyz - P);
		diffuse += (gl_LightSource[0].diffuse*gl_FrontMaterial.diffuse)
  				* max(dot(N, Ls), 0.0); //if angle is greater than 90 degrees, use 0

  	vec4 specular = vec4(0.0, 0.0, 0.0, 0.0); //specular light contribution
  	vec3 C = vec3(0.0, 0.0, 0.0); //camera position in view space
  	vec3 CP = normalize(C-P); //vector from vertex to camera
		vec3 R = reflect(-Ls, N); //reflected ray of light
  	specular += (gl_LightSource[0].specular)*gl_FrontMaterial.specular
  			*pow(max(dot(normalize(R), CP), 0.0), //if angle is grater than 90 degrees, use 0
  			gl_FrontMaterial.shininess); //cosine of the angle, to the power of shininess of the material

  	gl_FragColor = ambient + diffuse + specular; //final result
    gl_FragColor = gl_Color;
}
