#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 ColorModulate;

in vec2 texCoord;
out vec4 fragColor;
uniform float Amount;

void main() {
    fragColor.rgb = floor(texture(DiffuseSampler, texCoord).rgb * Amount) / Amount;
}
