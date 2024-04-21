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
    vec3 healthColor = inputColor;
    vec3 overlayColor = mix(vec3(0.87843137254, 0.03921568627, 0.05882352941), vec3(0.41960784313, 0.0, 0.0), lu_isWitheredSmooth);
    if (health < 1.0) healthColor = mix(mix(inputColor, overlayColor.rgb, smoothstep(0.0, 0.5, distance(texCoord, vec2(0.5, 0.5)))), inputColor, health);
    vec3 hurtColor = healthColor;
    if (hurt < 1.0) hurtColor = mix(mix(healthColor, mix(overlayColor.rgb, overlayColor.grb, lu_isPoisonedSmooth), smoothstep(0.0, 0.5, distance(texCoord, vec2(0.5, 0.5)))), healthColor, hurt);
    fragColor = vec4(hurtColor, 1.0);
}