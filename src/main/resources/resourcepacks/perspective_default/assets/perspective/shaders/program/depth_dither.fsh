#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DitherSampler;
uniform sampler2D DiffuseDepthSampler;

in vec2 texCoord;

uniform vec2 InSize;

out vec4 fragColor;

uniform float lu_viewDistance;

void main() {
    float depth = min(max(1.0 - (1.0 - texture(DiffuseDepthSampler, texCoord).r) * ((lu_viewDistance * 16) * (lu_viewDistance * 0.1)), 0.0), 1.0);
    vec2 halfSize = InSize * 0.5;

    vec2 steppedCoord = texCoord;
    vec4 color = texture(DiffuseSampler, steppedCoord);

    steppedCoord.x = float(int(steppedCoord.x*halfSize.x)) / halfSize.x;
    steppedCoord.y = float(int(steppedCoord.y*halfSize.y)) / halfSize.y;

    vec4 noise = texture(DitherSampler, steppedCoord * halfSize / 4.0);
    vec4 col = color + noise * vec4(1.0/12.0, 1.0/12.0, 1.0/6.0, 1.0);
    float r = float(int(col.r*8.0))/8.0;
    float g = float(int(col.g*8.0))/8.0;
    float b = float(int(col.b*4.0))/4.0;
    fragColor = vec4(mix(color.rgb, vec3(r, g, b), depth), 1.0);
}
