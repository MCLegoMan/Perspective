#version 150

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;
uniform float lu_currentHealthSmooth;
uniform float lu_maxHealthSmooth;
uniform float lu_currentHurtTimeSmooth;
uniform float lu_maxHurtTimeSmooth;
uniform float lu_isWitheredSmooth;
uniform float lu_isPoisonedSmooth;

void main() {
    vec3 inputColor = texture(DiffuseSampler, texCoord).rgb;
    float health = lu_currentHealthSmooth / (lu_maxHealthSmooth * 0.5);
    float hurt = 1.0 - (lu_currentHurtTimeSmooth / lu_maxHurtTimeSmooth);
    float withered = lu_isWitheredSmooth * 0.5;
    float poisoned = lu_isPoisonedSmooth * 0.5;

    vec3 color = inputColor;
    vec3 overlay = vec3(0.87843137254, 0.03921568627, 0.05882352941);
    vec3 wither = vec3(0.23921568627, 0.03137254901, 0.03137254901);
    vec3 poison = vec3(0.45882352941, 0.87843137254, 0.03921568627);

    color = mix(color, mix(color, wither, smoothstep(0.0, 0.5, distance(texCoord, vec2(0.5, 0.5)))), min(withered, 1.0));
    color = mix(color, mix(color, poison, smoothstep(0.0, 0.5, distance(texCoord, vec2(0.5, 0.5)))), min(poisoned, 1.0));
    color = mix(mix(color, overlay, smoothstep(0.0, 0.5, distance(texCoord, vec2(0.5, 0.5)))), color, min(health, 1.0));
    color = mix(mix(color, overlay, smoothstep(0.0, 0.5, distance(texCoord, vec2(0.5, 0.5)))), color, min(hurt, 1.0));

    fragColor = vec4(color, 1.0);
}