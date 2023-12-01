#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 ColorModulate;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    fragColor = vec4(fragColor.r, fragColor.g, fragColor.b, 0.0);
}
