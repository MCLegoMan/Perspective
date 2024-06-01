#version 150

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
out vec4 fragColor;
uniform float Amount;

void main() {
    fragColor = texture(DiffuseSampler, floor(texCoord / (Amount / textureSize(DiffuseSampler, 0)) + 0.5) * (Amount / textureSize(DiffuseSampler, 0)));
}