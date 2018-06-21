#version 430 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoords;
uniform mat4 projMat;
out vec2 fragTexCoords;


void main() 
{
    //gl_Position is a built in variable (ONLY for vertex shaders) specifying the transformed position to pass on to the next shader
    //appending just puts vector in affine 3D form
    gl_Position = projMat * vec4(position, 1.0);
    fragTexCoords = texCoords;
}