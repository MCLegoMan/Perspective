#version 150

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec3 colors[7];
    colors[0] = vec3(0.7, 0.0, 0.7);
    colors[1] = vec3(0.5, 0.0, 0.5);
    colors[2] = vec3(0.0, 0.0, 1.0);
    colors[3] = vec3(0.0, 1.0, 0.0);
    colors[4] = vec3(1.0, 1.0, 0.0);
    colors[5] = vec3(1.0, 0.5, 0.0);
    colors[6] = vec3(1.0, 0.0, 0.0);
    fragColor = vec4(colors[clamp(int(mod(texCoord.y, 1.0) / (1.0 / float(7))), 0, 7 - 1)], 1.0) * texture(DiffuseSampler, texCoord);
}