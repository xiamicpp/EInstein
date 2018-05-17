#!/bin/sh
set -ex
wget https://protobuf.googlecode.com/files/protobuf-3.5.1.tar.gz
tar -xzvf protobuf-3.5.1.tar.gz
cd protobuf-3.5.1 && ./configure --prefix=/usr && make && sudo make install