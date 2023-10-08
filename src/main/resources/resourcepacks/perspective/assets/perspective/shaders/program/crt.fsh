#version 150

uniform sampler2D DiffuseSampler;
uniform float Time;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec2 dt = texCoord + vec2(0.002 * sin(mod(Time, 20.0)), 0.002 * cos(mod(Time, 20.0)));
    vec4 c = texture(DiffuseSampler, dt);
    c.rgb = c.rgb * 1.2;
    c.r = texture(DiffuseSampler, dt + vec2(0.003, 0.0)).r;
    c.b = texture(DiffuseSampler, dt - vec2(0.003, 0.0)).b;
    if (mod(texCoord.y * 525.0, 2.0) < 1.0) c -= 0.025;
    else c += 0.025;
    fragColor = c;
}