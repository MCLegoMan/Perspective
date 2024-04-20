#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
out vec4 fragColor;

uniform vec2 InSize;
uniform float Amount;
uniform float Strength;
uniform float lu_alphaSmooth;

void main() {
    vec3 inputColor = texture(DiffuseSampler, texCoord).rgb;
    fragColor = vec4(mix(inputColor, inputColor - sin((texCoord.y * InSize.y) * Amount) * Strength, lu_alphaSmooth), 1.0);
}
