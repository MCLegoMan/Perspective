#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
out vec4 fragColor;
uniform vec2 InSize;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    float brightnessModifier = sin((texCoord.y * InSize.y * 0.25) * 3.1415926535 + 0.0 * InSize.y * 0.25);
    fragColor = vec4((color.rgb * mix(1.0, (pow(brightnessModifier * brightnessModifier, 1.0) + 1.0) * 0.5, 0.8)).rgb, color.a);
}
