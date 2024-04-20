#version 150

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;
uniform vec2 AspectRatio;
uniform vec3 BorderColor;
uniform float lu_alphaSmooth;

void main() {
    vec2 coord = abs(texCoord - vec2(0.5));
    float ratio = (AspectRatio.x/AspectRatio.y)/(oneTexel.y/oneTexel.x);
    if (ratio > 1.0) coord.y *= ratio;
    else coord.x /= ratio;
    vec3 inputColor = texture(DiffuseSampler, texCoord).rgb;
    vec3 outputColor = inputColor.rgb;
    if (coord.x > 0.5 || coord.y > 0.5) outputColor.rgb = BorderColor.rgb;
    fragColor = vec4(mix(inputColor, outputColor, lu_alphaSmooth), 1.0);
}
