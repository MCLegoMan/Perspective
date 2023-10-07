#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 ColorModulate;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    fragColor = texture(DiffuseSampler, floor(texCoord / 0.0125) * 0.0125) * ColorModulate * vec4(0.85, 0.85, 0.85, 1.0);
}