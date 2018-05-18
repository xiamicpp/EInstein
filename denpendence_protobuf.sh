#!/bin/sh
set -ex
wget https://github.com/google/protobuf/releases/download/v3.5.1/protobuf-all-3.5.1.tar.gz
tar -xzvf protobuf-all-3.5.1.tar.gz
cd protobuf-3.5.1 && ./configure --prefix=/usr && make -j8 && sudo make install