#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform vec2 BlurDir;
uniform float Radius;
uniform float Alpha;

out vec4 fragColor;

float gaussian(float x) {
    return exp(-(x * x) / (2.0 * (Radius / 3.0) * (Radius / 3.0))) / (sqrt(2.0 * 3.14159) * (Radius / 3.0));
}

void main() {
    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    float totalAlpha = 0.0;
    for(float r = -Radius; r <= Radius; r += 1.0) {
        vec4 sampleValue = texture(DiffuseSampler, texCoord + oneTexel * r * Alpha * BlurDir);
        totalAlpha += sampleValue.a * gaussian(r);
        blurred += sampleValue * gaussian(r);
        totalStrength += gaussian(r);
    }
    blurred /= totalStrength;
    fragColor = vec4(blurred.rgb, totalAlpha);
}