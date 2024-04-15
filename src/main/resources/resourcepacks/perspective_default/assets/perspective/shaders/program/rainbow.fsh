#version 150

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 colors[7] = vec4[](
        vec4(0.7, 0.0, 0.7, 1.0),
        vec4(0.5, 0.0, 0.5, 1.0),
        vec4(0.0, 0.0, 1.0, 1.0),
        vec4(0.0, 1.0, 0.0, 1.0),
        vec4(1.0, 1.0, 0.0, 1.0),
        vec4(1.0, 0.5, 0.0, 1.0),
        vec4(1.0, 0.0, 0.0, 1.0)
    );
    fragColor = colors[clamp(int(mod(texCoord.y, 1.0) / (1.0 / float(7))), 0, 7 - 1)] * texture(DiffuseSampler, texCoord);
}