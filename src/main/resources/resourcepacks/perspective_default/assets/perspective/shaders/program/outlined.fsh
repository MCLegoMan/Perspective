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

in vec2 texCoord;

uniform vec2 OutSize;
uniform float Transparency;
uniform float Thickness;
uniform float Outline;
uniform vec3 OutlineColor;
uniform vec3 OutlinePow;
uniform float OutlineColorMultiplier;
uniform float Silhouette;
uniform vec3 SilhouetteColor;
uniform float lu_viewDistance;

#define NUM_LAYERS 6

vec4 color_layers[NUM_LAYERS];
float depth_layers[NUM_LAYERS];
int active_layers = 0;

out vec4 fragColor;

vec4 outline( vec4 color, sampler2D DepthSampler ) {
    float depth = texture( DepthSampler, texCoord ).r;
    float outlineDepth = 2.0 * 0.025 * 1000.0 / (1000.0 + 0.025 - (depth * 2.0 - 1.0) * (1000.0 - 0.025));
    float offset = max(Thickness * max((32.0 - outlineDepth) / 32.0, 0.0), 1.0 / OutSize.y);
    float depth0 = texture(DepthSampler, texCoord + vec2(-offset * OutSize.y / OutSize.x, -offset)).r;
    float depth1 = texture(DepthSampler, texCoord + vec2(-offset * OutSize.y / OutSize.x, +offset)).r;
    float depth2 = texture(DepthSampler, texCoord + vec2(+offset * OutSize.y / OutSize.x, +offset)).r;
    float depth3 = texture(DepthSampler, texCoord + vec2(+offset * OutSize.y / OutSize.x, -offset)).r;
    float amount = clamp(pow(max(2.0 * 0.025 * 1000.0 / (1000.0 + 0.025 - (max(depth0, max(depth1, max(depth2, depth3))) * 2.0 - 1.0) * (1000.0 - 0.025)) - outlineDepth, 0.0) * 0.15, 2.0), 0.0, 1.0) * exp(-outlineDepth * 0.025);

    vec3 outlineColor;
    if (Outline == 0) {
        outlineColor = color.rgb;
    } else if (Outline == 1) {
        outlineColor = OutlineColor;
    }

    vec4 outputColor = vec4(mix(color.rgb, pow((pow(outlineColor, OutlinePow) * OutlineColorMultiplier) + Transparency, vec3(2.0)), amount), color.a);
    float depth4 = min(max(1.0 - (1.0 - depth) * ((lu_viewDistance * 16) * 0.64), 0.0), 1.0);
    return vec4(mix(outputColor.rgb, color.rgb, smoothstep(0.9, 0.91, depth4)), outputColor.a);
}

void try_insert( vec4 color, sampler2D DepthSampler ) {
    if ( color.a == 0.0 ) {
        return;
    }
    float depth = texture( DepthSampler, texCoord ).r;

    if (Silhouette != 0) color = vec4(SilhouetteColor, 1.0);

    color = outline(color, DepthSampler);

    color_layers[active_layers] = color;
    depth_layers[active_layers] = depth;

    int jj = active_layers++;
    int ii = jj - 1;
    while ( jj > 0 && depth_layers[jj] > depth_layers[ii] ) {
        float depthTemp = depth_layers[ii];
        depth_layers[ii] = depth_layers[jj];
        depth_layers[jj] = depthTemp;

        vec4 colorTemp = color_layers[ii];
        color_layers[ii] = color_layers[jj];
        color_layers[jj] = colorTemp;

        jj = ii--;
    }
}

vec3 blend( vec3 dst, vec4 src ) {
    return ( dst * ( 1.0 - src.a ) ) + src.rgb;
}

void main() {
    vec4 color;
    if (Silhouette != 0) {
        color = vec4(SilhouetteColor, 1.0);
    } else {
        color = texture(DiffuseSampler, texCoord);
    }
    color_layers[0] = vec4(outline(color, DiffuseDepthSampler).rgb, 1.0);
    depth_layers[0] = texture(DiffuseDepthSampler, texCoord).r;
    active_layers = 1;

    try_insert( texture( TranslucentSampler, texCoord ), TranslucentDepthSampler );
    try_insert( texture( ItemEntitySampler, texCoord ), ItemEntityDepthSampler );
    try_insert( texture( ParticlesSampler, texCoord ), ParticlesDepthSampler );
    try_insert( texture( WeatherSampler, texCoord ), WeatherDepthSampler );
    try_insert( texture( CloudsSampler, texCoord ), CloudsDepthSampler );

    vec3 texelAccum = color_layers[0].rgb;
    for ( int ii = 1; ii < active_layers; ++ii ) {
        texelAccum = blend( texelAccum, color_layers[ii] );
    }

    fragColor = vec4( texelAccum.rgb, 1.0 );
}
