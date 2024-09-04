#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MergeSampler;

uniform float lu_alphaSmooth;

in vec2 texCoord;

out vec4 fragColor;

void main(){
    vec4 color = mix(texture(MergeSampler, texCoord), texture(DiffuseSampler, texCoord), lu_alphaSmooth);
    fragColor = vec4(color.rgb, 1.0);
}
