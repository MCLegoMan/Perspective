#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
uniform vec2 OutSize;
in vec2 texCoord;
out vec4 fragColor;

uniform float Strength;
uniform float lu_RenderDistance;

void main() {
    vec4 inputColor = vec4(texture(DiffuseSampler, texCoord).rgb, 1.0);
    float diffuseDepth = texture(DiffuseDepthSampler, texCoord).r;
    float worldDepth = 2.0 * 0.025 * 1000.0 / (1000.0 + 0.025 - (diffuseDepth * 2.0 - 1.0) * (1000.0 - 0.025));
    float depthOffset = max(0.0048 * max((24.0 - worldDepth) / 24.0, 0.0), 1.0 / OutSize.y);
    float outlineFactor = clamp(pow(max(2.0 * 0.025 * 1000.0 / (1000.0 + 0.025 - (max(texture(DiffuseDepthSampler, texCoord + vec2(-depthOffset * OutSize.y / OutSize.x, -depthOffset)).r, max(texture(DiffuseDepthSampler, texCoord + vec2(-depthOffset * OutSize.y / OutSize.x, +depthOffset)).r, max(texture(DiffuseDepthSampler, texCoord + vec2(+depthOffset * OutSize.y / OutSize.x, +depthOffset)).r, texture(DiffuseDepthSampler, texCoord + vec2(+depthOffset * OutSize.y / OutSize.x, -depthOffset)).r))) * 2.0 - 1.0) * (1000.0 - 0.025)) - worldDepth, 0.0) * 0.15, 2.0), 0.0, 1.0) * exp(-worldDepth * 0.025);
    vec3 color = mix(inputColor.rgb, pow(pow(inputColor.rgb, vec3(1.0 / 2.2)) + Strength, vec3(2.2)), outlineFactor);

    float depth = min(max(1.0 - (1.0 - texture(DiffuseDepthSampler, texCoord).r) * (lu_RenderDistance * 16), 0.0), 1.0);

    vec3 outputColor;
    if (depth > 0.9) outputColor = mix(color.rgb, inputColor.rgb, smoothstep(0.9, 0.91, depth));
    else outputColor = color.rgb;

    fragColor = vec4(outputColor, inputColor.a);
}