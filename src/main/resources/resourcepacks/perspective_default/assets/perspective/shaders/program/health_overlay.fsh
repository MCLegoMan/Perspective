#version 150

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;
uniform float lu_currentHealthSmooth;
uniform float lu_maxHealthSmooth;

void main() {
    vec3 inputColor = texture(DiffuseSampler, texCoord).rgb;
    float health = lu_currentHealthSmooth / lu_maxHealthSmooth;
    vec3 outputColor;
    if (health < 0.5) {
        outputColor = mix(mix(inputColor, vec3(1.0, 0.0, 0.0), smoothstep(0.0, 0.5, distance(texCoord, vec2(0.5, 0.5)))), inputColor, (health * 2.0));
    } else {
        outputColor = inputColor;
    }
    fragColor = vec4(outputColor, 1.0);
}
