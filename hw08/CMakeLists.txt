cmake_minimum_required(VERSION 3.10)

set(CMAKE_CXX_STANDARD 11)

file(GLOB homework
        "*.h"
        "*.cpp"
        "*.tpp"
        )

add_executable(hw08
        ${homework})
