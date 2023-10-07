#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D TranslucentSampler;
uniform sampler2D TranslucentDepthSampler;
uniform sampler2D ItemEntitySampler;
uniform sampler2D ItemEntityDepthSampler;
uniform sampler2D ParticlesSampler;
uniform sampler2D ParticlesDepthSampler;
uniform sampler2D WeatherSampler;
uniform sampler2D WeatherDepthSampler;
uniform sampler2D CloudsSampler;
uniform sampler2D CloudsDepthSampler;


uniform vec2 OutSize;

in vec2 texCoord;

#define NUM_LAYERS 12

out vec4 fragColor;

void main() {
    vec4 diffuseColor = vec4(texture(DiffuseSampler, texCoord).rgb, 1.0);
    float diffuseDepth = texture(DiffuseDepthSampler, texCoord).r;

    float worldDepth = 2.0 * 0.05 * 1000.0 / (1000.0 + 0.05 - (diffuseDepth * 2.0 - 1.0) * (1000.0 - 0.05));
    float depthOffset = max(0.0045 * max((32.0 - worldDepth) / 32.0, 0.0), 1.0 / OutSize.y);
    float depth00 = texture(DiffuseDepthSampler, texCoord + vec2(-depthOffset * OutSize.y / OutSize.x, -depthOffset)).r;
    float depth01 = texture(DiffuseDepthSampler, texCoord + vec2(-depthOffset * OutSize.y / OutSize.x, +depthOffset)).r;
    float depth11 = texture(DiffuseDepthSampler, texCoord + vec2(+depthOffset * OutSize.y / OutSize.x, +depthOffset)).r;
    float depth10 = texture(DiffuseDepthSampler, texCoord + vec2(+depthOffset * OutSize.y / OutSize.x, -depthOffset)).r;
    float maxDepth = max(depth00, max(depth01, max(depth11, depth10)));
    float newDepth = 2.0 * 0.05 * 1000.0 / (1000.0 + 0.05 - (maxDepth * 2.0 - 1.0) * (1000.0 - 0.05));
    float depthDifference = newDepth - worldDepth;
    float outlineFactor = clamp(pow(max(depthDifference, 0.0) * 0.15, 2.0), 0.0, 1.0) * exp(-worldDepth * 0.025);

    diffuseColor.rgb = mix(diffuseColor.rgb, pow(pow(diffuseColor.rgb, vec3(1.0 / 2.2)) + 0.25, vec3(2.2)), outlineFactor);

    vec4 colorLayers[6];
    float depthLayers[6];
    int activeLayers = 0;

    colorLayers[0] = diffuseColor;
    depthLayers[0] = diffuseDepth;
    activeLayers = 1;

    vec4 translucentColor = texture(TranslucentSampler, texCoord);
    float translucentDepth = texture(TranslucentDepthSampler, texCoord).r;
    if (translucentColor.a > 0.0) {
        colorLayers[activeLayers] = translucentColor;
        depthLayers[activeLayers] = translucentDepth;
        activeLayers++;
    }

    vec4 itemEntityColor = texture(ItemEntitySampler, texCoord);
    float itemEntityDepth = texture(ItemEntityDepthSampler, texCoord).r;
    if (itemEntityColor.a > 0.0) {
        colorLayers[activeLayers] = itemEntityColor;
        depthLayers[activeLayers] = itemEntityDepth;
        activeLayers++;
    }

    vec4 particleColor = texture(ParticlesSampler, texCoord);
    float particleDepth = texture(ParticlesDepthSampler, texCoord).r;
    if (particleColor.a > 0.0) {
        colorLayers[activeLayers] = particleColor;
        depthLayers[activeLayers] = particleDepth;
        activeLayers++;
    }

    vec4 weatherColor = texture(WeatherSampler, texCoord);
    float weatherDepth = texture(WeatherDepthSampler, texCoord).r;
    if (weatherColor.a > 0.0) {
        colorLayers[activeLayers] = weatherColor;
        depthLayers[activeLayers] = weatherDepth;
        activeLayers++;
    }

    vec4 cloudsColor = texture(CloudsSampler, texCoord);
    float cloudsDepth = texture(CloudsDepthSampler, texCoord).r;
    if (cloudsColor.a > 0.0) {
        colorLayers[activeLayers] = cloudsColor;
        depthLayers[activeLayers] = cloudsDepth;
        activeLayers++;
    }

    vec3 texelAccumulator = colorLayers[0].rgb;
    for (int i = 1; i < activeLayers; ++i) {
        texelAccumulator = (texelAccumulator * (1.0 - colorLayers[i].a)) + colorLayers[i].rgb;
    }

    fragColor = vec4(texelAccumulator.rgb, 1.0);
}
