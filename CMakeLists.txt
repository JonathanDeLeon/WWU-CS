cmake_minimum_required(VERSION 3.10)
project(CPTR242_Homework)

set(CMAKE_CXX_STANDARD 11)

#file(GLOB homework
#        "hw01/*.h"
#        "hw01/*.cpp"
#        )
#
#add_executable(CPTR242_Homework
#        ${homework})

add_subdirectory(hw00)
add_subdirectory(hw01)
add_subdirectory(hw02)
