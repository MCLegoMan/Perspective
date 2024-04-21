#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D OverlaySampler;

in vec2 texCoord;
out vec4 fragColor;

uniform vec2 InSize;
uniform float lu_alphaSmooth;
uniform float lu_timeSmooth;
uniform float xAmount;
uniform float yAmount;
uniform float speed;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    vec4 overlay = texture(OverlaySampler, fract(vec2(texCoord.x, -texCoord.y) + (vec2(xAmount, yAmount) * (lu_timeSmooth * speed))));
    vec4 outputColor = color;
    if (overlay.a > 0.0) outputColor = mix(color, overlay, lu_alphaSmooth);
    fragColor = vec4(outputColor.rgb, 1.0);
}
