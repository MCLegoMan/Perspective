#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform vec2 BlurDir;
uniform float Radius;
uniform float Alpha;

out vec4 fragColor;

// Gaussian function
float gaussian(float x, float sigma) {
    return exp(-(x * x) / (2.0 * sigma * sigma)) / (sqrt(2.0 * 3.14159) * sigma);
}

void main() {
    vec4 blurred = vec4(0.0);
    float totalStrength = 0.0;
    float totalAlpha = 0.0;

    // Calculate sigma based on radius
    float sigma = Radius / 3.0;

    // Accumulate samples with Gaussian weights
    for(float r = -Radius; r <= Radius; r += 1.0) {
        vec4 sampleValue = texture(DiffuseSampler, texCoord + oneTexel * r * Alpha * BlurDir);

        // Calculate weight using Gaussian function
        float weight = gaussian(r, sigma);

        // Accumulate average alpha
        totalAlpha += sampleValue.a * weight;

        // Accumulate smoothed blur
        blurred += sampleValue * weight;
        totalStrength += weight;
    }

    // Normalize the blurred color
    blurred /= totalStrength;

    // Output the final color
    fragColor = vec4(blurred.rgb, totalAlpha);
}
