#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D OverlaySampler;

in vec2 texCoord;
out vec4 fragColor;

uniform float lu_alphaSmooth;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    vec4 overlay = texture(OverlaySampler, vec2(texCoord.x, -texCoord.y));
    vec4 outputColor = color;
    if (overlay.a > 0.0) outputColor = mix(color, overlay, lu_alphaSmooth);
    fragColor = vec4(outputColor.rgb, 1.0);
}
