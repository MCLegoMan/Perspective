#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;
uniform vec2 OutSize;
in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 uv = texCoord.xy;
    uv *= InSize;
    uv.x = InSize.x - uv.x;
    uv /= InSize;
    vec4 color = texture(DiffuseSampler, uv);
    fragColor = color;
}
