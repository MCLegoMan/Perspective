#version 150

in vec2 texCoord;
in vec2 oneTexel;
uniform vec2 InSize;
uniform vec2 OutSize;

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
out vec4 fragColor;
uniform float lu_viewDistance;

void main() {
    vec4 inputColor = texture(DiffuseSampler, texCoord);

    vec2 uv = texCoord.xy;
    uv *= InSize;
    uv.x = InSize.x - uv.x;
    uv /= InSize;
    vec4 color = texture(DiffuseSampler, uv);

    float depth = min(max(1.0 - (1.0 - texture(DiffuseDepthSampler, texCoord).r) * ((lu_viewDistance * 16) * 0.64), 0.0), 1.0);

    vec3 outputColor;
    if (depth > 0.9) outputColor = mix(inputColor.rgb, color.rgb, smoothstep(0.9, 0.91, depth));
    else outputColor = inputColor.rgb;

    fragColor = vec4(outputColor, inputColor.a);
}
