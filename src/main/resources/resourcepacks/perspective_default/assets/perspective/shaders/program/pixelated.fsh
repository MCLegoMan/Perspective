#version 150

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 pixelCenter = floor(texCoord / (8.0 / textureSize(DiffuseSampler, 0)) + 0.5) * (8.0 / textureSize(DiffuseSampler, 0));
    fragColor = texture(DiffuseSampler, pixelCenter);
}