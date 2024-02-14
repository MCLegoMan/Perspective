#version 150

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
out vec4 fragColor;
uniform float Thickness;
uniform float perspectiveRenderDistance;
uniform vec3 perspectiveSmoothFogColor;
uniform float perspectiveSmoothLight;

void main() {
    vec3 color = perspectiveSmoothFogColor;
    vec4 inputColor = texture(DiffuseSampler, texCoord);
    float depth = min(max(1.0 - (1.0 - texture(DiffuseDepthSampler, texCoord).r) * (Thickness * (perspectiveRenderDistance / 16)), 0.0), 1.0);
    fragColor = vec4(mix(inputColor.r, color.r, depth), mix(inputColor.g, color.g, depth), mix(inputColor.b, color.b, depth), inputColor.a) * (perspectiveSmoothLight / 15);
}
