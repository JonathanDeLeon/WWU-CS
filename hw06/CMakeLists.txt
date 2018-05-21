cmake_minimum_required(VERSION 3.10)

set(CMAKE_CXX_STANDARD 11)

file(GLOB homework
        "*.h"
        "*.cpp"
        )

add_executable(hw06
        ${homework})
