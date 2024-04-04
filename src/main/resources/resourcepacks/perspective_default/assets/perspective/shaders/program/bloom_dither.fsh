#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DitherSampler;
uniform sampler2D BloomSampler;
uniform sampler2D HighlightsSampler;

uniform float BloomFactor;
uniform float HighlightsFactor;
uniform float Thirst;

in vec2 texCoord;

uniform vec2 InSize;

out vec4 fragColor;

void main() {
    vec2 halfSize = InSize * 0.5;

    vec2 steppedCoord = texCoord;
    vec4 color = texture(DiffuseSampler, steppedCoord);

    steppedCoord.x = float(int(steppedCoord.x*halfSize.x)) / halfSize.x;
    steppedCoord.y = float(int(steppedCoord.y*halfSize.y)) / halfSize.y;

    vec4 noise = texture(DitherSampler, steppedCoord * halfSize / 4.0);
    vec4 col = color + noise * vec4(1.0/12.0, 1.0/12.0, 1.0/6.0, 1.0);

    vec4 bloom = texture(BloomSampler, texCoord);
    vec4 highlights = texture(HighlightsSampler, texCoord);
    float brightness = max(bloom.r * BloomFactor, highlights.r * HighlightsFactor);
    float thirstFactor = (1.0 - Thirst) * 0.5 + 1.0;

    float r = float(int(col.r*8.0))/8.0;
    float g = float(int(col.g*8.0))/8.0;
    float b = float(int(col.b*4.0))/4.0;
    fragColor = vec4(mix(color.rgb, vec3(r, g, b), brightness * thirstFactor).rgb, 1.0);
}

