#version 150

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
out vec4 fragColor;
uniform vec3 FogColor;
uniform float Thickness;

void main() {
    vec4 inputColor = texture(DiffuseSampler, texCoord);
    float depth = min(max(1.0 - (1.0 - texture(DiffuseDepthSampler, texCoord).r) * Thickness, 0.0), 1.0);
    fragColor = vec4(mix(inputColor.r, FogColor.r, depth), mix(inputColor.g, FogColor.g, depth), mix(inputColor.b, FogColor.b, depth), inputColor.a);
}
