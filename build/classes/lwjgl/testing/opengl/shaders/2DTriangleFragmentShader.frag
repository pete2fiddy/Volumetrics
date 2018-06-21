#version 430 core

//im assuming frag shaders only have one output, and that output is the color
in vec2 fragTexCoords;
//doesn't need any other inputs other than those passed by the vertex shader


//sampler2D does the job of linearly interpolating the color each vertex should be given the texture coords
uniform sampler2D textureSampler;

out vec4 fragColor;


void main() 
{
    //uses textureSampler and fragTexCoords to interp the color of the vertex to be drawn (pixel)
    fragColor = texture(textureSampler, fragTexCoords);//vec4(fragTexCoords.x, fragTexCoords.y, 0, 1);
}
