#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;

uniform vec2 qRectPos;
uniform vec2 qTileSize;
uniform vec4 qTileUv;

in vec2 vertexPosition;
out vec4 fragColor;

float lerp(float time, float a, float b) {
    return (1 - time) * a + time * b;
}

vec2 getTextureCoords(vec2 pos) {
    float deltaX = mod(pos.x - qRectPos.x, qTileSize.x) / qTileSize.x;
    float deltaY = mod(pos.y - qRectPos.y, qTileSize.y) / qTileSize.y;
    return vec2(
    lerp(deltaX, qTileUv.x, qTileUv.z),
    lerp(deltaY, qTileUv.y, qTileUv.w)
    );
}

void main() {
    vec4 color = texture(Sampler0, getTextureCoords(vertexPosition));
    if (color.a < 0.1) {
        discard;
    }
    fragColor = color * ColorModulator;
}