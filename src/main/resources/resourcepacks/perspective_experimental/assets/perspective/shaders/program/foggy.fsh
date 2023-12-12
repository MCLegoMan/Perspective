#version 150

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;

out vec4 fragColor;

void main() {
    vec4 fog = vec4(mix(texture(DiffuseSampler, texCoord).rgb, vec4(1.0, 1.0, 1.0, 1.0).rgb, min(max(1.0-(1.0-texture(DiffuseDepthSampler, texCoord).r) * 128.0, 0.0), 1.0)), texture(DiffuseSampler, texCoord).a);
    fragColor = vec4(fog.rgb * 0.64, 1.0);
}
