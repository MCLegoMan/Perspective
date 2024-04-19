#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 ColorModulate;

in vec2 texCoord;
out vec4 fragColor;

const vec4 palette[4] = vec4[4](
    vec4(0.058, 0.219, 0.058, 1.0),
    vec4(0.188, 0.384, 0.188, 1.0),
    vec4(0.545, 0.674, 0.058, 1.0),
    vec4(0.607, 0.737, 0.058, 1.0)
);

void main() {
    vec4 c = texture(DiffuseSampler, texCoord);
    float d = distance(c, palette[0]);
    vec4 q = palette[0];
    for (int i = 1; i < 4; i++) {
        float dtc = distance(c, palette[i]);
        if (dtc < d) {
            d = dtc;
            q = palette[i];
        }
    }
    fragColor = q * ColorModulate;
}
