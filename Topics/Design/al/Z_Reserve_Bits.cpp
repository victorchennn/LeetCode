#include <cstdint>

所以题目必须给你 bit width / byte width，或者由数据类型决定宽度。
00000001                    // 8 bit?
00000000 00000001           // 16 bit?
00000000 00000000 00000001 // 24 bit?
00000000 ... 00000001       // 32 bit?

uint32_t reverseBits(uint32_t n) {
    uint32_t result = 0;

    for (int i = 0; i < 32; ++i) {
        result <<= 1;      // 给新 bit 腾位置
        result |= (n & 1); // 取 n 最右边的 bit
        n >>= 1;           // 处理下一个 bit
    }

    return result;
}

uint64_t reverseBits(uint64_t n, int bytes) {
    uint64_t result = 0;

    for (int i = 0; i < bytes * 8; ++i) {
        result = (result << 1) | (n & 1);
        n >>= 1;
    }

    return result;
}
