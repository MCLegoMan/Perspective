#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 ColorModulate;

in vec2 texCoord;
out vec4 fragColor;

const vec3 palette[4] = vec3[4](
    vec3(0.058, 0.219, 0.058),
    vec3(0.188, 0.384, 0.188),
    vec3(0.545, 0.674, 0.058),
    vec3(0.607, 0.737, 0.058)
);

void main() {
    vec3 c = texture(DiffuseSampler, texCoord).rgb;
    float d = distance(c, palette[0]);
    vec3 q = palette[0];
    for (int i = 1; i < 4; i++) {
        float dtc = distance(c, palette[i]);
        if (dtc < d) {
            d = dtc;
            q = palette[i];
        }
    }
    fragColor = vec4(q * ColorModulate.rgb, 1.0);
}
