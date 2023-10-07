#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 ColorModulate;

in vec2 texCoord;
out vec4 fragColor;

const vec4 palette[16] = vec4[16](
    vec4(0.0, 0.0, 0.0, 1.0),
    vec4(0.5, 0.0, 0.0, 1.0),
    vec4(0.0, 0.5, 0.0, 1.0),
    vec4(0.5, 0.5, 0.0, 1.0),
    vec4(0.0, 0.0, 0.5, 1.0),
    vec4(0.5, 0.0, 0.5, 1.0),
    vec4(0.0, 0.5, 0.5, 1.0),
    vec4(0.75, 0.75, 0.75, 1.0),
    vec4(0.75, 0.75, 0.75, 1.0),
    vec4(1.0, 0.0, 0.0, 1.0),
    vec4(0.0, 1.0, 0.0, 1.0),
    vec4(1.0, 1.0, 0.0, 1.0),
    vec4(0.0, 0.0, 1.0, 1.0),
    vec4(1.0, 0.0, 1.0, 1.0),
    vec4(0.0, 1.0, 1.0, 1.0),
    vec4(1.0, 1.0, 1.0, 1.0)
);

void main(){
    vec4 sampledColor = texture(DiffuseSampler, texCoord);
    float minDistance = distance(sampledColor, palette[0]);
    vec4 quantizedColor = palette[0];
    for (int i = 1; i < 16; i++) {
        float distanceToPaletteColor = distance(sampledColor, palette[i]);
        if (distanceToPaletteColor < minDistance) {
            minDistance = distanceToPaletteColor;
            quantizedColor = palette[i];
        }
    }
    fragColor = quantizedColor * ColorModulate;
}