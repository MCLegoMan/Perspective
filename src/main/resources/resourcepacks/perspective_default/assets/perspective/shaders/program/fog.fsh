#version 150

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
out vec4 fragColor;
uniform float Thickness;
uniform float lu_RenderDistance;
uniform vec3 lu_SmoothFogColor;
uniform float lu_SmoothBlockLight;
uniform float lu_SmoothSkyLight;

void main() {
    float light = max(lu_SmoothBlockLight, lu_SmoothSkyLight);
    vec3 color = luSmoothFogColor;
    vec4 inputColor = texture(DiffuseSampler, texCoord);
    float depth = min(max(1.0 - (1.0 - texture(DiffuseDepthSampler, texCoord).r) * (Thickness * (lu_RenderDistance * lu_RenderDistance)), 0.0), 1.0);
    fragColor = vec4(mix(inputColor.r, color.r, depth), mix(inputColor.g, color.g, depth), mix(inputColor.b, color.b, depth), inputColor.a) * (light / 15);
}
