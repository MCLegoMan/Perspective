#version 150

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;

uniform vec2 BlurDir;
uniform float Radius;
uniform float perspective_zoomMultiplier;

float gaussian(float x) {
    return exp(-(x * x) / (2.0 * (Radius / 3.0) * (Radius / 3.0))) / (sqrt(2.0 * 3.141592653589793) * (Radius / 3.0));
}

void main() {
    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    for(float r = -Radius; r <= Radius; r += 1.0) {
        vec4 sampleValue = texture(DiffuseSampler, texCoord + oneTexel * r * BlurDir);
        blurred += sampleValue * gaussian(r);
        totalStrength += gaussian(r);
    }
    blurred /= totalStrength;
    fragColor = vec4(mix(texture(DiffuseSampler, texCoord).rgb, blurred.rgb, clamp((distance(texCoord, vec2(0.5)) * 4), 0.0, 1.0)), 1.0);
}
