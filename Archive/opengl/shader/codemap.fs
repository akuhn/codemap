uniform sampler2D tex;

vec4 shoreColor = vec4(92.0, 142.0, 255.0, 255.0)/255.0;
vec4 oceanColor = vec4(0.0, 68.0, 255.0, 255.0)/255.0;
vec4 hillColor = vec4(196.0, 236.0, 0.0, 255.0)/255.0;
float coastLineHeight = 2.0/255.0;
float hillLineHeight = 10.0/255.0;

void main()
{

//	gl_FragColor = gl_Color * texture2D(tex, gl_TexCoord[0].xy);
//	gl_FragColor = texture2D(tex, gl_TexCoord[0].xy);
	vec2 xy = gl_TexCoord[0].xy;
	vec2 left = vec2(xy[0]-0.01, xy[1]);
	float leftValue = texture2D(tex, left).b;
	float height = texture2D(tex, xy).b;
//	if (leftValue < height) {
//		gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
//		return;
//	}	

	if (xy[0] < 0.1 && xy[1] < 0.01) {

	}
	vec2 top = vec2(xy[0], xy[1]-1.0);
	
	vec4 color;
	if (height > hillLineHeight) {
//	if (texture[0] > 0.0 && texture[1] > 0.0 && texture[2] > 0.0) {
		color = hillColor;
	} else if(height > coastLineHeight) {
		color = shoreColor;
	} else {
		color = oceanColor;
	}
	
	gl_FragColor = color;
}
