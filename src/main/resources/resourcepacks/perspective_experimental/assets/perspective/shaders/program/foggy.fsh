#version 150

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
out vec4 fragColor;

void main() {
    vec3 inputColor = texture(DiffuseSampler, texCoord).rgb;
    float depth = min(max(1.0 - (1.0 - texture(DiffuseDepthSampler, texCoord).r) * 64.0, 0.0), 1.0);
    fragColor = vec4(mix(mix(inputColor, vec3(1.0), depth), mix(inputColor, vec3(0.7, 0.7, 0.7), depth * 0.2), 0.3), texture(DiffuseSampler, texCoord).a);
}
