#version 150

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;
uniform float lu_currentHealthSmooth;
uniform float lu_maxHealthSmooth;
uniform float lu_currentHurtTimeSmooth;
uniform float lu_maxHurtTimeSmooth;

void main() {
    vec3 inputColor = texture(DiffuseSampler, texCoord).rgb;
    float health = lu_currentHealthSmooth / (lu_maxHealthSmooth * 0.5);
    float hurt = 1.0 - (lu_currentHurtTimeSmooth / lu_maxHurtTimeSmooth);
    vec3 healthColor = inputColor;
    if (health < 1.0) {
        healthColor = mix(mix(inputColor, vec3(1.0, 0.0, 0.0), smoothstep(0.0, 0.5, distance(texCoord, vec2(0.5, 0.5)))), inputColor, health);
    }
    vec3 hurtColor = healthColor;
    if (hurt < 1.0) hurtColor = mix(mix(healthColor, vec3(0.5, 0.0, 0.0), smoothstep(0.0, 0.5, distance(texCoord, vec2(0.5, 0.5)))), healthColor, hurt);
    fragColor = vec4(hurtColor, 1.0);
}
