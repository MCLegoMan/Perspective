#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
out vec4 fragColor;

uniform float Strength;

void main(){
    vec2 coord = texCoord - vec2(0.5);
    float dist = length(coord);
    float radius = ((atan(dist, sqrt(1.0 + pow(dist, 2.0) * -1.2)) / 3.141592653589793) * 2.625) * Strength;
    float tanCoord = atan(coord.y, coord.x);

    fragColor = vec4(texture(DiffuseSampler, vec2(radius * cos(tanCoord), radius * sin(tanCoord)) + vec2(0.5)).rgb, 1.0);
}
