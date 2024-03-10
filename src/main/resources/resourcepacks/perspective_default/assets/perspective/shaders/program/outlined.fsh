#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
uniform vec2 OutSize;
uniform float Strength;
uniform float Thickness;
uniform float lu_viewDistance;
in vec2 texCoord;
out vec4 fragColor;

vec4 outlineColor (vec4 inputColor, sampler2D DepthSampler) {
    float depth = 2.0 * 0.025 * 1000.0 / (1000.0 + 0.025 - (texture(DepthSampler, texCoord).r * 2.0 - 1.0) * (1000.0 - 0.025));
    float offset = max(Thickness * max((32.0 - depth) / 32.0, 0.0), 1.0 / OutSize.y);
    float depth0 = texture(DepthSampler, texCoord + vec2(-offset * OutSize.y / OutSize.x, -offset)).r;
    float depth1 = texture(DepthSampler, texCoord + vec2(-offset * OutSize.y / OutSize.x, +offset)).r;
    float depth2 = texture(DepthSampler, texCoord + vec2(+offset * OutSize.y / OutSize.x, +offset)).r;
    float depth3 = texture(DepthSampler, texCoord + vec2(+offset * OutSize.y / OutSize.x, -offset)).r;
    float amount = clamp(pow(max(2.0 * 0.025 * 1000.0 / (1000.0 + 0.025 - (max(depth0, max(depth1, max(depth2, depth3))) * 2.0 - 1.0) * (1000.0 - 0.025)) - depth, 0.0) * 0.15, 2.0), 0.0, 1.0) * exp(-depth * 0.025);
    return vec4(mix(inputColor.rgb, pow(pow(inputColor.rgb, vec3(0.5)) + Strength, vec3(2.0)), amount), inputColor.a);
}

void main() {
    vec4 inputColor = texture(DiffuseSampler, texCoord);
    float depth = min(max(1.0 - (1.0 - texture(DiffuseDepthSampler, texCoord).r) * (lu_viewDistance * 16), 0.0), 1.0);
    vec3 outputColor = outlineColor(inputColor, DiffuseDepthSampler).rgb;
    if (depth > 0.9) outputColor = mix(outputColor.rgb, inputColor.rgb, smoothstep(0.9, 0.91, depth));
    fragColor = vec4(outputColor, inputColor.a);
}