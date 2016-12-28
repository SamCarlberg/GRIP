#!/bin/bash

brew update
brew install gcc
brew upgrade gcc
brew install cmake
brew upgrade cmake
brew install ant
brew upgrade ant
# TODO revert this once opencv-python has builds for Python 3.6
brew install pyenv
echo 'eval "$(pyenv init -)"' >> ~/.bash_profile
exec $SHELL
pyenv install 3.5.2
brew linkapps python3
pip3 install numpy
pip3 install opencv-python
mkdir -p $HOME/opencv/jni
